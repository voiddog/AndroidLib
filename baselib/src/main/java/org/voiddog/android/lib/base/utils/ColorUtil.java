package org.voiddog.android.lib.base.utils;

import android.graphics.Color;
import androidx.annotation.FloatRange;
import android.text.TextUtils;

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
 * 颜色工具类
 *
 * @author qigengxin
 * @since 2018-07-23 18:19
 */
public class ColorUtil {

    /**
     * 从字符串生成颜色
     * @param hexString
     * @param defaultColor
     * @return
     */
    public static int parseColorFromString(String hexString, int defaultColor){
        if (TextUtils.isEmpty(hexString) || hexString.length() < 6){
            return defaultColor;
        }
        if (hexString.charAt(0) == '0' && hexString.charAt(1) == 'x') {
            hexString = hexString.replace("0x", "");
        }
        if (hexString.charAt(0) == '#') {
            hexString = hexString.replace("#", "");
        } else {
            hexString = "#" + hexString;
        }
        try {
            return Color.parseColor(hexString);
        } catch (Exception ignore){
            return defaultColor;
        }
    }

    /**
     * mix two color color = colorA * (1 - value) + colorB * value
     * @param colorA
     * @param colorB
     * @param value
     * @return
     */
    public static int mixColor(int colorA, int colorB, @FloatRange(from = 0, to = 1) float value){
        int oldA = (colorA >> 24) & 0xff;
        int oldR = (colorA >> 16) & 0xff;
        int oldG = (colorA >> 8) & 0xff;
        int oldB = colorA & 0xff;

        int newA = (colorB >> 24) & 0xff;
        int newR = (colorB >> 16) & 0xff;
        int newG = (colorB >> 8) & 0xff;
        int newB = colorB & 0xff;

        int mixAlpha = (int) (oldA*(1-value) + newA*value);
        int mixR = (int) (oldR * (1 - value) + newR * value);
        int mixG = (int) (oldG * (1 - value) + newG * value);
        int mixB = (int) (oldB * (1 - value) + newB * value);

        return Color.argb(mixAlpha, mixR, mixG, mixB);
    }
}
