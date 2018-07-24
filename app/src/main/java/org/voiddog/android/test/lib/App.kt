package org.voiddog.android.test.lib

import android.app.Application
import com.alibaba.fastjson.JSON
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
        SPManager.INSTANCE.init(this, SpConfig.Builder()
                .setDefaultSpName(packageName)
                .setJsonParseSupplier(object : SpConfig.JSONParseSupplier {
                    override fun <T : Any?> parseList(value: String?, clazz: Class<T>): MutableList<T> {
                        return JSON.parseArray(value, clazz)
                    }

                    override fun <T : Any?> parseObject(value: String?, clazz: Class<T>): T? {
                        return JSON.parseObject(value, clazz)
                    }

                    override fun toJSONString(value: Any?): String? {
                        return JSON.toJSONString(value)
                    }
                })
                .build())
    }
}