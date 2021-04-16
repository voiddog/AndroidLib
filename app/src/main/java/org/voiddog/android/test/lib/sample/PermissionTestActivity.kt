package org.voiddog.android.test.lib.sample

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import org.voiddog.android.lib.rx.permission.RxPermissionActivity
import org.voiddog.android.lib.rx.permission.RxPermissionException
import org.voiddog.android.lib.rx.permission.RxPermissionRequest
import org.voiddog.android.lib.rx.permission.RxPermissionResult
import org.voiddog.android.test.lib.databinding.ActivityPermissionTestBinding

class PermissionTestActivity : RxPermissionActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityPermissionTestBinding.inflate(layoutInflater)
    binding.btnRequestPermission.setOnClickListener {
      val request = RxPermissionRequest.newRequest()
          .addForcePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          .addForcePermission(Manifest.permission.READ_EXTERNAL_STORAGE)
          .addForcePermission(Manifest.permission.INTERNET)
          .addExtraPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
          .addExtraPermission(Manifest.permission.ACCESS_FINE_LOCATION);
      sendRxPermissionRequest(request)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            toast("权限请求成功")
            logPermissionResult(it)
          }, {
            toast("权限请求失败")
            if (it is RxPermissionException) {
              logPermissionResult(it.result)
            }
          })
    }
    setContentView(binding.root)
  }

  private fun toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  private fun logPermissionResult(result: RxPermissionResult) {
    Log.i(PermissionTestActivity::class.java.simpleName, result.toString())
  }
}