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

package top.feadre.faictr.fragments.base;

import android.graphics.Color;
import android.view.ViewGroup;


import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xuidemo.DemoDataProvider;
import com.xuexiang.xuidemo.adapter.SimpleRecyclerAdapter;
import com.xuexiang.xuidemo.base.BaseFragment;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Collection;

import butterknife.BindView;
import top.feadre.faictr.R;


@Page(name = "FSwipeMenuItemFragment\nFeadre副标题")
public class FSwipeMenuItemFragment extends BaseFragment {
    private static final String TAG = "FSwipeMenuItemFragment";
    @BindView(R.id.srv)
    SwipeRecyclerView srv;

    private SimpleRecyclerAdapter mAdapter;
    private OnItemMenuClickListener mMenuItemClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();

        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            XToastUtils.toast("list第" + position + "; 右侧菜单第" + menuPosition);
        } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
            XToastUtils.toast("list第" + position + "; 左侧菜单第" + menuPosition);
        }
    };
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        // 添加左侧的，如果不添加，则左侧不会出现菜单。
//        {
//            SwipeMenuItem addItem = new SwipeMenuItem(getContext())
//                    .setBackground(R.drawable.menu_selector_green)
//                    .setImage(R.drawable.ic_swipe_menu_add)
//                    .setWidth(width)
//                    .setHeight(height);
//            swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。
//
//            SwipeMenuItem closeItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
//                    .setImage(R.drawable.ic_swipe_menu_close)
//                    .setWidth(width)
//                    .setHeight(height);
//            swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
//        }

        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
                    .setImage(R.drawable.ic_swipe_menu_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_green)
                    .setText("添加")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fsmf;
    }


    @Override
    protected TitleBar initTitle() {
        return super.initTitle();
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(srv);

        //必须在setAdapter之前调用
        srv.setSwipeMenuCreator(swipeMenuCreator);
        //必须在setAdapter之前调用
        srv.setOnItemMenuClickListener(mMenuItemClickListener);
        mAdapter = new SimpleRecyclerAdapter();
        Collection<String> demoData = DemoDataProvider.getDemoData();
        mAdapter.refresh(demoData);

        srv.setAdapter(mAdapter);


    }


}
