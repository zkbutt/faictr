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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import com.xuexiang.xpage.annotation.Page;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;

/**
 * 按钮，样式比较单一
 *
 * @author xuexiang
 * @since 5/16/23 12:21 AM
 */
@Page(name = "MaterialButton")
public class MaterialButtonFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_button;
    }

    @Override
    protected void initViews() {

    }
}
