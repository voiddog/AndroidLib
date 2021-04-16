package org.voiddog.android.lib.base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import org.voiddog.android.lib.base.rs.RenderScriptManager;

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
 * 模糊工具类
 *
 * @author qigengxin
 * @since 2018-07-23 18:11
 */
public class BlurUtil {

    /**
     * 从一个视图中获取到模糊图
     * @return
     */
    @Nullable
    public static Bitmap getBlurImgFromView(@NonNull View view, int maxBlurSize, int radius){
        Bitmap viewCache = getViewCacheWithMaxSize(view, maxBlurSize, maxBlurSize);
        if(viewCache == null){
            return null;
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            blurWithRenderScript(view.getContext(), viewCache, radius, true);
        } else {
            fastBlur(viewCache, 1, radius);
        }
        return viewCache;
    }

    /**
     * 模糊图片
     * @return
     */
    @Nullable
    public static Bitmap blurImage(@NonNull Context context, @Nullable Bitmap in, float scaleFactor, int radius){
        if (in == null){
            return null;
        }
        Bitmap overlay = Bitmap.createBitmap((int) (in.getWidth()*scaleFactor),
                (int) (in.getHeight()*scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(scaleFactor, scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(in, 0, 0, paint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            blurWithRenderScript(context, overlay, radius, true);
        } else {
            fastBlur(overlay, radius, true);
        }
        return overlay;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public synchronized static Bitmap blurWithRenderScript(@NonNull Context context, @Nullable Bitmap in
            , float radius, boolean canReuseInBitmap) throws RSRuntimeException {
        if (in == null) {
            return null;
        }
        RenderScriptManager.INSTANCE.init(context);
        return blurWithRenderScript(in, radius, canReuseInBitmap);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public synchronized static Bitmap blurWithRenderScript(@Nullable Bitmap in, float radius
            , boolean canReuseInBitmap) throws RSRuntimeException{
        if (in == null){
            return null;
        }

        RenderScript blurRs = RenderScriptManager.INSTANCE.getRenderScript();
        Bitmap outBitmap;

        if (canReuseInBitmap) {
            outBitmap = in;
        } else {
            outBitmap = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Bitmap.Config.ARGB_8888);
        }

        final Allocation input = Allocation.createFromBitmap(blurRs, in, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(blurRs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(blurRs, getElement(blurRs, in));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(outBitmap);
        output.destroy();
        input.destroy();
        script.destroy();

        return outBitmap;
    }

    private static Element getElement(RenderScript rs, Bitmap bitmap){
        switch (bitmap.getConfig()){
            case ARGB_8888:
                return Element.U8_4(rs);
            case RGB_565:
                return Element.RGB_565(rs);
            case ARGB_4444:
                return Element.U8_2(rs);
            case ALPHA_8:
                return Element.U8(rs);
        }
        return Element.U8_4(rs);
    }

    @Nullable
    public static Bitmap fastBlur(@Nullable Bitmap in, float scaleFactor, int radius){
        if (in == null) {
            return null;
        }

        Bitmap overlay = Bitmap.createBitmap((int) (in.getWidth()*scaleFactor),
                (int) (in.getHeight()*scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(scaleFactor, scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(in, 0, 0, paint);
        overlay = fastBlur(overlay, radius, true);
        return overlay;
    }

    /**
     * 快速模糊算法
     */
    @Nullable
    public static Bitmap fastBlur(@Nullable Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        if (sentBitmap == null) {
            return null;
        }

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    private static Bitmap getViewCacheWithMaxSize(View rootView, int maxWidth, int maxHeight) throws OutOfMemoryError {
        Rect rect = new Rect();

        rootView.getWindowVisibleDisplayFrame(rect);
        if (rootView.getWidth() == 0 || rootView.getHeight() == 0) {
            rootView.measure(
                    View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
            );
            rootView.layout(0, 0, rootView.getMeasuredWidth(),
                    rootView.getMeasuredHeight());
        }
        float w = rootView.getWidth(), h = rootView.getHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }

        float minScale = Math.min(maxWidth / w, maxHeight / h);
        int bitmapWidth = (int) (w * minScale), bitmapHeight = (int) (h * minScale);
        Bitmap drawBitmap = Bitmap.createBitmap(
                bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(drawBitmap);

        // 设置缩放
        canvas.scale(minScale, minScale);
        rootView.draw(canvas);

        return drawBitmap;
    }
}
