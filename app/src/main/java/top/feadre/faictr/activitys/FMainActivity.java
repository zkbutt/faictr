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

import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import top.feadre.faictr.R;
import top.feadre.faictr.flib.base.FBaseActivity;

public class FMainActivity extends FBaseActivity {
    private static final String TAG = "FMainActivity";

    @BindView(R.id.tb)
    TitleBar tb;

    @BindView(R.id.met_ip)
    MaterialEditText met_ip;

    @BindView(R.id.xuiab_ip)
    Button xuiab_ip;

    private String[] mConstellationOption;
    private int constellationSelectOption = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fmain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flog_d(TAG, "onCreate --- ");

        tb.setLeftClickListener(view -> XToastUtils.toast("点击返回"))
                .setCenterClickListener(v -> XToastUtils.toast("点击标题"));

        tb.addAction(new TitleBar.ImageAction(R.drawable.more) {
            @Override
            public void performAction(View view) {
                XToastUtils.toast("点击菜单！");
            }
        });

        tb.addAction(new TitleBar.ImageAction(R.drawable.help) {
            @Override
            public void performAction(View view) {
                XToastUtils.toast("点击帮忙！");
            }
        });

        mConstellationOption = ResUtils.getStringArray(this, R.array.constellation_entry);

    }

    @OnClick({R.id.xuiab_ip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xuiab_ip:
                showConstellationPickerView();
                break;
            default:
                break;
        }
    }

    private void showConstellationPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (v, options1, options2, options3) -> {
            xuiab_ip.setText(mConstellationOption[options1]);
            constellationSelectOption = options1;
            return false;
        })
                .setTitleText(getString(R.string.title_constellation_select))
                .setSelectOptions(constellationSelectOption)
                .build();
        pvOptions.setPicker(mConstellationOption);
        pvOptions.show();
    }


}
