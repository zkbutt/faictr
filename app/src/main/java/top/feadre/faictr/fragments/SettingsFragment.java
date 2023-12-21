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

import java.util.Arrays;

import butterknife.BindView;
import top.feadre.faictr.R;
import top.feadre.faictr.activitys.Help4SharedPreferences;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;

@Page(name = "设置")
public class SettingsFragment extends BaseFragment {
    private static final String TAG = "SettingsFragment";
    @BindView(R.id.xuiglv_set)
    XUIGroupListView xuiglv_set;
    private Float v_ctr_size_ratio;
    private int v_ctr_bitrate;
    private int v_ctr_display_mode;
    private SharedPreferences sp;
    private String[] arr_select_set_ctr_size_ratio;
    private String[] arr_select_set_ctr_bitrate_keys;
    private String[] arr_select_set_ctr_display_mode_keys;
    private XUICommonListItemView view_set_ctr_size_ratio;
    private XUICommonListItemView view_set_ctr_bitrate;
    private XUICommonListItemView view_set_ctr_display_mode;
    private int option_set_ctr_size_ratio;
    private int option_set_ctr_display_mode;
    private int option_set_ctr_bitrate;
    private String[] arr_select_set_ctr_display_mode_vals;
    private String[] arr_select_set_ctr_bitrate_vals;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        Help4SharedPreferences h = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN);
        Help4SharedPreferences.DataMain dataMain = h.getMainCfg();
        v_ctr_size_ratio = dataMain.v_ctr_size_ratio;
        v_ctr_bitrate = dataMain.v_ctr_bitrate;
        v_ctr_display_mode = dataMain.v_ctr_display_mode;

        //初始化显示和取值
        arr_select_set_ctr_size_ratio = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_size_ratio);

        arr_select_set_ctr_bitrate_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_keys);
        arr_select_set_ctr_bitrate_vals = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_vals);
        int indexBitrate = Arrays.binarySearch(arr_select_set_ctr_bitrate_vals, Integer.toString(v_ctr_bitrate));

        arr_select_set_ctr_display_mode_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_keys);
        arr_select_set_ctr_display_mode_vals = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_vals);
        int indexDisplayMode = Arrays.binarySearch(arr_select_set_ctr_display_mode_vals, Integer.toString(v_ctr_display_mode));

        view_set_ctr_size_ratio = xuiglv_set.createItemView(getString(R.string.set_label_ctr_size_ratio));
        view_set_ctr_size_ratio.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_size_ratio.setDetailText(v_ctr_size_ratio.toString());
        view_set_ctr_size_ratio.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_bitrate = xuiglv_set.createItemView(getString(R.string.set_label_ctr_bitrate));
        view_set_ctr_bitrate.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_bitrate.setDetailText(arr_select_set_ctr_bitrate_keys[indexBitrate]);
        view_set_ctr_bitrate.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_display_mode = xuiglv_set.createItemView(getString(R.string.set_label_ctr_display_mode));
        view_set_ctr_display_mode.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_display_mode.setDetailText(arr_select_set_ctr_display_mode_keys[indexDisplayMode]);
        view_set_ctr_display_mode.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

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
                .setTitle(getString(R.string.set_title_1))
//                .setDescription("Section 1 的描述") //这个是在结尾
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(view_set_ctr_size_ratio, onClickListener)
                .addItemView(view_set_ctr_bitrate, onClickListener)
                .addItemView(view_set_ctr_display_mode, onClickListener)
                .addTo(xuiglv_set);
    }

    private void show_set_ctr_size_ratio_view() {
        FTools.log_d(TAG, "show_set_ctr_size_ratio_view " + arr_select_set_ctr_size_ratio);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view_set_ctr_size_ratio.setDetailText(arr_select_set_ctr_size_ratio[options1]);
                    option_set_ctr_size_ratio = options1;
                    v_ctr_size_ratio = Float.valueOf(arr_select_set_ctr_size_ratio[options1]);
                    sp.edit().putFloat(FCFGBusiness.SPSet.SIZE_RATIO, v_ctr_size_ratio).apply();
                    FTools.log_d(TAG, "show_set_ctr_size_ratio_view " + v_ctr_size_ratio);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_size_ratio))
                .setSelectOptions(option_set_ctr_size_ratio)
                .build();
        pvOptions.setPicker(arr_select_set_ctr_size_ratio);
        pvOptions.show();
    }

    private void show_set_ctr_bitrate() {
        FTools.log_d(TAG, "show_set_ctr_bitrate " + arr_select_set_ctr_bitrate_keys);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view_set_ctr_bitrate.setDetailText(arr_select_set_ctr_bitrate_keys[options1]);
                    option_set_ctr_bitrate = options1;
                    v_ctr_bitrate = Integer.valueOf(arr_select_set_ctr_bitrate_vals[options1]);
                    sp.edit().putInt(FCFGBusiness.SPSet.BITRATE, v_ctr_bitrate).apply();
                    FTools.log_d(TAG, "show_set_ctr_size_ratio_view " + v_ctr_bitrate);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_bitrate))
                .setSelectOptions(option_set_ctr_bitrate)
                .build();
        pvOptions.setPicker(arr_select_set_ctr_bitrate_keys);
        pvOptions.show();
    }

    private void show_set_ctr_display_mode() {
        FTools.log_d(TAG, "show_set_ctr_display_mode " + arr_select_set_ctr_display_mode_keys);
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view_set_ctr_display_mode.setDetailText(arr_select_set_ctr_display_mode_keys[options1]);
                    option_set_ctr_display_mode = options1;
                    v_ctr_display_mode = Integer.valueOf(arr_select_set_ctr_display_mode_vals[options1]);
                    sp.edit().putInt(FCFGBusiness.SPSet.DISPLAY_MODE, v_ctr_display_mode).apply();
                    FTools.log_d(TAG, "show_set_ctr_display_mode " + v_ctr_display_mode);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_display_mode))
                .setSelectOptions(option_set_ctr_display_mode)
                .build();
        pvOptions.setPicker(arr_select_set_ctr_display_mode_keys);
        pvOptions.show();
    }
}
