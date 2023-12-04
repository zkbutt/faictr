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

package com.xuexiang.xuidemo.fragment.expands.materialdesign;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.config.AppPageConfig;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xuidemo.DemoDataProvider;
import top.feadre.faictr.R;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.adapter.WidgetItemAdapter;
import com.xuexiang.xuidemo.base.BaseSimpleListFragment;
import com.xuexiang.xuidemo.fragment.expands.materialdesign.bottom.DemoBottomSheetDialog;

import java.util.Collections;
import java.util.List;

/**
 * @author xuexiang
 * @since 2019-05-11 21:49
 */
@Page(name = "BottomSheetDialog")
public class BottomSheetDialogFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("List");
        lists.add("Grid");
        lists.add("DialogFragment");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                showBottomSheetListDialog(true);
                break;
            case 1:
                showBottomSheetListDialog(false);
                break;
            case 2:
                DemoBottomSheetDialog.newInstance().show(getFragmentManager());
                break;
            default:
                break;
        }
    }

    private void showBottomSheetListDialog(boolean isList) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_sheet, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        if (isList) {
            initDialogList(recyclerView);
        } else {
            initDialogGrid(recyclerView);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        WidgetUtils.transparentBottomSheetDialogBackground(dialog);
    }

    private void initDialogList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.refresh(DemoDataProvider.getDemoData());
    }

    private void initDialogGrid(RecyclerView recyclerView) {
        WidgetUtils.initGridRecyclerView(recyclerView, 3, DensityUtils.dp2px(getContext(), 2));

        WidgetItemAdapter widgetItemAdapter = new WidgetItemAdapter(sortPageInfo(AppPageConfig.getInstance().getComponents()));
        recyclerView.setAdapter(widgetItemAdapter);
    }

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


}
