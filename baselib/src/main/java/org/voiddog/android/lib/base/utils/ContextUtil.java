package org.voiddog.android.lib.base.utils;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 * 可以用于获取 Application 的 Context
 *
 * @author qigengxin
 * @since 2018-07-23 18:23
 */
public class ContextUtil {

    @Nullable
    private static Context appContext;

    public static synchronized void init(@NonNull Context context) {
        if (context instanceof Application) {
            appContext = context;
        } else {
            appContext = context.getApplicationContext();
        }
    }

    /**
     *
     * @return
     */
    @NonNull
    public static Context getAppContext() {
        if (appContext == null) {
            throw new IllegalStateException("Must call ContextUtil.init(appContext) first");
        }
        return appContext;
    }
}
