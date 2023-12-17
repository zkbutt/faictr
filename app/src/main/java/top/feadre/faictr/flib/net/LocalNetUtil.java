package top.feadre.faictr.flib.net;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.base.Thread2Main;


/**
 * @author Kevin-
 * @time 20181203
 * @description 局域网操作类
 * @updateTime 20181203
 */

public class LocalNetUtil extends Thread2Main<String, LinkedList<String>, String> {
    private static final String TAG = "LocalNetUtil";
    private Context context;

    private String localIp; //本地ip地址 如 192.168.0.1
    private String netMask; //子网掩码，用来计算网段
    private String networkSegment; //本地网段 如 192.168.0.

    private int ipIndex; //ip网段最后位 如 1~255
    private volatile LinkedList<String> resIps = new LinkedList<>();//存放待检测的ip

    private boolean isRoot = false;//是否root

    //命令行变量
    private String command_ping = "ping -c 3 -w 10 ";//-c 是指ping的次数 -w 10  以秒为单位指定超时间
    private Runtime runtime = Runtime.getRuntime();
    private Process process = null;

    //定义WifiManager对象
    private WifiManager mWifiManager;
    //定义WifiInfo对象
    private WifiInfo mWifiInfo;
    //定义DhcpInfo对象
    private DhcpInfo mDhcpInfo;
    //扫描出的网络连接列表
//    private List<ScanResult> mWifiList;
    //网络连接列表
//    private List<WifiConfiguration> mWifiConfiguration;
    //定义一个WifiLock
//    WifiManager.WifiLock mWifiLock;

    //判断是否root的辅助常量(暂时没用)
    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private final static int COUNT_TOTAL = 255;//检测IP的总数
    private static int systemRootState = kSystemRootStateUnknow;
    private static boolean is_scanning;
    private ThreadPoolExecutor pool;
    private int countTask = COUNT_TOTAL - 2;// 1-255 -> 254-1 个IP或线程 -自身 =253

    public LocalNetUtil(Context context, OnThread2MainCallback<String, List<String>, String> callback) {
        this.context = context;
        onThread2MainCallback = callback;
//        getWifiIP(context); // 初始化wifi ip
//        getWifiNetmask(); //
    }

    public int getCountTask() {
        //获得任务总数
        return countTask;
    }

    private void getWifiNetmask() {
        //取得DhcpInfo对象
        mDhcpInfo = mWifiManager.getDhcpInfo();
        //设置子网掩码
        long mask = mDhcpInfo.netmask;
        netMask = ip_long2str(mask);
        Log.d(TAG, "getWifiNetmask: 获取Mask：" + netMask);
        //取 192.168.0. 不考虑子网
        networkSegment = this.localIp.substring(0, this.localIp.lastIndexOf(".") + 1);
    }

    private void getWifiIP() {
        //取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(this.context.WIFI_SERVICE);
        mWifiManager.startScan();

        /* 估计要 root 才能用 */
        //得到扫描结果
//        mWifiList = mWifiManager.getScanResults();
        //得到配置好的网络连接
//        mWifiConfiguration = mWifiManager.getConfiguredNetworks();

        //取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
        //设置本地ip
        String ip = transferLocalIp().toString();
        localIp = ip.substring(1, ip.length());
        Log.d(TAG, "getWifiIP: 获取IP：" + localIp);
    }


