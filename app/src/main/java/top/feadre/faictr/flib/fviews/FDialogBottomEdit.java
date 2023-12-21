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

package top.feadre.faictr.flib.fviews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import top.feadre.faictr.R;
import top.feadre.faictr.cfg.FCFGBusiness;
import top.feadre.faictr.flib.FTools;
import top.feadre.faictr.flib.fviews.dialog_edit.EntityItem4SimpleRecyclerAdapter;
import top.feadre.faictr.flib.fviews.dialog_edit.FSimpleRecyclerAdapter;
import top.feadre.faictr.flib.fviews.dialog_edit.FSwipeRecyclerView;

public class FDialogBottomEdit extends BottomSheetDialog implements View.OnClickListener, OnItemClickListener {
    private static final String TAG = "FDialogBottomEdit ";

    private TextView tv_dbe_mid;
    //    private TextView tv_dbe_left;
    private TextView tv_dbe_right;

    private FSwipeRecyclerView fSwipeRecyclerView;
    private FSimpleRecyclerAdapter mAdapter;

    private OnItemMenuClickListener mMenuItemClickListener;

    public FDialogBottomEdit(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_bottom_edit, null);

        this.setContentView(view);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        WidgetUtils.transparentBottomSheetDialogBackground(this);

        fSwipeRecyclerView = view.findViewById(R.id.fsrv);
        mAdapter = new FSimpleRecyclerAdapter();

        mMenuItemClickListener = (menuBridge, position) -> {
            FTools.log_d(TAG, "mMenuItemClickListener "
                    + " menuBridge = " + menuBridge
                    + " position = " + position);
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                onRemoveData(position);
//                XToastUtils.toast("list第" + position + "; 右侧菜单第" + menuPosition);
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                XToastUtils.toast("list第" + position + "; 左侧菜单第" + menuPosition);
            }
        };

        //必须在setAdapter之前调用
        fSwipeRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        fSwipeRecyclerView.setOnItemClickListener(this);
//        Collection<String> demoData = DemoDataProvider.getDemoData();
//        mAdapter.refresh(demoData);
        fSwipeRecyclerView.setAdapter(mAdapter);

        LinkedList<EntityItem4SimpleRecyclerAdapter> dataItem = new LinkedList<EntityItem4SimpleRecyclerAdapter>();
//        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K41", "192.168.0.1"));
//        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K42", "192.168.0.1"));
//        dataItem.add(new EntityItem4SimpleRecyclerAdapter("K43", "192.168.0.1"));
        setDatas(dataItem);

        tv_dbe_mid = view.findViewById(R.id.tv_dbe_mid);
        tv_dbe_mid.setText(ResUtils.getString(R.string.FMainHelp4DialogBottomEdit_tv_dbe_mid_text));
        update_tv_dbe_mid();

//        tv_dbe_mid.setOnClickListener(this);
//        tv_dbe_left = view.findViewById(R.id.tv_dbe_left);
//        tv_dbe_left.setOnClickListener(this);

        tv_dbe_right = view.findViewById(R.id.tv_dbe_right);
        tv_dbe_right.setText(ResUtils.getString(R.string.FMainHelp4DialogBottomEdit_tv_dbe_right_text));
        tv_dbe_right.setTextColor(Color.BLUE);
        tv_dbe_right.setOnClickListener(this);
    }

    public void onClick(View view) {
        FTools.log_d(TAG, "onClick view.getId() = " + view.getId());
        switch (view.getId()) {
//            case R.id.tv_dbe_mid:
//                XToastUtils.info("tv_dbe_mid");
//                break;
//            case R.id.tv_dbe_left:
//                XToastUtils.info("tv_dbe_left");
//                break;
            case R.id.tv_dbe_right:
                new MaterialDialog.Builder(getContext())
                        .content(R.string.FMainHelp4DialogBottomEdit_tv_dbe_right_dialog)
                        .positiveText(R.string.lab_yes)
                        .negativeText(R.string.lab_no)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                onClearDatas();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    protected void onRemoveData(int position) {
        List<EntityItem4SimpleRecyclerAdapter> _d = mAdapter.getDatas();
        _d.remove(position);
        mAdapter.notifyItemRemoved(position);
        update_tv_dbe_mid();
    }

    protected void onClearDatas() {
        List<EntityItem4SimpleRecyclerAdapter> datas = mAdapter.getDatas();
        Iterator<EntityItem4SimpleRecyclerAdapter> iterator = datas.iterator();
        while (iterator.hasNext()) {
            EntityItem4SimpleRecyclerAdapter e = iterator.next();
            if (e.getTitle().equals(FCFGBusiness.FDialogBottomEdit.DEFAULT_NAME)) {
                iterator.remove();
            }
        }
//        data.clear();
        mAdapter.notifyDataSetChanged();
        this.cancel();
    }

    @Override
    public void show() {
        if (mAdapter.getDatas().size() > 0) {
            super.show();
        } else {
            XToastUtils.info(getContext().getString(R.string.FMainHelp4DialogBottomEdit_hint));
        }
    }

    public void addData(EntityItem4SimpleRecyclerAdapter e) {
        /* 这里不管是不是一样 全部进行添加最前操作 */
        // 新连接成功的要添加到第一个位置
        LinkedList<EntityItem4SimpleRecyclerAdapter> datas = (LinkedList<EntityItem4SimpleRecyclerAdapter>) mAdapter.getDatas();
        for (EntityItem4SimpleRecyclerAdapter d : datas) {
            //如果内容相等，则删除后加到第一个
            if (d.getContent().equals(e.getContent())) {
                datas.remove(d);
                break;
            }
            //如果内容都不一样的则直接加到第一个
        }
        datas.addFirst(e);
        update_tv_dbe_mid();
    }

    public void setDatas(LinkedList<EntityItem4SimpleRecyclerAdapter> datas) {
        mAdapter.getDatas().addAll(datas);
    }

    protected LinkedList<EntityItem4SimpleRecyclerAdapter> getDatas() {
        return (LinkedList<EntityItem4SimpleRecyclerAdapter>) mAdapter.getDatas();
    }

    protected void update_tv_dbe_mid() {
        String _text = ResUtils.getString(R.string.FMainHelp4DialogBottomEdit_tv_dbe_mid_text);
        tv_dbe_mid.setText(String.format(_text, mAdapter.getDatas().size()));
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        FTools.log_d(TAG, "setOnItemClickListener "
                + " view = " + view
                + " adapterPosition = " + adapterPosition
        );
    }
}
