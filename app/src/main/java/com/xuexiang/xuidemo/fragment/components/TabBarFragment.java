/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xuidemo.fragment.components;

import com.xuexiang.xpage.annotation.Page;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.EasyIndicatorFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.JPTabBarFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.TabControlViewFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.TabLayoutFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.TabSegmentFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.VerticalTabLayoutFragment;

/**
 * @author xuexiang
 * @since 2018/12/26 下午2:01
 */
@Page(name = "选项卡", extra = R.drawable.ic_widget_tabbar)
public class TabBarFragment extends ComponentContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                EasyIndicatorFragment.class,
                TabSegmentFragment.class,
                TabLayoutFragment.class,
                VerticalTabLayoutFragment.class,
                TabControlViewFragment.class,
                JPTabBarFragment.class
        };
    }
}
