package org.voiddog.android.lib.base.touch;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

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
 * @since 2017-08-01 15:18
 */

public class ExpandClickTouchDelegate extends TouchDelegate{
    private static class ViewExpandData{
        View delegateView;
        Rect expandRect;
        boolean delegateTargeted;
    }

    private List<ViewExpandData> viewExpandDataList = new ArrayList<>();

    // the distance for judge scroll
    private int slop;

    /**
     * mBounds inflated to include some slop. This rect is to track whether the motion events
     * should be considered to be be within the delegate view.
     */
    private Rect slopBounds = new Rect();

    // the bounds of current view
    private Rect viewBounds = new Rect();

    /**
     * Constructor
     * @param expandOffset expand area size
     * @param delegateView The view that should receive motion events
     */
    public ExpandClickTouchDelegate(@NonNull View delegateView, int expandOffset) {
        super(new Rect(), delegateView);
        slop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        ViewExpandData expandData = new ViewExpandData();
        expandData.delegateView = delegateView;
        expandData.expandRect = new Rect(expandOffset, expandOffset, expandOffset, expandOffset);
        viewExpandDataList.add(expandData);
    }

    /**
     *
     * @param delegateView
     * @param expandRect expand rect size
     */
    public ExpandClickTouchDelegate(@NonNull View delegateView, Rect expandRect) {
        super(new Rect(), delegateView);
        ViewExpandData expandData = new ViewExpandData();
        expandData.delegateView = delegateView;
        expandData.expandRect = new Rect(expandRect);
        viewExpandDataList.add(expandData);
    }

    public void addDelegateView(@NonNull View delegateView, int expandOffset){
        ViewExpandData expandData = new ViewExpandData();
        expandData.delegateView = delegateView;
        expandData.expandRect = new Rect(expandOffset, expandOffset, expandOffset, expandOffset);
        viewExpandDataList.add(expandData);
    }

    public void addDelegateView(@NonNull View delegateView, Rect expandRect){
        ViewExpandData expandData = new ViewExpandData();
        expandData.delegateView = delegateView;
        expandData.expandRect = new Rect(expandRect);
        viewExpandDataList.add(expandData);
    }

    public void removeDelegateView(@NonNull View delegateView){
        for (ViewExpandData expandData : viewExpandDataList){
            if (expandData.delegateView == delegateView){
                viewExpandDataList.remove(expandData);
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = false;
        for (ViewExpandData expandData : viewExpandDataList){
            if (applyDelegateOnView(expandData, event)) {
                res = true;
                break;
            }
        }
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP){
            for (ViewExpandData expandData : viewExpandDataList){
                expandData.delegateTargeted = false;
            }
        }
        return res;
    }

    private RectF tmpRectF = new RectF();
    private boolean applyDelegateOnView(ViewExpandData viewExpandData, MotionEvent event){
        int x = (int)event.getX();
        int y = (int)event.getY();
        boolean sendToDelegate = false;
        boolean hit = true;
        boolean handled = false;
        View delegateView = viewExpandData.delegateView;

        if (delegateView.getVisibility() == View.GONE || delegateView.getVisibility() == View.INVISIBLE || !delegateView.isEnabled()){
            return false;
        }

        tmpRectF.set(delegateView.getLeft(), delegateView.getTop(), delegateView.getRight(), delegateView.getBottom());
        // expand view
        tmpRectF.left -= viewExpandData.expandRect.left;
        tmpRectF.top -= viewExpandData.expandRect.top;
        tmpRectF.right += viewExpandData.expandRect.right;
        tmpRectF.bottom += viewExpandData.expandRect.bottom;
        delegateView.getMatrix().mapRect(tmpRectF);
        tmpRectF.round(viewBounds);
        // compute slopBounds
        slopBounds.set(viewBounds);
        slopBounds.inset(-slop, -slop);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Rect bounds = viewBounds;

                if (bounds.contains(x, y)) {
                    viewExpandData.delegateTargeted = true;
                    sendToDelegate = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                sendToDelegate = viewExpandData.delegateTargeted;
                if (sendToDelegate) {
                    if (!slopBounds.contains(x, y)) {
                        hit = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                sendToDelegate = viewExpandData.delegateTargeted;
                viewExpandData.delegateTargeted = false;
                break;
        }
        if (sendToDelegate) {
            MotionEvent dispatchEvent = MotionEvent.obtain(event);
            if (hit) {
                // Offset event coordinates to be inside the target view
                dispatchEvent.setLocation(delegateView.getWidth() / 2, delegateView.getHeight() / 2);
            } else {
                // Offset event coordinates to be outside the target view (in case it does
                // something like tracking pressed state)
                dispatchEvent.setLocation(-(slop * 2), -(slop * 2));
            }
            handled = delegateView.dispatchTouchEvent(dispatchEvent);
            dispatchEvent.recycle();
        }
        return handled;
    }
}
