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

package top.feadre.faictr.services.adb;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.tananaev.adblib.AdbConnection;
import com.tananaev.adblib.AdbCrypto;
import com.tananaev.adblib.AdbStream;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.base.DelayThread2Main;
import top.feadre.faictr.flib.base.Thread2Main;

/**
 * 服务需要注册 用于进行 adb 命令的响应逻辑
 */
public class ADBShellService extends Service implements DelayThread2Main.FunDelay {
    private static final String TAG = "ADBShellService";
    private static final String KEY_PUBLIC = "public.key";
    private static final String KEY_PRIVATE = "private.key";
    private static final String ORDER_SHELL = "shell:";
    public static final int ID_ADB_ORDER_DEFAULT = -1;
    public static final int ID_ADB_ORDER_INIT = -2;
    protected final static int MSG_WHAT_OVER_FIRST = 101;
    protected final static int MSG_WHAT_OVER = 102;
    protected final static int MSG_WHAT_OVER_LINE = 103;
    private boolean isAdbRunJar = false; //用于处理进度 如果是运行jar 则进度减半 由 startConnect 中转传入
    private String host;
    private int port;
    private AdbConnection adbConnection;
    public AdbStream shellStream;
    private Activity activity;
    private DelayThread2Main<String, String> adbDelayThread2Main;
    private LinkedBlockingQueue<ADBShellOrderEntity> commandQueue = new LinkedBlockingQueue<ADBShellOrderEntity>(); //自动阻塞队列
    private final IBinder mBinder = new ADBShellBinder();
    private Socket obj_socket;
    private boolean isCommandLoop; //sendCommandLoop 正在工作
    private boolean isInitSuccess; //shell初始化成功
    private OnADBShellResListener onADBShellResListener;
    private AtomicBoolean is_lock_command = new AtomicBoolean(true);//不再接收命令
    private ADBShellOrderEntity obj_order_entity;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (onADBShellResListener == null) {
                return;
            }

            ADBShellOrderEntity obj = null;
            switch (msg.what) {
                // 三种消息类型
                case MSG_WHAT_OVER_FIRST://第一次连接成功的反馈不处理
                    obj = (ADBShellOrderEntity) msg.obj;
                    onADBShellResListener.onInitSuccess(obj);
                    onADBShellResListener.onResText(obj);
                    break;
                case MSG_WHAT_OVER://一个命令的结果输出完结
                    obj = (ADBShellOrderEntity) msg.obj;
                    onADBShellResListener.onResText(obj);
                    break;
                case MSG_WHAT_OVER_LINE://一个命令的过程输出 第行
                    String res_text = (String) msg.obj;
                    int isFinish = msg.arg1;
                    onADBShellResListener.onResTextLine(res_text, obj_order_entity.getOrderId(), isFinish);
                    break;
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        int overtimeAdb = 10;
        adbDelayThread2Main = new DelayThread2Main<String, String>(overtimeAdb, this, null);
    }

    public void setOnThread2MainCallback(Thread2Main.OnThread2MainCallback<String, String, String> l) {
        //表示ADB启动成功
        adbDelayThread2Main.setOnThread2MainCallback(l);
    }

    public void setOnADBShellResListener(OnADBShellResListener onADBShellResListener) {
        this.onADBShellResListener = onADBShellResListener;
    }

    public class ADBShellBinder extends Binder {

        public ADBShellService getService() {
            return ADBShellService.this;
        }
    }

    public void startConnect(Activity that, String host, int port, boolean isAdbRunJar) {
        this.activity = that;
        this.host = host;
        this.port = port;
        this.isAdbRunJar = isAdbRunJar;

        adbDelayThread2Main.start();//子线程运行
    }

    private void startReceiveThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                StringBuilder response = new StringBuilder();
                    ArrayList<String> res_list = new ArrayList<>();
                    obj_order_entity = new ADBShellOrderEntity(null, ID_ADB_ORDER_INIT, true);//第一次不shell的回应不要
                    while (shellStream != null && !shellStream.isClosed() && !isInitSuccess) {
                        byte[] data = shellStream.read(); //阻塞读 卡在这里 会读出命令加回车
                        int isFinish = 0;
                        String res_text = String.valueOf(ADBCommands.bytes2texts(data));
                        res_list.add(res_text);
                        if (res_text.endsWith("$ ") || res_text.endsWith("# ")) {
                            isFinish = 1;
                            isInitSuccess = true;
                            obj_order_entity.setResText(parse_res_text(res_list));
                            obj_order_entity.setResList((ArrayList<String>) res_list.clone());
                            send_msg4obj(MSG_WHAT_OVER_FIRST, -101, obj_order_entity);
                            res_list.clear();
                            is_lock_command.set(false); //初始化成功才解锁
                        }
                        send_msg4obj(MSG_WHAT_OVER_LINE, isFinish, res_text);
                    }


