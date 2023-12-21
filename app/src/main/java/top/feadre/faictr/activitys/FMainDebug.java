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

package top.feadre.faictr.activitys;

import android.graphics.Color;
import android.view.View;

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;
import com.xuexiang.xuidemo.adapter.FlowTagAdapter;

import java.sql.Array;
import java.util.ArrayList;

import top.feadre.faictr.R;
import top.feadre.faictr.flib.FToolsAndroid;
import top.feadre.faictr.flib.base.Thread2Main;
import top.feadre.faictr.flib.net.FNetTools;
import top.feadre.faictr.services.adb.ADBShellService;
import top.feadre.faictr.services.scrcpy.ScrcpyService;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/20 19:21
 * @description:
 */
public class FMainDebug implements FlowTagLayout.OnTagClickListener {
    private static final String TAG = "FMainDebug";
    private final FMainActivity fMainActivity;
    private FlowTagLayout ftl_debug;
    private final static String bt_start_adb = "重开ADB";
    private final static String bt_start_adb_fast = "快速ADB";
    private final static String bt_start_scrcpy = "开启服务";
    private final static String bt_start_transmit = "开启传输";

    public FMainDebug(FMainActivity fMainActivity) {
        this.fMainActivity = fMainActivity;
    }

    protected void debug_init() {
        ftl_debug = fMainActivity.findViewById(R.id.ftl_debug);
        ftl_debug.setVisibility(View.VISIBLE);
        FlowTagAdapter tagAdapter = new FlowTagAdapter(fMainActivity);
        ftl_debug.setAdapter(tagAdapter);
        ftl_debug.setOnTagClickListener(this);
//        String[] stringArray = ResUtils.getStringArray(fMainActivity, R.array.tags_values);
        ArrayList<String> s = new ArrayList<>();
        s.add(bt_start_adb);
        s.add(bt_start_adb_fast);
        s.add(bt_start_scrcpy);
        s.add(bt_start_transmit);
        String[] strings = s.toArray(new String[s.size()]);
        tagAdapter.addTags(strings);

    }

    @Override
    public void onItemClick(FlowTagLayout parent, View view, int position) {
        String text = (String) parent.getAdapter().getItem(position);
        if (text.equals(bt_start_adb)) {
            fMainActivity.fMainHelp4FProgressDialog.showDialog(bt_start_adb);
            fMainActivity.fMainHelp4FProgressDialog.updateProgress(2, fMainActivity.control_ip_str + "开始连接");
            fMainActivity.state_dialog_run = 1;
            new FNetTools(new Thread2Main.OnThread2MainCallback<String, String, String>() {
                @Override
                public void on_fun_running(int status_run, String obj) {
                }

                @Override
                public void on_fun_res_success(int status_run, String obj) {
                    if (fMainActivity.state_dialog_run != 0) {
                        fMainActivity.is_bt_one_link = false; // 按键开始时
                        fMainActivity.fMainHelp4ADBShellService.setAdbConnected(false);
                        fMainActivity.state_dialog_run = 2;//网络启动完成，ADB开始
                        fMainActivity.fMainHelp4FProgressDialog.updateProgress(5, fMainActivity.control_ip_str + "网络OK");
                        //开始ADB服务
                        fMainActivity.fMainHelp4ADBShellService.fstart_service(ADBShellService.class,
                                fMainActivity.control_ip_str, fMainActivity.control_port, true);
                    }
                }

                @Override
                public void on_fun_res_fail(int status_run, String obj) {
                    //网络检测失败
                    fMainActivity.state_dialog_run = 0;
                    fMainActivity.fMainHelp4FProgressDialog.close();
                    fMainActivity.is_bt_one_link = false;
                    XToastUtils.error(fMainActivity.control_ip_str + " 网络检测失败！");
                }
            }).fping(fMainActivity.control_ip_str, 4);

        } else if (text.equals(bt_start_adb_fast)) {
            boolean adbConnected = fMainActivity.fMainHelp4ADBShellService.isAdbConnected();
            if (adbConnected) {
                fMainActivity.fMainHelp4FProgressDialog.showDialog(bt_start_adb_fast);
                fMainActivity.fMainHelp4ADBShellService.fstart_service4fast();
            }else {
                XToastUtils.error("快速启动 不满足条件");
            }
        } else if (text.equals(bt_start_scrcpy)) {
            boolean adbConnected = fMainActivity.fMainHelp4ADBShellService.isAdbConnected();
            if (adbConnected) {
                fMainActivity.fMainHelp4FProgressDialog.showDialog(bt_start_scrcpy);
                fMainActivity.fMainHelp4ScrcpyService.fstart_service(ScrcpyService.class);
            }else {
                XToastUtils.error("请先安装ADB连接");
            }
        } else if (text.equals(bt_start_transmit)) {
            if (fMainActivity.fMainHelp4ScrcpyService.getServiceBound()) {
                fMainActivity.start_ctr_activity();
            } else {
                XToastUtils.warning("请先开启 scrcpy 服务");
            }
        }
    }
}
