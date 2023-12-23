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

import com.xuexiang.xui.widget.grouplist.XUIGroupListView;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description: 结果是 ints的
 */
public class SettingsPullDown4IntsSize extends SettingsPullDown<int[]> {
    private static final String TAG = "SettingsPullDown4IntsSize";
    private final SharedPreferences sp;
    private final String sp_key_w;
    private final String sp_key_h;

    public SettingsPullDown4IntsSize(Context context, XUIGroupListView xuiglv_set,
                                     String[] keys, String[] vals,
                                     String title, int[] initial_value,
                                     SharedPreferences sp, String sp_key_w, String sp_key_h
    ) {
        super(context, xuiglv_set, keys, vals, title, initial_value);
        this.sp = sp;
        this.sp_key_w = sp_key_w;
        this.sp_key_h = sp_key_h;
    }

    @Override
    public String t2str(int[] val) {
        return String.format("%dx%d", val[0], val[1]);
    }

    @Override
    public int[] str2t(String val) {
        String[] xes = val.split("x");
        int[] ints = new int[2];
        ints[0] = Integer.valueOf(xes[0]);
        ints[1] = Integer.valueOf(xes[1]);
        return ints;
    }

    @Override
    public void select_save4sp(int[] valRes) {
        sp.edit().putInt(sp_key_w, valRes[0]).apply();
        sp.edit().putInt(sp_key_h, valRes[1]).apply();
    }

    @Override
    public boolean compareFun(int[] data, int[] valRes) {
        return data[0] == valRes[0] && data[1] == valRes[1];
    }
}