                    Log.d(TAG, "startReceiveThread run: 初始化成功，开始正常接收");
                    /** -------------- 阻塞接收 -------------------- */
                    while (shellStream != null && !shellStream.isClosed()) { //会自动完成
//                        Log.d(TAG, "startReceiveThread: shell更新正常，阻塞前 ---");

                        byte[] data = shellStream.read(); //阻塞读 卡在这里 会读出命令加回车
//                        Log.d(TAG, "startReceiveThread - shell更新正常读: "
//                                + "data = " + String.valueOf(ADBCommands.bytes2texts(data))
//                                + " length = " + data.length
//                        );
                        int isFinish = 0;
                        String res_text = String.valueOf(ADBCommands.bytes2texts(data));
                        res_list.add(res_text);
                        if (res_text.endsWith("$ ") || res_text.endsWith("# ")) {
                            isFinish = 1;
                            if (obj_order_entity.isReadRes()) {
                                obj_order_entity.setResText(parse_res_text(res_list));
                                obj_order_entity.setResList((ArrayList<String>) res_list.clone());
                                send_msg4obj(MSG_WHAT_OVER, -101, obj_order_entity);
                            }
                            res_list.clear();
                            is_lock_command.set(false); //这条命令完成
                        }
                        if (obj_order_entity.isReadRes()) {
                            send_msg4obj(MSG_WHAT_OVER_LINE, isFinish, res_text);
                        }
                    }
                } catch (Exception e) {
                    FToolsAndroid.fthrow_err(TAG, "startReceiveThread run: 出错", e);
                } finally {
                    stop_service();
                }
            }
        }).start();
    }

    private String parse_res_text(ArrayList<String> resList) {
        String res = "";
        for (String t : resList) {
            res += t;
        }
        return res;
    }

    private void sendCommandLoop() {
        //用来阻塞执行任务
        Log.d(TAG, "sendCommandLoop: 开始");

        adbDelayThread2Main.running_info("sendCommandLoop 全部成功！ "
                + FCFGBusiness.PARSE_STRING + getProgressVal(100));
        adbDelayThread2Main.run_over_success("启动成功");
        adbDelayThread2Main.setCheckoutDelay(false); //不用再超时检测

        isCommandLoop = true;
        try {
            while (isCommandLoop) {
                if (is_lock_command.get()) {
                    Thread.sleep(300);
                } else {
                    obj_order_entity = commandQueue.take();

                    Log.d(TAG, "sendCommandLoop: 取出一次命令 = "
                            + String.valueOf(ADBCommands.bytes2texts(obj_order_entity.getOrderByte()))
                            + "  id = " + obj_order_entity.getOrderId());
                    /* This may be a close indication */
                    if (shellStream.isClosed()) {
                        Log.w(TAG, "sendCommandLoop: isClosed 关闭");
                        break;
                    }
                    is_lock_command.set(true);
                    shellStream.write(obj_order_entity.getOrderByte());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "sendCommandLoop:  进程被打断" + e.getMessage());

        } catch (IOException e) {
            Log.e(TAG, "sendCommandLoop:  shellStream" + e.getMessage());
            adbDelayThread2Main.run_over_fail("shellStream 出错" + e.getMessage());
            return;
        } finally {
            stop_service();
            Log.w(TAG, "sendCommandLoop: 停止");
        }
    }

    private int getProgressVal(int val) {
        // 三目运算 直接进度除以2
        return isAdbRunJar ? val / 2 : val;
    }

    @Override
    public void on_fun_run4thread(DelayThread2Main that) {
        /**用DelayThread2Main start启动，这个就是核心运行方法  用于启动并进行循环侦听 */
        obj_socket = new Socket();
        try {
            //默认端口 5555
            obj_socket.connect(new InetSocketAddress(host, port), 5000);
        } catch (IOException e) {
            adbDelayThread2Main.run_over_fail("socket 连接失败！ "
                    + "\nhost = " + host
                    + ":" + port + "\n错误详情：" + e.getMessage());
            return;
        }
        adbDelayThread2Main.running_info("socket 连接成功！ "
                + " host = " + host
                + ":" + port
                + FCFGBusiness.PARSE_STRING + getProgressVal(15));

        AdbCrypto crypto = getCrypto(activity);
        if (crypto == null) {
            adbDelayThread2Main.run_over_fail("crypto 为 null 失败！ ");
            return;
        }
        adbDelayThread2Main.running_info("crypto 成功！ "
                + FCFGBusiness.PARSE_STRING + getProgressVal(20));

        try {
            adbConnection = AdbConnection.create(obj_socket, crypto);
            adbConnection.connect();
            adbDelayThread2Main.running_info("AdbConnection 成功！ "
                    + FCFGBusiness.PARSE_STRING + 30);
        } catch (IOException e) {
            adbDelayThread2Main.run_over_fail("adbConnection.connect 失败！ " + e.getMessage());
            return;
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            Log.e(TAG, "on_fun_run4thread: InterruptedException " + e.getMessage());
            return;
        }

        try {
            shellStream = adbConnection.open(ORDER_SHELL);
            adbDelayThread2Main.running_info("shell 成功！ "
                    + FCFGBusiness.PARSE_STRING + getProgressVal(50));
        } catch (UnsupportedEncodingException e) {
            adbDelayThread2Main.run_over_fail("adbConnection.open 失败！ \n错误详情：UnsupportedEncodingException:" + e.getMessage());
            return;
        } catch (IOException e) {
            adbDelayThread2Main.run_over_fail("adbConnection.open 失败！ \nIOException:" + e.getMessage());
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "on_fun_run4thread: InterruptedException" + e.getMessage());
            return;
        }

        startReceiveThread();
        adbDelayThread2Main.running_info("startReceiveThread 成功！ "
                + FCFGBusiness.PARSE_STRING + getProgressVal(80));

        sendCommandLoop(); //阻塞方法
        Log.w(TAG, "on_fun_run4thread: 运行完成 ");
    }

    private void stop_service() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isInitSuccess = false;
                try {
                    isCommandLoop = false;
                    commandQueue.clear();

                    if (shellStream != null && !shellStream.isClosed()) {
                        shellStream.close();
                        shellStream = null;
                        if (adbConnection != null) {
                            adbConnection.close();
                            adbConnection = null;
                        }
                    }
                    if (obj_socket != null && !obj_socket.isClosed()) {
                        obj_socket.close();
                        obj_socket = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public AdbCrypto getCrypto(Activity that) {
        File filesDir = that.getFilesDir();
        AdbCrypto crypto = readCrypto(filesDir);
        //读不倒就创建
        if (crypto == null) {
            crypto = writeNewCryptoConfig(filesDir);
        }
        if (crypto == null) {
//            throw new RuntimeException(new Exception("crypto = null 无法创建"));
            return null;
        }
        return crypto;
    }

    private static AdbCrypto readCrypto(File dataDir) {
        File pubKey = new File(dataDir, KEY_PUBLIC);
        File privKey = new File(dataDir, KEY_PRIVATE);

        AdbCrypto crypto = null;
        if (pubKey.exists() && privKey.exists()) {
            try {
                crypto = AdbCrypto.loadAdbKeyPair(new ADBBase64(), privKey, pubKey);
            } catch (Exception e) {
                crypto = null;
            }
        }

        return crypto;
    }

    private static AdbCrypto writeNewCryptoConfig(File dataDir) {
        File pubKey = new File(dataDir, KEY_PUBLIC);
        File privKey = new File(dataDir, KEY_PRIVATE);

        AdbCrypto crypto = null;

        try {
            crypto = AdbCrypto.generateAdbKeyPair(new ADBBase64());
            crypto.saveAdbKeyPair(privKey, pubKey);
        } catch (Exception e) {
            crypto = null;
        }

        return crypto;
    }

    public void executeCommand(byte[] order_byte, int order_id, boolean isReadRes) {
        // -------------  最终命令
        if (isInitSuccess) {
            commandQueue.add(new ADBShellOrderEntity(order_byte, order_id, isReadRes));
        } else {
            Log.d(TAG, "executeCommand: 未初始化");
        }
    }

    public void executeCommand(byte[] order_byte) {
        executeCommand(order_byte, ID_ADB_ORDER_DEFAULT, true);
    }

    public void executeCommand(String order_text, int order_id) {
        // 联动 sendCommandLoop
        try {
            byte[] _b = order_text.getBytes("UTF-8");
            executeCommand(_b, order_id, true);
        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
            Log.e(TAG, "executeCommand: getBytes出错" + e.getMessage());
        }
    }

    public void executeCommand(String order_text, int order_id, boolean isReadRes) {
        // 联动 sendCommandLoop
        try {
            byte[] _b = order_text.getBytes("UTF-8");
            executeCommand(_b, order_id, isReadRes);
        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
            Log.e(TAG, "executeCommand: getBytes出错" + e.getMessage());
        }
    }

    public void executeCommand(String order_text) {
        executeCommand(order_text, ID_ADB_ORDER_DEFAULT);
    }

    public void executeCtrlC() {
        byte[] ctr_ctrl_c = {0x03};
        executeCommand(ctr_ctrl_c);
        is_lock_command.set(false); //解除锁定
    }

    public boolean isInitSuccess() {
        return isInitSuccess;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stop_service();
        return super.onUnbind(intent);
    }

    public interface OnADBShellResListener {
        //adb shell命令完成后 启动完成
        void onInitSuccess(ADBShellOrderEntity objOrderEntity);


        void onResTextLine(String resText, int orderId, int isFinish);

        void onResText(ADBShellOrderEntity objOrderEntity);
    }

    private void send_msg4obj(int type_msg, int status, Object o) {
        Message msg = new Message();
        msg.what = type_msg;
        msg.arg1 = status;
        msg.obj = o;
        this.mHandler.sendMessage(msg);
    }

}
