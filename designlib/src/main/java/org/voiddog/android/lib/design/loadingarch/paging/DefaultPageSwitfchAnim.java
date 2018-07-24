package org.voiddog.android.lib.design.loadingarch.paging;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

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
 * @since 2018-04-27 17:32
 */
public class DefaultPageSwitfchAnim implements IPageSwitchAnim {

    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_SCALE = 1.1f;

    View currentView;

    @Override
    public View getCurrentView() {
        return currentView;
    }

    @Override
    public void setupCurrentView(View view) {
        currentView = view;
    }

    @Override
    public void switchTo(View view) {
        if (currentView == view) {
            return;
        }

        if (currentView != null) {
            currentView.animate().cancel();
            if (currentView.getVisibility() == View.VISIBLE && currentView.getAlpha() > 0) {
                currentView.animate()
                        .scaleX(MAX_SCALE)
                        .scaleY(MAX_SCALE)
                        .alpha(0)
                        .setDuration(300)
                        .setInterpolator(new DecelerateInterpolator(2))
                        .setListener(new VisibleAnimatorListener(currentView, View.INVISIBLE))
                        .start();
            }
        }

        view.animate().cancel();
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() == 0) {
            view.setScaleY(MIN_SCALE);
            view.setScaleX(MIN_SCALE);
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator(2))
                .setListener(new VisibleAnimatorListener(view, View.VISIBLE))
                .start();

        currentView = view;
    }

    static class VisibleAnimatorListener implements Animator.AnimatorListener{
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
}
