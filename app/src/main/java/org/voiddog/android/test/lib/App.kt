package org.voiddog.android.test.lib

import android.app.Application
import com.google.gson.Gson
import org.voiddog.android.lib.base.config.SPManager
import org.voiddog.android.lib.base.config.SpConfig
import org.voiddog.android.lib.design.DesignLibInit

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
 * @author qigengxin
 * @since 2018-07-24 18:38
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DesignLibInit.init(this)
        val gson = Gson()
        SPManager.INSTANCE.init(this, SpConfig.Builder()
                .setDefaultSpName(packageName)
                .setJsonParseSupplier(object : SpConfig.JSONParseSupplier {

                    override fun <T : Any?> parseObject(value: String?, clazz: Class<T>): T? {
                        return gson.fromJson(value, clazz)
                    }

                    override fun toJSONString(value: Any?): String? {
                        return gson.toJson(value)
                    }
                })
                .build())
    }
}