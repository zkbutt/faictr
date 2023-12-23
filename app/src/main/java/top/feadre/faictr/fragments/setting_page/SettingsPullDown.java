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

package top.feadre.faictr.fragments.setting_page;

import android.content.Context;
import android.nfc.Tag;
import android.view.View;

import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;

import java.util.Arrays;

import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description:
 */
public abstract class SettingsPullDown<T> extends SettingsItemBase<T> implements View.OnClickListener {
    private static final String TAG = "SettingsPullDown";
    protected final String[] keys;
    protected final String[] vals;
    private OnSelectedListener<T> onSelectedListener;

    public SettingsPullDown(Context context, XUIGroupListView xuiglv_set,
                            String[] keys, String[] vals,
                            String title, T initial_value
    ) {
        super(context, xuiglv_set, title);
        this.keys = keys;
        this.vals = vals;
        this.viewCtr.setOrientation(XUICommonListItemView.HORIZONTAL);

        valRes = initial_value;
        String detail_text = null;
        String initial_str = t2str(initial_value);
        for (int i = 0; i < vals.length; i++) {
            if (vals[i].equals(initial_str)) {
                detail_text = keys[i];
            }
        }

//        int _index = Arrays.binarySearch(vals, t2str(initial_value));
        FTools.log_d(TAG, "SettingsPullDown initial_value = " + initial_value
                + "\ninitial_value = " + initial_value
//                + "\n_index = " + _index
                + "\nkeys = " + Arrays.toString(keys)
                + "\nvals = " + Arrays.toString(vals)
                + "\n"
        );
        this.viewCtr.setDetailText(detail_text);
        this.viewCtr.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        this.viewCtr.setOnClickListener(this);
    }

    private void show_select() {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context,
                (v, options1, options2, options3) -> {
                    viewCtr.setDetailText(keys[options1]);
                    valRes = str2t(vals[options1]);
                    select_save4sp(valRes);
                    if (onSelectedListener != null) {
                        onSelectedListener.onItemSelected(valRes);
                    }
                    FTools.log_d(TAG, "show_select select_val = " + valRes);
                    return false;
                })
//                .setTitleText(this.title)
//                .setSelectOptions(option_temp)
                .build();
        pvOptions.setPicker(keys);
        int i; //用于选择后，下次打开，默认选中上次的选择结果
        for (i = 0; i < keys.length; i++) {
            T data = str2t(vals[i]); //复写数据转换方法
            FTools.log_d(TAG, "show_select data = " + data + "\nvalRes = " + valRes);
            if (compareFun(data, valRes)) {
                break;
            }
        }
        pvOptions.setSelectOptions(i);
        pvOptions.show();
    }

    public abstract String t2str(T val);

    /**
     * int integer = Integer.parseInt(keys[index]);
     */
    public abstract T str2t(String val);

    /**
     * sp.edit().putFloat(FCFGBusiness.SPSet.QUALITY_RATIO, v_ctr_quality_ratio).apply();
     */
    public abstract void select_save4sp(T valRes);

    /**
     * 选择比较方法
     */
    public abstract boolean compareFun(T data, T valRes);

    public void setOnSelectedListener(OnSelectedListener<T> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    public void onClick(View view) {
        show_select();
    }

    public interface OnSelectedListener<T> {
        void onItemSelected(T valRes);
    }

}
