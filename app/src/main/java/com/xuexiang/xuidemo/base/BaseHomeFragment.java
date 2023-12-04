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

package com.xuexiang.xuidemo.base;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.activity.MainActivity;
import com.xuexiang.xuidemo.adapter.WidgetItemAdapter;
import com.xuexiang.xuidemo.fragment.other.AboutFragment;
import com.xuexiang.xuidemo.fragment.other.SearchComponentFragment;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 基础主页面
 *
 * @author xuexiang
 * @since 2018/12/29 上午11:18
 */
public abstract class BaseHomeFragment extends BaseFragment implements RecyclerViewHolder.OnItemClickListener<PageInfo> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setLeftImageResource(R.drawable.ic_action_menu);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            @SingleClick
            public void onClick(View v) {
                getContainer().openMenu();
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_query) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openNewPage(SearchComponentFragment.class);
            }

            @Override
            public int rightPadding() {
                return DensityUtils.dp2px(getContext(), 10);
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_about) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openNewPage(AboutFragment.class);
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_container;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        WidgetUtils.initGridRecyclerView(mRecyclerView, 3, DensityUtils.dp2px(getContext(), 2), ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_separator_light, ResUtils.getColor(getContext(), R.color.xui_config_color_separator_light_phone)));

        WidgetItemAdapter mWidgetItemAdapter = new WidgetItemAdapter(sortPageInfo(getPageContents()));
        mWidgetItemAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mWidgetItemAdapter);
    }

    /**
     * @return 页面内容
     */
    protected abstract List<PageInfo> getPageContents();

    /**
     * 进行排序
     *
     * @param pageInfoList
     * @return
     */
    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, (o1, o2) -> o1.getClassPath().compareTo(o2.getClassPath()));
        return pageInfoList;
    }

    @Override
    @SingleClick
    public void onItemClick(View itemView, PageInfo widgetInfo, int pos) {
        if (widgetInfo != null) {
            openNewPage(widgetInfo.getName());
        }
    }

    public MainActivity getContainer() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

}
