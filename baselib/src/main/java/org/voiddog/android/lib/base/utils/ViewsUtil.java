package org.voiddog.android.lib.base.utils;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.android.lib.base.touch.ExpandClickTouchDelegate;

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
 * @since 2018-07-23 18:52
 */
public class ViewsUtil {

    public static void expandViewTouchDelegate(@Nullable View view, int expandArea){
        expandViewTouchDelegate(view, expandArea, expandArea, expandArea, expandArea);
    }

    public static void expandViewTouchDelegate(@Nullable View view, int left, int top, int right, int bottom) {
        if (view == null){
            return;
        }
        if (!(view.getParent() instanceof ViewGroup)){
            Log.e("ViewUtils", "the parent must instance of ViewGroup");
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent.getTouchDelegate() instanceof ExpandClickTouchDelegate){
            ExpandClickTouchDelegate delegate = (ExpandClickTouchDelegate) parent.getTouchDelegate();
            delegate.addDelegateView(view, new Rect(left, top, right, bottom));
        } else {
            ExpandClickTouchDelegate delegate = new ExpandClickTouchDelegate(view, new Rect(left, top, right, bottom));
            parent.setTouchDelegate(delegate);
        }
    }

    public static void removeExpandViewTouchDelegate(@Nullable View view){
        if (view == null || view.getParent() == null){
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent.getTouchDelegate() instanceof ExpandClickTouchDelegate){
            ExpandClickTouchDelegate delegate = (ExpandClickTouchDelegate) parent.getTouchDelegate();
            delegate.removeDelegateView(view);
        }
    }

    public static void requireClipChild(@Nullable View view, boolean clip){
        if(view == null || !(view.getParent() instanceof ViewGroup)){
            return;
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        requireClipParent(parent, clip);
    }

    public static void requireClipParent(@Nullable ViewGroup parent, boolean clip){
        while(parent != null){
            parent.setClipChildren(clip);
            parent.setClipToPadding(clip);
            if(parent.getParent() instanceof ViewGroup){
                parent = (ViewGroup) parent.getParent();
            } else {
                parent = null;
            }
        }
    }
}
