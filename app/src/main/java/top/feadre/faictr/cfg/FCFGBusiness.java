package top.feadre.faictr.cfg;

public final class FCFGBusiness {
    public static final String PARSE_STRING = "%_%"; //要进度 需通过这个手动分解析

    public static final class SPSet {
        public final static String KEY_CTR = "CTRActivity";
        public final static String KEY_ADB = "AdbActivity";
        public final static String SERVER_ADDRESS = "Server Address";
        public final static String COMMAND_HISTORY_SIZE = "command_history_size";
        public final static String NAV_SWITCH = "Nav Switch";
        public final static String LINKED_IP_SIZE = "linked_ip";
        public final static String RESOLUTION_RATIO = "resolution_ratio";
        public final static String VIDEO_BITRATE = "video_bitrate";
        public final static String position_resolution_ratio_t = "position_resolution_ratio";
        public final static String position_video_bitrate_t = "position_video_bitrate";
        public final static String position_sp_scale_t = "position_sp_scale";

    }

    public static final class KeyIntent {
        public final static String MAIN_CONTROL_IP = "control_ip";
        public final static String MAIN_PORT = "port";
    }


    public static final class FHandler {
        public static String ERR = "err";

        public static final class Main {
            public static String SCREEN_W = "screen_w";
            public static String SCREEN_H = "screen_h";
            public static String NAME_DEVICE = "name_device";
        }
    }

    public static final class permission {
        public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

        public static final String BYDAUTO_AC_COMMON = "android.permission.BYDAUTO_AC_COMMON";
        public static final String BYDAUTO_BODYWORK_COMMON = "android.permission.BYDAUTO_BODYWORK_COMMON";
        public static final String BYDAUTO_CHARGING_COMMON = "android.permission.BYDAUTO_CHARGING_COMMON";
        public static final String BYDAUTO_DOOR_LOCK_COMMON = "android.permission.BYDAUTO_DOOR_LOCK_COMMON";
        public static final String BYDAUTO_ENERGY_COMMON = "android.permission.BYDAUTO_ENERGY_COMMON";
        public static final String BYDAUTO_ENGINE_COMMON = "android.permission.BYDAUTO_ENGINE_COMMON";
        public static final String BYDAUTO_GEARBOX_COMMON = "android.permission.BYDAUTO_GEARBOX_COMMON";
        public static final String BYDAUTO_INSTRUMENT_COMMON = "android.permission.BYDAUTO_INSTRUMENT_COMMON";
        public static final String BYDAUTO_LIGHT_COMMON = "android.permission.BYDAUTO_LIGHT_COMMON";
        public static final String BYDAUTO_MULTIMEDIA_COMMON = "android.permission.BYDAUTO_MULTIMEDIA_COMMON";
        public static final String BYDAUTO_PANORAMA_COMMON = "android.permission.BYDAUTO_PANORAMA_COMMON";
        public static final String BYDAUTO_PM2P5_COMMON = "android.permission.BYDAUTO_PM2P5_COMMON";
        public static final String BYDAUTO_RADAR_COMMON = "android.permission.BYDAUTO_RADAR_COMMON";
        public static final String BYDAUTO_SAFETY_BELT_COMMON = "android.permission.BYDAUTO_SAFETY_BELT_COMMON";
        public static final String BYDAUTO_SETTING_COMMON = "android.permission.BYDAUTO_SETTING_COMMON";
        public static final String BYDAUTO_SPEED_COMMON = "android.permission.BYDAUTO_SPEED_COMMON";
        public static final String BYDAUTO_STATISTIC_COMMON = "android.permission.BYDAUTO_STATISTIC_COMMON";
        public static final String BYDAUTO_TIME_COMMON = "android.permission.BYDAUTO_TIME_COMMON";
        public static final String BYDAUTO_TYRE_COMMON = "android.permission.BYDAUTO_TYRE_COMMON";
        public static final String BYDAUTO_BODYWORK_GET = "android.permission.BYDAUTO_BODYWORK_GET";

    }
}
