package top.feadre.faictr.flib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;


import com.xuexiang.xui.utils.XToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;

import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;

public class FToolsAndroid {
    private static final String TAG = "FToolsAndroid";

    private static Toast mToast = null; //支持打断

    public static void ftoast_long(Context that, String txt) {
        if (mToast == null) {
            mToast = Toast.makeText(that, txt, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(that, txt, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void ftoast(Context that, String txt) {
        if (mToast == null) {
            mToast = Toast.makeText(that, txt, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(that, txt, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void ftoast(Context that, String txt, int color_text) {
        if (mToast == null) {
            mToast = Toast.makeText(that, txt, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(that, txt, Toast.LENGTH_SHORT);
        }
        if (Build.VERSION.SDK_INT < 30) {//Build.VERSION_CODES.R
            TextView _tv = (TextView) mToast.getView().findViewById(android.R.id.message);
            _tv.setTextColor(color_text);
//        _tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
        }
        mToast.show();
    }


    public static String fget_ip() {
        /**
         * 获取  ip地址 包括内网和外网
         * https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
         */
//        (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        mWifiInfo = mWifiManager.getConnectionInfo();

        try {
            InetAddress ipv4 = null;
            InetAddress ipv6 = null;
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface int_f = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = int_f
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet6Address) {
                        ipv6 = inetAddress;
                        continue;
                    }
                    if (inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipv4 = inetAddress;
                        continue;
                    }
                    return inetAddress.getHostAddress();
                }
            }
            if (ipv6 != null) {
                return ipv6.getHostAddress();
            }
            if (ipv4 != null) {
                return ipv4.getHostAddress();
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void fset_sys_UI(Activity that) {
        View decorView = that.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE //隐藏状态栏或者导航栏，稳定布局
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隐藏状态栏或者导航栏，稳定布局
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //隐藏状态栏或者导航栏，稳定布局
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //有用户上下拉才显示状态栏或者导航栏
                        | View.SYSTEM_UI_FLAG_FULLSCREEN//有用户上下拉才显示状态栏或者导航栏
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //状态栏或者导航栏全屏半透明间隙隐藏
    }

    public static void fthrow_err(String tag, Exception e) {
        e.printStackTrace();
        Log.e(tag, e.getMessage());
    }

    public static void fthrow_err(String tag, String s, Exception e) {
        e.printStackTrace();
        Log.e(tag, s + " " + e.getMessage());
    }

    public static void exit_app() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static class SYS {

        public static void open_dev_options(Context context) {
            /**
             *  打开开发者选项  小米 我的设备 - 全部参数与信息 - 一直点 MIUI 版本
             *  此时 可以跳转 但USB未打开， 打开后 USB可检测
             *  可以先判断 USB 再跳转 开发者
             *  */
            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    intent.setAction("android.intent.action.View");
                    context.startActivity(intent);
                } catch (Exception e1) {
                    try {
                        Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                        context.startActivity(intent);
                    } catch (Exception e2) {
                        FToolsAndroid.fthrow_err(TAG, e2);
                    }
                }
            }
        }

        public static boolean is_usb_debug(Context context) {
            boolean enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
            if (enableAdb) {
                FToolsAndroid.ftoast(context, "USB调试 已打开");
                return true;
            } else {
                FToolsAndroid.ftoast(context, "USB调试 未打开", Color.RED);
                return false;
            }
        }

        public static float[] fget_screen_hw(Activity that) {
            /**
             * 获取屏幕尺寸
             */
            DisplayMetrics metrics = new DisplayMetrics();
            if (ViewConfiguration.get(that).hasPermanentMenuKey()) {
                that.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            } else {
                final Display display = that.getWindowManager().getDefaultDisplay();
                display.getRealMetrics(metrics);
            }
            float h = metrics.heightPixels;
            float w = metrics.widthPixels;
            return new float[]{h, w};
        }

        public static boolean is_root_sys() {
            File f = null;
            final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                    "/system/sbin/", "/sbin/", "/vendor/bin/"};
            try {
                for (int i = 0; i < kSuSearchPaths.length; i++) {
                    f = new File(kSuSearchPaths[i] + "su");
                    if (f != null && f.exists()) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public static String getAssetsMD5(Context context, String file_jar) {
            MessageDigest digest = null;
            FileInputStream in = null;
            byte buffer[] = new byte[1024];
            int len;
            try {
                InputStream i = context.getResources().getAssets().open(file_jar);
                in = inputStream2FileInputStream(i);
                digest = MessageDigest.getInstance("MD5");
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        }

        public static FileInputStream inputStream2FileInputStream(InputStream input) {
            try {
                File tempFile = File.createTempFile("temp", ".txt");
                try (OutputStream output = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred during conversion.");
                    e.printStackTrace();
                } finally {
                    if (!input.markSupported()) {
                        input.close();
                    } else {
                        input.reset();
                    }
                }

                FileInputStream fileInputStream = new FileInputStream(tempFile);
                return fileInputStream;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        public static void speakTTS(Activity a, TextToSpeech tts, String ttsText) {
            if (tts != null && ttsText.length() != 0) {
                if (tts.isSpeaking()) {
                    Toast.makeText(a, "当前正在说话", Toast.LENGTH_SHORT).show();
                    tts.stop();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        }

        public static void fOnBackPressed(Activity activity, int res_string_id) {
            // 两次返回退回
            if (FCFGBusiness.Temp.on_back_pressed_temp_time == 0) {
                FCFGBusiness.Temp.on_back_pressed_temp_time = SystemClock.uptimeMillis();
                CharSequence text = activity.getResources().getText(res_string_id);
                XToastUtils.info(String.valueOf(text));
            } else {
                long now = SystemClock.uptimeMillis();
                if (now < FCFGBusiness.Temp.on_back_pressed_temp_time + 1000) {
                    FCFGBusiness.Temp.on_back_pressed_temp_time = 0;
                    //                if (is_service_bound) {
////                    MainActivity.service_scrcpy.stop_service();
////                    unbindService(serviceConnection);
//                }
                    activity.finish();
                }
                FCFGBusiness.Temp.on_back_pressed_temp_time = 0;
            }
        }


        public static int[] getDisplaySize(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int[] res_wh = new int[2];
            res_wh[0] = displayMetrics.widthPixels;
            res_wh[1] = displayMetrics.heightPixels;
            return res_wh;
        }
    }

}

