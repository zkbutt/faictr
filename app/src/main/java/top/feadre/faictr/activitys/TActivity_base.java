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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xuexiang.xui.utils.XToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.base.FBaseActivity;
import top.feadre.faictr.flib.net.FNetTools;
import top.feadre.faictr.fragments.base.FSwipeMenuItemFragment;

public class TActivity_base extends FBaseActivity {
    private static final String TAG = "TActivity_base";
    @BindView(R.id.bt_t001)
    Button bt_t001;
    @BindView(R.id.bt_t002)
    Button bt_t002;
    @BindView(R.id.bt_t003)
    Button bt_t003;
    @BindView(R.id.bt_t004)
    Button bt_t004;
    @BindView(R.id.bt_t005)
    Button bt_t005;
    @BindView(R.id.bt_t006)
    Button bt_t006;
    @BindView(R.id.bt_t007)
    Button bt_t007;
    @BindView(R.id.bt_t008)
    Button bt_t008;

    @BindView(R.id.bt_debug_test)
    Button bt_debug_test;
    @BindView(R.id.bt_adb_shell)
    Button bt_adb_shell;
    private String control_ip_str;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_t_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Help4SharedPreferences hsp = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN);
        control_ip_str = hsp.getVetIpVal();
    }

    @OnClick({R.id.bt_t001, R.id.bt_t002, R.id.bt_t003, R.id.bt_t004,
            R.id.bt_t005, R.id.bt_t006, R.id.bt_t007, R.id.bt_t008,
            R.id.bt_debug_test,
            R.id.bt_adb_shell,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_t001:
//                openNewPage(FSwipeMenuItemFragment.class);
                break;
            case R.id.bt_t002:
                ArrayList<String> ipAddress = FNetTools.getLocalIPAddress();
                XToastUtils.info("bt_ip_search" + ipAddress, Toast.LENGTH_LONG);
                String local_ip_str = ipAddress.get(0);
                break;
            case R.id.bt_t003:
                break;
            case R.id.bt_t004:
                break;
            case R.id.bt_t005:
                break;
            case R.id.bt_t006:
                break;
            case R.id.bt_t007:
                break;
            case R.id.bt_t008:
                startActivity(new Intent(this, TActivity_tfragment.class));
                break;
            case R.id.bt_debug_test:
                break;
            case R.id.bt_adb_shell:
                Intent intent = new Intent(this, AdbActivity.class);
                intent.putExtra(FCFGBusiness.KeyIntent.MAIN_CONTROL_IP, control_ip_str);
                int port = 5555;
                intent.putExtra(FCFGBusiness.KeyIntent.MAIN_PORT, port);
                this.startActivity(intent);
                break;
        }
    }
}
