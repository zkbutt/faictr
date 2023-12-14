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
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xutil.app.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;
import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FREValidator;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.base.FBaseActivity;
import top.feadre.faictr.flib.fviews.dialog_edit.EntityItem4SimpleRecyclerAdapter;
import top.feadre.faictr.fragments.HelpFragment;
import top.feadre.faictr.fragments.SettingsFragment;

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

    private int option_ip_res = 0;
    private FMainHelp4FProgressDialog fMainHelp4FProgressDialog;
    private FMainDialogBottomEdit fMainDialogBottomEdit; //历史下拉菜单
    private FMainHelp4AutoUpdater fMainHelp4AutoUpdater; //自动更新

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fmain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FTools.log_d(TAG, "onCreate --- ");

        fMainHelp4AutoUpdater = new FMainHelp4AutoUpdater(this,
                FCFGBusiness.APPSet.AU_URL_PATH,
                FCFGBusiness.APPSet.AU_NAME_APK_CFG);
        fMainHelp4AutoUpdater.checkPermissionsAndUpdate();


        /*  --- title点击侦听 --- */
//        String appName = getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
//        tb_titlebar.setLeftClickListener(view -> XToastUtils.toast("点击返回"))
//                .setTitle(appName)
//                .setCenterClickListener(v -> XToastUtils.warning("点击标题"));


        /*添加顶部菜单*/
        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.bt_help) {
            @Override
            public void performAction(View view) {
                ActivityUtils.startActivity(TActivity_base.class);
//                startActivity(new Intent(FMainActivity.this, TActivity_base.class));
            }
        });
        tb_titlebar.setTitle(getString(R.string.main_title));

        tb_titlebar.addAction(new TitleBar.ImageAction(R.drawable.set) {
            @Override
            public void performAction(View view) {
                openNewPage(SettingsFragment.class);
            }
        });

        /*  --- ip输入校验 --- */
//        met_ip.addValidator(new RegexpValidator("只能输入数字!", "\\d+"));
        vet_ip.addValidator(new RegexpValidator(getString(R.string.regex_ip), FREValidator.REGEX_IP));

        fMainHelp4FProgressDialog = new FMainHelp4FProgressDialog(this);
        sb_android11.setOnCheckedChangeListener(this);

        fMainDialogBottomEdit = new FMainDialogBottomEdit(this);
    }

    @OnClick({R.id.xuiab_ip_res, R.id.bt_ip_search, R.id.bt_one_link, R.id.bt_pair_link,
            R.id.bt_qr_code_link,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xuiab_ip_res:
                fMainDialogBottomEdit.show();
                break;
            case R.id.bt_ip_search:
//                startActivity(new Intent(FMainActivity.this, TActivity_base.class));
                XToastUtils.info("bt_ip_search");
                fMainDialogBottomEdit.addData(new EntityItem4SimpleRecyclerAdapter("K40", "192.168.0.1"));
                break;
            case R.id.bt_one_link:
                //手动校验
                boolean validateOnFocusLost = vet_ip.validate();
                vet_ip.showErrorMsg();
//                XToastUtils.info(String.valueOf(validateOnFocusLost));
//                fMainHelp4FProgressDialog.showDialog("bt_one_link", "一键连接", 180);
//                fMainHelp4FProgressDialog.updateProgress(170, "正在工作的内容 bt_one_link");

//                Intent intent = new Intent(this, CtrActivity.class);
//                startActivity(intent);
//                ActivityUtils.startActivity(CtrActivity.class);
                break;
            case R.id.bt_pair_link:
                XToastUtils.info("bt_pair_link");
                fMainHelp4FProgressDialog.showDialog("bt_pair_link", "bt_pair_link", 90);
                fMainHelp4FProgressDialog.updateProgress(90, "正在工作的内容 bt_pair_link");
                openNewPage(HelpFragment.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fMainHelp4AutoUpdater.funOnRequestPermissionsResult(requestCode, grantResults);
    }
}
