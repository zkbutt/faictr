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
import android.util.Log;

import com.xuexiang.xui.widget.grouplist.XUIGroupListView;

import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description: 结果是Float的
 */
public class SettingsPullDown4Float extends SettingsPullDown<Float> {
    private static final String TAG = "SettingsPullDown4Int";
    private final SharedPreferences sp;
    private final String sp_key;

    public SettingsPullDown4Float(Context context, XUIGroupListView xuiglv_set,
                                  String[] keys, String[] vals,
                                  String title, Float initial_value,
                                  SharedPreferences sp, String sp_key
    ) {
        super(context, xuiglv_set, keys, vals, title, initial_value);
        this.sp = sp;
        this.sp_key = sp_key;
    }

    @Override
    public String t2str(Float val) {
        return Float.toString(val);
    }

    @Override
    public Float str2t(String val) {
        return Float.parseFloat(val);
    }

    @Override
    public void select_save4sp(Float valRes) {
        sp.edit().putFloat(sp_key, valRes).apply();
    }

    @Override
    public boolean compareFun(Float data, Float valRes) {

        float abs = Math.abs(data.floatValue() - valRes.floatValue());
        Log.d(TAG, "compareFun: "
                +" data.floatValue()= "+data.floatValue()
                +" valRes.floatValue()= "+valRes.floatValue()
                +" abs= "+abs
        );
        return abs < 0.000001f;
    }
}
