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

package com.xuexiang.xuidemo.fragment.components.statelayout;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.statelayout.StatusLoader;
import com.xuexiang.xuidemo.base.ComponentContainerFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.status.adapter.DefaultStatusAdapter;
import com.xuexiang.xuidemo.fragment.components.statelayout.status.StatusLoaderMultipleFragment;
import com.xuexiang.xuidemo.fragment.components.statelayout.status.StatusLoaderSingleFragment;

/**
 * @author xuexiang
 * @since 2020/4/29 11:44 PM
 */
@Page(name = "StatusLoader\n使用状态适配器动态设置")
public class StatusLoaderFragment extends ComponentContainerFragment {

    @Override
    protected void initArgs() {
        StatusLoader.initDefault(new DefaultStatusAdapter());
    }

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                StatusLoaderMultipleFragment.class,
                StatusLoaderSingleFragment.class
        };
    }
}
