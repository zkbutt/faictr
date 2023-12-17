package top.feadre.faictr.flib.net;

import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.base.Thread2Main;

public class FNetTools extends Thread2Main<String, String, String> {
    private static final String TAG = "FNetTools";

    public FNetTools(OnThread2MainCallback<String, String, String> callback) {
        super(callback);
    }

    public void fping(String ip) {
        fping(ip, 3);
    }

    public void fping(String ip, int overtime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
//                String command_ping = "ping -c 3 -w 3 " + ip;
                String command_ping = "ping -c 3 -w " + overtime + " " + ip;
                Process process = null;
                try {
                    process = runtime.exec(command_ping);
                    int result = process.waitFor();
                    if (result == 0) {
                        FNetTools.this.run_over_success(ip);
                    } else {
                        FNetTools.this.run_over_fail(ip);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "fping: IOException 出错" + e.getMessage());
                    FNetTools.this.run_over_fail(ip);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "fping: InterruptedException 出错" + e.getMessage());
                    FNetTools.this.run_over_fail(ip);
                }
            }
        }).start();
    }

    public static ArrayList<String> getLocalIPAddress() {
        // 通过 第一个是IP内网地址
        ArrayList<String> res = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        res.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public static boolean isLocalNet(String ip) {
        if (!FREValidator.ip_verify(ip)) {
            return false;
        }
        String[] ip_place_vals = ip.split("\\.");
        if ("10".equals(ip_place_vals[0]) && "0".equals(ip_place_vals[1])) {   // 10.0.x.x
            return true;
        } else if ("192".equals(ip_place_vals[0]) && "168".equals(ip_place_vals[1])) { //192.168.x.x
            return true;
        } else if ("172".equals(ip_place_vals[0]) && "16".equals(ip_place_vals[1])) {//172.16.x.x
            return true;
        } else {
            return false;
        }
    }
}
