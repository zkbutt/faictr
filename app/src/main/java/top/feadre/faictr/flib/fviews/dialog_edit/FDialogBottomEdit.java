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

package top.feadre.faictr.flib.fviews.dialog_edit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.feadre.faictr.R;

public class FDialogBottomEdit extends BottomSheetDialog implements View.OnClickListener {
    private TextView tv_dbe_mid;
    private TextView tv_dbe_left;
    private TextView tv_dbe_right;

    private FSwipeRecyclerView fSwipeRecyclerView;
    private ArrayList<EntityItem4SimpleRecyclerAdapter> dataItem;
    private FSimpleRecyclerAdapter mAdapter;

    private OnItemMenuClickListener mMenuItemClickListener;

    public FDialogBottomEdit(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_bottom_edit, null);
        tv_dbe_mid = view.findViewById(R.id.tv_dbe_mid);
        tv_dbe_left = view.findViewById(R.id.tv_dbe_left);
        tv_dbe_right = view.findViewById(R.id.tv_dbe_right);
        tv_dbe_mid.setOnClickListener(this);
        tv_dbe_left.setOnClickListener(this);
        tv_dbe_right.setOnClickListener(this);

        this.setContentView(view);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        WidgetUtils.transparentBottomSheetDialogBackground(this);


        fSwipeRecyclerView = view.findViewById(R.id.fsrv);
        mAdapter = new FSimpleRecyclerAdapter();

        mMenuItemClickListener = (menuBridge, position) -> {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                List<EntityItem4SimpleRecyclerAdapter> _d = mAdapter.getData();
                _d.remove(position);
                mAdapter.notifyItemRemoved(position);
                XToastUtils.toast("list第" + position + "; 右侧菜单第" + menuPosition);
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                XToastUtils.toast("list第" + position + "; 左侧菜单第" + menuPosition);
            }
        };

        //必须在setAdapter之前调用
        fSwipeRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
//        Collection<String> demoData = DemoDataProvider.getDemoData();
//        mAdapter.refresh(demoData);
        fSwipeRecyclerView.setAdapter(mAdapter);

        dataItem = new ArrayList<EntityItem4SimpleRecyclerAdapter>();
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K41", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K42", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K43", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K44", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K45", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K46", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K47", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K48", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K49", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K40", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K41", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K42", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K43", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K44", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K45", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K46", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K47", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K48", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K49", "192.168.0.1"));
        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K40", "192.168.0.1"));
        mAdapter.getData().addAll(dataItem);
//        tv_dbe_mid.setText();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dbe_mid:
                XToastUtils.info("tv_dbe_mid");
                break;
            case R.id.tv_dbe_left:
                XToastUtils.info("tv_dbe_left");
                break;
            case R.id.tv_dbe_right:
                XToastUtils.info("tv_dbe_right");
                break;
            default:
                break;
        }
    }
}
