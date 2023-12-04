/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.activity;

import android.os.Bundle;

import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xuidemo.base.BaseActivity;
import com.xuexiang.xuidemo.fragment.other.SearchComponentFragment;
import com.xuexiang.xuidemo.utils.Utils;

/**
 * 组件搜索页面
 *
 * @author xuexiang
 * @since 2020/4/1 1:26 AM
 */
public class SearchComponentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PageOption.to(SearchComponentFragment.class)
                .setAnim(CoreAnim.none)
                .open(this);
    }

    @Override
    public void onBackPressed() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            Utils.syncMainPageStatus();
        }
        super.onBackPressed();
    }
}
