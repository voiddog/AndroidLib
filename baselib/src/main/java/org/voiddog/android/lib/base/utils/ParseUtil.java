package org.voiddog.android.lib.base.utils;

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
 * @since 2018-07-23 18:39
 */
public class ParseUtil {

    public static int parseInt(String s, int failureValue){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e){
            return failureValue;
        }
    }

    public static long parseLong(String s, long failureValue){
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ignore){
            return failureValue;
        }
    }

    public static float parseFloat(String s, float failureValue){
        try{
            return Float.parseFloat(s);
        } catch (NumberFormatException ignore){
            return failureValue;
        }
    }

    public static double parseDouble(String s, double failureValue){
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ignore){
            return failureValue;
        }
    }

    public static boolean parseBoolean(String s, boolean defaultValue){
        try {
            return Boolean.parseBoolean(s);
        } catch (NumberFormatException ignore){
            return defaultValue;
        }
    }
}
