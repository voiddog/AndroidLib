package org.voiddog.android.lib.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import org.voiddog.android.lib.base.config.SPManager;
import org.voiddog.android.lib.base.config.SpConfig;
import org.voiddog.android.lib.base.rs.RenderScriptManager;
import org.voiddog.android.lib.base.utils.ContextUtil;

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
 * 此工具类库的初始化
 *
 * @author qigengxin
 * @since 2018-07-24 18:34
 */
public class BaseLibInit {

    private static boolean isInit;

    public static synchronized void init(@NonNull Context context) {
        if (isInit) {
            return;
        }
        isInit = true;
        context = context instanceof Application ? context : context.getApplicationContext();
        RenderScriptManager.INSTANCE.init(context);
        ContextUtil.init(context);
    }
}
