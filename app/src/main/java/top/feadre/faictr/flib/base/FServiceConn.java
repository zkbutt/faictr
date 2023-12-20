package top.feadre.faictr.flib.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * 这个用于服务绑定时，进行停止，和状态处理的逻辑
 * 服务需要注册哦
 */
public abstract class FServiceConn<T> implements ServiceConnection {
    private static final String TAG = "FServiceConn";
    protected Activity activity;
    protected boolean isServiceBound = false;

    public FServiceConn(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        isServiceBound = false;
    }

    public void fstart_service(Class<T> cls) {
        // 先停止再绑定
        if (getServiceBound()) {
            fstop_service();
        }
        //网 PING 成功，开始绑定并开启ADB  执行 conn_adb_shell 及回调
        Intent intent = new Intent(activity, cls);
        activity.bindService(intent, this, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "fstart_service: 绑定中......");
    }

    public void fstop_service() {
        Log.d(TAG, "fstop_service: ---");
        if (getServiceBound()) {
            try {
                activity.unbindService(this);//这个触发是子线程
            } catch (Exception e) {
                Log.w(TAG, "fstop_service unbindService(conn_adb_shell):出错 " + e.getMessage());
            }
            setServiceBound(false);
        }
    }

    public void setServiceBound(boolean b) {
        isServiceBound = b;
    }

    public boolean getServiceBound() {
        return isServiceBound;
    }
}
