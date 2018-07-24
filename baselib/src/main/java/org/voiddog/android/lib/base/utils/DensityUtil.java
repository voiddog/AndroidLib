package org.voiddog.android.lib.base.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

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
 * @since 2018-07-23 18:20
 */
public class DensityUtil {

    private static DisplayMetrics mDisplayMetrics;
    private static float mDensity;
    private static int mScreenWidth;
    private static int mScreenHeight;


    private static void initDisplayMetrics(@NonNull Context context) {
        if (mDisplayMetrics == null) {
            mDisplayMetrics = context.getResources().getDisplayMetrics();
            mScreenWidth = mDisplayMetrics.widthPixels;
            mScreenHeight = mDisplayMetrics.heightPixels;
            mDensity = mDisplayMetrics.density;
        }
    }

    public static int getScreenWidth(@NonNull Context context) {
        initDisplayMetrics(context);
        return mScreenWidth;
    }

    public static int getScreenHeight(@NonNull Context context) {
        initDisplayMetrics(context);
        return mScreenHeight;
    }

    /**
     * 获取当前屏幕宽、高分辨率
     *
     * @return
     */
    public static int[] getScreenWidthHeight(@NonNull Context context) {
        initDisplayMetrics(context);
        return new int[] { mScreenWidth, mScreenHeight };
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(@NonNull Context context, float dpValue) {
        if (dpValue == 0) {
            return 0;
        }
        initDisplayMetrics(context);
        int ret = (int) (dpValue * mDensity + 0.5f);
        return ret == 0 ? 1 : ret;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dp2floatPx(@NonNull Context context, float dpValue) {
        initDisplayMetrics(context);
        return dpValue * mDensity + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(@NonNull Context context, float pxValue) {
        initDisplayMetrics(context);
        return (int) (pxValue / mDensity + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)，用于字体大小设置
     */
    public static int sp2px(@NonNull Context context, float dpValue) {
        initDisplayMetrics(context);
        return (int) (dpValue * mDensity + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp，用于字体大小设置
     */
    public static int px2sp(@NonNull Context context, float pxValue) {
        initDisplayMetrics(context);
        return (int) (pxValue / mDensity + 0.5f);
    }

    public static int dp2px(float dp){
        return dp2px(ContextUtil.getAppContext(), dp);
    }

    public static int sp2px(float sp){
        return sp2px(ContextUtil.getAppContext(), sp);
    }
}
