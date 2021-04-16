package org.voiddog.android.lib.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.annotation.NonNull;

import static android.content.Context.CLIPBOARD_SERVICE;

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
 * 剪贴板工具类
 *
 * @author qigengxin
 * @since 2018-07-23 18:18
 */
public class ClipboardUtil {

    public static void copyToClipboard(@NonNull Context context, String content) {
        ClipboardManager mgr = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        mgr.setText(content);
    }

    public static String getCopyStr(Context context) {
        String copyStr = "";
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData;
        if (cm != null) {
            clipData = cm.getPrimaryClip();
            copyStr = clipData.getItemAt(0).getText().toString();
        }
        return copyStr;
    }
}
