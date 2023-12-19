package top.feadre.faictr.services.adb;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class ADBCommands {
    /**
     * https://zhuanlan.zhihu.com/p/640216705
     * */
    private static final String TAG = "ADBCommands";

    public static class Order {
        public final static String MUTUAL_CLICK = "input tap %s %s"; //点击 adb shell input tap X Y
        public final static String MUTUAL_UPGLIDE = "input swipe 300 1000 300 500"; //上滑 起始点x 起始点y 结束点x 结束点y
        public final static String MUTUAL_GLIDE = "input swipe 300 100 300 1000"; //下滑 起始点x 起始点y 结束点x 结束点y
        public final static String MUTUAL_LEFT = "input swipe 1000 500 200 500"; //左滑 起始点x 起始点y 结束点x 结束点y
        public final static String MUTUAL_RIGHT = "input swipe 200 500 1000 500"; //右滑 起始点x 起始点y 结束点x 结束点y

        public final static String GET_DEVICE_NAME = "getprop ro.product.model 1"; //V1818A
        public final static String GET_P_SCREEN_SIZE = "wm size"; //Physical size: 720x1520

        /*
        WINDOW MANAGER DISPLAY CONTENTS (dumpsys window displays)
          Display: mDisplayId=0
            init=720x1520 320dpi cur=720x1520 app=720x1520 rng=720x664-1520x1464
            deferred=false mLayoutNeeded=false mTouchExcludeRegion=SkRegion((0,0,720,1520))

          Application tokens in top down Z order:
            mStackId=1
            mDeferRemoval=false
            mFillsParent=true
            mBounds=[0,0][720,1520]
              taskId=4
                mFillsParent=true
                mBounds=[0,0][720,1520]
                mdr=false
                appTokens=[AppWindowToken{c6856ce token=Token{27e101b ActivityRecord{4fe42ff u0 com.android.settings/.Settings t4}}}, AppWindowToken{51a9fd token=Token{18ff654 ActivityRecord{c16f1a7 u0 com.android.settings/.Settings$GeneralSettingsActivity t4}}}, AppWindowToken{e17688 token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}}]
                mTempInsetBounds=[0,0][0,0]
                  Activity #2 AppWindowToken{e17688 token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}}
                  windows=[Window{5d80fcd u0 com.android.settings/com.android.settings.Settings$DevelopmentSettingsActivity}]
                  windowType=2 hidden=false hasVisible=true
                  callingUid=1000 callingPid=1420 mPersistOnEmpty=true
                  app=true mVoiceInteraction=false
                  task={taskId=4 appTokens=[AppWindowToken{c6856ce token=Token{27e101b ActivityRecord{4fe42ff u0 com.android.settings/.Settings t4}}}, AppWindowToken{51a9fd token=Token{18ff654 ActivityRecord{c16f1a7 u0 com.android.settings/.Settings$GeneralSettingsActivity t4}}}, AppWindowToken{e17688 token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}}] mdr=false}
                   mFillsParent=true mOrientation=1
                  hiddenRequested=false mClientHidden=false reportedDrawn=true reportedVisible=true
                  mAppStopped=true
                  mNumInterestingWindows=1 mNumDrawnWindows=1 inPendingTransaction=false allDrawn=true (animator=true)
                  startingData=null removed=false firstWindowDrawn=true mIsExiting=false
                  controller=AppWindowContainerController{ token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}} mContainer=AppWindowToken{e17688 token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}} mListener=ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}
                  Activity #1 AppWindowToken{51a9fd token=Token{18ff654 ActivityRecord{c16f1a7 u0 com.android.settings/.Settings$GeneralSettingsActivity t4}}}
                  windows=[Window{e94be31 u0 com.android.settings/com.android.settings.Settings$GeneralSettingsActivity}]
                  windowType=2 hidden=true hasVisible=true
                  callingUid=1000 callingPid=1420 mPersistOnEmpty=true
                  app=true mVoiceInteraction=false
                  task={taskId=4 appTokens=[AppWindowToken{c6856ce token=Token{27e101b ActivityRecord{4fe42ff u0 com.android.settings/.Settings t4}}}, AppWindowToken{51a9fd token=Token{18ff654 ActivityRecord{c16f1a7 u0 com.android.settings/.Settings$GeneralSettingsActivity t4}}}, AppWindowToken{e17688 token=Token{582422b ActivityRecord{6ac3a7a u0 com.android.settings/.Settings$DevelopmentSettingsActivity t4}}}] mdr=false}
                   mFillsParent=true mOrientation=1
                  hiddenRequested=true mClientHidden=true reportedDrawn=false reportedVisible=false
                  mAppStopped=true
                  mNumInterestingWindows=1 mNumDrawnWindows=1 inPendingTransaction=false allDrawn=true (animator=true)
                  startingData=null removed=false firstWindowDrawn=true mIsExiting=false
        * */
        public final static String GET_SCREEN_SIZE1 = "dumpsys window displays";
        public final static String GET_SCREEN_SIZE2 = "dumpsys window";//这个太多了
        public final static String SYS_CD = "cd %s"; // /data/local/tmp
        public final static String SYS_MD5 = "md5sum %s"; // /data/local/tmp


        /* //文件是否存在
        不靠普
         * 不存在:1|PD1818:/data/local/tmp
         * 存在没有输出
         * */
        public final static String FILE_EXIST = "test -e %s";
        /*
         * 存在:/data/local/tmp/serverBase64
         * 不存在没输出
         * */
        public final static String FILE_FIND = "find /data/local/tmp -name serverBase64";

        /*
         * 文件大小
         * 成功：36K	/data/local/tmp/serverBase64
         * 失败：du: /data/local/tmp/serverBase6n: No such file or directory
         * */
        public final static String FILE_SIZE1 = "du -h /data/local/tmp/serverBase64";
        /*
         * -rw-rw-rw- 1 shell shell 34545 2023-11-20 13:24 /data/local/tmp/serverBase64
         * -rw-rw-rw- 1 shell shell 69086 2023-11-20 19:40 /data/local/tmp/serverBase64
         * */
        public final static String FILE_SIZE2 = "ls -l %s";//ls -l /data/local/tmp/serverBase64
        /*
         * app_process:用于启动一个Android应用进程的执行文件,位于/system/bin目录下
         * CLASSPATH=/data/local/tmp/scrcpy-server.jar app_process / top.feadre.fctr.Server  /192.168.22.120 200 1024000;
         * CLASSPATH=/home/ubuntu/temp/11/scrcpy-server.jar app_process / org.las2mile.scrcpy.Server  /192.168.22.120 200 1024000;
         * */
        public final static String FILE_RUN_JAR = " CLASSPATH=/data/local/tmp/%S app_process /";
        public final static String FILE_RM = "rm %s";

        /*
         * echo abc >> serverBase64
         * >会覆盖目标的原有内容。当文件存在时会先删除原文件，再重新创建文件，然后把内容写入该文件；否则直接创建文件。
         * >>会在目标原有内容后追加内容。当文件存在时直接在文件末尾进行内容追加，不会删除原文件；否则直接创建文件。
         * */
        public final static String FILE_COPY_file2base64 = "echo %s >> %s"; // base64part, file_name_base64
        public final static String FILE_COPY_base642file = " base64 -d < %s > %s"; // base64 -d < file_temp_base64 > scrcpy-server.jar
        public final static String RUN_JAR_SCRCPY = " CLASSPATH=/data/local/tmp/%s app_process / top.feadre.fctr.Server /%s %s %s"; //
    }

    public static class Key {
        public final static int A_POWER = 26;//电源键
        public final static int A_MENU = 82;//菜单键
        public final static int A_HOME = 3;//HOME 键
        public final static int A_RETURNE = 4;//返回键
        public final static int A_SOUND_ADD = 24;//音量+
        public final static int A_SOUND_SUB = 25;//音量-
        public final static int A_SOUND_NONE = 164;//音量-

        public final static int M_PLAY_PAUSE = 85;//播放/暂停
        public final static int M_STOP = 86;//停止播放
        public final static int M_NEXT = 87;//播放下一首
        public final static int M_LAST = 88;//播放上一首
        public final static int M_PLAY = 126;//播放
        public final static int M_PAUSE = 127;//暂停

        public final static int A_LIGHT_SCREEN = 224;//点亮屏幕
        public final static int A_CLOSE_SCREEN = 223;//熄灭屏幕
    }

    public static char[] bytes2texts(byte[] bs) {
        char[] cs = new char[bs.length];
        for (int i = 0; i < bs.length; i++) {
            cs[i] = (char) bs[i];
        }
        return cs;
    }

    public static byte[] file2base64(Context context, String file_name) {
        byte[] file_jar_base64 = null;
        try {
            //init插件  scrcpy-server.jar 创建文件流，用于ADB复制
            AssetManager assetManager = context.getAssets();
            InputStream input_Stream = assetManager.open(file_name);
            byte[] buffer = new byte[input_Stream.available()];
            input_Stream.read(buffer);
            file_jar_base64 = Base64.encode(buffer, 2); //fileBase64初始化
            input_Stream.close();
            Log.d(TAG, "init_jar: 完成---------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init_jar: 出错拉:" + e.getMessage());
        }
        return file_jar_base64;
    }


    public static void copy_assets_jar4shell(Context context, String file_name_jar, String file_name_base64, ADBShellService adbShellService, int orderId) {
        /*
        耗时活动
        */
//        String file_name_base64 = "file_temp_base64";
        String path = "/data/local/tmp";

        byte[] fileBase64 = file2base64(context, file_name_jar);
        int len = fileBase64.length; //jar文件的编码
        adbShellService.executeCommand(String.format(Order.SYS_CD, path) + '\n', orderId);
        adbShellService.executeCommand(String.format(Order.FILE_RM, file_name_base64) + '\n', orderId);

        int num_buff = 4056; //命令需要40个字节 Writing in 4KB pieces. 4096-40  ---> 40 Bytes for actual command text.
        byte[] file_buff = new byte[num_buff];
        byte[] file_buff_res;
        int sourceOffset = 0;

        while (sourceOffset < len) {
            if (len - sourceOffset >= num_buff) {
                System.arraycopy(fileBase64, sourceOffset, file_buff, 0, num_buff);//取 len_buff 个字符出来到 file_buff
                sourceOffset += num_buff;
                file_buff_res = file_buff;
            } else {
                int _remain = len - sourceOffset;
                byte[] file_buff_remain = new byte[_remain];
                System.arraycopy(fileBase64, sourceOffset, file_buff_remain, 0, _remain);
                sourceOffset += _remain;
                file_buff_res = file_buff_remain;
            }
            String base64part = new String(file_buff_res, StandardCharsets.US_ASCII);

//            String _s = "echo " + ServerBase64part + " >> " + fileTempBase64;
            String _s = String.format(Order.FILE_COPY_file2base64, base64part, file_name_base64);
            adbShellService.executeCommand(_s + '\n', orderId, false);
        }
        // 下一步文件转换  base64 -d < file_temp_base64 > scrcpy-server.jar
    }

}
