package top.feadre.faictr.activitys;


import static top.feadre.faictr.flib.FToolsAndroid.*;

import android.util.Log;

import com.xuexiang.xui.utils.SnackbarUtils;

import java.util.Arrays;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.services.adb.ADBCommands;
import top.feadre.faictr.services.adb.ADBShellConn;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.services.adb.ADBShellOrderEntity;
import top.feadre.faictr.services.adb.ADBShellService;


public class FMainHelp4ADBShellService extends ADBShellConn implements
        Thread2Main.OnThread2MainCallback<String, String, String>,
        ADBShellService.OnADBShellResListener {
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
    public static final String FILE_LOG = "scrcpy.log";
    private static final String PACKAGE_NAME_SERVER = "top.feadre.fctr.Server";
    private boolean isAdbConnected = false;
    private final FMainActivity fMainActivity;
    private boolean isInit = false;//强制重新开始  用于重新ADB
    private OnHelp4ADBListener onHelp4ADBListener;

    public FMainHelp4ADBShellService(FMainActivity fMainActivity, OnHelp4ADBListener onHelp4ADBListener) {
        super(fMainActivity);
        //必须创建后才能用自己
        this.fMainActivity = fMainActivity;
        this.setOnADBShellResListener(this);
        this.setOnThread2MainCallback(this);
        this.onHelp4ADBListener = onHelp4ADBListener;
    }

    public boolean fstart_service4fast() {
        // 这个用于单独按钮进行快速启动
        if (isAdbConnected) {
            serviceAdbShell.executeCtrlC();
            executeRunJar();
            return true;
        } else {
            return false;
        }
    }

    public void fstart_service(Class<ADBShellService> cls, String control_ip_str, int control_port) {
        // 默认不进行 isInit  强制重开 用于大多数情况
        this.fstart_service(cls, control_ip_str, control_port, false);
    }

    public void fstart_service(Class<ADBShellService> cls, String control_ip_str, int control_port, boolean isInit) {
        //父类方法复写
        super.fstart_service(cls); //去运行先停止后 bind服务
        //本方法的逻辑控制
        this.isInit = isInit;
        setControlIpStr(control_ip_str);
        setPort(control_port);
        setAdbRunJar(true);
    }

    @Override
    public void onResText(ADBShellOrderEntity objOrderEntity) {
        /** 核心流程 ADBShellService.OnADBShellResListener 服务回调针对命令，流程运行完成  和结果调度解析*/
        FTools.log_d(TAG, "onResText: " + objOrderEntity.getOrderId());
        String temp_str = "";
        switch (objOrderEntity.getOrderId()) {
            case ID_ORDER_MainActivity_GET_DEVICE_NAME:
                // 获取设备名
                try {
                    String _t = objOrderEntity.getResList().get(1);

                    //名称根据网速解析
                    String[] _split = _t.split("\r\n");
                    if (_split.length == 1) {
                        fMainActivity.remote_device_name = _split[0];
                    } else {
                        // 大于等于2
                        fMainActivity.remote_device_name = _split[_split.length - 2];
                    }
//                    mainActivity.remote_device_name = FREValidator.extract_remote_device_name(_t);
                    fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(60), "名称获取成功 " + fMainActivity.remote_device_name);
                    FTools.log_d(TAG, "onResText: 找到 GET_DEVICE_NAME = " + fMainActivity.remote_device_name);
                } catch (Exception e) {
                    String _t = "设备名称获取失败";
                    fthrow_err(TAG, _t, e);
                    //失败了整一个默认的
                    fMainActivity.remote_device_name = "d " + objOrderEntity.getResList();
                    fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(60), "名称获取失败 " + fMainActivity.remote_device_name);
                    if (onHelp4ADBListener != null) {
                        onHelp4ADBListener.onADBRunFail(_t);
                    }
                    return;
                }
                break;

            case ID_ORDER_MainActivity_GET_SCREEN_SIZE2:
                //屏幕尺寸解析
                try {
                    String resText = objOrderEntity.getResText();
                    String[] screen_wh = FREValidator.extract_remote_device_wh2(resText);
                    fMainActivity.remote_device_width = Integer.parseInt(screen_wh[0]);
                    fMainActivity.remote_device_height = Integer.parseInt(screen_wh[1]);
                    temp_str = Arrays.toString(screen_wh);
                    fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(70),
                            "设备屏幕尺寸：" + fMainActivity.remote_device_width + "x" + fMainActivity.remote_device_height);
                    FTools.log_d(TAG, "onResText: 找到 GET_SCREEN_SIZE2 = " + temp_str);
                } catch (Exception e) {
                    String _t = "屏幕尺寸获取失败";
                    fthrow_err(_t, e);
                    if (onHelp4ADBListener != null) {
                        onHelp4ADBListener.onADBRunFail(_t);
                    }
                    return;
                }
                break;

            case ID_ORDER_MainActivity_RM_TEMP:
                //独立 下一步复制文件  仅更新一个进度
                FTools.log_d(TAG, "onResText: RM_TEMP " + objOrderEntity.getResText());
                fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(75), "删除临时文件完成");
                break;

            case ID_ORDER_MainActivity_MD5:
                /** 核心处理方法 */
                FTools.log_d(TAG, "onResText: MD5: " + objOrderEntity.getResText());
                //测试输出
                debug_md5(objOrderEntity);

                if (!isInit) { // 默认 如果不是重制初始化JAR 则进行MD5码比较 加速加载
                    String resText = objOrderEntity.getResText();
                    String res_md5_remote = FREValidator.extract_remote_file_md5(resText, FILE_NAME_JAR);

                    if (res_md5_remote != null) {
                        String res_md5_local = FToolsAndroid.SYS.getAssetsMD5(fMainActivity, FILE_NAME_JAR);

                        FTools.log_d(TAG, "onResText:MD5 快速启动"
                                + " res_md5_remote = " + res_md5_remote
                                + " res_md5_local = " + res_md5_local
                        );

                        if (res_md5_remote.equals(res_md5_local)) {
                            //如果相等，直接启动 shell将卡住 完成
                            executeRunJar();// executeJar 这个运行后不会有反馈
                            break;
                        }
                    }
                }

                // isInit = true; 时重制复制并启动
                executeCopyJar(); //这个是正常流程
                break;

            case ID_ORDER_MainActivity_TEMP2JAR:
                //这个是文件复制 关联启动
                FTools.log_d(TAG, "onResText: TEMP2JAR " + objOrderEntity.getResText());
                fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(85), "临时文件转JAR完成");

                executeRunJar();

                break;
            case ID_ORDER_MainActivity_COPY_FILE:
                FTools.log_d(TAG, "onResText: COPY_FILE " + objOrderEntity.getResText());
                //复制文件，关闭显示
                break;

            case ID_ORDER_MainActivity_FILE_SIZE2:
                //这个暂时没有用
                FTools.log_d(TAG, "onResText: FILE_SIZE2 " + objOrderEntity.getResText());
                break;
            case ID_ORDER_MainActivity_RUN_JAR:
                FTools.log_d(TAG, "onResText: RUN_JAR完成处理 " + objOrderEntity.getResText());
                //这个是最后一个没有反馈
                break;
            case ID_ORDER_MainActivity_OTHER:
                //流程中不需要解析的放这里面
                FTools.log_d(TAG, "onResText: OTHER: " + objOrderEntity.getResText());
                break;
            default:
                FTools.log_w(TAG, "onResText: 没有这个ID " + objOrderEntity.getResText());
        }
    }

    private void debug_md5(ADBShellOrderEntity objOrderEntity) {
        String resText = objOrderEntity.getResText();
        String res_md5_remote = FREValidator.extract_remote_file_md5(resText, FILE_NAME_JAR);
        String res_md5_local = SYS.getAssetsMD5(fMainActivity, FILE_NAME_JAR);
        FTools.log_d(TAG, "onResText:MD5 快速启动"
                + " res_md5_remote = " + res_md5_remote
                + " res_md5_local = " + res_md5_local
        );
    }

    private void adbRunSuccess() {
        FTools.log_d(TAG, "adbRunSuccess");
        isAdbConnected = true;
        String _t = "ADB启动服务成功";
//        fMainActivity.state_dialog_run = 0; //最终完成
        fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(100), _t);
        if (onHelp4ADBListener != null) {
            onHelp4ADBListener.onADBRunSuccess(_t);
        }
    }


    @Override
    public void onInitSuccess(ADBShellOrderEntity objOrderEntity) {
//        XToastUtils.success("shell 初始化成功 ...");
        fMainActivity.fMainHelp4FProgressDialog.updateProgress(getProgressVal(50), "ADB初始化成功，命令集开始");
        //独立 --- 设备名和屏幕尺寸
        executeADBBase();
        //独立 --- 初始化删除临时文件
        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.FILE_RM, FILE_NAME_BASE64) + '\n', ID_ORDER_MainActivity_RM_TEMP);

        //独立不需处理，但后续操作必须在这个目录
        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.SYS_CD, FILE_PATH) + '\n', ID_ORDER_MainActivity_OTHER);

        //判断MD5 核心优化方法  关联业务逻辑
        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.SYS_MD5, FILE_NAME_JAR) + '\n', ID_ORDER_MainActivity_MD5);
