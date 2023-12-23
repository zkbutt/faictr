package top.feadre.faictr.activitys;

import android.content.SharedPreferences;

import com.xuexiang.xui.XUI;

import top.feadre.faictr.cfg.FCFGBusiness;

/**
 * @author Administrator
 * @version 1.0.0
 * @projectName faictr
 * @description: top.feadre.faictr.flib.base
 * @date :2023/12/19 19:22
 */
public class Help4SharedPreferences {
    private SharedPreferences sp;

    public Help4SharedPreferences(String key_sp) {
//        sp = XUI.getContext().getSharedPreferences(FCFGBusiness.SPSet.KEY_MAIN, 0);
        sp = XUI.getContext().getSharedPreferences(key_sp, 0);
    }

    public class DataMain {
        public float vf_ctr_remote_ratio;
        public int vi_ctr_bitrate;

        public int vi_ctr_display_mode; // 拉伸尺寸-0  保持比例-1
        public boolean vb_ctr_ss_size;
        public float vf_ctr_local_ratio;
        public boolean vb_ctr_bottom;
        public int[] vis_ctr_local_size;

        public DataMain(float vf_ctr_remote_ratio, int vi_ctr_bitrate,
                        int vi_ctr_display_mode, boolean vb_ctr_ss_size, float vf_ctr_local_ratio, boolean vb_ctr_bottom,
                        int[] vis_ctr_local_size) {
            this.vf_ctr_remote_ratio = vf_ctr_remote_ratio;
            this.vi_ctr_bitrate = vi_ctr_bitrate;

            this.vi_ctr_display_mode = vi_ctr_display_mode;
            this.vb_ctr_ss_size = vb_ctr_ss_size;
            this.vf_ctr_local_ratio = vf_ctr_local_ratio;
            this.vb_ctr_bottom = vb_ctr_bottom;
            this.vis_ctr_local_size = vis_ctr_local_size;
        }

    }

    public DataMain getMainCfg() {
        //传输参数配置
        float vf_ctr_remote_ratio = sp.getFloat(FCFGBusiness.SPSet.SPD_REMOTE_RATIO, 0.6F);
        int vi_ctr_bitrate = sp.getInt(FCFGBusiness.SPSet.SPD_BITRATE, 1024000);

        //本机参数配置
        int vi_ctr_display_mode = sp.getInt(FCFGBusiness.SPSet.SPD_DISPLAY_MODE, 0); // 0是拉伸屏幕
        boolean vb_ctr_ss_size = sp.getBoolean(FCFGBusiness.SPSet.SS_SIZE, true);
        float vf_ctr_local_ratio = sp.getFloat(FCFGBusiness.SPSet.SPD_LOCAL_RATIO, 1.0F); // 0是拉伸屏幕
        boolean vb_ctr_bottom = sp.getBoolean(FCFGBusiness.SPSet.SS_BOTTOM, true);

        int[] vis_ctr_local_size = new int[2];
        vis_ctr_local_size[0] = sp.getInt(FCFGBusiness.SPSet.SI_SIZE_W, 720);
        vis_ctr_local_size[1] = sp.getInt(FCFGBusiness.SPSet.SI_SIZE_H, 1280);

        DataMain dataMain = new DataMain(
                vf_ctr_remote_ratio, vi_ctr_bitrate,
                vi_ctr_display_mode, vb_ctr_ss_size, vf_ctr_local_ratio, vb_ctr_bottom,
                vis_ctr_local_size
        );
        return dataMain;
    }

    public String getVetIpVal() {
        return sp.getString(FCFGBusiness.SPSet.LINK_IP, null);
    }

    public void setVetIpVal(String ip) {
        sp.edit().putString(FCFGBusiness.SPSet.LINK_IP, ip).apply();
    }

    public SharedPreferences getSp() {
        return sp;
    }
}
