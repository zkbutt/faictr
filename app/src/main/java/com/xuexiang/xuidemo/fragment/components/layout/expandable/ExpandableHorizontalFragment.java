/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xuidemo.fragment.components.layout.expandable;

import androidx.appcompat.widget.AppCompatImageView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-11-22 14:42
 */
@Page(name = "水平伸缩使用")
public class ExpandableHorizontalFragment extends BaseFragment {
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.expand_button)
    AppCompatImageView expandButton;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expandable_horizontal;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (expandButton != null) {
                expandButton.setRotation(expansion * 180);
            }
        });
    }

    @OnClick(R.id.expand_button)
    public void onViewClicked() {
        expandableLayout.toggle();
    }
}
