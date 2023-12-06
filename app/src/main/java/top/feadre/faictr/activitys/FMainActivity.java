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


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import top.feadre.faictr.R;
import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.base.FBaseActivity;
import top.feadre.faictr.flib.base.FProgressDialog;

public class FMainActivity extends FBaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "FMainActivity";
    @BindView(R.id.tb_titlebar)
    TitleBar tb_titlebar;
    @BindView(R.id.vet_ip)
    ValidatorEditText vet_ip;
    @BindView(R.id.xuiab_ip_res)
    Button xuiab_ip_res;
    @BindView(R.id.bt_ip_search)
    Button bt_ip_search;
    @BindView(R.id.bt_one_link)
    Button bt_one_link;
    @BindView(R.id.bt_qr_code_link)
    Button bt_qr_code_link;
    @BindView(R.id.bt_pair_link)
    Button bt_pair_link;
    @BindView(R.id.sb_android11)
    SwitchButton sb_android11;


    private String[] arr_ip_res;
    private int option_ip_res = 0;
    private FMainHelp4FProgressDialog fMainHelp4FProgressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fmain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flog_d(TAG, "onCreate --- ");

        /*  --- title --- */
        String appName = getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
        tb_titlebar.setLeftClickListener(view -> XToastUtils.toast("点击返回"))
                .setTitle(appName)
                .setCenterClickListener(v -> XToastUtils.warning("点击标题"));

        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.bt_more) {
            @Override
            public void performAction(View view) {
                XToastUtils.error("点击菜单！");
            }
        });

        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.bt_help) {
            @Override
            public void performAction(View view) {
                XToastUtils.success("点击帮忙！");
            }
        });

        arr_ip_res = ResUtils.getStringArray(this, R.array.constellation_entry);

        /*  --- ip输入校验 --- */
//        met_ip.addValidator(new RegexpValidator("只能输入数字!", "\\d+"));
        vet_ip.addValidator(new RegexpValidator(getString(R.string.regex_ip), FREValidator.REGEX_IP));

        fMainHelp4FProgressDialog = new FMainHelp4FProgressDialog(this);
        sb_android11.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.xuiab_ip_res, R.id.bt_ip_search, R.id.bt_one_link, R.id.bt_pair_link,
            R.id.bt_qr_code_link,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xuiab_ip_res:
                show_ip_res();
                break;
            case R.id.bt_ip_search:
                XToastUtils.info("bt_ip_search");
                break;
            case R.id.bt_one_link:
                //手动校验
                boolean validateOnFocusLost = vet_ip.validate();
                XToastUtils.info(String.valueOf(validateOnFocusLost));
                fMainHelp4FProgressDialog.showDialog("bt_one_link", "一键连接", 180);
                fMainHelp4FProgressDialog.updateProgress(170, "正在工作的内容 bt_one_link");
                break;
            case R.id.bt_pair_link:
                XToastUtils.info("bt_pair_link");
                fMainHelp4FProgressDialog.showDialog("bt_pair_link", "bt_pair_link", 90);
                fMainHelp4FProgressDialog.updateProgress(90, "正在工作的内容 bt_pair_link");
                break;
            case R.id.bt_qr_code_link:
                XToastUtils.info("bt_qr_code_link");
                fMainHelp4FProgressDialog.showDialog("bt_qr_code_link", "bt_qr_code_link", 180);
                fMainHelp4FProgressDialog.updateProgress(110, "正在工作的内容 bt_qr_code_link");
                break;

            default:
                break;
        }
    }


    private void show_ip_res() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (v, options1, options2, options3) -> {
//            xuiab_ip.setText(mConstellationOption[options1]);
            vet_ip.setText(arr_ip_res[options1]);
            ;
            option_ip_res = options1;
            return false;
        })
//                .setTitleText(getString(R.string.title_constellation_select))
                .setSelectOptions(option_ip_res)
                .build();
        pvOptions.setPicker(arr_ip_res);
        pvOptions.show();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.sb_android11) {
            if (b) {
                bt_qr_code_link.setVisibility(View.VISIBLE);
                bt_pair_link.setVisibility(View.VISIBLE);
                bt_one_link.setEnabled(false);
            } else {
                bt_qr_code_link.setVisibility(View.INVISIBLE);
                bt_pair_link.setVisibility(View.INVISIBLE);
                bt_one_link.setEnabled(true);
            }
        }
    }
}
