package org.voiddog.android.lib.base.rs;

import android.app.Application;
import android.content.Context;
import android.renderscript.RenderScript;

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
 * @since 2018-07-23 18:14
 */
public enum RenderScriptManager {
    INSTANCE;

    private RenderScript renderScript;

    public void init(Context context){
        if(renderScript == null){
            if (context instanceof Application) {
                renderScript = RenderScript.create(context);
            } else {
                renderScript = RenderScript.create(context.getApplicationContext());
            }
        }
    }

    public RenderScript getRenderScript(){
        if (renderScript == null){
            throw new IllegalArgumentException("RenderScriptManager 未初始化");
        }
        return renderScript;
    }
}
