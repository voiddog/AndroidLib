package org.voiddog.android.test.lib.sample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.voiddog.android.lib.base.config.ISPService
import org.voiddog.android.lib.base.config.SPApi
import org.voiddog.android.lib.base.config.SPManager
import org.voiddog.android.test.lib.R
import org.voiddog.android.test.lib.databinding.ActivitySpTestBinding

private data class UserData(val name: String, val phone: String, val age: Int)

private interface Api : ISPService {

    @SPApi(spKey = "id")
    fun writeId(id: Int): Api

    @SPApi(spKey = "price")
    fun writeFloat(price: Float): Api

    @SPApi(spKey = "name")
    fun writeName(name: String): Api

    @SPApi(spKey = "user")
    fun writeData(user: UserData): Api

    @SPApi(spKey = "userList")
    fun writeList(userList: List<UserData>): Api

    @SPApi(spKey = "id")
    fun getId(default: Int): Int

    @SPApi(spKey = "price")
    fun getFloat(default: Float): Float

    @SPApi(spKey = "name")
    fun getString(default: String?): String

    @SPApi(spKey = "user")
    fun getData(): UserData

    @SPApi(spKey = "userList")
    fun getDataList(): List<UserData>
}

/**
 * 测试 sp
 */
class SpTestActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySpTestBinding.inflate(layoutInflater)
        binding.btnWriteSp.setOnClickListener {
            SPManager.INSTANCE.getService(Api::class.java)
                    .writeFloat(100f)
                    .writeId(11037)
                    .writeName("voiddog")
                    .writeData(UserData("voiddog", "192.168.1.1", 25))
                    .writeList(arrayListOf(UserData("傅林南", "3838438", 26),
                            UserData("戚神", "1234567", 100000),
                            UserData("李锐", "6666666", 26)))
        }
        binding.btnReadSp.setOnClickListener {
            val api = SPManager.INSTANCE.getService(Api::class.java)
            binding.txtSp.text = """
                ${api.getId(-1)}
                ${api.getFloat(-1f)}
                ${api.getString(null)}
                ${api.getData()}
                ${api.getDataList()}
            """.trimIndent()
        }
        binding.btnClean.setOnClickListener {
            SPManager.INSTANCE.clean()
        }

        setContentView(binding.root)
    }
}
