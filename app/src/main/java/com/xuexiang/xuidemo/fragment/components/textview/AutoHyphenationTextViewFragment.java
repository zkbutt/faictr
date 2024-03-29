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

package com.xuexiang.xuidemo.fragment.components.textview;

import com.xuexiang.xpage.annotation.Page;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;

/**
 * @author xuexiang
 * @since 2020/5/20 1:15 AM
 */
@Page(name = "自动断字换行文本")
public class AutoHyphenationTextViewFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_auto_hyphenation_textview;
    }

    @Override
    protected void initViews() {


    }
}
