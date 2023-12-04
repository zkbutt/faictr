/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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
import com.xuexiang.xuidemo.fragment.components.pickerview.AddressPickerFragment;
import com.xuexiang.xuidemo.fragment.components.pickerview.OptionsPickerViewFragment;
import com.xuexiang.xuidemo.fragment.components.pickerview.RulerViewFragment;
import com.xuexiang.xuidemo.fragment.components.pickerview.SeekBarFragment;
import com.xuexiang.xuidemo.fragment.components.pickerview.TimePickerFragment;

/**
 * @author xuexiang
 * @since 2019/1/1 下午8:30
 */
@Page(name = "选择器", extra = R.drawable.ic_widget_picker_view)
public class PickerViewFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                TimePickerFragment.class,
                AddressPickerFragment.class,
                OptionsPickerViewFragment.class,
                RulerViewFragment.class,
                SeekBarFragment.class
        };
    }
}
