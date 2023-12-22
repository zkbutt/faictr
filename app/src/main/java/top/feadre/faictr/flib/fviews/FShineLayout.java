package top.feadre.faictr.flib.fviews;

import android.content.Context;
import android.util.AttributeSet;
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
        childAt.callOnClick();
        FTools.log_d("FRLShineButton", "callOnClick childAt = " + childAt.getId());
        return false;
    }

}
