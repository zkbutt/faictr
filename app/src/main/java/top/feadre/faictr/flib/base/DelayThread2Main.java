package top.feadre.faictr.flib.base;

import android.os.Message;
import android.util.Log;


public class DelayThread2Main<RUNNING, SUCCESS> extends Thread2Main<RUNNING, SUCCESS, String> {
    /**
     * DelayThread2Main  传入方法自动实现超时检测
     */
    private static final String TAG = "DelayThread2Main";
    static final String TIME_OUT_HINT = "fun 耗时连接超时";

    private int overtimeAdb;//5秒连接超时
    private boolean isCheckoutDelay = true; //0：未运行 1：运行中
    private Thread thread_heartbeat;
    private FunDelay funDelay;

    public DelayThread2Main(int overtimeAdb, FunDelay funDelay, OnThread2MainCallback<RUNNING, SUCCESS, String> callback) {
        super(callback);
        this.overtimeAdb = overtimeAdb;
        this.funDelay = funDelay;
    }

    //延时监控
    public void start() {
        isCheckoutDelay = true; // 时间检测运行中
        is_send_fail.set(true);
        Thread thread_run_fun = new Thread(new Runnable() {
            @Override
            public void run() {
                if (funDelay != null) {

                    // 可能成功,可能一直运行
                    funDelay.on_fun_run4thread(DelayThread2Main.this);
                }
                Log.d(TAG, "thread_run_fun: on_fun_run4thread 运行完成 ");
            }
        });
        thread_run_fun.setName("thread_run_fun");
        thread_run_fun.start();

        thread_heartbeat = new Thread(new Runnable() {
            @Override
            public void run() {
                int _overtime_adb = overtimeAdb;//5秒
                _overtime_adb = _overtime_adb * 10;
                while (_overtime_adb != 0 && isCheckoutDelay == true) {
                    try {
                        Thread.sleep(100);//连接10秒钟
//                        Log.d(TAG, "frun 耗时连接检测中: ........" + _overtime_adb
//                                + "  is_running = " + is_running);
                    } catch (InterruptedException e) {
                        e.printStackTrace(); //sleep 打断后报错
                        return;
                    } finally {
                        _overtime_adb--;
                    }
                }

                if (_overtime_adb == 0) {
                    //连接超时
                    try {
                        if (!thread_run_fun.isInterrupted()) {
                            thread_run_fun.interrupt();
                        }
                        thread_run_fun.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (funDelay != null) {
                        DelayThread2Main.this.run_over_fail(TIME_OUT_HINT);
                    }
                    Log.e(TAG, TIME_OUT_HINT);
                }
            }
        });
        thread_heartbeat.setName("thread_heartbeat");
        thread_heartbeat.start();
    }

    public void setCheckoutDelay(boolean checkoutDelay) {
        this.isCheckoutDelay = checkoutDelay;
    }

    @Override
    public void run_over_success(SUCCESS t3) {
        setCheckoutDelay(false);
        super.run_over_success(t3);
    }

    @Override
    public void run_over_fail(String t2) {
        setCheckoutDelay(false);
        super.run_over_fail(t2);
    }

    @Override
    protected void send_fail(Message msg, int status) {
        super.send_fail(msg, status);
        if (msg.obj.equals(TIME_OUT_HINT)) {
            Log.w(TAG, "send_fail: 耗时连接超时");
            is_send_fail.set(false);
        }
    }

    //    public void setFunDelay(FunDelay funDelay) {
//        this.funDelay = funDelay;
//    }

    public interface FunDelay {
        //这个接口只能在handler中调  这里泛型不好用
        void on_fun_run4thread(DelayThread2Main that); //自定义方法 子线程
    }

}

