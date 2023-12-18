package top.feadre.faictr.activitys;

import android.widget.Toast;

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;

import java.util.LinkedList;

import top.feadre.faictr.R;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.base.FProgressDialog;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.flib.fviews.dialog_edit.EntityItem4SimpleRecyclerAdapter;
import top.feadre.faictr.flib.net.LocalNetUtil;

public class FMainHelp4NetUtils extends LocalNetUtil implements Thread2Main.OnThread2MainCallback<String, LinkedList<String>, String> {
    private static final String TAG = "FMainH4NetUtils";
    private final FMainActivity fMainActivity;

    public FMainHelp4NetUtils(FMainActivity fMainActivity) {
        super(fMainActivity, null);
        this.fMainActivity = fMainActivity;
        this.setOnThread2MainCallback(this);
    }

    @Override
    public void on_fun_running(int status_run, String obj) {
        // 解析更新进度
        String[] _s = this.parse_running_info(obj);
        if (_s != null) {
            fMainActivity.fMainHelp4FProgressDialog.updateProgress(Integer.parseInt(_s[1]), _s[0]);
        }
    }

    @Override
    public void on_fun_res_success(int status_run, LinkedList<String> obj) {
        FTools.log_d(TAG, "on_fun_res_success obj = " + obj.toString());
        String _txt = String.format(
                ResUtils.getString(R.string.FMainHelp4NetUtils_on_fun_res_success),
                obj.size(),
                obj.toString());
        XToastUtils.success(_txt, Toast.LENGTH_LONG);
        if (obj.size() == 1) {
            fMainActivity.vet_ip.setText(obj.get(0));
        }
        //添加到历史
        for (String ip : obj) {
            fMainActivity.fMainDialogBottomEdit.addData(
                    new EntityItem4SimpleRecyclerAdapter("未知", ip));
        }

    }

    @Override
    public void on_fun_res_fail(int status_run, String obj) {
        // 促发对话框取消
        XToastUtils.error(obj, Toast.LENGTH_LONG);
        fMainActivity.fMainHelp4FProgressDialog.close();
    }
}
