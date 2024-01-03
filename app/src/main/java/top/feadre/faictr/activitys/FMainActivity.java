package top.feadre.faictr.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.base.FBaseActivity;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.flib.fviews.dialog_edit.EntityItem4SimpleRecyclerAdapter;
import top.feadre.faictr.flib.net.FNetTools;
import top.feadre.faictr.fragments.HelpFragment;
import top.feadre.faictr.fragments.setting_page.SettingsFragment;
import top.feadre.faictr.services.adb.ADBShellService;
import top.feadre.faictr.services.scrcpy.ScrcpyService;

public class FMainActivity extends FBaseActivity implements
        CompoundButton.OnCheckedChangeListener,
        FMainHelp4ADBShellService.OnHelp4ADBListener {
    private static final String TAG = "FMainActivity";
    @BindView(R.id.tb_titlebar)
    TitleBar tb_titlebar;
    @BindView(R.id.vet_ip)
    ValidatorEditText vet_ip;
    @BindView(R.id.xuiab_ip_res)
    Button xuiab_ip_res;
    @BindView(R.id.bt_ip_search)
    Button bt_ip_search;
    @BindView(R.id.bt_one_link)
    Button bt_one_link;
    @BindView(R.id.bt_qr_code_link)
    Button bt_qr_code_link;
    @BindView(R.id.bt_pair_link)
    Button bt_pair_link;
    @BindView(R.id.sb_android11)
    SwitchButton sb_android11;

    protected FMainHelp4FProgressDialog fMainHelp4FProgressDialog;
    protected FMainHelp4FDialogBottomEdit fMainDialogBottomEdit; //历史下拉菜单
    private FMainHelp4AutoUpdater fMainHelp4AutoUpdater; //自动更新
    protected FMainHelp4NetUtils fMainHelp4NetUtils;
    protected FMainHelp4ADBShellService fMainHelp4ADBShellService;
    protected FMainHelp4ScrcpyService fMainHelp4ScrcpyService;
    protected Help4SharedPreferences help4SharedPreferences;
    protected final int control_port = 5555;
    protected int state_dialog_run = 0;//0未启动 1网络启动 2ADB已启动 3scrcpy服务开始
    protected boolean is_bt_one_link = false;
    protected String control_ip_str;
    public int remote_device_width;//远程屏幕真实尺寸 在ADB时获取 用于executeRunJar时取最大的运行，和提前显示
    public int remote_device_height;
    protected String remote_device_name;
    protected Help4SharedPreferences.DataMain spMainCfg;//配置更新
    private boolean is_start_ctr_activity;
    public static ScrcpyService service_scrcpy;
    /*
     * 由 remote_ctr_resolution_wh 结合配置 spMainCfg.v_ctr_size_ratio
     * 用于横屏坚屏， *** 指定远程传输尺寸 ***
     * */
//    protected static int[] local_wh;//本机 的分辨率 oncreate 初始化
//    protected static final int[] surface_wh = new int[2];//surface 的分辨率 对应
    protected static final int[] transmit_wh = new int[2]; //传输的分辨率 是根据被控机屏幕尺寸来的
    //远程屏幕分辨率：1.在 7007获取 用于求 surface_resolution_wh
    public static final int[] remote_wh = new int[2];//远程屏幕的分辨率

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fmain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FTools.log_d(TAG, "onCreate");
//        local_wh = FToolsAndroid.SYS.getDisplaySize(this);

        //自动更新
        fMainHelp4AutoUpdater = new FMainHelp4AutoUpdater(this,
                FCFGBusiness.APPSet.AU_URL_PATH,
                FCFGBusiness.APPSet.AU_NAME_APK_CFG);
        fMainHelp4AutoUpdater.checkPermissionsAndUpdate();

        /*  --- title点击侦听 --- */
//        String appName = getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
//        tb_titlebar.setLeftClickListener(view -> XToastUtils.toast("点击返回"))
//                .setTitle(appName)
//                .setCenterClickListener(v -> XToastUtils.warning("点击标题"));


        /*添加顶部菜单*/
        tb_titlebar.setLeftImageDrawable(null);
//        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.bt_help) {
//            @Override
//            public void performAction(View view) {
//                ActivityUtils.startActivity(TActivity_base.class);
////                startActivity(new Intent(FMainActivity.this, TActivity_base.class));
//            }
//        });
        tb_titlebar.setTitle(getString(R.string.main_title));

        //设置介面
        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.set) {
            @Override
            public void performAction(View view) {
                openNewPage(SettingsFragment.class);
            }
        });

        /*  --- ip输入校验 --- */
