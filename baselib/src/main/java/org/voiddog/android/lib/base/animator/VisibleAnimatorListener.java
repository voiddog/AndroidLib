package org.voiddog.android.lib.base.animator;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.view.View;

/**
 * ┏┛ ┻━━━━━┛ ┻┓
 * ┃　　　　　　 ┃
 * ┃　　　━　　　┃
 * ┃　┳┛　  ┗┳　┃
 * ┃　　　　　　 ┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　 ┃
 * ┗━┓　　　┏━━━┛
 * * ┃　　　┃   神兽保佑
 * * ┃　　　┃   代码无BUG！
 * * ┃　　　┗━━━━━━━━━┓
 * * ┃　　　　　　　    ┣┓
 * * ┃　　　　         ┏┛
 * * ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
 * * * ┃ ┫ ┫   ┃ ┫ ┫
 * * * ┗━┻━┛   ┗━┻━┛
 *
 * @author qigengxin
 * @since 2017-08-09 22:53
 */

public class VisibleAnimatorListener implements Animator.AnimatorListener{

    private int endVisible;
    private int cancelVisible = -1;
    private View targetView;

    public VisibleAnimatorListener(View targetView, int endVisible) {
        this.endVisible = endVisible;
        this.targetView = targetView;
    }

    public VisibleAnimatorListener(View targetView, int endVisible, int cancelVisible) {
        this.endVisible = endVisible;
        this.cancelVisible = cancelVisible;
        this.targetView = targetView;
    }

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {
        if (targetView != null){
            targetView.setVisibility(endVisible);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onAnimationCancel(Animator animation) {
        if (targetView != null && cancelVisible > 0){
            targetView.setVisibility(cancelVisible);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
