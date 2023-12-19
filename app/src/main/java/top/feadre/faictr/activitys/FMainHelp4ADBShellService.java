package top.feadre.faictr.activitys;


import static top.feadre.faictr.flib.FToolsAndroid.*;

import android.util.Log;

import java.util.Arrays;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.services.adb.ADBCommands;
import top.feadre.faictr.services.adb.ADBShellConn;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.services.adb.ADBShellOrderEntity;
import top.feadre.faictr.services.adb.ADBShellService;


public class FMainHelp4ADBShellService extends ADBShellConn implements Thread2Main.OnThread2MainCallback<String, String, String>, ADBShellService.OnADBShellResListener {
    private static final String TAG = "Help4ADBShellService";
    private static final int ID_ORDER_MainActivity_GET_DEVICE_NAME = 1001;
    private static final int ID_ORDER_MainActivity_GET_SCREEN_SIZE2 = 1002;
    private static final int ID_ORDER_MainActivity_COPY_FILE = 1003;
    private static final int ID_ORDER_MainActivity_RM_TEMP = 1004;
    private static final int ID_ORDER_MainActivity_RUN_JAR = 1005;
    private static final int ID_ORDER_MainActivity_FILE_SIZE2 = 1006;
    private static final int ID_ORDER_MainActivity_TEMP2JAR = 1007;
    private static final int ID_ORDER_MainActivity_OTHER = 1008;
    private static final int ID_ORDER_MainActivity_MD5 = 1009;
    private static final String FILE_NAME_JAR = "scrcpy-server.jar";
    private static final String FILE_NAME_BASE64 = "file_temp_base64";
    private static final String FILE_PATH = "/data/local/tmp";
    private static final String FILE_LOG = "aaa";
    private static final String PACKAGE_NAME_SERVER = "top.feadre.fctr.Server";
    private boolean isAdbConnected = false;
    private final FMainActivity fMainActivity;
    private OnHelp4ADBListener onHelp4ADBListener;
    private boolean isInit = true;//强制重新开始  用于重新ADB

    public FMainHelp4ADBShellService(FMainActivity fMainActivity) {
        super(fMainActivity);
        //必须创建后才能用自己
        this.fMainActivity = fMainActivity;
        this.setOnADBShellResListener(this);
        this.setOnThread2MainCallback(this);
    }

