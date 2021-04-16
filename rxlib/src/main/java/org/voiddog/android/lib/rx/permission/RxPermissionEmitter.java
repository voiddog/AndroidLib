package org.voiddog.android.lib.rx.permission;

import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.voiddog.android.lib.base.utils.ContextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
 * Rx权限请求发送
 *
 * @author qigengxin
 * @since 2018-07-25 11:37
 */
public class RxPermissionEmitter {

    @NonNull
    private final RxPermissionRequest permissionRequest;

    /**
     * 请求回调队列
     */
    private BlockingQueue<RxPermissionResult> permissionResultQueue = new LinkedBlockingQueue<>();

    public RxPermissionEmitter(@NonNull RxPermissionRequest permissionRequest) {
        this.permissionRequest = permissionRequest;
    }

    public int getRequestCode(){
        return permissionRequest.getRequestCode();
    }

    public void emitPermission(RxPermissionResult result){
        permissionResultQueue.offer(result);
    }

    @NonNull
    public RxPermissionRequest getPermissionRequest() {
        return permissionRequest;
    }

    public RxPermissionResult emitPermission(@Nullable String permissions[], @Nullable int[] grantResults){
        RxPermissionResult result = new RxPermissionResult();
        if (permissions != null && grantResults != null) {
            for (int i = 0, length = permissions.length; i < length; ++i) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    result.getDeniedPermissionList().add(permission);
                } else {
                    result.getGrantedPermissionList().add(permission);
                }
            }
        }

        // add permission that not in request list
        List<String> allRequestPermission = new ArrayList<>();
        allRequestPermission.addAll(permissionRequest.getForcePermissionList());
        allRequestPermission.addAll(permissionRequest.getExtraPermissionList());
        List<String> allReturnPermission = new ArrayList<>();
        if (permissions != null) {
            Collections.addAll(allReturnPermission, permissions);
        }
        for (String permission : allRequestPermission){
            if (!allReturnPermission.contains(permission)){
                if (RxPermissionUtil.hasPermission(ContextUtil.getAppContext(), permission)) {
                    // permission success
                    result.getGrantedPermissionList().add(permission);
                } else {
                    // permission denied
                    result.getDeniedPermissionList().add(permission);
                }
            }
        }

        // check force permission
        result.setSuccess(true);
        for (String forcePermission : permissionRequest.getForcePermissionList()){
            if (!result.getGrantedPermissionList().contains(forcePermission)){
                result.setSuccess(false);
                break;
            }
        }
        permissionResultQueue.offer(result);
        return result;
    }

    public RxPermissionResult request(){
        RxPermissionResult result;
        try {
            result = permissionResultQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = new RxPermissionResult();
        }
        return result;
    }
}
