package org.voiddog.android.lib.design.loadingarch.refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.voiddog.android.lib.design.R;
import org.voiddog.android.lib.design.animator.PhysicsSpringAnimSet;

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


    private static final int STATE_DISMISS = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOW = 2;
    private static final int STATE_DISMISSING = 3;

    private TextView txtContent;
    private ImageView imgIcon;
    private View header;
    private int showState;
    private Animation rotationAnimation;

    protected void initView(AttributeSet attrs, int defStyles) {
        header = inflate(getContext(), R.layout.void_design_layout_nested_refresh_head, this);
        txtContent = header.findViewById(R.id.txt_content);
        imgIcon = header.findViewById(R.id.img_icon);
        rotationAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.void_design_loading_rotation);
        rotationAnimation.setInterpolator(new LinearInterpolator());
        rotationAnimation.setRepeatCount(-1);
        showState = STATE_DISMISS;
    }

    private void showAnim() {
        if (showState == STATE_SHOW || showState == STATE_SHOWING) {
            return;
        }
        imgIcon.clearAnimation();
        PhysicsSpringAnimSet.from(imgIcon).rotation(180)
                .setStiffness(SpringForce.STIFFNESS_LOW)
                .getAnimationByIndex(PhysicsSpringAnimSet.ROTATION)
                .addEndListener(animationEndListener);
        imgIcon.setImageResource(R.drawable.void_design_ic_svg_down_gray_24dp);
        txtContent.setText(R.string.void_design_loose_to_refresh);
        newShowState(STATE_SHOWING);
    }

    private void loadingAnim() {
        txtContent.setText(R.string.void_design_is_refreshing);
        PhysicsSpringAnimSet.from(imgIcon).cancelAll();
        imgIcon.setRotation(0);
        imgIcon.setImageResource(R.drawable.void_design_ic_svg_star_loading_gray_24dp);
        imgIcon.setAnimation(rotationAnimation);
        rotationAnimation.start();
    }

    private void dismissAnim() {
        if (showState == STATE_DISMISSING || showState == STATE_DISMISS) {
            return;
        }
        imgIcon.clearAnimation();
        txtContent.setText(R.string.void_design_refresh_end);
        imgIcon.setRotation(0);
        imgIcon.setImageResource(R.drawable.void_design_ic_svg_check_gray_24dp);
        newShowState(STATE_DISMISS);
    }

    private void cancelAnim() {
        if (showState == STATE_DISMISS || showState == STATE_DISMISSING) {
            return;
        }
        imgIcon.clearAnimation();
        txtContent.setText(R.string.void_design_pull_to_refresh);
        imgIcon.setImageResource(R.drawable.void_design_ic_svg_down_gray_24dp);
        PhysicsSpringAnimSet.from(imgIcon).rotation(0)
                .setStiffness(SpringForce.STIFFNESS_LOW)
                .getAnimationByIndex(PhysicsSpringAnimSet.ROTATION)
                .addEndListener(animationEndListener);
        newShowState(STATE_DISMISSING);
    }
    private DynamicAnimation.OnAnimationEndListener animationEndListener = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
            if (showState == STATE_SHOWING) {
                showState = STATE_SHOW;
            } else if (showState == STATE_DISMISSING) {
                showState = STATE_DISMISSING;
            }
        }
    };

    private void newShowState(int newState) {
        if (showState == newState) {
            return;
        }
        showState = newState;
    }
}