    public boolean fstart_service4fast() {
        if (isAdbConnected) {
            serviceAdbShell.executeCtrlC();
            executeRunJar();
            adbRunSuccess();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResText(ADBShellOrderEntity objOrderEntity) {
        Log.d(TAG, "onResText: " + objOrderEntity.getOrderId());
        String temp_str = "";
//        switch (objOrderEntity.getOrderId()) {
//            case ID_ORDER_MainActivity_GET_DEVICE_NAME:
//                try {
//                    String _t = objOrderEntity.getResList().get(1);
//
//                    //名称根据网速解析
//                    String[] _split = _t.split("\r\n");
//                    if (_split.length == 1) {
//                        fMainActivity.remote_device_name = _split[0];
//                    } else {
//                        // 大于等于2
//                        mainActivity.remote_device_name = _split[_split.length - 2];
//                    }
////                    mainActivity.remote_device_name = FREValidator.extract_remote_device_name(_t);
//                    mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(60), "名称获取成功 " + mainActivity.remote_device_name);
//                    Log.d(TAG, "onResText: 找到 GET_DEVICE_NAME = " + mainActivity.remote_device_name);
//                } catch (Exception e) {
//                    String _t = "设备名称获取失败";
//                    fthrow_err(TAG, _t, e);
//                    //失败了整一个默认的
//                    mainActivity.remote_device_name = "d " + objOrderEntity.getResList();
//                    mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(60), "名称获取失败 " + mainActivity.remote_device_name);
////                    if (onHelp4ADBListener != null) {
////                        onHelp4ADBListener.onADBRunFail(_t);
////                    }
//                    return;
//                }
//                break;
//
//            case ID_ORDER_MainActivity_GET_SCREEN_SIZE2:
//                try {
//                    String resText = objOrderEntity.getResText();
//                    String[] screen_wh = FREValidator.extract_remote_device_wh2(resText);
//                    mainActivity.remote_device_width = Integer.parseInt(screen_wh[0]);
//                    mainActivity.remote_device_height = Integer.parseInt(screen_wh[1]);
//                    temp_str = Arrays.toString(screen_wh);
//                    mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(70), "设备宽高：" + mainActivity.remote_device_width + "x" + mainActivity.remote_device_height);
//                    Log.d(TAG, "onResText: 找到 GET_SCREEN_SIZE2 = " + temp_str);
//                } catch (Exception e) {
//                    String _t = "屏幕尺寸获取失败";
//                    fthrow_err(_t, e);
//                    if (onHelp4ADBListener != null) {
//                        onHelp4ADBListener.onADBRunFail(_t);
//                    }
//                    return;
//                }
//                break;
//
//            case ID_ORDER_MainActivity_COPY_FILE:
//                Log.d(TAG, "onResText: COPY_FILE " + objOrderEntity.getResText());
//                //复制文件，关闭显示
//                break;
//
//            case ID_ORDER_MainActivity_FILE_SIZE2:
//                //这个暂时没有用
//                Log.d(TAG, "onResText: FILE_SIZE2 " + objOrderEntity.getResText());
//                break;
//
//            case ID_ORDER_MainActivity_RM_TEMP:
//                //独立 下一步复制文件
//                Log.d(TAG, "onResText: RM_TEMP " + objOrderEntity.getResText());
//                mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(75), "删除临时文件完成");
//                break;
//            case ID_ORDER_MainActivity_TEMP2JAR:
//                //这个是文件复制 关联启动
//                Log.d(TAG, "onResText: TEMP2JAR " + objOrderEntity.getResText());
//                mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(85), "临时文件转JAR完成");
//
//                executeRunJar();
//                // executeJar 这个运行后不会有反馈
//                adbRunSuccess();
//                break;
//
//            case ID_ORDER_MainActivity_RUN_JAR:
//                Log.d(TAG, "onResText: RUN_JAR完成处理 " + objOrderEntity.getResText());
//                //这个是最后一个没有反馈
//                break;
//            case ID_ORDER_MainActivity_MD5:
//                Log.d(TAG, "onResText: MD5: " + objOrderEntity.getResText());
//
//                if (!isInit) {
//                    String resText = objOrderEntity.getResText();
//                    String res_md5_remote = FREValidator.extract_remote_file_md5(resText, FILE_NAME_JAR);
//
//                    if (res_md5_remote != null) {

//        getAssetsMD5 这个代替
//                        String res_md5_local = SYS.md5_assets(mainActivity, FILE_NAME_JAR);
//
//                        Log.d(TAG, "onResText:MD5 快速启动"
//                                + " res_md5_remote = " + res_md5_remote
//                                + " res_md5_local = " + res_md5_local
//                        );
//
//                        if (res_md5_remote.equals(res_md5_local)) {
//                            //启动命令
//                            executeRunJar();// executeJar 这个运行后不会有反馈
//                            // executeJar 这个运行后不会有反馈
//                            adbRunSuccess();
//                            break;
//                        }
//                    }
//                }
//
//                //复制并启动
//                executeCopyJar(); //这个是正常流程
//                break;
//            case ID_ORDER_MainActivity_OTHER:
//                Log.d(TAG, "onResText: OTHER: " + objOrderEntity.getResText());
//                break;
//            default:
//                Log.w(TAG, "onResText: 没有这个ID " + objOrderEntity.getResText());
//        }
    }

    private void adbRunSuccess() {
        isAdbConnected = true;

//                FToolsAndroid.ftoast(mainActivity, "运行彻底完成！");
        String _t = "ADB启动服务成功";
//        mainActivity.state_dialog_run = 0; //最终完成
//        mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(100), _t);
        if (onHelp4ADBListener != null) {
            onHelp4ADBListener.onADBRunSuccess(_t);
        }
    }


    @Override
    public void onInitSuccess(ADBShellOrderEntity objOrderEntity) {
//        FToolsAndroid.ftoast(mainActivity, "shell 初始化成功 ...");
//        mainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(50), "ADB初始化成功，命令集开始");
//        //独立 --- 设备名和屏幕尺寸
//        executeADBBase();
//        //独立 --- 初始化删除临时文件
//        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.FILE_RM, FILE_NAME_BASE64) + '\n', ID_ORDER_MainActivity_RM_TEMP);
//        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.SYS_CD, FILE_PATH) + '\n', ID_ORDER_MainActivity_OTHER);
//
//        //判断MD5 核心优化方法
//        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.SYS_MD5, FILE_NAME_JAR) + '\n', ID_ORDER_MainActivity_MD5);

//        serviceAdbShell.executeCommand("ls -l" + '\n', ID_ORDER_MainActivity_LS);
    }

    private void executeRunJar() {
        String temp_str;
        // 联动文件复制
//        long _size = (long) (Math.max(mainActivity.remote_device_width, mainActivity.remote_device_height) * mainActivity.remote_screen_ratio);
////                temp_str = String.format(" CLASSPATH=/data/local/tmp/scrcpy-server.jar >aaa app_process / top.feadre.fctr.Server ");
//        temp_str = String.format(" CLASSPATH=%s/%s app_process >>%s / %s ", FILE_PATH, FILE_NAME_JAR, FILE_LOG, PACKAGE_NAME_SERVER);
//        temp_str += "/" + mainActivity.local_ip_str + " " + _size + " " + Long.toString(mainActivity.remote_bitrate) + ";" + "\n";
//        Log.d(TAG, "onResText: RUN_JAR命令 = " + temp_str);
//        serviceAdbShell.executeCommand(temp_str, ID_ORDER_MainActivity_RUN_JAR, true);
    }

    private void executeADBBase() {
        serviceAdbShell.executeCommand(ADBCommands.Order.GET_DEVICE_NAME + "\n",
                ID_ORDER_MainActivity_GET_DEVICE_NAME);
        serviceAdbShell.executeCommand(ADBCommands.Order.GET_SCREEN_SIZE2 + "\n",
                ID_ORDER_MainActivity_GET_SCREEN_SIZE2);
    }

    private void executeCopyJar() {
        ADBCommands.copy_assets_jar4shell(fMainActivity,
                FILE_NAME_JAR,
                FILE_NAME_BASE64,
                serviceAdbShell,
                ID_ORDER_MainActivity_COPY_FILE);

        //先删除上次运行的日志
        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.FILE_RM, FILE_LOG) + '\n', ID_ORDER_MainActivity_OTHER);

        // base --- > jar 这个是关联启动
        String order = String.format(ADBCommands.Order.FILE_COPY_base642file, FILE_NAME_BASE64, FILE_NAME_JAR);
        serviceAdbShell.executeCommand(order + '\n', ID_ORDER_MainActivity_TEMP2JAR);
    }

