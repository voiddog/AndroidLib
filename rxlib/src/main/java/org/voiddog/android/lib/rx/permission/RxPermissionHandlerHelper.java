package org.voiddog.android.lib.rx.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
 * 帮助处理 Activity 中的逻辑
 *
 * @author qigengxin
 * @since 2018-07-25 14:32
 */
public class RxPermissionHandlerHelper implements IRxPermissionHandler {

    private final WeakReference<Activity> activity;
    private List<RxPermissionEmitter> permissionEmitterList = new ArrayList<>();

    public RxPermissionHandlerHelper(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void requestPermission(RxPermissionEmitter emitter) {
        @Nullable Activity activity = this.activity.get();
        if (activity == null) {
            // activity is null just emmit failure
            emitter.emitPermission(null, null);
            return;
        }
        RxPermissionRequest request = emitter.getPermissionRequest();
        // filter all deniedPermission
        List<String> filterPermissionList = new ArrayList<>();
        List<String> allPermissionList = new ArrayList<>();
        allPermissionList.addAll(request.getForcePermissionList());
        allPermissionList.addAll(request.getExtraPermissionList());
        for (String permission :  allPermissionList) {
            if (!RxPermissionUtil.shouldShowRationaleUI(activity, permission) &&
                    !RxPermissionUtil.hasPermission(activity, permission)) {
                filterPermissionList.add(permission);
            }
        }
        if (filterPermissionList.isEmpty()) {
            // no request permission, just emmit null
            emitter.emitPermission(null, null);
            return;
        }
        permissionEmitterList.add(emitter);
        ActivityCompat.requestPermissions(activity, filterPermissionList.toArray(new String[filterPermissionList.size()])
                , request.getRequestCode());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (RxPermissionEmitter emitter : permissionEmitterList) {
            if (requestCode == emitter.getRequestCode()) {
                // get result
                permissionEmitterList.remove(emitter);
                emitter.emitPermission(permissions, grantResults);
            }
        }
    }
}
