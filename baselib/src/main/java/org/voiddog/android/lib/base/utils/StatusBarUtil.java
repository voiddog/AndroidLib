package org.voiddog.android.lib.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.voiddog.android.lib.base.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
 * @since 2018-07-23 18:42
 */
public class StatusBarUtil {

    private static final int DEFAULT_STATUS_BAR_COLOR = 0x00ffffff;
    private static final int DEFAULT_TRANSLUCENT_LIGHT_STATUS_BAR_COLOR = 0x30000000;
    private static final int DEFAULT_LIGHT_STATUS_BAR_COLOR = 0xff84838F;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.void_base_status_bar_util_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.void_base_status_bar_util_translucent_view;

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     */

    public static void setColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        removeTranslucentView(activity);
        clearTransparentStatusBar(activity, color);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        setTranslucent(activity, DEFAULT_STATUS_BAR_COLOR);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarColor 状态栏透明度
     */
    public static void setTranslucent(Activity activity, @ColorInt int statusBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        transparentStatusBar(activity);
        addTranslucentView(activity, statusBarColor);
    }

    /**
     * 设置状态栏为白色主题
     * @param activity
     */
    public static void setLightTheme(Activity activity, int statusBarColor){
        setLightTheme(activity, statusBarColor, DEFAULT_LIGHT_STATUS_BAR_COLOR);
    }

    /**
     * 设置状态栏为白色主题
     * @param activity
     */
    public static void setLightTheme(Activity activity, int statusBarColor, int failureColor){
        if (!AndroidVersionUtil.isAfterL()){
            return;
        }
        // 小米后来被国外开发者妥协了，启用了原生设置头部的方式，所以这里设置两遍
        if (AndroidVersionUtil.isAfterM()){
            setFlymeStatusBarLightMode(activity.getWindow(), true);
            setMIUIStatusBarLightMode(activity.getWindow(), true);
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setColor(activity, statusBarColor);
        } else {
            setColor(activity, failureColor);
        }
    }

    /**
     * 设置状态栏为白色主题
     * @param activity
     */
    public static void setLightThemeTranslucent(Activity activity, int statusBarColor){
        setLightThemeTranslucent(activity, statusBarColor, DEFAULT_TRANSLUCENT_LIGHT_STATUS_BAR_COLOR);
    }

    /**
     * 设置状态栏为白色主题
     * @param activity
     */
    public static void setLightThemeTranslucent(Activity activity, int statusBarColor, int failureColor){
        if (!AndroidVersionUtil.isAfterL()){
            return;
        }
        // 小米后来被国外开发者妥协了，启用了原生设置头部的方式，所以这里设置两遍
        if (AndroidVersionUtil.isAfterM()){
            setFlymeStatusBarLightMode(activity.getWindow(), true);
            setMIUIStatusBarLightMode(activity.getWindow(), true);
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            statusBarColor = failureColor;
        }
        setTranslucent(activity, statusBarColor);
    }

    public static void setDarkTheme(Activity activity, int statusBarColor){
        setDarkTheme(activity, statusBarColor, Color.BLACK);
    }

    /**
     * 设置状态栏为黑色主题
     * @param activity
     */
    public static void setDarkTheme(Activity activity, int statusBarColor, int failureColor){
        if (!AndroidVersionUtil.isAfterL()){
            return;
        }
        if (AndroidVersionUtil.isAfterM()){
            setFlymeStatusBarLightMode(activity.getWindow(), false);
            setMIUIStatusBarLightMode(activity.getWindow(), false);
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
            setColor(activity, statusBarColor);
        } else {
            setColor(activity, failureColor);
        }
    }

    /**
     * 设置状态栏为黑色主题
     * @param activity
     */
    public static void setDarkThemeTranslucent(Activity activity, int statusBarColor){
        if (!AndroidVersionUtil.isAfterL()){
            return;
        }
        if (AndroidVersionUtil.isAfterM()){
            setFlymeStatusBarLightMode(activity.getWindow(), false);
            setMIUIStatusBarLightMode(activity.getWindow(), false);
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int flag = window.getDecorView().getSystemUiVisibility();
            flag &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(flag);
        }
        setTranslucent(activity, statusBarColor);
    }

    /**
     * 隐藏伪状态栏 View
     *
     * @param activity 调用的 Activity
     */
    public static void hideFakeStatusBarView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
        View fakeTranslucentView = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            fakeTranslucentView.setVisibility(View.GONE);
        }
    }

    /**
     * 为status 设置 padding
     * @param view
     */
    public static void setupPaddingForStatus(View view){
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        int statusBarHeight = getStatusBarHeight(view.getContext());
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(view.getContext())
                , view.getPaddingRight(), view.getPaddingBottom());
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null){
            return;
        }
        if (lp.height != ViewGroup.LayoutParams.MATCH_PARENT && lp.height != ViewGroup.LayoutParams.WRAP_CONTENT){
            lp.height += statusBarHeight;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     */
    private static void addTranslucentView(Activity activity, @ColorInt int color) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(color);
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, color));
        }
    }

    private static void removeTranslucentView(Activity activity){
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null){
            contentView.removeView(fakeTranslucentView);
        }
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        setRootView(activity, true);
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity, boolean fitSystemWindow) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fitSystemWindow);
            }
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void transparentStatusBar(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        View decorView = activity.getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        flags = addFlags(flags, View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        decorView.setSystemUiVisibility(flags);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void clearTransparentStatusBar(Activity activity, @ColorInt int statusBarColor){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(statusBarColor);
        View decorView = activity.getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        flags = removeFlags(flags, View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        decorView.setSystemUiVisibility(flags);
    }

    /**
     * 创建半透明矩形 View
     *
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private static boolean setFlymeStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignore) {}
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private static boolean setMIUIStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception ignore){}
        }
        return result;
    }


    private static int addFlags(int oldFlags, int flag){
        return (oldFlags&(~flag)) | flag;
    }

    private static int removeFlags(int oldFlags, int flag){
        return oldFlags & (~flag);
    }
}
