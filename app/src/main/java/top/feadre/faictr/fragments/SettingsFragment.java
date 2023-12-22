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
import android.widget.CheckBox;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
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
    private Float v_ctr_quality_ratio;
    private Float v_ctr_show_ratio;
    private int v_ctr_bitrate;
    private int v_ctr_display_mode;
    private boolean v_ctr_bt_bottom;
    private SharedPreferences sp;
    private String[] arr_select_set_ctr_quality_ratio;
    private String[] arr_select_set_ctr_bitrate_keys;
    private String[] arr_select_set_ctr_display_mode_keys;
    private XUICommonListItemView view_set_ctr_quality_ratio;
    private XUICommonListItemView view_set_ctr_show_ratio;
    private XUICommonListItemView view_set_ctr_bitrate;
    private XUICommonListItemView view_set_ctr_display_mode;
    private XUICommonListItemView view_set_ctr_bt_bottom;
    private int option_temp;
    private String[] arr_select_set_ctr_display_mode_vals;
    private String[] arr_select_set_ctr_bitrate_vals;
    private String[] arr_select_set_ctr_show_ratio;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        Help4SharedPreferences h = new Help4SharedPreferences(FCFGBusiness.SPSet.KEY_MAIN);
        Help4SharedPreferences.DataMain dataMain = h.getMainCfg();
        sp = h.getSp();
        v_ctr_quality_ratio = dataMain.v_ctr_quality_ratio;
        v_ctr_show_ratio = dataMain.v_ctr_show_ratio;
        v_ctr_bitrate = dataMain.v_ctr_bitrate;
        v_ctr_display_mode = dataMain.v_ctr_display_mode;
        v_ctr_bt_bottom = dataMain.v_ctr_bt_bottom;

        //初始化显示和取值
        arr_select_set_ctr_quality_ratio = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio);
        arr_select_set_ctr_show_ratio = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_ratio);

        arr_select_set_ctr_bitrate_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_keys);
        arr_select_set_ctr_bitrate_vals = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_bitrate_vals);
        int indexBitrate = Arrays.binarySearch(arr_select_set_ctr_bitrate_vals, Integer.toString(v_ctr_bitrate));

        arr_select_set_ctr_display_mode_keys = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_keys);
        arr_select_set_ctr_display_mode_vals = ResUtils.getStringArray(getContext(), R.array.select_set_ctr_display_mode_vals);
        int indexDisplayMode = Arrays.binarySearch(arr_select_set_ctr_display_mode_vals, Integer.toString(v_ctr_display_mode));

        view_set_ctr_quality_ratio = xuiglv_set.createItemView(getString(R.string.set_label_ctr_quality_ratio));
        view_set_ctr_quality_ratio.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_quality_ratio.setDetailText(v_ctr_quality_ratio.toString());
        view_set_ctr_quality_ratio.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_show_ratio = xuiglv_set.createItemView(getString(R.string.set_label_ctr_show_ratio));
        view_set_ctr_show_ratio.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_show_ratio.setDetailText(v_ctr_show_ratio.toString());
        view_set_ctr_show_ratio.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_bitrate = xuiglv_set.createItemView(getString(R.string.set_label_ctr_bitrate));
        view_set_ctr_bitrate.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_bitrate.setDetailText(arr_select_set_ctr_bitrate_keys[indexBitrate]);
        view_set_ctr_bitrate.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_display_mode = xuiglv_set.createItemView(getString(R.string.set_label_ctr_display_mode));
        view_set_ctr_display_mode.setOrientation(XUICommonListItemView.HORIZONTAL);
        view_set_ctr_display_mode.setDetailText(arr_select_set_ctr_display_mode_keys[indexDisplayMode]);
        view_set_ctr_display_mode.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        view_set_ctr_bt_bottom = xuiglv_set.createItemView("本机底部控制按钮");
        view_set_ctr_bt_bottom.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        CheckBox _tcb = view_set_ctr_bt_bottom.getSwitch();

        FTools.log_d(TAG, "initViews v_ctr_bt_bottom = " + v_ctr_bt_bottom);
        _tcb.setChecked(v_ctr_bt_bottom);
        _tcb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FTools.log_d(TAG, "initViews setOnCheckedChangeListener v_ctr_bt_bottom = " + v_ctr_bt_bottom);
            v_ctr_bt_bottom = isChecked;
            _tcb.setChecked(v_ctr_bt_bottom);
            sp.edit().putBoolean(FCFGBusiness.SPSet.BT_BOTTOM, v_ctr_bt_bottom).apply();
        });


        View.OnClickListener onClickListener = v -> {
            if (v instanceof XUICommonListItemView) {
                CharSequence text = ((XUICommonListItemView) v).getText();
                if (text.equals(getString(R.string.set_label_ctr_quality_ratio))) {
                    show_set_ctr_quality_ratio();
                } else if (text.equals(getString(R.string.set_label_ctr_bitrate))) {
                    show_set_ctr_bitrate();
                } else if (text.equals(getString(R.string.set_label_ctr_display_mode))) {
                    show_set_ctr_display_mode();
                } else if (text.equals(getString(R.string.set_label_ctr_show_ratio))) {
                    show_set_ctr_show_ratio();
                }
            }
        };

        int size = DensityUtils.dp2px(getContext(), 20);
        XUIGroupListView.newSection(getContext())
                .setTitle(getString(R.string.set_title_1))
