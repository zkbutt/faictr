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
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;

import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description:
 */
public class SettingsSwitch extends SettingsItemBase<Boolean> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = "SettingsPullDown";
    private final SharedPreferences sp;
    private final String sp_key;
    private final CheckBox tcb;
    private OnClickExtendListener onClickExtendListener;

    public SettingsSwitch(Context context, XUIGroupListView xuiglv_set,
                          String title,
                          boolean initial_value,
                          SharedPreferences sp, String sp_key
    ) {
        super(context, xuiglv_set, title);
        this.sp = sp;
        this.sp_key = sp_key;
        this.viewCtr.setAccessoryType(XUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        viewCtr.setOnClickListener(this);
        tcb = viewCtr.getSwitch();
        tcb.setChecked(initial_value);
        tcb.setOnCheckedChangeListener(this);
        valRes = initial_value;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        valRes = b;
        tcb.setChecked(valRes);
        FTools.log_d(TAG, "onCheckedChanged "
                + "valRes = " + valRes
                + "b = " + b
        );
        sp.edit().putBoolean(sp_key, valRes).apply();
    }

    @Override
    public void onClick(View view) {
        FTools.log_d(TAG, "onCheckedChanged select_val = " + valRes);
        tcb.toggle();
        if (onClickExtendListener != null) {
            onClickExtendListener.onClickExtend();
        }
    }

    public void setOnClickExtendListener(OnClickExtendListener onClickExtendListener) {
        this.onClickExtendListener = onClickExtendListener;
    }

    public interface OnClickExtendListener {
        void onClickExtend();
    }
}
