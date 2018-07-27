package org.voiddog.android.lib.base.widget;

import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewOutlineProvider;

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
 * 一些方便的 outline 提供器
 *
 * @author qigengxin
 * @since 2018-07-26 16:50
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class VoidOutLine {

    public static final ViewOutlineProvider OUTLINE_OVAL = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    };

    public static class OutlineBackground extends ViewOutlineProvider{
        float alpha = 1;

        public OutlineBackground(float alpha) {
            this.alpha = alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            if (view.getBackground() != null){
                view.getBackground().getOutline(outline);
            }
            outline.setAlpha(alpha);
        }
    }

    public static class OutlineOval extends ViewOutlineProvider {
        int left, top, right, bottom;
        float alpha = 1;

        public OutlineOval(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public OutlineOval(float alpha) {
            this.alpha = alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public void setRect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(left, top, view.getWidth() - right, view.getHeight() - bottom);
            outline.setAlpha(alpha);
        }
    }

    public static class OutlineRect extends ViewOutlineProvider {
        int left, top, right, bottom;
        float alpha = 1;

        public OutlineRect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public OutlineRect(float alpha) {
            this.alpha = alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public void setRect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(left, top, view.getWidth() - right, view.getHeight() - bottom);
            outline.setAlpha(alpha);
        }
    }

    public static class OutlineRoundRect extends ViewOutlineProvider {

        int left, top, right, bottom;
        float radius, alpha = 1;

        public OutlineRoundRect(float radius, int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.radius = radius;
        }

        public OutlineRoundRect(float radius, float alpha) {
            this.radius = radius;
            this.alpha = alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setRect(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(left, top, view.getWidth() - right, view.getHeight() - bottom, radius);
            outline.setAlpha(alpha);
        }
    }
}