//                .setDescription("XXXX set_title_1 的描述")//这个在最后栏目的最后
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(view_set_ctr_quality_ratio, onClickListener)
                .addItemView(view_set_ctr_bitrate, onClickListener)
                .addTo(xuiglv_set);

        XUIGroupListView.newSection(getContext())
                .setTitle(getString(R.string.set_title_2))
//                .setDescription("XXXX set_title_2 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(view_set_ctr_display_mode, onClickListener)
                .addItemView(view_set_ctr_show_ratio, onClickListener)
                .addItemView(view_set_ctr_bt_bottom, onClickListener)
                .addTo(xuiglv_set);
    }

    private void show_set_ctr_quality_ratio() {
        String[] keys = arr_select_set_ctr_quality_ratio;
        XUICommonListItemView view = view_set_ctr_quality_ratio;
        String name_fun = "show_set_ctr_quality_ratio ";

        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view.setDetailText(keys[options1]);
                    option_temp = options1;

                    v_ctr_quality_ratio = Float.valueOf(keys[options1]);
                    sp.edit().putFloat(FCFGBusiness.SPSet.QUALITY_RATIO, v_ctr_quality_ratio).apply();
                    FTools.log_d(TAG, name_fun + v_ctr_quality_ratio);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_quality_ratio))
//                .setSelectOptions(option_temp)
                .build();
        pvOptions.setPicker(keys);
        int i;
        for (i = 0; i < keys.length; i++) {
            Float t = Float.valueOf(keys[i]);
            FTools.log_d(TAG, name_fun + Arrays.toString(keys) + " " + t + " " + i + " " + v_ctr_quality_ratio);
            //浮点比较
            if (Math.abs(t - v_ctr_quality_ratio) < 0.000001f) {
                break;
            }
        }
        pvOptions.setSelectOptions(i);
        pvOptions.show();
    }

    private void show_set_ctr_show_ratio() {
        String[] keys = arr_select_set_ctr_show_ratio;
        XUICommonListItemView view = view_set_ctr_show_ratio;
        String name_fun = "show_set_ctr_show_ratio ";

        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view.setDetailText(keys[options1]);
                    option_temp = options1;

                    v_ctr_show_ratio = Float.valueOf(keys[options1]);
                    sp.edit().putFloat(FCFGBusiness.SPSet.SHOW_RATIO, v_ctr_show_ratio).apply();
                    FTools.log_d(TAG, name_fun + v_ctr_show_ratio);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_show_ratio))
//                .setSelectOptions(option_temp)
                .build();
        pvOptions.setPicker(keys);
        int i;
        for (i = 0; i < keys.length; i++) {
            Float t = Float.valueOf(keys[i]);
            FTools.log_d(TAG, name_fun + Arrays.toString(keys) + " " + t + " " + i + " " + v_ctr_show_ratio);
            //浮点比较
            if (Math.abs(t - v_ctr_show_ratio) < 0.000001f) {
                break;
            }
        }
        pvOptions.setSelectOptions(i);
        pvOptions.show();
    }

    private void show_set_ctr_bitrate() {
        String[] keys = arr_select_set_ctr_bitrate_keys;
        String[] vals = arr_select_set_ctr_bitrate_vals;
        XUICommonListItemView view = view_set_ctr_bitrate;
        String name_fun = "show_set_ctr_bitrate ";
        FTools.log_d(TAG, name_fun + Arrays.toString(keys));
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view.setDetailText(keys[options1]);
                    option_temp = options1;

                    v_ctr_bitrate = Integer.valueOf(vals[options1]);
                    sp.edit().putInt(FCFGBusiness.SPSet.BITRATE, v_ctr_bitrate).apply();
                    FTools.log_d(TAG, name_fun + v_ctr_bitrate);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_bitrate))
//                .setSelectOptions(option_temp)
                .build();
        pvOptions.setPicker(keys);
        int i;
        for (i = 0; i < vals.length; i++) {
            Integer t = Integer.valueOf(vals[i]);
            FTools.log_d(TAG, name_fun + Arrays.toString(keys) + " " + t + " " + i + " " + v_ctr_bitrate);
            if (t == v_ctr_bitrate) {
                break;
            }
        }
        pvOptions.setSelectOptions(i);
        pvOptions.show();
    }

    private void show_set_ctr_display_mode() {
        String[] keys = arr_select_set_ctr_display_mode_keys;
        String[] vals = arr_select_set_ctr_display_mode_vals;
        XUICommonListItemView view = view_set_ctr_display_mode;
        String name_fun = "show_set_ctr_display_mode ";
        FTools.log_d(TAG, name_fun + Arrays.toString(keys));

        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(getContext(),
                (v, options1, options2, options3) -> {
                    view.setDetailText(keys[options1]);
                    option_temp = options1;

                    //这里需要自定义
                    v_ctr_display_mode = Integer.valueOf(vals[options1]);
                    sp.edit().putInt(FCFGBusiness.SPSet.DISPLAY_MODE, v_ctr_display_mode).apply();
                    FTools.log_d(TAG, name_fun + v_ctr_display_mode);
                    return false;
                })
                .setTitleText(getString(R.string.set_label_ctr_display_mode))
//                .setSelectOptions(option_temp)
                .build();
        pvOptions.setPicker(keys);
        int i;
        for (i = 0; i < vals.length; i++) {
            Integer t = Integer.valueOf(vals[i]);
            FTools.log_d(TAG, name_fun + Arrays.toString(keys) + " " + t + " " + i + " " + v_ctr_display_mode);
            if (t == v_ctr_display_mode) {
                break;
            }
        }
        pvOptions.setSelectOptions(i);
        pvOptions.show();
    }

}
