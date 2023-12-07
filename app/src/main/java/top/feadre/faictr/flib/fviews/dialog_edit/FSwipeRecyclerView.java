package top.feadre.faictr.flib.fviews.dialog_edit;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.xuexiang.xui.utils.WidgetUtils;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;


import top.feadre.faictr.R;

public class FSwipeRecyclerView extends SwipeRecyclerView {

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
                    .setText(getResources().getString(R.string.FSwipeRecyclerView_bt_del))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            /*
             * int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
             * int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
             * */
//            SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_green)
//                    .setText("添加")
//                    .setTextColor(Color.WHITE)
//                    .setWidth(width)
//                    .setHeight(height);
//            swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
        }
    };

    public FSwipeRecyclerView(Context context) {
        super(context);
        init();
    }

    public FSwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FSwipeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        WidgetUtils.initRecyclerView(this);

        //必须在setAdapter之前调用
        this.setSwipeMenuCreator(swipeMenuCreator);

    }
}
