package top.feadre.faictr.fragments.setting_page;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import top.feadre.faictr.R;
import top.feadre.faictr.activitys.Help4SharedPreferences;
import top.feadre.faictr.cfg.FCFGBusiness;

@Page(name = "设置")
public class SettingsFragment extends BaseFragment {
    private static final String TAG = "SettingsFragment";
    @BindView(R.id.xuiglv_set)
    XUIGroupListView xuiglv_set;
    private Float vf_ctr_remote_ratio;
    private int vi_ctr_bitrate;

    private int vi_ctr_display_mode;
    private boolean vb_ctr_ss_size;
    private Float vf_ctr_local_ratio;
    private boolean vb_ctr_bottom;
    private SharedPreferences sp;
    private int[] vis_ctr_local_size;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        Help4SharedPreferences h = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN);
        Help4SharedPreferences.DataMain dataMain = h.getMainCfg();
        sp = h.getSp();
        vf_ctr_remote_ratio = dataMain.vf_ctr_remote_ratio;
        vi_ctr_bitrate = dataMain.vi_ctr_bitrate;

        vis_ctr_local_size = dataMain.vis_ctr_local_size;
        vi_ctr_display_mode = dataMain.vi_ctr_display_mode;
        vb_ctr_ss_size = dataMain.vb_ctr_ss_size;
        vf_ctr_local_ratio = dataMain.vf_ctr_local_ratio;
        vb_ctr_bottom = dataMain.vb_ctr_bottom;

        //传输参数配置
        SettingsPullDown4Float spd_remote_ratio = new SettingsPullDown4Float(
                getContext(), xuiglv_set,
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio),
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio),
                getString(R.string.set_label_ctr_remote_ratio), vf_ctr_remote_ratio,
                sp, FCFGBusiness.SPSet.SPD_REMOTE_RATIO
        );
        SettingsPullDown4Int spd_bitrate = new SettingsPullDown4Int(
                getContext(), xuiglv_set,
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_keys),
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_vals),
                getString(R.string.set_label_ctr_bitrate), vi_ctr_bitrate,
                sp, FCFGBusiness.SPSet.SPD_BITRATE
        );

        //本机参数配置
        SettingsPullDown4Int spd_display_mode = new SettingsPullDown4Int(
                getContext(), xuiglv_set,
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_keys),
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_vals),
                getString(R.string.set_label_ctr_display_mode), vi_ctr_display_mode,
                sp, FCFGBusiness.SPSet.SPD_DISPLAY_MODE
        );
        SettingsSwitch ss_size = new SettingsSwitch(getContext(),
                xuiglv_set, "是否指定显示尺寸", vb_ctr_ss_size,
                sp, FCFGBusiness.SPSet.SS_SIZE);
        SettingsPullDown4Float spd_local_ratio = new SettingsPullDown4Float(
                getContext(), xuiglv_set,
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio),
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio),
                getString(R.string.set_label_ctr_local_ratio), vf_ctr_local_ratio,
                sp, FCFGBusiness.SPSet.SPD_LOCAL_RATIO
        );
        SettingsPullDown4IntsSize spd_size = new SettingsPullDown4IntsSize(
                getContext(), xuiglv_set,
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_size_wh_keys),
                ResUtils.getStringArray(getContext(), R.array.select_set_ctr_size_wh_vals),
                "设置分辨率", vis_ctr_local_size,
                sp, FCFGBusiness.SPSet.SI_SIZE_W, FCFGBusiness.SPSet.SI_SIZE_H
        );
        SettingsSwitch ss_bottom = new SettingsSwitch(getContext(),
                xuiglv_set, "本机底部控制按钮", vb_ctr_bottom,
                sp, FCFGBusiness.SPSet.SS_BOTTOM);

        //DP2PX
//        int size = DensityUtils.dp2px(getContext(), 100);
        XUIGroupListView.newSection(getContext())
                .setTitle(getString(R.string.set_title_1))
//                .setDescription("XXXX set_title_1 的描述")//这个在最后栏目的最后
//                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .faddItemView(spd_remote_ratio.getViewRes())
                .faddItemView(spd_bitrate.getViewRes())
                .addTo(xuiglv_set);

        XUIGroupListView.newSection(getContext())
                .setTitle(getString(R.string.set_title_2))
//                .setDescription("XXXX set_title_2 的描述")
//                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .faddItemView(spd_display_mode.getViewRes())
                .faddItemView(ss_size.getViewRes())
                .faddItemView(spd_size.getViewRes())
                .faddItemView(spd_local_ratio.getViewRes())
                .faddItemView(ss_bottom.getViewRes())
                .addTo(xuiglv_set);


        /** 处理逻辑 */
        ss_size_ctr(ss_size, spd_local_ratio, spd_size);
        spd_display_mode.setOnSelectedListener(valRes -> {
            if (valRes.intValue() == 0) {//拉伸尺寸-0   可支持显示比例和指定显示尺寸
                ss_size.getViewRes().setVisibility(View.VISIBLE);//是否指定
                ss_size_ctr(ss_size, spd_local_ratio, spd_size);
            } else if (valRes.intValue() == 1) {//保持比例-1  只支持显示比例
                ss_size.getViewRes().setVisibility(View.GONE);
                spd_size.getViewRes().setVisibility(View.GONE);
                spd_local_ratio.getViewRes().setVisibility(View.VISIBLE);
            }
        });

        ss_size.setOnClickExtendListener(() -> {
            ss_size_ctr(ss_size, spd_local_ratio, spd_size);
        });

    }

    private void ss_size_ctr(SettingsSwitch ss_size, SettingsPullDown4Float spd_local_ratio, SettingsPullDown4IntsSize spd_size) {
        if (ss_size.valRes) { // 是指定尺寸,逻辑没做提醒
            XToastUtils.info("拉伸 - 设置分辨率暂时没做，设置无效，需要改ctr的初始代码", Toast.LENGTH_LONG);
            //用不起
//            SnackbarUtils.FIndefinite(spd_size.getContext(), spd_size.getViewRes(),
//                            "拉伸 - 设置分辨率暂时没做，设置无效，需要改ctr的初始代码")
//                    .danger()
//                    .setAction("确定", v -> {
//                    }) //v -> XToastUtils.toast("点击了确定！")
//                    .show();
            spd_size.getViewRes().setVisibility(View.VISIBLE);//设置尺寸
            spd_local_ratio.getViewRes().setVisibility(View.GONE); //本机显示比例
        } else {
            spd_size.getViewRes().setVisibility(View.GONE);
            spd_local_ratio.getViewRes().setVisibility(View.VISIBLE);
        }
    }
}
