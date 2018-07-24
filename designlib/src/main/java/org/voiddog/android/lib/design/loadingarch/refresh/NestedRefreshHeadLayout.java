package org.voiddog.android.lib.design.loadingarch.refresh;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.voiddog.android.lib.design.R;

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
 * 默认头部下拉刷新
 *
 * @author qigengxin
 * @since 2018-05-28 19:07
 */
public class NestedRefreshHeadLayout extends FrameLayout implements NestedRefreshLayout.OnRefreshStateChangeListener{

    private static final int STATE_DISMISS = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOW = 2;
    private static final int STATE_DISMISSING = 3;

    private TextView txtContent;
    private View header;
    private int showState;

    public NestedRefreshHeadLayout(@NonNull Context context) {
        super(context);
        initView(null, 0);
    }

    public NestedRefreshHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public NestedRefreshHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    @TargetApi(21)
    public NestedRefreshHeadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setTranslationY(-getMeasuredHeight());
        if (showState == STATE_DISMISS) {
            reset();
        }
    }

    @Override
    public void onChange(NestedRefreshLayout layout, int oldState, int newState) {
        switch (newState) {
            case NestedRefreshLayout.STATE_NORMAL:
            case NestedRefreshLayout.STATE_CANCEL:
                cancelAnim();
                break;
            case NestedRefreshLayout.STATE_DISMISS:
                dismissAnim();
                break;
            case NestedRefreshLayout.STATE_REFRESH:
                showAnim();
                loadingAnim();
                break;
            case NestedRefreshLayout.STATE_PREPARE:
                showAnim();
                break;
        }
    }

    protected void initView(AttributeSet attrs, int defStyles) {
        header = inflate(getContext(), R.layout.void_design_layout_nested_refresh_head, this);
        txtContent = header.findViewById(R.id.txt_content);
    }

    public void reset() {
        header.setVisibility(INVISIBLE);
        header.setTranslationY(-header.getMeasuredHeight() * 2);
        header.setAlpha(0);
        txtContent.setText(R.string.void_design_pull_to_refresh);
        newShowState(STATE_DISMISS);
    }

    private void showAnim() {
        if (showState == STATE_SHOW || showState == STATE_SHOWING) {
            return;
        }
        header.setVisibility(VISIBLE);
        header.animate().setListener(null);
        header.animate().translationY(-header.getMeasuredHeight()).alpha(1)
                .setListener(showAnimListener).start();
        newShowState(STATE_SHOWING);
    }

    private void loadingAnim() {
        txtContent.setText("正在刷新");
    }

    private void dismissAnim() {
        txtContent.setText("刷新结束");
        if (showState == STATE_DISMISSING || showState == STATE_DISMISS) {
            return;
        }
        newShowState(STATE_DISMISSING);
        header.animate().setListener(null);
        header.animate().translationY(-header.getMeasuredHeight()*2).alpha(0)
                .setListener(dismissAnimListener).start();
    }

    private void cancelAnim() {
        if (showState == STATE_DISMISS || showState == STATE_DISMISSING) {
            return;
        }
        newShowState(STATE_DISMISSING);
        header.animate().setListener(null);
        header.animate().translationY(-header.getMeasuredHeight()*2).alpha(0)
                .setListener(dismissAnimListener).start();
    }

    private Animator.AnimatorListener dismissAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (showState == STATE_DISMISSING) {
                reset();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private Animator.AnimatorListener showAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (showState == STATE_SHOWING) {
                newShowState(STATE_SHOW);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private void newShowState(int newState) {
        if (showState == newState) {
            return;
        }
        showState = newState;
        if (newState == STATE_DISMISS) {
            header.setVisibility(INVISIBLE);
        } else {
            header.setVisibility(VISIBLE);
        }
    }
}