    /**
     * 获取本地ip 格式对象化
     */
    public InetAddress transferLocalIp() {
        int hostAddress = mWifiInfo.getIpAddress();
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    /**
     * 将long型数据转成网络地址型的字符串
     *
     * @param numLong
     * @return
     */
    private String ip_long2str(long numLong) {
        String numStr = "";
        for (int i = 3; i >= 0; i--) {
            numStr += String.valueOf((numLong & 0xff));
            if (i != 0) {
                numStr += ".";
            }
            numLong = numLong >> 8;
        }
        return numStr;
    }

    /**
     * 将网络地址型的字符串转成long型数据
     *
     * @param numStr
     * @return
     */
    private long ip_str2long(String numStr) {
        Long numLong = 0L;
        String[] numbers = numStr.split("\\.");
        //等价上面
        for (int i = 0; i < 4; ++i) {
            numLong = numLong << 8 | Integer.parseInt(numbers[i]);
        }
        return numLong;
    }

    /**
     * 定义（静态）回调接口，必须实现以下回调方法
     */


    /**
     * 扫描同网段的所有IP  255.255.255.0
     */
    public void scan_ip(String ip) {
        // 传IP进行
        if (is_scanning) {
//            Log.w(TAG, "scanIpInSameSegment: 正在运行，请稍后再试 ----");
            run_over_fail("scanIpInSameSegment: 正在运行，请稍后再试 ---");
            return;
        }
        boolean _b = FREValidator.ip_verify(ip);

        if (!_b) {
            run_over_fail("ip 地址格式错误 ip = " + ip);
            return;
        }

        is_scanning = true;
        final CountDownLatch latch = new CountDownLatch(countTask);//全部完成控制
        resIps.clear();
//        if (this.localNetIps == null || "".equals(this.localNetIps)) {
//            return;
//        }

//        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, _total, 60L,
        pool = new ThreadPoolExecutor(4, COUNT_TOTAL, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));

        //没有权限
        //if (!LocalNetUtil.isRootSystem()) {
        //    toast("扫描网络ip需要root权限,请先root后再尝试!");
        //    return null;
        //}


        //产生 相当于 1-255 -> 254-1=253 个IP或线程
        for (int i = 1; i < COUNT_TOTAL; i++) {
            if (!is_scanning) {
                break;
            }
            String networkSegment = FREValidator.getNetworkSegment(ip);

            ipIndex = i;
            String currentIp = networkSegment + ipIndex;
//            Log.d(TAG, "scanIpInSameSegment: " + currentIp);
            if (currentIp.equals(ip)) {
                // 不处理
                continue;
            }

            //产生253个线程  即 int count_task = count_total - 2;
            Thread _thread = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    String command = command_ping + currentIp;
                    try {
                        process = runtime.exec(command);
                        int result = process.waitFor();
                        if (result == 0) {
//                            Log.d(TAG, "scanIpInSameSegment 连接成功:" + currentIp);
                            resIps.add(currentIp);
                        } else {
//                            Log.i(TAG, "scanIpInSameSegment 连接失败:" + currentIp);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    } finally {
                        process.destroy();
                        latch.countDown(); //子线程执行完毕后，latch中的数字减1
                        //如果latch中数字为0代表全部子线程执行完毕，回调
                        // 全局进度
                        int _progress = (int) (countTask - latch.getCount());
//                        int _progress = (int) (((count_task - latch.getCount()) / (count_task * 1.0)) * 100);
                        running_info("IP = " + currentIp + FCFGBusiness.PARSE_STRING + _progress);
//                        Log.d(TAG, "scanIpInSameSegment: latch.getCount() = " + latch.getCount()
//                                + " pool.getActiveCount() = " + pool.getActiveCount());
                        if (latch.getCount() <= 0) {
                            //结束
                            Log.d(TAG, "scanIpInSameSegment: 线程全部运行完成 ----");
                            pool.shutdownNow();
                            pool = null;
                            is_scanning = false;
//                            pool.shutdownNow(); //暴力关闭
                            if (resIps.size() > 0) {
                                run_over_success(resIps); //这个传错要报错
//                                run_over_success("成功扫描到 " + localNetIps.size());
                            } else {
                                run_over_fail("失败，没有扫描到设备");
                            }
                        }
                    }
                }
            });
//            pool.execute(_thread);
            pool.submit(_thread);
        }
        Log.d(TAG, "scanIpInSameSegment: 主线程完成----------");
        //        latch.await();//阻塞当前线程，直到所有子线程执行完毕
    }

    public void stop_scan_ip() {
//            pool.shutdown();//不再接受新任务
        Log.d(TAG, "stop_scan_ip: ");
        if (pool != null && !pool.isShutdown()) {
            pool.shutdownNow();
        }
        is_scanning = false;
        run_over_fail("失败，已手动取消");
    }

    public String[] parse_running_info(String res) {
        String[] split = res.split(FCFGBusiness.PARSE_STRING);
        Log.d(TAG, "parse_running_info: split" + Arrays.toString(split));
        if (split.length == 2) {
            return split;
        } else {
            return null;
        }
    }

    public String getLocalIp() {
//        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        /* 平地WIFI没有  */
        getWifiIP();
        if (localIp.equals("0.0.0.0")) {
            return null;
        }
        return localIp;
    }

    /**
     * 获取root权限（暂时没有用上）
     */
    public void getRootPermission() {
        if (LocalNetUtil.isRootSystem()) {
            return;
        }

        try {
            String rootCommand = "su";
            process = runtime.exec(rootCommand);

            int result = process.waitFor();

            if (result == 0) {
                this.isRoot = true;
                Log.i("IP", "Root成功");
                Toast.makeText(context, "Root成功", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("IP", "Root失败");
                Toast.makeText(context, "Root失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否已经root
     */
    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    public String getNetMask() {
        return netMask;
    }

    public String getNetworkSegment() {
        return networkSegment;
    }


}