//        met_ip.addValidator(new RegexpValidator("只能输入数字!", "\\d+"));
        vet_ip.addValidator(new RegexpValidator(getString(R.string.ValidatorEditText_regex_ip), FREValidator.REGEX_IP));

        // sp配置帮助
        help4SharedPreferences = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN);
        control_ip_str = help4SharedPreferences.getVetIpVal();
        vet_ip.setText(control_ip_str);

        sb_android11.setOnCheckedChangeListener(this);

        fMainHelp4FProgressDialog = new FMainHelp4FProgressDialog(this);
        fMainDialogBottomEdit = new FMainHelp4FDialogBottomEdit(this);
        fMainHelp4NetUtils = new FMainHelp4NetUtils(this);
        fMainHelp4ADBShellService = new FMainHelp4ADBShellService(this, this);
        fMainHelp4ScrcpyService = new FMainHelp4ScrcpyService(this);

        bt_one_link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                XToastUtils.info("长按");
                run_one_line(true, "一键连接(重置)");
                return false;
            }
        });


        FMainDebug fMainDebug = new FMainDebug(this);
//        fMainDebug.debug_init();
    }

    @OnClick({R.id.xuiab_ip_res, R.id.bt_ip_search, R.id.bt_one_link, R.id.bt_pair_link,
            R.id.bt_qr_code_link,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xuiab_ip_res: //历史连接的IP
//                fMainDialogBottomEdit.addData(new EntityItem4SimpleRecyclerAdapter("K40", "192.168.0.1"));
                fMainDialogBottomEdit.show();
                break;
            case R.id.bt_ip_search: //IP 扫描
//                startActivity(new Intent(FMainActivity.this, TActivity_base.class));
//                XToastUtils.info("bt_ip_search");
                ArrayList<String> ipAddress = FNetTools.getLocalIPAddress();
                if (ipAddress == null) {
                    XToastUtils.error(getString(R.string.FMain_bt_ip_search));
                    return;
                }
                fMainHelp4FProgressDialog.showDialog(
                        getString(R.string.FMainHelp4FProgressDialog_bt_ip_search),
                        fMainHelp4NetUtils.getCountTask()
                );

                boolean enable_localIP = false;
                for (String ip : ipAddress) {
                    //找一个内网IP进行扫描
                    if (FNetTools.isLocalNet(ip)) {
                        fMainHelp4NetUtils.scan_ip(ip);
                        enable_localIP = true;
                        break;
                    }
                }
                if (!enable_localIP) {
                    XToastUtils.warning("没有内网地址： ipAddress = " + ipAddress, Toast.LENGTH_LONG);
                    fMainHelp4FProgressDialog.close();
                }

                break;
            case R.id.bt_one_link: //一键连接
                run_one_line(false, "一键连接");
                break;
            case R.id.bt_pair_link: // 配对码连接
                XToastUtils.info("bt_pair_link");
//                fMainHelp4FProgressDialog.showDialog("bt_pair_link", "bt_pair_link", 90);
//                fMainHelp4FProgressDialog.updateProgress(90, "正在工作的内容 bt_pair_link");
                openNewPage(HelpFragment.class);
                break;
            case R.id.bt_qr_code_link://扫码连接
                XToastUtils.info("bt_qr_code_link");
//                fMainHelp4FProgressDialog.showDialog("bt_qr_code_link", "bt_qr_code_link", 180);
//                fMainHelp4FProgressDialog.updateProgress(110, "正在工作的内容 bt_qr_code_link");
                break;

            default:
                break;
        }
    }

    private void run_one_line(boolean b, String text) {
        //手动校验 通过
        boolean fvailid = vet_ip.fvailid();
//                FTools.log_d(TAG,"validate = "+fvailid);
        if (!fvailid) {
            vet_ip.showErrorMsg();
            return;
        }
        state_dialog_run = 1;//网络启动开始
        fMainHelp4FProgressDialog.showDialog(text);

        control_ip_str = vet_ip.getInputValue();
        help4SharedPreferences.setVetIpVal(control_ip_str);
        fMainHelp4FProgressDialog.updateProgress(2, control_ip_str + "开始连接");

        boolean adbConnected = fMainHelp4ADBShellService.isAdbConnected();
        FTools.log_d(TAG, "isAdbConnected() = " + adbConnected);
//                if (adbConnected && control_ip_str.equals(fMainHelp4ADBShellService.getControlIpStr())) {
//                    //如果已经连接过的设备 则快速连接 这里要判断设备名 !!!
//                    is_bt_one_link = true; // 按键开始时
//                    state_dialog_run = 2;//ADB开始
//                    fMainHelp4ADBShellService.fstart_service4fast();
//                    return;
//                }

        new FNetTools(new Thread2Main.OnThread2MainCallback<String, String, String>() {
            @Override
            public void on_fun_running(int status_run, String obj) {
            }

            @Override
            public void on_fun_res_success(int status_run, String obj) {
                if (state_dialog_run != 0) {
                    is_bt_one_link = true; // 按键开始时
                    fMainHelp4ADBShellService.setAdbConnected(false);
                    state_dialog_run = 2;//网络启动完成，ADB开始
                    fMainHelp4FProgressDialog.updateProgress(5, control_ip_str + "网络OK");
                    //开始ADB服务
                    fMainHelp4ADBShellService.fstart_service(ADBShellService.class,
                            control_ip_str, control_port, b);
                }
            }

            @Override
            public void on_fun_res_fail(int status_run, String obj) {
                //网络检测失败
                state_dialog_run = 0;
                fMainHelp4FProgressDialog.close();
                is_bt_one_link = false;
                XToastUtils.error(control_ip_str + " 网络检测失败！");
            }
        }).fping(control_ip_str, 4);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // sb_android11.setOnCheckedChangeListener(this);
        if (compoundButton.getId() == R.id.sb_android11) {
            // android 11 的切换
            if (b) {
                bt_qr_code_link.setVisibility(View.VISIBLE);
                bt_pair_link.setVisibility(View.VISIBLE);
                bt_one_link.setEnabled(false);
            } else {
                bt_qr_code_link.setVisibility(View.INVISIBLE);
                bt_pair_link.setVisibility(View.INVISIBLE);
                bt_one_link.setEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 自动更新权限及启动
        fMainHelp4AutoUpdater.funOnRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onADBRunSuccess(String res_text) {
        FTools.log_d(TAG, "onADBRunSuccess "
                + " remote_device_name = " + remote_device_name
                + " control_ip_str = " + control_ip_str
                + " state_dialog_run = " + state_dialog_run
        );
        fMainDialogBottomEdit.addData(new EntityItem4SimpleRecyclerAdapter(
                remote_device_name,
                control_ip_str));

        if (is_bt_one_link) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (state_dialog_run == 2) {
                //启动服务 打开CTR
                FTools.log_d(TAG, "onADBRunSuccess 开始启动服务");
                state_dialog_run = 3; //状态更新
                fMainHelp4ScrcpyService.fstart_service(ScrcpyService.class);
            }
        } else {
            XToastUtils.success(res_text);
        }
    }

    @Override
    public void onADBRunFail(String res_text) {
        fMainHelp4FProgressDialog.close();
        XToastUtils.error(res_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //不停更新
        spMainCfg = help4SharedPreferences.getMainCfg();

    }

    @Override
    protected void onPause() {
        // 停止，下次只能重新连   不停时，是快速连
        // 跳转时也会运行
        if (!is_start_ctr_activity) {
            fMainHelp4ADBShellService.fstop_service();
//            is_adb_connected = false;
        }
        is_start_ctr_activity = false; //每次的最后重置
        super.onPause();
    }

    public void update_surface_resolution() {
        // 由 FMainHelp4ScrcpyService on_fun_success4scrcpy  运行成功时赋值
        transmit_wh[0] = (int) (remote_wh[0] * spMainCfg.vf_ctr_remote_ratio);
        transmit_wh[1] = (int) (remote_wh[1] * spMainCfg.vf_ctr_remote_ratio);


        // update_surface_resolution
        //       本机屏幕尺寸：[1080, 2030]
        //       远程屏幕尺寸：720x1520
        //       传输尺寸：432x912:
        FTools.log_d(TAG, "update_surface_resolution "
                + "\n 本机屏幕尺寸：" + Arrays.toString(FToolsAndroid.SYS.getDisplaySize(this))
                + "\n 远程屏幕尺寸：" + remote_device_width + "x" + remote_device_height
                + "\n 传输尺寸：" + transmit_wh[0] + "x" + transmit_wh[1]
        );
    }

    public void start_ctr_activity() {
        Intent intent = new Intent(this, CtrActivity.class);
        is_start_ctr_activity = true;
        startActivity(intent);
        service_scrcpy = fMainHelp4ScrcpyService.getServiceScrcpy();
        fMainHelp4FProgressDialog.updateProgress(100, "");
        if (is_bt_one_link) {
            is_bt_one_link = false;//全部成功后关闭
        }
    }

    @Override
    public void onBackPressed() {
        FToolsAndroid.SYS.fOnBackPressed(this, R.string.FMain_on_back_pressed);
    }
}
