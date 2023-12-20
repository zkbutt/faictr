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

    public DataMain getMainSet() {
        float v_ctr_size_ratio = sp.getFloat(FCFGBusiness.SPSet.SIZE_RATIO, 0.6F);
        int v_ctr_bitrate = sp.getInt(FCFGBusiness.SPSet.BITRATE, 1024000);
        int v_ctr_display_mode = sp.getInt(FCFGBusiness.SPSet.DISPLAY_MODE, 0); // 0是拉伸屏幕
        DataMain dataMain = new DataMain(v_ctr_size_ratio, v_ctr_bitrate, v_ctr_display_mode);
        return dataMain;
    }

    public String getVetIpVal() {
        return sp.getString(FCFGBusiness.SPSet.LINK_IP, null);
    }

    public void setVetIpVal(String ip) {
        sp.edit().putString(FCFGBusiness.SPSet.LINK_IP, ip).apply();
    }

    public class DataMain {
        public float v_ctr_size_ratio;
        public int v_ctr_bitrate;
        public int v_ctr_display_mode;

        public DataMain(float v_ctr_size_ratio, int v_ctr_bitrate, int v_ctr_display_mode) {
            this.v_ctr_size_ratio = v_ctr_size_ratio;
            this.v_ctr_bitrate = v_ctr_bitrate;
            this.v_ctr_display_mode = v_ctr_display_mode;
        }
    }

}
