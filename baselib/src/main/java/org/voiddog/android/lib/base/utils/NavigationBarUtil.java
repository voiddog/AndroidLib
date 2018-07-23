package org.voiddog.android.lib.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

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
 * @since 2018-07-23 18:36
 */
public class NavigationBarUtil {

    static ThreadLocal<Point> sSize = new ThreadLocal<>();
    static ThreadLocal<Point> sRealSize = new ThreadLocal<>();

    /**
     * 设置 navigation bar color
     * @param activity
     * @param color
     */
    public static void setNavigationBarColor(@NonNull Activity activity, int color){
        setNavigationBarColor(activity.getWindow(), color);
    }

    /**
     * 设置 navigation bar color
     * @param window
     * @param color
     */
    public static void setNavigationBarColor(@NonNull Window window, int color){
        if (AndroidVersionUtil.isAfterL()){
            window.setNavigationBarColor(color);
        }
    }

    public static void setLightTheme(@NonNull Activity activity, int color){
        setLightTheme(activity, color, 0);
    }

    public static void setLightTheme(@NonNull Activity activity, int color, int failureColor){
        if (AndroidVersionUtil.isAfterO()){
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            setNavigationBarColor(activity, color);
        } else if (failureColor != 0){
            setNavigationBarColor(activity, failureColor);
        }
    }

    public static void setDarkTheme(@NonNull Activity activity, int color){
        setDarkTheme(activity, color, 0);
    }

    public static void setDarkTheme(@NonNull Activity activity, int color, int failureColor){
        if (AndroidVersionUtil.isAfterO()){
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()&~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            setNavigationBarColor(activity, color);
        } else if (failureColor != 0){
            setNavigationBarColor(activity, failureColor);
        }
    }

    /**
     * 获取底部导航条高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(@NonNull Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
