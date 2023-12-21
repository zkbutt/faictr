package top.feadre.faictr.activitys;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.services.scrcpy.ScrcpyService;


public class CtrHandler extends Handler {
    private static final String TAG = "CtrHandler";
    private CtrActivity obj_activity;

    public CtrHandler(CtrActivity that) {
        this(Looper.getMainLooper());
        this.obj_activity = that;
    }

    public CtrHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ScrcpyService.MSG_WHAT:
                String text = (String) msg.obj;

                if (msg.arg1 == ScrcpyService.MSG_STATE_SUCCESS) {
                    FToolsAndroid.ftoast(obj_activity, text);
                } else if (msg.arg1 == ScrcpyService.MSG_STATE_FAIL) {
                    FToolsAndroid.ftoast(obj_activity, text, Color.RED);
                } else {
                    Log.e(TAG, "handleMessage: 状态错误 msg.arg1 = " + msg.arg1);
                }
                break;
//            case 5:
//                //Scrcpy 连接超时
//                FToolsAndroid.ftoast(obj_activity, "Scrcpy 连接超时 ...");
//                obj_activity.finish();
//                break;
//            case 6:
//                //Scrcpy 连接超时
//                FToolsAndroid.ftoast(obj_activity, "Scrcpy 连接出错 ...");
//                obj_activity.finish();
//                break;
            default:
                break;
        }
    }
}
