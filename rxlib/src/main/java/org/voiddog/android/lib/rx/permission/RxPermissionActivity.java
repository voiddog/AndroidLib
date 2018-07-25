package org.voiddog.android.lib.rx.permission;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;

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
 * 支持 Rx 权限请求的 Activity，具体代码借助 RxPermissionHandlerHelper 实现，
 * 也可以写在自定义的 Activity 或 Fragment 中
 *
 * @author qigengxin
 * @since 2018-07-25 15:20
 */
public class RxPermissionActivity extends AppCompatActivity {

    private RxPermissionHandlerHelper permissionHandlerHelper = new RxPermissionHandlerHelper(this);

    public Observable<RxPermissionResult> sendRxPermissionRequest(RxPermissionRequest request) {
        return RxPermissionUtil.checkPermission(this, permissionHandlerHelper, request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHandlerHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
