package top.feadre.faictr.flib.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.feadre.faictr.BuildConfig;
import top.feadre.faictr.R;
import top.feadre.faictr.flib.FTools;


/**
 * private String name_apk_cfg = "output.json";
 * private String url_path = "http://tx.feadre.top:89/app_auto_updater/faictr/";
 */
public class FAutoUpdater {
    private static final String TAG = "AutoUpdater";
    private static final boolean isDebugTest = false;
    private static final String NAME_SAVE_APK = "app-release.apk"; // 在下载时先要指定一个名称，叫什么名字无所谓
    private String name_apk_cfg_remote; // "output.json"
    private File file_apk_local; // 在下载时先要指定一个名称
    private String url_path; //"http://tx.feadre.top:89/app_auto_updater/faictr/"

    private Context mContext;
    private boolean isDownload = false; //是否已下载文件，暂时没用
    private boolean intercept = false; //手动中断
    private int val_progress;// 当前进度
    private static final int MSG_DOWN_UPDATE = 1;
    private static final int MSG_DOWN_OVER = 2;
    private static final int MSG_SHOW_DOWN = 3;
    private String ver_name_remote = "1"; //这个在更新时需要显示
    private String url_apk;//解析配置后拿到
    private TextView tv_au_progress_val; //更新时进度值
    private ProgressBar pb_au; //更新进进度条
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DOWN:
                    showUpdateDialog();
                    break;
                case MSG_DOWN_UPDATE:
                    tv_au_progress_val.setText(val_progress + "%");
                    pb_au.setProgress(val_progress);
                    break;
                case MSG_DOWN_OVER:
                    Toast.makeText(mContext, "下载完毕", Toast.LENGTH_SHORT).show();
                    isDownload = true;
                    installAPK();
                    break;
                default:
                    break;
            }
        }

    };


    public FAutoUpdater(Context context, String url_path, String name_apk_cfg_remote) {
        mContext = context;
        file_apk_local = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), NAME_SAVE_APK);
        this.url_path = url_path;
        this.name_apk_cfg_remote = name_apk_cfg_remote;
    }

    /**
     * 下载更新的进度
     */
    private void showDownloadDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.auto_update_app, null);
        tv_au_progress_val = (TextView) v.findViewById(R.id.tv_au);
        pb_au = (ProgressBar) v.findViewById(R.id.pb_au);

        new MaterialDialog.Builder(mContext)
                .customView(v, true)
                .title(String.format(ResUtils.getString(R.string.au_dialog_title), ver_name_remote))
//                .positiveText(R.string.au_submit)
                .negativeText(R.string.au_cancel)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        intercept = true;
                    }
                })
                .cancelable(false)
                .show();

        downloadApk();
    }

    /**
     * 安装APK内容
     */
    public void installAPK() {
        if (!file_apk_local.exists()) {
            Log.w(TAG, "installAPK: file_apk 不存在  " + file_apk_local);
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后打开新版本
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 给目标应用一个临时授权
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0 -17
                //如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24，使用FileProvider兼容安装apk
                String packageName = mContext.getApplicationContext().getPackageName();
                String authority = new StringBuilder(packageName).append(".fileprovider").toString();
                Uri apkUri = FileProvider.getUriForFile(mContext, authority, file_apk_local);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file_apk_local), "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());//安装完之后会提示”完成” “打开”。
        } catch (Exception e) {
        }
    }

    /**
     * 更新提示框
     */
    public void showUpdateDialog() {

        new MaterialDialog.Builder(mContext)
                .title(ResUtils.getString(R.string.au_dialog_update_title))
                .content(String.format(ResUtils.getString(R.string.au_dialog_content), ver_name_remote))
                .positiveText(ResUtils.getString(R.string.au_update_submit))
                .negativeText(ResUtils.getString(R.string.au_update_cancel))
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDownloadDialog();
                    }
                })
                .show();


    }

    /**
     * 检查是否更新的内容
     */
    public void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ver_name_local = "1";
                int ver_code_local = 1;
                int ver_code_remote = -1;
                try {
                    ver_name_local = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                    ver_code_local = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                    FTools.log_d(TAG, "checkUpdate run: "
                            + " ver_name_local = " + ver_name_local
                            + " ver_code_local = " + ver_code_local
                    );
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String outputFile = "";

                String config = http_get_file_cfg(url_path + name_apk_cfg_remote);
                if (config != null && config.length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        /*
                        *  "versionName": "20231127133408",
                            "outputFile": "my_20231127133408.apk"
                        * */

                        // 获取 apk文件名
                        String regex = "\"outputFile\":\\s*\"(?<m>[^\"]*?)\"";
                        Matcher m = Pattern.compile(regex).matcher(config);
                        if (m.find()) {
                            outputFile = m.group("m");
                        }

                        // 获取得版本号
                        m = Pattern.compile("\"versionName\":\\s*\"(?<m>[^\"]*?)\"").matcher(config);
                        if (m.find()) {
                            ver_name_remote = m.group("m");
                        }

                        // 获取版本号
                        m = Pattern.compile("\"versionCode\":.*?(?<m>\\d+),").matcher(config);
                        if (m.find()) {
                            ver_code_remote = Integer.valueOf(m.group("m"));
                        }

                    }
                }

                FTools.log_d(TAG, "CheckUpdate run: "
                        + "ver_code_remote = " + ver_code_remote
                        + "ver_name_remote = " + ver_name_remote
                        + "ver_code_local = " + ver_code_local
                        + "ver_name_local = " + ver_name_local

                );

                if (ver_code_local < ver_code_remote || BuildConfig.DEBUG) {
                    //开始下载
                    url_apk = url_path + outputFile;
                    mHandler.sendEmptyMessage(MSG_SHOW_DOWN);
                } else {
                    FTools.log_d(TAG, "不满足更新条件");
                }
            }
        }).start();
    }

    /**
     * 下载服务器的配置文件
     */
    public static String http_get_file_cfg(String httpurl) {
        /* 获取 file_cfg 文件内容 */
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();
        }
        return result;
    }

    /**
     * 从服务器下载APK安装包
     */
    public void downloadApk() {
        intercept = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {

                    //如果下载地址是HTTPS，则把这段加上，http则不需要
//                SSLContext sslContext = SSLContext.getInstance("SSL");//第一个参数为 返回实现指定安全套接字协议的SSLContext对象。第二个为提供者
//                TrustManager[] tm = {new MyX509TrustManager()};
//                sslContext.init(null, tm, new SecureRandom());
//                SSLSocketFactory ssf = sslContext.getSocketFactory();

                    url = new URL(url_apk);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream ins = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(file_apk_local);
                    int count = 0;
                    byte[] buf = new byte[1024];
                    while (!intercept) {
                        int numread = ins.read(buf);
                        count += numread;
                        val_progress = (int) (((float) count / length) * 100);
                        // 下载进度
                        mHandler.sendEmptyMessage(MSG_DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(MSG_DOWN_OVER);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }
                    fos.close();
                    ins.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}