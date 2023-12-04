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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import top.feadre.faictr.R;
import com.xuexiang.xutil.common.RandomUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author xuexiang
 * @since 2020/4/21 12:24 AM
 */
public class SimpleTabFragment extends Fragment {
    private static final String TAG = "SimpleTabFragment";

    private static final String KEY_TITLE = "title";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private Unbinder mUnbinder;

    @AutoWired(name = KEY_TITLE)
    String title;

    private OnRefreshListener mListener;


    public static SimpleTabFragment newInstance(String title, OnRefreshListener listener) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        SimpleTabFragment fragment = new SimpleTabFragment();
        fragment.setArguments(args);
        fragment.setOnRefreshListener(listener);
        return fragment;
    }

    public SimpleTabFragment setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach:" + title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach:" + title);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:" + title);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop:" + title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XRouter.getInstance().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_tab, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        int randomNumber = RandomUtils.getRandom(10, 100);
        Log.e(TAG, "initView, random number:" + randomNumber + ", " + title);
        tvTitle.setText(String.format("这个是%s页面的内容", title));
        tvExplain.setText(String.format("这个是页面随机生成的数字:%d", randomNumber));
        btnRefresh.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTabRefresh(this, title);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
        Log.e(TAG, "onDestroyView:" + title);

    }

    public interface OnRefreshListener {
        /**
         * 刷新
         *
         * @param fragment 页面
         * @param title 标题
         */
        void onTabRefresh(Fragment fragment, String title);
    }
}
