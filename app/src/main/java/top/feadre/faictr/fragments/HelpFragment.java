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

package top.feadre.faictr.fragments;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.other.SponsorFragment;
import com.xuexiang.xuidemo.utils.Utils;
import com.xuexiang.xuidemo.widget.GuideTipsDialog;

import butterknife.BindView;
import top.feadre.faictr.R;


@Page(name = "帮助")
public class HelpFragment extends BaseFragment {
    @BindView(R.id.xui_gl)
    XUIGroupListView xui_gl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    protected void initViews() {
        XUIGroupListView.newSection(getContext())
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_homepage)), v -> Utils.goWeb(getContext(), "https://xuexiangjys.github.io/XUI/"))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_wiki)), v -> Utils.goWeb(getContext(), "https://github.com/xuexiangjys/XUI/wiki/"))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_github)), v -> Utils.goWeb(getContext(), "https://github.com/xuexiangjys/XUI/"))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_update)), v -> Utils.checkUpdate(getContext(), true))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_sponsor)), v -> openPage(SponsorFragment.class))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_tips)), v -> GuideTipsDialog.showTipsForce(getContext()))
                .addItemView(xui_gl.createItemView(getResources().getString(R.string.about_item_add_qq_group)), v -> Utils.goWeb(getContext(), getString(R.string.url_add_qq_group)))
                .addTo(xui_gl);
    }
}
