package top.feadre.faictr.activitys;


import android.content.ComponentName;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.xuexiang.xtask.logger.TaskLogger;
import com.xuexiang.xui.utils.XToastUtils;

import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.base.FServiceConn;
import top.feadre.faictr.services.scrcpy.ScrcpyService;

public class FMainHelp4ScrcpyService extends FServiceConn<ScrcpyService> implements ScrcpyService.OnScrcpyServiceStateCallbacks {
    private static final String TAG = "Help4ScrcpyService";
    private final FMainActivity fmainActivity;
    private ScrcpyService serviceScrcpy;

    public ScrcpyService getServiceScrcpy() {
        return serviceScrcpy;
    }

    public FMainHelp4ScrcpyService(FMainActivity fmainActivity) {
        super(fmainActivity);
        this.fmainActivity = fmainActivity;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        //这个必填
        FTools.log_d(TAG, "onServiceConnected: 开始中...");
        serviceScrcpy = ((ScrcpyService.ScrcpyBinder) iBinder).getService();
        serviceScrcpy.setOnScrcpyServiceStateCallbacks(this); //设置旋转CALLBACK
        serviceScrcpy.start_services4delay(fmainActivity.control_ip_str);
        super.onServiceConnected(componentName, iBinder);
    }


    @Override
    public void on_remote_resolution_gain(int[] remote_resolution_wh) {
        // 这个在子进程不能更新 UI
        FTools.log_d(TAG, "on_remote_resolution_change: ---w: "
                + remote_resolution_wh[0] + "  h: " + remote_resolution_wh[1]);
        fmainActivity.remote_ctr_resolution_wh[0] = remote_resolution_wh[0];
        fmainActivity.remote_ctr_resolution_wh[1] = remote_resolution_wh[1];
        // 成功后更新 s _resolution 和UI
    }

    @Override
    public void on_fun_running4scrcpy(int status_run, String text) {
        FTools.log_d(TAG, "on_fun_running4scrcpy: text = " + text);
        fmainActivity.fMainHelp4FProgressDialog.updateProgress(60, text);
    }

    @Override
    public void on_fun_fail4scrcpy(int status_run, String text) {
        FTools.log_d(TAG, "on_fun_fail4scrcpy: ---" + status_run + " text = " + text);
        fstop_service(); //服务启动失败 关闭
        XToastUtils.error(text);
        isServiceBound = false;

        fmainActivity.is_bt_one_link = false; //服务失败时
        fmainActivity.fMainHelp4FProgressDialog.close();
    }

    @Override
    public void on_fun_success4scrcpy(int statusRun, String text) {
        int[] remoteDeviceResolution = serviceScrcpy.get_remote_resolution_wh();
        fmainActivity.remote_ctr_resolution_wh[0] = remoteDeviceResolution[0];
        fmainActivity.remote_ctr_resolution_wh[1] = remoteDeviceResolution[1];
        FTools.log_d(TAG, "on_fun_success4scrcpy: ---w: "
                + fmainActivity.remote_ctr_resolution_wh[0] + " h: " +  fmainActivity.remote_ctr_resolution_wh[1]);
        isServiceBound = true;

        fmainActivity.update_surface_resolution();

        if (fmainActivity.is_bt_one_link) {
            fmainActivity.start_ctr_activity();
            fmainActivity.fMainHelp4FProgressDialog.updateProgress(95, text);
        } else {
            XToastUtils.success(text + ",可以开始传输");
            fmainActivity.fMainHelp4FProgressDialog.updateProgress(100, text);
        }
    }
}
