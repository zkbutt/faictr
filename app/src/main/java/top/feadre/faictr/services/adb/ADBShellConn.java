/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package top.feadre.faictr.services.adb;

import android.app.Activity;
import android.content.ComponentName;
import android.os.IBinder;
import android.util.Log;

import top.feadre.faictr.flib.base.FServiceConn;
import top.feadre.faictr.flib.base.Thread2Main;

/**
 * 这个添加了主进程转换和回调
 * 拿到了服务的对象
 * 和是否执行JAR 的业务逻辑
 */
public class ADBShellConn extends FServiceConn<ADBShellService> {
    private static final String TAG = "ADBShellConn";
    protected ADBShellService serviceAdbShell;
    private String controlIpStr;
    private int controlPort;
    private Thread2Main.OnThread2MainCallback<String, String, String> onThread2MainCallback;
    private ADBShellService.OnADBShellResListener onADBShellResListener;
    private boolean isAdbRunJar = false;//是否执行JAR

    public void setOnThread2MainCallback(Thread2Main.OnThread2MainCallback<String, String, String> onThread2MainCallback) {
        this.onThread2MainCallback = onThread2MainCallback;
    }

    public void setOnADBShellResListener(ADBShellService.OnADBShellResListener onADBShellResListener) {
        this.onADBShellResListener = onADBShellResListener;
    }

    public ADBShellConn(Activity activity) {
        super(activity);
    }

    public ADBShellConn(Activity activity,
                        Thread2Main.OnThread2MainCallback<String, String, String> callback,
                        ADBShellService.OnADBShellResListener onADBShellResListener
    ) {
        super(activity);
        this.onThread2MainCallback = callback;
        this.onADBShellResListener = onADBShellResListener; // 连续更新才需要这个  独立更新通过 传handler解决
    }

    public ADBShellConn(String control_ip_str,
                        int port,
                        Activity activity,
                        Thread2Main.OnThread2MainCallback<String, String, String> callback,
                        ADBShellService.OnADBShellResListener onADBShellResListener
    ) {
        this(activity, callback, onADBShellResListener);
        this.controlIpStr = control_ip_str;
        this.controlPort = port;
    }


    public ADBShellService getServiceAdbShell() {
        return serviceAdbShell;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: 开始中...");

        serviceAdbShell = ((ADBShellService.ADBShellBinder) iBinder).getService();
        serviceAdbShell.setOnThread2MainCallback(onThread2MainCallback);//开启侦听
        serviceAdbShell.setOnADBShellResListener(onADBShellResListener);
        serviceAdbShell.startConnect(activity, controlIpStr, controlPort, isAdbRunJar);
        super.onServiceConnected(componentName, iBinder);
    }


    public void close() {
        setServiceBound(false);
        serviceAdbShell = null;
    }


    public void setControlIpStr(String controlIpStr) {
        this.controlIpStr = controlIpStr;
    }

    public boolean isAdbRunJar() {
        return isAdbRunJar;
    }

    public void setAdbRunJar(boolean adbRunJar) {
        isAdbRunJar = adbRunJar;
    }

    public void setPort(int port) {
        this.controlPort = port;
    }

    public String getControlIpStr() {
        return controlIpStr;
    }

    public int getControlPort() {
        return controlPort;
    }
}