    public void fstart_service(Class<ADBShellService> cls, String control_ip_str, int control_port) {
        this.fstart_service(cls, control_ip_str, control_port, false);
    }

    public void fstart_service(Class<ADBShellService> cls, String control_ip_str, int control_port, boolean isInit) {
        //父类方法复写
        super.fstart_service(cls);
        this.isInit = isInit;
        setControlIpStr(control_ip_str);
        setPort(control_port);
        setAdbRunJar(true);
    }

    private int getProgressVal(int val) {
//        int res = fMainActivity.is_bt_one_link ? val / 2 : val;
//        return res;
        return 1;
    }


    protected boolean isAdbConnected() {
        return isAdbConnected;
    }

    public void setAdbConnected(boolean adbConnected) {
        isAdbConnected = adbConnected;
    }

    @Override
    public void on_fun_res_fail(int status_run, String obj) {
//        fMainActivity.state_dialog_run = 0;
        Log.e(TAG, "on_fun_res_fail: conn_adb_shell失败 = " + obj);
        isAdbConnected = false;
//        mainActivity.mainActivityHelp4Dialog.close();
        if (onHelp4ADBListener != null) {
            onHelp4ADBListener.onADBRunFail(obj);
        }
    }

    @Override
    public void on_fun_running(int status_run, String obj) {
        Log.d(TAG, "on_fun_running: obj = " + obj);//这个进度很快不处理
        String[] _split = obj.split(FCFGBusiness.PARSE_STRING);
//        fMainActivity.mainActivityHelp4Dialog.set_progress_val(Integer.parseInt(_split[1]), _split[0]);
    }

    @Override
    public void on_fun_res_success(int status_run, String obj) {
        //启动成功后开始执行命令  --- 这里是初始化成功  部份
        Log.d(TAG, "on_fun_res_success: conn_adb_shell 连接成功，开始执行命令 = " + obj);
//        fMainActivity.mainActivityHelp4Dialog.set_progress_val(getProgressVal(90), "adb初始化连接成功");
    }

    @Override
    public void onResTextLine(String resText, int orderId, int isFinish) {
//        if (orderId == ID_ORDER_MainActivity_RUN_JAR) {
//            Log.d(TAG, "onResTextLine: " + resText);
//        } else {
//            Log.d(TAG, "onResTextLine: else  " + resText);
//        }
    }

    public void setOnHelp4ADBListener(OnHelp4ADBListener onHelp4ADBListener) {
        this.onHelp4ADBListener = onHelp4ADBListener;
    }

    public interface OnHelp4ADBListener {
        void onADBRunSuccess(String res_text);

        void onADBRunFail(String res_text);

    }
}
