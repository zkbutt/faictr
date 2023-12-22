package top.feadre.faictr.flib.fviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.xuexiang.xui.widget.button.shinebutton.ShineButton;

/**
 * @projectName faictr
 * created by Administrator on 2023/12/22 11:25
 * @description: 用于点击效果的按钮
 */
public class FShineButton extends ShineButton {
    public FShineButton(Context context) {
        super(context);
    }

    public FShineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setChecked(boolean checked, boolean anim, boolean callBack) {
        mIsChecked = checked;
        setTintColor(mCheckedColor);
        if (anim) {
            showAnim();
        }
        if (callBack) {
            onCheckedChanged(checked);
        }
        setEnabled(false);
    }

    @Override
    protected void doShareAnim() {
        mShakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f);
        mShakeAnimator.setInterpolator(new LinearInterpolator());
        mShakeAnimator.setDuration(500);
        mShakeAnimator.setStartDelay(180);
        invalidate();
        mShakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        mShakeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                setTintColor(mCheckedColor);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setTintColor(mNormalColor);
                setEnabled(true);
                mIsChecked = false;//强制是没有点击的状态
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                setTintColor(mNormalColor);
            }

        });
        mShakeAnimator.start();
    }
}
