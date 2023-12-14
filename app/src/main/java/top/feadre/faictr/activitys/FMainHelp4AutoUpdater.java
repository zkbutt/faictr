package top.feadre.faictr.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.xuexiang.xui.utils.XToastUtils;

import java.util.ArrayList;
import java.util.List;

import top.feadre.faictr.flib.base.FAutoUpdater;


public class FMainHelp4AutoUpdater {
    private static final String TAG = "Help4AutoUpdater";
    private final Activity activity;
    private final String name_apk_cfg;
    private final String url_path;
    private AlertDialog alertDialog;


    public FMainHelp4AutoUpdater(Activity activity,String url_path, String name_apk_cfg) {
        this.activity = activity;
        this.url_path = url_path;
        this.name_apk_cfg = name_apk_cfg;
    }

    /**
     * 开机检测权限
     */
    public void checkPermissionsAndUpdate() {
        /*
         *  ActivityCompat.requestPermissions -> onRequestPermissionsResult -> funOnRequestPermissionsResult
         * */
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.INTERNET};
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                if (permissionList.size() <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新
                    FAutoUpdater manager = new FAutoUpdater(activity,
                            url_path,
                            name_apk_cfg
                    );
                    manager.checkUpdate();
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(activity, permissions, 100);
                }
            }
        } catch (Exception ex) {
            XToastUtils.warning("更新异常：" + ex.getMessage());
        }
    }

    /**
     * onRequestPermissionsResult 回调处理 -> funOnRequestPermissionsResult
     */
    protected void funOnRequestPermissionsResult(int requestCode, int[] grantResults) {

        boolean haspermission = false;
        if (100 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    haspermission = true;
                }
            }
            if (haspermission) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionDialog();
            } else {
                //全部权限通过，可以进行下一步操作
                FAutoUpdater manager = new FAutoUpdater(activity,
                        url_path,
                        name_apk_cfg
                );
                manager.checkUpdate();
            }
        }
    }


    //打开手动设置应用权限
    private void permissionDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，无法自动更新。请单击【设置】按钮进行【权限】授权。")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    })
                    .create();
        }
        alertDialog.show();
    }
}
