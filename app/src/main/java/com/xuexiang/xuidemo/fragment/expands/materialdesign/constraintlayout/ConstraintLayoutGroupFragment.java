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

package com.xuexiang.xuidemo.fragment.expands.materialdesign.constraintlayout;

import android.view.View;
import android.widget.CompoundButton;

import androidx.constraintlayout.widget.Group;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2020-01-09 11:22
 */
@Page(name = "分组Group使用")
public class ConstraintLayoutGroupFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.sb_group)
    SwitchButton sbGroup;
    @BindView(R.id.sb_group2)
    SwitchButton sbGroup2;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.group2)
    Group group2;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraint_group;
    }

    @Override
    protected void initViews() {
        sbGroup.setChecked(true);
        sbGroup2.setChecked(true);
    }

    @Override
    protected void initListeners() {
        sbGroup.setOnCheckedChangeListener(this);
        sbGroup2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sb_group:
                group.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.sb_group2:
                group2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            default:
                break;
        }
    }
}
