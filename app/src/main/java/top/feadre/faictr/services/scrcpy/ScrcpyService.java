/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.feadre.faictr.services.scrcpy;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import top.feadre.faictr.flib.base.DelayThread2Main;
import top.feadre.faictr.flib.base.Thread2Main;


public class ScrcpyService extends Service {
    private boolean is_video = true;
    public static final int MSG_WHAT = 1;
    public static final int MSG_STATE_SUCCESS = 1;
    public static final int MSG_STATE_FAIL = 0;

    private static final String TAG = "ScrcpyService";
    private String serverAdr;
    private byte[] event_val_bytes = null;
    private VideoDecoder videoDecoder;
    private IBinder mBinder = new ScrcpyBinder();
    private boolean is_first_run = true;
    private OnScrcpyServiceStateCallbacks onScrcpyServiceStateCallbacks;
    private OnScrcpyServiceRotationCallbacks onScrcpyServiceRotationCallbacks;
    private final int[] remote_resolution_wh = new int[2];
    private AtomicBoolean is_socket_success = new AtomicBoolean(false);//控制 socket 出错时 等待
    private AtomicBoolean is_service_running = new AtomicBoolean(true); //连接成功后，控制视频更新 等待
    private AtomicBoolean is_receive_update_video = new AtomicBoolean(false); //阻塞 视频管道
    //    private Handler handler4link; //通过接口回调
    private Handler handler4ser;
    private Surface surface; //这个的用来接收图像的大小
    private int surface_resolution_w;
    private int surface_resolution_h;
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private static VideoPacket.StreamSettings streamSettings = null;

    public class ScrcpyBinder extends Binder {
        public ScrcpyService getService() {
            return ScrcpyService.this;
        }
    }

    private void start_services4thread(DelayThread2Main that) {
        close_socket();
//        is_service_running.set(true); //is_socket_success 连接成功后，进入开始传输等待 ， 不等待则马上方法完成

        int count_attempts = 50; //socket 错了就一次错，连接50次失败
        while (count_attempts > 0) {
            Log.d(TAG, "start_services4thread: is_socket_success.get() = " + is_socket_success.get());
            while (!is_socket_success.get()) {
                // 默认false 如果 socket 7707没有成功则等待关闭 ----------------
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (socket == null) {
                    socket = new Socket(serverAdr, 7007); //只能连接一次，断开后再连报错
                }
                dataInputStream = new DataInputStream(socket.getInputStream());

                //获取控制机分辨率  及设置显示的分辨率
                byte[] buf = new byte[16];
                dataInputStream.read(buf, 0, 16);//拿到远程分辨率
                // 由 sever 端解析到结果
                for (int i = 0; i < remote_resolution_wh.length; i++) {
                    remote_resolution_wh[i] = (((int) (buf[i * 4]) << 24) & 0xFF000000) | (((int) (buf[i * 4 + 1]) << 16) & 0xFF0000) | (((int) (buf[i * 4 + 2]) << 8) & 0xFF00) | ((int) (buf[i * 4 + 3]) & 0xFF);
                }
                if (onScrcpyServiceStateCallbacks != null) {
                    onScrcpyServiceStateCallbacks.on_remote_resolution_gain(remote_resolution_wh);
                }

                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                //socket 连接成功 -----------------------------------
//                _attempts_count = 0; //重置为0,过了这行，while即失败
                is_service_running.set(true);
                is_socket_success.set(true); //关闭socket 必须完全解绑关闭后才会重新开启
                that.run_over_success("scrcpy 服务开启成功");
                Log.d(TAG, "start_services4thread: scrcpy 服务开启成功");

                //阻塞方法
                _show_video();
                Log.w(TAG, "start_services4thread: _show_video 运行完成");
                break;//走完则退出

            } catch (Exception e) {
                e.printStackTrace();
                count_attempts = count_attempts - 1;
                Log.e(TAG, "start_services4thread:---  attempts_count = " + count_attempts
                        + "  " + e.getMessage());
                if (count_attempts == 0) {
                    is_socket_success.set(false);
                }
            } finally {
                //解锁会导致这个完成
                close_socket();
                Log.d(TAG, "start_services4thread: finally 运行完成");
            }
        }

        //循环外 （50次弄完） --- 如果已开启服务，正常关闭  is_service_running 被设为 false 时完成
        if (is_socket_success.get()) {
            is_socket_success.set(false);
            is_service_running.set(false);//已经成功后正常走完后的 重置
            is_receive_update_video.set(false); //不开始接收，会导致服务端卡死
        } else {
            Log.e(TAG, "start_services4thread: is_socket_success = " + is_socket_success
                    + " is_service_running = " + is_service_running
                    + " is_receive_update_video = " + is_receive_update_video
            );
            if (count_attempts == 0) { //一定是出错完成的， 这个不为0,则为手动关闭的 不是错误
                that.run_over_fail("scrcpy 服务开启失败"); //运行中间出错
            }
        }
    }

