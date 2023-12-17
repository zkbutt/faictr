package top.feadre.faictr.flib.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;


public class Thread2Main<RUNNING, SUCCESS, FAIL> {
    /**
     * 通过 running_info(RUNNING t1)  run_over_success(SUCCESS t3) run_over_fail(FAIL t2)
     * 在子线程中自动进行转换， 再通过接口返回到主线程
     */
    private static final String TAG = "Thread2Main";
    protected OnThread2MainCallback onThread2MainCallback;
    protected final static int STATUS_FAIL = 0;
    protected final static int STATUS_SUCCESS = 1;
    protected final static int STATUS_RUNNING = 2;
    protected final static int MSG_WHAT_RUNNING = 2;
    protected final static int MSG_WHAT_RES = 3;
    protected AtomicBoolean is_send_fail = new AtomicBoolean(true);


    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_RUNNING:
                    if (onThread2MainCallback != null) {
                        int status = msg.arg1; //状态
                        onThread2MainCallback.on_fun_running(status, msg.obj);
                    }
                    break;
                case MSG_WHAT_RES:
                    //运行结果包括成功和失败

                    if (onThread2MainCallback != null) {
                        int status = msg.arg1; // 0是运行失败    1是运行成功
                        if (status == STATUS_SUCCESS) {
                            onThread2MainCallback.on_fun_res_success(status, msg.obj);
                        } else if (status == STATUS_FAIL) {
                            send_fail(msg, status);
                        }
                    }
                    break;
                default: {
                    Log.e(TAG, "handleMessage: msg.what 错误 msg.what = " + msg.what);
                }

            }
//            Log.d(TAG, "handleMessage: 检测完成");
        }
    };

    protected void send_fail(Message msg, int status) {
        //运行在主进程
        if (is_send_fail.get()) {
            onThread2MainCallback.on_fun_res_fail(status, msg.obj);
        }
    }


    public Thread2Main() {
//        super();
    }

    public Thread2Main(OnThread2MainCallback<RUNNING, SUCCESS, FAIL> callback) {
        onThread2MainCallback = callback;
//        new Thread2Main();
    }

    public void running_info(RUNNING t1) {
        send_msg4obj(MSG_WHAT_RUNNING, STATUS_RUNNING, t1);
    }

    public void run_over_success(SUCCESS t3) {
        send_msg4obj(MSG_WHAT_RES, STATUS_SUCCESS, t3);
    }

    public void run_over_fail(FAIL t2) {
        send_msg4obj(MSG_WHAT_RES, STATUS_FAIL, t2);
    }


    private void send_msg4obj(int type_msg, int status, Object o) {
        Message msg = new Message();
        msg.what = type_msg;
        msg.arg1 = status;
        msg.obj = o;
        this.mHandler.sendMessage(msg);
    }

    public void setOnThread2MainCallback(OnThread2MainCallback<RUNNING, SUCCESS, FAIL> onThread2MainCallback) {
        this.onThread2MainCallback = onThread2MainCallback;
    }

    /**
     * 要传什么类型 在定义继续时，通过接口指定
     */
    public interface OnThread2MainCallback<RUNNING, SUCCESS, FAIL> {
        //这个接口只能在handler中调
        void on_fun_running(int status_run, RUNNING obj); //运行时回馈   主线程

        void on_fun_res_success(int status_run, SUCCESS obj);// 完成时 主线程

        void on_fun_res_fail(int status_run, FAIL obj);// 完成时    主线程
    }
}