//        serviceAdbShell.executeCommand("ls -l" + '\n', ID_ORDER_MainActivity_LS);
    }

    private void executeRunJar() {
        String temp_str;
        // 联动文件复制
        long _size = (long) (Math.max(fMainActivity.remote_device_width, fMainActivity.remote_device_height) * fMainActivity.spMainCfg.v_ctr_quality_ratio);
//                temp_str = String.format(" CLASSPATH=/data/local/tmp/scrcpy-server.jar >aaa app_process / top.feadre.fctr.Server ");
        temp_str = String.format(" CLASSPATH=%s/%s app_process >>%s / %s ", FILE_PATH, FILE_NAME_JAR, FILE_LOG, PACKAGE_NAME_SERVER);
        temp_str += "/" + "199.199.199.199" + " " + _size + " " + Long.toString(fMainActivity.spMainCfg.v_ctr_bitrate) + ";" + "\n";
        FTools.log_d(TAG, "onResText: RUN_JAR命令 = " + temp_str);
        serviceAdbShell.executeCommand(temp_str, ID_ORDER_MainActivity_RUN_JAR, true);

        // executeRunJar 这个运行后不会有反馈
        adbRunSuccess();
    }

    private void executeADBBase() {
        serviceAdbShell.executeCommand(ADBCommands.Order.GET_DEVICE_NAME + "\n",
                ID_ORDER_MainActivity_GET_DEVICE_NAME);
        serviceAdbShell.executeCommand(ADBCommands.Order.GET_SCREEN_SIZE2 + "\n",
                ID_ORDER_MainActivity_GET_SCREEN_SIZE2);
    }

    private void executeCopyJar() {
        // 复制 文件到设备上
        ADBCommands.copy_assets_jar4shell(fMainActivity,
                FILE_NAME_JAR,
                FILE_NAME_BASE64,
                serviceAdbShell,
                ID_ORDER_MainActivity_COPY_FILE);

        //独立不处理 先删除上次运行的日志
        serviceAdbShell.executeCommand(String.format(ADBCommands.Order.FILE_RM, FILE_LOG) + '\n', ID_ORDER_MainActivity_OTHER);

        // base --- > jar 改名， 这个是关联 改名后启动
        String order = String.format(ADBCommands.Order.FILE_COPY_base642file, FILE_NAME_BASE64, FILE_NAME_JAR);
        serviceAdbShell.executeCommand(order + '\n', ID_ORDER_MainActivity_TEMP2JAR);
    }


    private int getProgressVal(int val) {
        int res = fMainActivity.is_bt_one_link ? val / 2 : val;
        return res;
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
        fMainActivity.fMainHelp4FProgressDialog.close();
//        XToastUtils.error(obj, 1);
        SnackbarUtils.FIndefinite(fMainActivity, fMainActivity.xuiab_ip_res, obj)
                .danger()
                .setAction("确定", v -> {
                }) //v -> XToastUtils.toast("点击了确定！")
                .show();
    }

    @Override
    public void on_fun_running(int status_run, String obj) {
        FTools.log_d(TAG, "on_fun_running: obj = " + obj);//这个进度很快不处理
        String[] _split = obj.split(FCFGBusiness.PARSE_STRING);
        fMainActivity.fMainHelp4FProgressDialog.updateProgress(
                Integer.parseInt(_split[1]),
                _split[0]);
    }

    @Override
    public void on_fun_res_success(int status_run, String obj) {
        //启动成功后开始执行命令  --- 这里是初始化成功  部份
        FTools.log_d(TAG, "on_fun_res_success: conn_adb_shell 连接成功，开始执行命令 = " + obj);
        fMainActivity.fMainHelp4FProgressDialog.updateProgress(
                getProgressVal(90),
                "adb初始化连接成功");
    }

    @Override
    public void onResTextLine(String resText, int orderId, int isFinish) {
        //用于对一个命令有多个结果进行调试
//        if (orderId == ID_ORDER_MainActivity_RUN_JAR) {
//            FTools.log_d(TAG, "onResTextLine: " + resText);
//        } else {
//            FTools.log_d(TAG, "onResTextLine: else  " + resText);
//        }
    }

    public interface OnHelp4ADBListener {
        void onADBRunSuccess(String res_text);

        void onADBRunFail(String res_text);

    }

}