    private void _show_video() throws IOException {
        byte[] packetSize;
        //默认是true
        while (is_service_running.get()) {
            // is_service_running stop_services 由外部控制

            if (is_receive_update_video.get()) {
                //不更新视频则一直等待
                //有事件值,先发给控制机-----------------------------
                if (event_val_bytes != null) {
                    //发出事件并清空
                    dataOutputStream.write(event_val_bytes, 0, event_val_bytes.length);
                    event_val_bytes = null; //事件重置
                }

                int _available = dataInputStream.available();
                if (_available > 0) {
                    //读出数据
                    packetSize = new byte[4]; //4个字节
                    dataInputStream.readFully(packetSize, 0, 4);
                    int size = ByteUtils.bytesToInt(packetSize); //前4个 [0, 0, 0, 39]

//                    Log.d(TAG, "_show_video: dataInputStream.available() = "
//                            + dataInputStream.available()
//                            + " size = " + size
//                            + " is_vodio = " + is_video
//                    );

                    byte[] packet = new byte[size];
                    dataInputStream.readFully(packet, 0, size);
//                    if (true) {
//                        continue;
//                    }

                    VideoPacket videoPacket = VideoPacket.fromArray(packet);
                    if (videoPacket.type == MediaPacket.Type.VIDEO) {
                        //解析正常 只有 VIDEO        AUDIO
                        byte[] data = videoPacket.data;
//                        Log.d(TAG, "_show_video: 配置--- videoPacket.flag = " + videoPacket.flag +
//                                "surface_resolution_wh = " + surface_resolution_w + " x " + surface_resolution_h);
                        if (is_first_run) {
                            /*
                             * 第一次运行----videoPacket.flag = CONFIG
                             * 第一次运行----videoPacket.flag = FRAME
                             * */
                            Log.d(TAG, "_show_video: 第一次运行----"
                                    + " videoPacket.flag = " + videoPacket.flag
                                    + " surface_resolution_w =" + surface_resolution_w
                                    + " surface_resolution_h =" + surface_resolution_h);

                            if (videoPacket.flag == VideoPacket.Flag.CONFIG) {
                                //第一次连通后数据， 中间锁屏 关闭后不再运行---复用 旋转时需重建这个
                                //KEY_FRAME  这个类型解析要着错
                                streamSettings = VideoPacket.getStreamSettings(data);
                            }

                            try {
                                //内部出错不中止 7007 端口
                                videoDecoder.configure_worker4surface(surface,
                                        surface_resolution_w, surface_resolution_h,
                                        streamSettings.sps, streamSettings.pps);
                            } catch (Exception e) {
                                Log.e(TAG, "_show_video: 第一次运行 configure 出错" + e.getMessage());
                                if (handler4ser != null) {
                                    Message msg = new Message();
                                    msg.what = MSG_WHAT;
                                    msg.arg1 = MSG_STATE_FAIL;
                                    msg.obj = "config 出错，请重新开启";
                                    handler4ser.sendMessage(msg);
                                    is_receive_update_video.set(false);
                                }
                            }
                            is_first_run = false;
                            if (is_receive_update_video.get()) {
                                Log.d(TAG, "_show_video: 第一次运行----配置成功");
                                Message msg = new Message();
                                msg.what = MSG_WHAT;
                                msg.arg1 = MSG_STATE_SUCCESS;
                                msg.obj = "config 成功";
                                handler4ser.sendMessage(msg);
                            }

                        } else if (videoPacket.flag == VideoPacket.Flag.CONFIG) {
                            Log.d(TAG, "_show_video: 屏幕在旋转---- is_first_run = " + is_first_run);
                            streamSettings = VideoPacket.getStreamSettings(data);
                            if (!is_first_run) {
                                // 子进程更新 ctrActivity
                                if (onScrcpyServiceRotationCallbacks != null) {
                                    onScrcpyServiceRotationCallbacks.on_remote_rotation();
                                }
                            }
                            is_receive_update_video.set(false); //暂停读视频 等待外部信号
                            is_first_run = true; //开启配置
                            //重新运行配置
                        } else if (videoPacket.flag == VideoPacket.Flag.END) {
                            // FRAME((byte) 0), KEY_FRAME((byte) 1)暂时无, CONFIG((byte) 2), END((byte) 4)暂时无  结束不处理
                            Log.e(TAG, "_show_video: 视频是END((byte) 4 结束标志 ");
                        } else {
                            if (is_video) {
                                // FRAME
                                videoDecoder.decodeSample(data, 0, data.length, 0, videoPacket.flag.getFlag());
//                                Log.d(TAG, "_show_video: videoDecoder.decodeSamp  videoPacket.flag = " + videoPacket.flag);
                            }
                        }
                    } else {
                        Log.e(TAG, "_show_video:  videoPacket.type 类型错误 = " + videoPacket.type);
                    }
                }
            } else {
                //等待控制信号
//                Log.d(TAG, "_show_video: 等待控制信号 is_socket_update_video = " + is_socket_update_video);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start_services4delay(String serverAdr) {
        //带延时功能
        this.serverAdr = serverAdr;
//        this.handler4link = handler; //通过接口回调

        DelayThread2Main<String, String> delayThread2Main = new DelayThread2Main<String, String>(7, new DelayThread2Main.FunDelay() {
            @Override
            public void on_fun_run4thread(DelayThread2Main that) {
                start_services4thread(that);
            }
        }, new Thread2Main.OnThread2MainCallback<String, String, String>() {
            @Override
            public void on_fun_running(int status_run, String obj) {

            }

            @Override
            public void on_fun_res_success(int status_run, String obj) {
                if (onScrcpyServiceStateCallbacks != null) {
                    onScrcpyServiceStateCallbacks.on_fun_success4scrcpy(status_run, obj);
                }
            }

            @Override
            public void on_fun_res_fail(int status_run, String obj) {
                if (onScrcpyServiceStateCallbacks != null) {
                    onScrcpyServiceStateCallbacks.on_fun_fail4scrcpy(status_run, obj);
                }
            }
        });

        delayThread2Main.start();
        is_socket_success.set(true);
    }

    public void start_ser(Surface surface, Handler handler,
                          int surface_resolution_w, int surface_resolution_h) {
        Log.d(TAG, "start_ser: ---");
        this.surface = surface;
        this.handler4ser = handler;
        this.surface_resolution_w = surface_resolution_w;
        this.surface_resolution_h = surface_resolution_h;

        this.is_first_run = true;
        is_video = true;
        is_receive_update_video.set(true);
        this.videoDecoder = new VideoDecoder();
        videoDecoder.start_worker_thread(); //主线程控制子线程，连接并解码
    }

    public boolean touch_event(MotionEvent touch_event, int displayW, int displayH) {
        /**
         * 用于 sv_decoder setOnTouchListener 联动调用
         * displayW = sv_decoder.getWidth()
         * */
        if (is_service_running.get()) {
            //这里需要真实的屏幕尺寸
            int x = (int) touch_event.getX(); //这个是获取 sv_decoder 的分辨率
            int y = (int) touch_event.getY();
            // 这里获取的是实际设置的分辨率的点击位置
            int _x = x * surface_resolution_w / displayW;
            int _y = y * surface_resolution_h / displayH;
            Log.d(TAG, "touch_event: _x=" + _x + " _y=" + _y);
            int[] buf = new int[]{touch_event.getAction(), touch_event.getButtonState(), _x, _y};
            final byte[] array = new byte[buf.length * 4]; // https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
            for (int j = 0; j < buf.length; j++) {
                final int c = buf[j];
                array[j * 4] = (byte) ((c & 0xFF000000) >> 24);
                array[j * 4 + 1] = (byte) ((c & 0xFF0000) >> 16);
                array[j * 4 + 2] = (byte) ((c & 0xFF00) >> 8);
                array[j * 4 + 3] = (byte) (c & 0xFF);
            }
//            Log.d(TAG, "touch_event: array=" + Arrays.toString(array));
            event_val_bytes = array;
            return true;
        } else {
            return false;
        }

    }

    public void resume() {
        Log.d(TAG, "resume: -----------------");
        this.is_first_run = true;
        is_video = true;
//        is_receive_update_video.set(true);
        if (videoDecoder == null) {
            this.videoDecoder = new VideoDecoder();
            videoDecoder.start_worker_thread();
        }
    }

    public void pause() {
        //熄屏  数据仍然在传输， 但图像不会堆积  可以进行 触控
        Log.d(TAG, "ScrcpyService pause: -----------------");
        if (videoDecoder != null) {
//            is_receive_update_video.set(false);
            videoDecoder.stop_worker_thread();
            videoDecoder = null;
            is_video = false;
        }
    }

    private void close_socket() {
        if (socket != null && !socket.isClosed()) {
            try {
                dataInputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "close_socket: IOException dataInputStream" + e.getMessage());
            }
            try {
                dataOutputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "close_socket: IOException dataOutputStream" + e.getMessage());
            }
            try {
                socket.close();
                Thread.sleep(100);
                socket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.w(TAG, "close_socket: 确认已关闭");
    }

    public void stop_service() {
        this.pause();
        is_service_running.set(false);
        is_socket_success.set(false);
        close_socket();
//        stopSelf();
    }

    public int[] get_remote_resolution_wh() {
        return remote_resolution_wh;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop_service();
        boolean _res = super.onUnbind(intent);
        Log.d(TAG, "onUnbind: 完成 ---");
        return _res;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        /* bindService() 客户端绑定到服务时调用*/
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ---");
//        stop_service();
    }

    public void send_keyevent(int keycode) {
        int[] buf = new int[]{keycode};
        // https://stackoverflow.com/questions/2183240/java-integer-to-byte-array
        final byte[] array = new byte[buf.length * 4];
        for (int j = 0; j < buf.length; j++) {
            final int c = buf[j];
            array[j * 4] = (byte) ((c & 0xFF000000) >> 24);
            array[j * 4 + 1] = (byte) ((c & 0xFF0000) >> 16);
            array[j * 4 + 2] = (byte) ((c & 0xFF00) >> 8);
            array[j * 4 + 3] = (byte) (c & 0xFF);
        }
        Log.d(TAG, "send_keyevent: array" + array);
        event_val_bytes = array;
    }

    public interface OnScrcpyServiceRotationCallbacks {
        void on_remote_rotation();

    }

    public interface OnScrcpyServiceStateCallbacks {
        // 当7707 连接时获取尺寸

        void on_remote_resolution_gain(int[] remote_resolution_wh);

        void on_fun_running4scrcpy(int status_run, String text); //运行时回馈

        void on_fun_fail4scrcpy(int status_run, String text);

        void on_fun_success4scrcpy(int statusRun, String text);


    }

    public void setOnScrcpyServiceRotationCallbacks(OnScrcpyServiceRotationCallbacks onScrcpyServiceRotationCallbacks) {
        this.onScrcpyServiceRotationCallbacks = onScrcpyServiceRotationCallbacks;
    }

    public void setOnScrcpyServiceStateCallbacks(OnScrcpyServiceStateCallbacks callbacks) {
        // 定义同级接口 加set方法  加调用
        onScrcpyServiceStateCallbacks = callbacks;
    }
}
