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

import com.xuexiang.xui.widget.grouplist.XUICommonListItemView;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/23 10:20
 * @description:
 */
public class SettingsItemBase<T>{
    private static final String TAG = "SettingsItemBase";
    protected final Context context;

    protected final XUICommonListItemView viewCtr;
    protected T valRes;

    public SettingsItemBase(Context context, XUIGroupListView xuiglv_set,
                           String title
    ) {
        this.context = context;
        this.viewCtr = xuiglv_set.createItemView(title);
    }

    public XUICommonListItemView getViewRes() {
        return viewCtr;
    }

    public Context getContext() {
        return context;
    }
}
