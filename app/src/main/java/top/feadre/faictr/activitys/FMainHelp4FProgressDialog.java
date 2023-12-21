package top.feadre.faictr.activitys;

import android.content.DialogInterface;


import com.xuexiang.xui.utils.ResUtils;

import top.feadre.faictr.R;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.base.FProgressDialog;

public class FMainHelp4FProgressDialog extends FProgressDialog implements FProgressDialog.OnFProgressDialogListener {
    private static final String TAG = "FMainH4FProgressDialog";
    private final FMainActivity fMainActivity;

    public FMainHelp4FProgressDialog(FMainActivity fMainActivity) {
        super(fMainActivity);
        this.setOnFProgressDialogListener(this);
        this.fMainActivity = fMainActivity;
    }

    public void close() {
        dialog.dismiss();
    }


    @Override
    public void on_dialog_success(String id, DialogInterface di) {
        FTools.log_d(TAG, "任务成功 id = " + id);
    }

    @Override
    public void on_dialog_cancel(String id, DialogInterface di) {
        //区别在于 进度值  this.progress_val
        FTools.log_d(TAG, "任务取消 this.progress_val = " + this.progress_val
                + " id = " + id);
        if (id.equals(ResUtils.getString(R.string.FMainHelp4FProgressDialog_bt_ip_search))) {
            fMainActivity.fMainHelp4NetUtils.stop_scan_ip();
        } else if (id.equals("快速ADB")) {
            //这个可能需要无法取消
        }
    }


}
