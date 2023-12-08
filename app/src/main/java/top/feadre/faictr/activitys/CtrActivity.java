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

import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xuexiang.xui.utils.XToastUtils;

import butterknife.BindView;
import rjsv.floatingmenu.floatingmenubutton.FloatingMenuButton;
import rjsv.floatingmenu.floatingmenubutton.subbutton.FloatingSubButton;
import top.feadre.faictr.R;
import top.feadre.faictr.flib.base.FBaseActivity;

public class CtrActivity extends FBaseActivity implements View.OnClickListener {
    @BindView(R.id.fmb_ctr_l_bt1)
    FloatingSubButton fmb_ctr_l_bt1;
    @BindView(R.id.fmb_ctr_l_bt2)
    FloatingSubButton fmb_ctr_l_bt2;
    @BindView(R.id.fmb_ctr_l_bt3)
    FloatingSubButton fmb_ctr_l_bt3;
    @BindView(R.id.fmb_ctr_l_bt4)
    FloatingSubButton fmb_ctr_l_bt4;
    @BindView(R.id.fmb_ctr_l_bt5)
    FloatingSubButton fmb_ctr_l_bt5;

    @BindView(R.id.fmb_ctr_r_bt1)
    FloatingSubButton fmb_ctr_r_bt1;
    @BindView(R.id.fmb_ctr_r_bt2)
    FloatingSubButton fmb_ctr_r_bt2;
    @BindView(R.id.fmb_ctr_r_bt3)
    FloatingSubButton fmb_ctr_r_bt3;
    @BindView(R.id.fmb_ctr_r_bt4)
    FloatingSubButton fmb_ctr_r_bt4;
    @BindView(R.id.fmb_ctr_r_bt5)
    FloatingSubButton fmb_ctr_r_bt5;

    @BindView(R.id.fam_ctr_bt1)
    FloatingActionButton fam_ctr_bt1;
    @BindView(R.id.fam_ctr_bt2)
    FloatingActionButton fam_ctr_bt2;
    @BindView(R.id.fam_ctr_bt3)
    FloatingActionButton fam_ctr_bt3;
    @BindView(R.id.fam_ctr_bt4)
    FloatingActionButton fam_ctr_bt4;
    @BindView(R.id.fam_ctr_bt5)
    FloatingActionButton fam_ctr_bt5;

    private FloatingActionMenu fam_ctr;
    private FloatingMenuButton fmb_ctr_r;
    private FloatingMenuButton fmb_ctr_l;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ctr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fam_ctr = findViewById(R.id.fam_ctr);
        fmb_ctr_r = findViewById(R.id.fmb_ctr_r);
        fmb_ctr_l = findViewById(R.id.fmb_ctr_l);

        fmb_ctr_l_bt5.setOnClickListener(this);
        fmb_ctr_l_bt4.setOnClickListener(this);
        fmb_ctr_l_bt3.setOnClickListener(this);
        fmb_ctr_l_bt2.setOnClickListener(this);
        fmb_ctr_l_bt1.setOnClickListener(this);

        fmb_ctr_r_bt5.setOnClickListener(this);
        fmb_ctr_r_bt4.setOnClickListener(this);
        fmb_ctr_r_bt3.setOnClickListener(this);
        fmb_ctr_r_bt2.setOnClickListener(this);
        fmb_ctr_r_bt1.setOnClickListener(this);

        fam_ctr_bt1.setOnClickListener(this);
        fam_ctr_bt2.setOnClickListener(this);
        fam_ctr_bt3.setOnClickListener(this);
        fam_ctr_bt4.setOnClickListener(this);
        fam_ctr_bt5.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        fam_ctr.getBackground().setAlpha(100);
//        fmb_ctr_l.getBackground().setAlpha(20);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fmb_ctr_l_bt1:
                XToastUtils.info("fmb_ctr_l_bt1");
                break;
            case R.id.fmb_ctr_l_bt2:
                XToastUtils.info("fmb_ctr_l_bt2");
                break;
            case R.id.fmb_ctr_l_bt3:
                XToastUtils.info("fmb_ctr_l_bt3");
                break;
            case R.id.fmb_ctr_l_bt4:
                XToastUtils.info("fmb_ctr_l_bt4");
                break;
            case R.id.fmb_ctr_l_bt5:
                XToastUtils.info("fmb_ctr_l_bt5");
                break;
            case R.id.fmb_ctr_r_bt1:
                XToastUtils.info("fmb_ctr_r_bt1");
                break;
            case R.id.fmb_ctr_r_bt2:
                XToastUtils.info("fmb_ctr_r_bt2");
                break;
            case R.id.fmb_ctr_r_bt3:
                XToastUtils.info("fmb_ctr_r_bt3");
                break;
            case R.id.fmb_ctr_r_bt4:
                XToastUtils.info("fmb_ctr_r_bt4");
                break;
            case R.id.fmb_ctr_r_bt5:
                XToastUtils.info("fmb_ctr_r_bt5");
                break;
            case R.id.fam_ctr_bt1:
                XToastUtils.info("fam_ctr_bt1");
                break;
            case R.id.fam_ctr_bt2:
                XToastUtils.info("fam_ctr_bt2");
                break;
            case R.id.fam_ctr_bt3:
                XToastUtils.info("fam_ctr_bt3");
                break;
            case R.id.fam_ctr_bt4:
                XToastUtils.info("fam_ctr_bt4");
                break;
            case R.id.fam_ctr_bt5:
                XToastUtils.info("fam_ctr_bt5");
                break;

        }
    }
}
