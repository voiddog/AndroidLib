package org.voiddog.android.sample.glidevsfresco

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher
import com.facebook.imagepipeline.core.ImagePipelineConfig
import okhttp3.OkHttpClient
import org.voiddog.android.lib.base.BaseLibInit
import org.voiddog.android.lib.design.DesignLibInit
import org.voiddog.android.lib.rx.RxLibInit

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
 * @since 2018-07-27 10:56
 */
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        val imageConfig = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .setNetworkFetcher(OkHttpNetworkFetcher(OkHttpClient()))
                .build()
        Fresco.initialize(this, imageConfig)
        DesignLibInit.init(this)
        RxLibInit.init(this)
        BaseLibInit.init(this)
    }
}