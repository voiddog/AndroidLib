package org.voiddog.android.test.lib.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sp_test.*
import org.voiddog.android.lib.base.config.ISPService
import org.voiddog.android.lib.base.config.SPApi
import org.voiddog.android.lib.base.config.SPManager
import org.voiddog.android.test.lib.R

private data class UserData(val name: String, val phone: String, val age: Int)

private interface Api : ISPService {

    @SPApi(spKey = "id")
    fun writeId(id: Int)

    @SPApi(spKey = "price")
    fun writeFloat(price: Float)

    @SPApi(spKey = "name")
    fun writeString(name: String)

    @SPApi(spKey = "user")
    fun writeData(user: UserData)
}

/**
 * 测试 sp
 */
class SpTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_test)
        btn_write_sp.setOnClickListener {
            SPManager.INSTANCE.getService(Api::class.java)
                    .writeFloat(100f)
        }
    }
}
