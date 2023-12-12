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

package top.feadre.faictr.fragments;

import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;

@Page(name = "设置")
public class SettingsFragment extends BaseFragment {
    private static final String TAG = "SettingsFragment";
    @BindView(R.id.xuiglv_set)
    XUIGroupListView xuiglv_set;
    private Float set_ctr_size_ratio;
    private int set_ctr_bitrate;
    private boolean set_ctr_display_mode;
    private SharedPreferences sp;
    private String[] texts_select_set_ctr_size_ratio;
    private String[] texts_select_set_ctr_bitrate_val_keys;
    private String[] texts_select_set_ctr_display_mode_keys;
    private XUICommonListItemView select_set_ctr_size_ratio;
    private XUICommonListItemView select_set_ctr_bitrate;
    private XUICommonListItemView select_set_ctr_display_mode;
    private int option_set_ctr_size_ratio;
    private int option_set_ctr_display_mode;
    private int option_set_ctr_bitrate;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        sp = getContext().getSharedPreferences(FCFGBusiness.SPSet.KEY_CTR, 0);
        set_ctr_size_ratio = sp.getFloat(FCFGBusiness.SPSet.RESOLUTION_RATIO, 0.6F);
        set_ctr_bitrate = sp.getInt(FCFGBusiness.SPSet.VIDEO_BITRATE, 1024000);
        set_ctr_display_mode = sp.getBoolean(FCFGBusiness.SPSet.VIDEO_BITRATE, true);

        //这两个要用字符
        String set_ctr_bitrate_show = String.valueOf(set_ctr_bitrate);
        String set_ctr_display_mode_show = String.valueOf(set_ctr_display_mode);

        texts_select_set_ctr_size_ratio = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_size_ratio);
        texts_select_set_ctr_bitrate_val_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_val_keys);
        texts_select_set_ctr_display_mode_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_keys);

        select_set_ctr_size_ratio = xuiglv_set.createItemView(getString(R.string.set_label_ctr_size_ratio));
        select_set_ctr_size_ratio.setOrientation(XUICommonListItemView.HORIZONTAL);
        select_set_ctr_size_ratio.setDetailText(set_ctr_size_ratio.toString());
        select_set_ctr_size_ratio.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        select_set_ctr_bitrate = xuiglv_set.createItemView(getString(R.string.set_label_ctr_bitrate));
        select_set_ctr_bitrate.setOrientation(XUICommonListItemView.HORIZONTAL);
        select_set_ctr_bitrate.setDetailText(set_ctr_bitrate_show);
        select_set_ctr_bitrate.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        select_set_ctr_display_mode = xuiglv_set.createItemView(getString(R.string.set_label_ctr_display_mode));
        select_set_ctr_display_mode.setOrientation(XUICommonListItemView.HORIZONTAL);
        select_set_ctr_display_mode.setDetailText(set_ctr_display_mode_show);
        select_set_ctr_display_mode.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        View.OnClickListener onClickListener = v -> {
            if (v instanceof XUICommonListItemView) {
                CharSequence text = ((XUICommonListItemView) v).getText();
                if (text.equals(getString(R.string.set_label_ctr_size_ratio))) {
                    show_set_ctr_size_ratio_view();
                } else if (text.equals(getString(R.string.set_label_ctr_bitrate))) {
                    show_set_ctr_bitrate();
                } else if (text.equals(getString(R.string.set_label_ctr_display_mode))) {
                    show_set_ctr_display_mode();
                }
            }
        };

        int size = DensityUtils.dp2px(getContext(), 20);
        XUIGroupListView.newSection(getContext())
                .setTitle("控制参数")
//                .setDescription("Section 1 的描述") //这个是在结尾
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(select_set_ctr_size_ratio, onClickListener)
                .addItemView(select_set_ctr_bitrate, onClickListener)
                .addItemView(select_set_ctr_display_mode, onClickListener)
                .addTo(xuiglv_set);
    }

    private void show_set_ctr_size_ratio_view() {
        FTools.log_d(TAG, "show_set_ctr_size_ratio_view " + texts_select_set_ctr_size_ratio);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            select_set_ctr_size_ratio.setDetailText(texts_select_set_ctr_size_ratio[options1]);
            option_set_ctr_size_ratio = options1;
            return false;
        })
                .setTitleText(getString(R.string.set_label_ctr_size_ratio))
                .setSelectOptions(option_set_ctr_size_ratio)
                .build();
        pvOptions.setPicker(texts_select_set_ctr_size_ratio);
        pvOptions.show();
    }

    private void show_set_ctr_bitrate() {
        FTools.log_d(TAG, "show_set_ctr_bitrate " + texts_select_set_ctr_bitrate_val_keys);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            select_set_ctr_bitrate.setDetailText(texts_select_set_ctr_bitrate_val_keys[options1]);
            option_set_ctr_bitrate = options1;
            return false;
        })
                .setTitleText(getString(R.string.set_label_ctr_bitrate))
                .setSelectOptions(option_set_ctr_bitrate)
                .build();
        pvOptions.setPicker(texts_select_set_ctr_bitrate_val_keys);
        pvOptions.show();
    }

    private void show_set_ctr_display_mode() {
        FTools.log_d(TAG, "show_set_ctr_display_mode " + texts_select_set_ctr_display_mode_keys);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            select_set_ctr_display_mode.setDetailText(texts_select_set_ctr_display_mode_keys[options1]);
            option_set_ctr_display_mode = options1;
            return false;
        })
                .setTitleText(getString(R.string.set_label_ctr_display_mode))
                .setSelectOptions(option_set_ctr_display_mode)
                .build();
        pvOptions.setPicker(texts_select_set_ctr_display_mode_keys);
        pvOptions.show();
    }


}
