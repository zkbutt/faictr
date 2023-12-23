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
import android.content.SharedPreferences;
import android.view.View;

import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;

import java.util.Arrays;

import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description: 结果是 int的
 */
public class SettingsPullDown4Int extends SettingsPullDown<Integer> {
    private static final String TAG = "SettingsPullDown4Int";
    private final SharedPreferences sp;
    private final String sp_key;

    public SettingsPullDown4Int(Context context, XUIGroupListView xuiglv_set,
                                String[] keys, String[] vals,
                                String title, Integer initial_value,
                                SharedPreferences sp, String sp_key
    ) {
        super(context, xuiglv_set, keys, vals, title, initial_value);
        this.sp = sp;
        this.sp_key = sp_key;
    }

    @Override
    public String t2str(Integer val) {
        return Integer.toString(val);
    }

    @Override
    public Integer str2t(String val) {
        return Integer.parseInt(val);
    }

    @Override
    public void select_save4sp(Integer valRes) {
        sp.edit().putInt(sp_key, valRes).apply();
    }

    @Override
    public boolean compareFun(Integer data, Integer valRes) {
        return data.intValue() == valRes.intValue();
    }
}
