package org.voiddog.android.lib.design.loadingarch.refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.animation.SpringFlingAnimation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.voiddog.android.damp.view.NestedDampLayout;

import java.util.ArrayList;
import java.util.List;

public class NestedRefreshLayout extends NestedDampLayout implements IRefreshSender {

    // loading state
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PREPARE = 1;
    public static final int STATE_REFRESH = 2;
    public static final int STATE_CANCEL = 3;
    public static final int STATE_DISMISS = 4;

    public interface OnRefreshStateChangeListener {
        /**
         * on loading state change (refresh state, loadMore State)
         * @param layout the layout of owner
         * @param oldState old loading state {@link #STATE_NORMAL}
         * @param newState new loading state {@link #STATE_REFRESH}
         */
        void onChange(NestedRefreshLayout layout, int oldState, int newState);
    }

    private int refreshState = STATE_NORMAL;
    // 下拉刷新头部的高度，如果 == 0，则取用 children 中实现了 OnRefreshStateChangeListener 的子 view
    private int refreshHeight = -1;
    // bind refresh head
    private View bindRefreshHead;

    // listener
    private List<IRefreshListener> refreshListenerList = new ArrayList<>();
    private List<OnRefreshStateChangeListener> refreshStateListener = new ArrayList<>();

    public NestedRefreshLayout(@NonNull Context context) {
        super(context);
        initSelf();
    }

    public NestedRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSelf();
    }

    public NestedRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelf();
    }

    @TargetApi(21)
    public NestedRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSelf();
    }

    @Override
    public void addRefreshListener(IRefreshListener listener) {
        refreshListenerList.add(listener);
    }

    @Override
    public void removeRefreshListener(IRefreshListener listener) {
        refreshListenerList.remove(listener);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        SpringFlingAnimation animation = getDampAnimation();
        if (refreshState == STATE_REFRESH && !refreshing) {
            changeRefreshState(STATE_DISMISS);
            animation.start();
        } else if (refreshing && refreshState != STATE_REFRESH) {
            changeRefreshState(STATE_REFRESH);
            // 播放展开动画
            forceOffset(0);
            float offset = getOffset();
            float overScrollOffset = getMaxFlingOffset();
            if (offset < overScrollOffset) {
                // 需要往下滚，此处需要求一个简单积分
                float flingFriction = animation.getFlingFriction() * -4.2f;
                animation.setStartVelocity(flingFriction*(offset - overScrollOffset));
                animation.start();
            }
        }
    }

    @Override
    public float getMaxFlingOffset() {
        int refreshHeight = getRefreshMeasureHeight();
        return super.getMaxFlingOffset() + (refreshState == STATE_REFRESH ? refreshHeight : 0);
    }

    public void setRefreshHeight(int refreshHeight) {
        this.refreshHeight = refreshHeight;
    }

    public void addRefreshStateChangeListener(OnRefreshStateChangeListener listener) {
        refreshStateListener.add(listener);
    }

    public void removeRefreshStateChangeListener(OnRefreshStateChangeListener listener) {
        refreshStateListener.remove(listener);
    }

    public int getRefreshState() {
        return refreshState;
    }

    public int getRefreshMeasureHeight() {
        if (refreshHeight >= 0) {
            return refreshHeight;
        }
        ensureBindHeadView();
        return bindRefreshHead == null ? 0 : bindRefreshHead.getMeasuredHeight();
    }

    // private function
    private void initSelf() {
        addRefreshStateChangeListener(new OnRefreshStateChangeListener() {
            @Override
            public void onChange(NestedRefreshLayout layout, int oldState, int newState) {
                ensureBindHeadView();
                if (bindRefreshHead != null) {
                    ((OnRefreshStateChangeListener) bindRefreshHead).onChange(layout, oldState, newState);
                }
            }
        });
        addOffsetChangeListener(new OffsetChangeListener() {
            @Override
            public void onOffsetChange(NestedDampLayout layout, float oldOffset, float newOffset) {
                ensureBindHeadView();
                switch (refreshState) {
                    case STATE_NORMAL: {
                        if (isInNestedOrTouch() && newOffset > getRefreshMeasureHeight()) {
                            changeRefreshState(STATE_PREPARE);
                        }
                        break;
                    }
                    case STATE_PREPARE: {
                        if (!isInNestedOrTouch()) {
                            if (newOffset > getRefreshMeasureHeight()) {
                                changeRefreshState(STATE_REFRESH);
                            } else {
                                changeRefreshState(STATE_CANCEL);
                            }
                        } else {
                            if (newOffset < getRefreshMeasureHeight()) {
                                changeRefreshState(STATE_NORMAL);
                            }
                        }
                        break;
                    }
                    case STATE_DISMISS:
                    case STATE_CANCEL: {
                        if (isInNestedOrTouch() || newOffset == 0) {
                            changeRefreshState(STATE_NORMAL);
                        }
                        break;
                    }
                }
            }
        });
    }

    private void changeRefreshState(int newState) {
        if (refreshState == newState) {
            return;
        }
        int oldState = refreshState;
        refreshState = newState;
        for (OnRefreshStateChangeListener listener : refreshStateListener) {
            listener.onChange(this, oldState, newState);
        }
        if (newState == STATE_REFRESH) {
            notifyRefresh();
        }
    }

    private void notifyRefresh() {
        for (IRefreshListener listener : refreshListenerList) {
            listener.onRefresh(this);
        }
    }

    private View ensureBindHeadView() {
        if (bindRefreshHead == null || bindRefreshHead.getParent() != this) {
            bindRefreshHead = null;
            for (int i = getChildCount() - 1; i >= 0; --i) {
                View child = getChildAt(i);
                if (child instanceof OnRefreshStateChangeListener) {
                    bindRefreshHead = child;
                }
            }
        }
        return bindRefreshHead;
    }
}
