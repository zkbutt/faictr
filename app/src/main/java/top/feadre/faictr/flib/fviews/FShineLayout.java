package top.feadre.faictr.flib.fviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


import top.feadre.faictr.flib.FTools;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/22 11:52
 * @description: 点击用于父类传到子类
 */
public class FShineLayout extends RelativeLayout {
    public FShineLayout(Context context) {
        super(context);
    }

    public FShineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FShineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean callOnClick() {
        View childAt = getChildAt(0);
        childAt.callOnClick();//起效果
        FTools.log_d("FShineLayout", "onClick childAt = " + childAt.getId());
        return false; //结束
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;//拦截向下
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int deviceId = event.getDeviceId();
        FTools.log_d("FShineLayout", "onTouchEvent "
                + " super.onTouchEvent(event) = " + super.onTouchEvent(event)
                + " deviceId = " + deviceId);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            View childAt = getChildAt(0);
            childAt.callOnClick();//调用点击起效果
        }
        super.onTouchEvent(event);//响应保持原有点击
        return true; //消费掉
    }



}
