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

package com.xuexiang.xuidemo.fragment.components.tabbar.tablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.xuexiang.xuidemo.fragment.components.tabbar.tabsegment.MultiPage;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

/**
 * @author xuexiang
 * @since 2020/5/21 1:19 AM
 */
@Page(name = "TabLayout+ViewPager2动态加载")
public class TabLayoutViewPager2Fragment extends BaseFragment implements TabLayout.OnTabSelectedListener, SimpleTabFragment.OnRefreshListener  {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_switch)
    AppCompatImageView ivSwitch;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;

    private boolean mIsShowNavigationView;

    private FragmentStateViewPager2Adapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tablayout_viewpager2;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("增加") {
            @Override
            public void performAction(View view) {
                mAdapter.addFragment(2, SimpleTabFragment.newInstance("动态加入", TabLayoutViewPager2Fragment.this), "动态加入");
                mAdapter.notifyDataSetChanged();
            }
        });
        titleBar.addAction(new TitleBar.TextAction("减少") {
            @Override
            public void performAction(View view) {
                mAdapter.removeFragment(2);
                mAdapter.notifyDataSetChanged();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        initTabLayout();
    }

    private void initTabLayout() {
        mAdapter = new FragmentStateViewPager2Adapter(this);
        tabLayout.setTabMode(MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setAdapter(mAdapter);
        // 设置缓存的数量
        viewPager.setOffscreenPageLimit(1);
        // 去除刷新动画
        viewPager.setPageTransformer(new MarginPageTransformer(0));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(mAdapter.getPageTitle(position))).attach();
    }

    @SingleClick
    @OnClick({R.id.iv_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch:
                refreshStatus(mIsShowNavigationView = !mIsShowNavigationView);
                break;
            default:
                break;
        }
    }

    private void refreshStatus(final boolean isShow) {
        ObjectAnimator rotation;
        ObjectAnimator tabAlpha;
        ObjectAnimator textAlpha;
        if (isShow) {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(tvTitle, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(tabAlpha).with(textAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                refreshAdapter(isShow);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switchContainer(isShow);
            }
        });
        animatorSet.setDuration(500).start();
    }

    private void refreshAdapter(boolean isShow) {
        if (viewPager == null) {
            return;
        }
        if (isShow) {
            // 动态加载选项卡内容
            for (String page : MultiPage.getPageNames()) {
                mAdapter.addFragment(SimpleTabFragment.newInstance(page, this), page);
            }
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(tabLayout);
        } else {
            mAdapter.clear();
        }
    }


    private void switchContainer(boolean isShow) {
        ViewUtils.setVisibility(tvTitle, !isShow);
        ViewUtils.setVisibility(viewPager, isShow);
    }


    @Override
    public void onTabRefresh(Fragment fragment, String title) {
        String newTitle = title + "R";
        int index = mAdapter.replaceFragment(fragment, SimpleTabFragment.newInstance(newTitle, this), newTitle);
        mAdapter.notifyItemChanged(index);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        XToastUtils.toast("选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroyView() {
        mAdapter.clear();
        super.onDestroyView();
    }

}
