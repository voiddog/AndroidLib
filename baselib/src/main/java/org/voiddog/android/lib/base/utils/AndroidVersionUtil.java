package org.voiddog.android.lib.base.utils;

import android.os.Build;

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
 * Android 版本工具类
 *
 * @author qigengxin
 * @since 2018-07-23 18:35
 */
public class AndroidVersionUtil {

    /**
     * android 4.4 以后
     * @return
     */
    public static boolean isAfterKITKAT(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * android 5.0 以后
     * @return
     */
    public static boolean isAfterL(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * android 6.0 以后
     * @return
     */
    public static boolean isAfterM(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * android 7.0 以后
     * @return
     */
    public static boolean isAfterN(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * android 8.0 以后
     * @return
     */
    public static boolean isAfterO(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * Android 9.0 以后
     * @return
     */
    public static boolean isAfterP() {
        return Build.VERSION.SDK_INT >= 28;
    }
}
