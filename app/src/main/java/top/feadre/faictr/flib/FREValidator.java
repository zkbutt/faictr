package top.feadre.faictr.flib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FREValidator {
    private static final String TAG = "FREValidator";
    public final static String REGEX_IP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    ;

    public static boolean ip_verify(String ip_str) {
        Pattern pattern = Pattern.compile(REGEX_IP);
        Matcher matcher = pattern.matcher(ip_str);
        return matcher.matches();
    }

    public static String[] extract_remote_device_wh1(String s_str) {
        // 解析长宽
        String regex = ".*: (\\d+)x(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s_str);
        String[] ss = null;
        if (matcher.find()) {
            if (matcher.groupCount() == 2) {
                ss = new String[]{matcher.group(1), matcher.group(2)};
            }
        }
        return ss;
    }

    public static String[] extract_remote_device_wh2(String s_str) {
        // 解析长宽
//        String regex = "\\sinit=(\\d+)x(\\d+) ";
        String regex = "(?m)Display:[\\s \\S]*? init=(\\d+)x(\\d+) ";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s_str);
        String[] ss = null;
        if (matcher.find()) {
            if (matcher.groupCount() == 2) {
                ss = new String[]{matcher.group(1), matcher.group(2)};
            }
        }
        return ss;
    }

    public static String extract_remote_device_name(String s_str) {
        /*
        V1818A
        PD1818:/ $
        * */
        String regex = "(?m)(.*)[\\s \\S]*?[$ #]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s_str);
        String ss = null;
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                ss = matcher.group(1);
            }
        }
        return ss;
    }

    public static String extract_remote_file_md5(String s_str, String file) {
        /*
         crcpy-server.jar
        md5sum: scrcpy-server.jar: No such file or directory
        1|z3q:/ #

        MD5: md5sum scrcpy-server.jar
        0188ffc2f0537b7e35f6957bb076b006  scrcpy-server.jar
        z3q:/data/local/tmp #
        * */
        String regex = "(?m).*(No such file or directory)[\\s \\S]*[$ #]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s_str);
        if (matcher.find()) {
            return null;
        }
        String ss = null;
//            regex = "(?m)(?<md5>.{32})  scrcpy-server\\.jar";
        regex = "(?m)(?<md5>.{32})  scrcpy-server\\.jar";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(s_str);
        if (matcher.find()) {
            ss = matcher.group(1);
        }
        return ss;
    }

    public static String getNetworkSegment(String ip) {
        /* 192.168.22.22 -> 结果为：192.168.22.*/
        String res = null;
        if (ip_verify(ip)) {
            Pattern pattern = Pattern.compile(REGEX_IP);
            Matcher matcher = pattern.matcher(ip);
            if (matcher.find()) {
                res = matcher.group(1) + "." + matcher.group(2) + "." + matcher.group(3) + ".";
            }
        }
        return res;
    }
}
