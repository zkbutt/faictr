package top.feadre.faictr.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xuexiang.xui.utils.XToastUtils;

import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.fviews.FShineLayout;
import top.feadre.faictr.services.scrcpy.ScrcpyService;

public class CtrActivity extends Activity implements
        View.OnClickListener,
        ScrcpyService.OnScrcpyServiceRotationCallbacks {
    private static final String TAG = "CtrActivity";
    private boolean is_landscape = false; //是否横屏 只影响 setRequestedOrientation 用于触发
    private boolean is_first_run = true; //消失后重新计算
    private boolean is_control = true;
    private final int[] transmit_wh = new int[2]; //传输分辨率

    private FloatingSubButton fmb_ctr_l_bt1;
    private FloatingSubButton fmb_ctr_l_bt2;
    private FloatingSubButton fmb_ctr_l_bt3;
    private FloatingSubButton fmb_ctr_l_bt4;
    private FloatingSubButton fmb_ctr_l_bt5;

    private FloatingSubButton fmb_ctr_r_bt1;
    private FloatingSubButton fmb_ctr_r_bt2;
    private FloatingSubButton fmb_ctr_r_bt3;
    private FloatingSubButton fmb_ctr_r_bt4;
    private FloatingSubButton fmb_ctr_r_bt5;

    private FloatingActionButton fam_ctr_bt1;
    private FloatingActionButton fam_ctr_bt2;
    private FloatingActionButton fam_ctr_bt3;
    private FloatingActionButton fam_ctr_bt4;
    private FloatingActionButton fam_ctr_bt5;

    private FloatingActionMenu fam_ctr;
    private FloatingMenuButton fmb_ctr_r;
    private FloatingMenuButton fmb_ctr_l;
    private SurfaceView sv_decoder;
    private Surface obj_surface;
    private CtrHandler handler_ctr;
    private Help4SharedPreferences.DataMain spMainCfg;
    private FShineLayout bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         *  启动竖 onCreate - onResume
         *  变横 on_remote_rotation - onConfigurationChanged
         *  启动横 onCreate - onResume -
         * */
        super.onCreate(savedInstanceState);
        FTools.log_d(TAG, "onCreate: ------ is_first_run = " + is_first_run);

        transmit_wh[0] = FMainActivity.transmit_wh[0];
        transmit_wh[1] = FMainActivity.transmit_wh[1];
        is_landscape = transmit_wh[0] > transmit_wh[1];
        if (is_landscape) {
            FTools.log_d(TAG, "onCreate: 启动横屏状态---------------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为竖屏
            setContentView(R.layout.activity_ctr_land);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置为竖屏
            setContentView(R.layout.activity_ctr);
        }

        handler_ctr = new CtrHandler(this);
        FMainActivity.service_scrcpy.setOnScrcpyServiceRotationCallbacks(this);

        initSurfaceView();

        initSer(); //这个在 onConfigurationChanged 中复用

        FTools.log_d(TAG, "onCreate: 运行完成---------------- surface_wh = " + transmit_wh[0]
                + " x " + transmit_wh[1]);
    }

    private void initSurfaceView() {
        // 更新sp
        spMainCfg = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN).getMainCfg();
//        spMainCfg.v_ctr_bt_bottom

        sv_decoder = (SurfaceView) findViewById(R.id.sv_decoder);
        obj_surface = sv_decoder.getHolder().getSurface();
        sv_decoder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (is_control) {
                    int width = sv_decoder.getWidth();
                    int height = sv_decoder.getHeight();
                    FTools.log_d(TAG, "init_ui_listener:sv_decoder Touch x="
                            + motionEvent.getX() + " x height=" + motionEvent.getY());
                    if (FMainActivity.service_scrcpy != null) {
                        return FMainActivity.service_scrcpy.touch_event(motionEvent, width, height);
                    } else {
                        return true;
                    }
                }
                return false;
            }
        });


        if (spMainCfg.vi_ctr_display_mode == 0) { //这个是 拉伸尺寸
            int[] local_wh = FToolsAndroid.SYS.getDisplaySize(this);
            if (!(Math.abs(spMainCfg.vf_ctr_local_ratio - 1.0f) < 0.000001f)) {
                //拉伸 小于1 需要处理
                ViewGroup.LayoutParams layoutParams = sv_decoder.getLayoutParams();
                layoutParams.width = (int) (spMainCfg.vf_ctr_local_ratio * local_wh[0]);
                layoutParams.height = (int) (spMainCfg.vf_ctr_local_ratio * local_wh[1]);
                sv_decoder.setLayoutParams(layoutParams);
            }
            float v = spMainCfg.vf_ctr_local_ratio * local_wh[0];

        } else if (spMainCfg.vi_ctr_display_mode == 1) {//这个是 保持比例

        }
    }

    private void initSer() {
        initViews();

        //开启传输
        FMainActivity.service_scrcpy.start_ser(obj_surface, handler_ctr,
                transmit_wh[0], transmit_wh[1]);
    }

    private void initViews() {

        bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(this);


        fam_ctr = findViewById(R.id.fam_ctr);
        fmb_ctr_r = findViewById(R.id.fmb_ctr_r);
        fmb_ctr_l = findViewById(R.id.fmb_ctr_l);

        fmb_ctr_l_bt5 = findViewById(R.id.fmb_ctr_l_bt5);
        fmb_ctr_l_bt4 = findViewById(R.id.fmb_ctr_l_bt4);
        fmb_ctr_l_bt3 = findViewById(R.id.fmb_ctr_l_bt3);
        fmb_ctr_l_bt2 = findViewById(R.id.fmb_ctr_l_bt2);
        fmb_ctr_l_bt1 = findViewById(R.id.fmb_ctr_l_bt1);

        fmb_ctr_r_bt5 = findViewById(R.id.fmb_ctr_r_bt5);
        fmb_ctr_r_bt4 = findViewById(R.id.fmb_ctr_r_bt4);
        fmb_ctr_r_bt3 = findViewById(R.id.fmb_ctr_r_bt3);
        fmb_ctr_r_bt2 = findViewById(R.id.fmb_ctr_r_bt2);
        fmb_ctr_r_bt1 = findViewById(R.id.fmb_ctr_r_bt1);

        fam_ctr_bt1 = findViewById(R.id.fam_ctr_bt1);
        fam_ctr_bt2 = findViewById(R.id.fam_ctr_bt2);
        fam_ctr_bt3 = findViewById(R.id.fam_ctr_bt3);
        fam_ctr_bt4 = findViewById(R.id.fam_ctr_bt4);
        fam_ctr_bt5 = findViewById(R.id.fam_ctr_bt5);

        fmb_ctr_l_bt5.setOnClickListener(this);
        fmb_ctr_l_bt4.setOnClickListener(this);
        fmb_ctr_l_bt3.setOnClickListener(this);
        fmb_ctr_l_bt2.setOnClickListener(this);
        fmb_ctr_l_bt1.setOnClickListener(this);

        fmb_ctr_r_bt5.setOnClickListener(this);
        fmb_ctr_r_bt4.setOnClickListener(this);
        fmb_ctr_r_bt3.setOnClickListener(this);
        fmb_ctr_r_bt2.setOnClickListener(this);
        fmb_ctr_r_bt1.setOnClickListener(this);

        fam_ctr_bt1.setOnClickListener(this);
        fam_ctr_bt2.setOnClickListener(this);
        fam_ctr_bt3.setOnClickListener(this);
        fam_ctr_bt4.setOnClickListener(this);
        fam_ctr_bt5.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        fam_ctr.getBackground().setAlpha(100);
//        fmb_ctr_l.getBackground().setAlpha(20);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                bt_back.callOnClick();
                XToastUtils.info("bt_back");
                break;

            case R.id.fmb_ctr_l_bt1:
                XToastUtils.info("fmb_ctr_l_bt1");
                break;
            case R.id.fmb_ctr_l_bt2:
                XToastUtils.info("fmb_ctr_l_bt2");
                break;
            case R.id.fmb_ctr_l_bt3:
                XToastUtils.info("fmb_ctr_l_bt3");
                break;
            case R.id.fmb_ctr_l_bt4:
                XToastUtils.info("fmb_ctr_l_bt4");
                break;
            case R.id.fmb_ctr_l_bt5:
                XToastUtils.info("fmb_ctr_l_bt5");
                break;
            case R.id.fmb_ctr_r_bt1:
                XToastUtils.info("fmb_ctr_r_bt1");
                break;
            case R.id.fmb_ctr_r_bt2:
                XToastUtils.info("fmb_ctr_r_bt2");
                break;
            case R.id.fmb_ctr_r_bt3:
                XToastUtils.info("fmb_ctr_r_bt3");
                break;
            case R.id.fmb_ctr_r_bt4:
                XToastUtils.info("fmb_ctr_r_bt4");
                break;
            case R.id.fmb_ctr_r_bt5:
                XToastUtils.info("fmb_ctr_r_bt5");
                break;
            case R.id.fam_ctr_bt1:
                //bt_back
                if (FMainActivity.service_scrcpy != null) {
                    FMainActivity.service_scrcpy.send_keyevent(4);
                }
                break;
            case R.id.fam_ctr_bt2:
                //bt_home
                if (FMainActivity.service_scrcpy != null) {
                    FMainActivity.service_scrcpy.send_keyevent(3);
                }
                break;
            case R.id.fam_ctr_bt3:
                //bt_appswitch
                if (FMainActivity.service_scrcpy != null) {
                    FMainActivity.service_scrcpy.send_keyevent(187);
                }
                break;
            case R.id.fam_ctr_bt4:
                //bt_exit
                if (FMainActivity.service_scrcpy != null) {
                    FMainActivity.service_scrcpy.pause();
                }
//                Intent intent = new Intent(); //还是用Intent传递数据，这里用的是Intent的空参构造器，不需要指定跳转的界面，因为按下返回按钮会返回到上一个Activity.
//                intent.putExtra("user",user.getText().toString());
//                intent.putExtra("password",password.getText().toString());
//                setResult(RESULT_OK, intent);//使用该方法向上一个Activity返回数据。第一个参数是处理结果，第二个参数是将带有数据的intent传递回去。
                finish();
                break;
            case R.id.fam_ctr_bt5:
                XToastUtils.info("fam_ctr_bt5");
                break;

        }
    }

    @Override
    public void on_remote_rotation() {
        //子线程触发旋转 onConfigurationChanged
        FTools.log_d(TAG, "on_remote_rotation: ----");
        is_landscape = !is_landscape;
        swap_surface_wh();
//        MainActivity.service_scrcpy.stop_service();
        if (is_landscape) {
            FTools.log_d(TAG, "on_remote_rotation: 启动横屏状态---------------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为竖屏
//            setContentView(R.layout.activity_ctr_land); //子线程获取不了
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置为竖屏
//            setContentView(R.layout.activity_ctr);
        }
    }


    private void swap_surface_wh() {
        int _t = transmit_wh[0];
        transmit_wh[0] = transmit_wh[1];
        transmit_wh[1] = _t;
    }


    @Override
    public void onBackPressed() {
        //不作反应
//        FToolsAndroid.SYS.fOnBackPressed(this, R.string.ctr_on_back_pressed);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FTools.log_d(TAG, "onPause: --------- is_first_run = " + is_first_run);
        if (FMainActivity.service_scrcpy != null) {
            FMainActivity.service_scrcpy.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FTools.log_d(TAG, "onResume: --------- is_first_run = " + is_first_run);
        if (FMainActivity.service_scrcpy != null && !is_first_run) {
            FMainActivity.service_scrcpy.resume();
        } else {
            is_first_run = false;
        }
//        if (!is_first_time && !is_rotation) {
//            FToolsAndroid.fset_sys_UI(that);
//            if (is_service_bound) {
//                ll_container = findViewById(R.id.ll_container);
////                MainActivity.service_scrcpy.resume();
//            }
//        }
//        is_remote_rotate = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FTools.log_d(TAG, "onConfigurationChanged: ------newConfig = " + newConfig);
        if (!is_first_run) {
            //阻止第一次运行 只在旋转时生效
            if (is_landscape) {
                setContentView(R.layout.activity_ctr_land);
            } else {
                setContentView(R.layout.activity_ctr);
            }
            initSer(); //相当于半个 onCreate
        }
    }

}
