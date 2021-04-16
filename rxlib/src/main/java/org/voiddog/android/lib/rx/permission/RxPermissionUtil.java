package org.voiddog.android.lib.rx.permission;

import android.app.Activity;
import androidx.lifecycle.Lifecycle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
 * 权限适配帮助
 *
 * @author qigengxin
 * @since 2018-07-25 10:59
 */
public class RxPermissionUtil {

    private RxPermissionUtil() {
        throw new RuntimeException("Stub!");
    }

    static boolean shouldShowRationaleUI(@NonNull Activity activity, @Nullable String permission) {
        return !TextUtils.isEmpty(permission) && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    static boolean shouldShowRationaleUI(@NonNull Activity activity, @Nullable List<String> permissionList) {
        if (permissionList == null) {
            return false;
        }
        for (String permission : permissionList) {
            if (shouldShowRationaleUI(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    static boolean hasPermission(@NonNull Context context, @Nullable String permission) {
        if (TextUtils.isEmpty(permission)) {
            return true;
        }
        // if below then 23, just return true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    static boolean hasPermission(@NonNull Context context, @Nullable List<String> permissionList) {
        if (permissionList == null) {
            return true;
        }
        for (String permission : permissionList) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    //===============================共有方法==================================

    public static Observable<RxPermissionResult> checkPermission(@NonNull final FragmentActivity activity
            , @NonNull final IRxPermissionHandler sender, @NonNull RxPermissionRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Observable<RxPermissionRequest> observable = Observable.just(request);
            if (Looper.myLooper() != Looper.getMainLooper()) {
                observable = observable.subscribeOn(AndroidSchedulers.mainThread());
            }
            return observable.map(new Function<RxPermissionRequest, RxPermissionEmitter>() {
                @Override
                public RxPermissionEmitter apply(RxPermissionRequest request) throws Exception {
                    RxPermissionEmitter emitter = new RxPermissionEmitter(request);
                    List<String> allPermission = new ArrayList<>();
                    allPermission.addAll(request.getForcePermissionList());
                    allPermission.addAll(request.getExtraPermissionList());
                    if (hasPermission(activity, allPermission)) {
                        // 直接给予结果
                        RxPermissionResult result = new RxPermissionResult();
                        result.setSuccess(true);
                        result.getGrantedPermissionList().addAll(allPermission);
                        emitter.emitPermission(result);
                    } else if (!activity.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        // 如果不是 created 状态，直接给予结果
                        RxPermissionResult result = new RxPermissionResult();
                        result.setSuccess(true);
                        // force permission
                        for (String permission : request.getForcePermissionList()) {
                            if (hasPermission(activity, permission)) {
                                result.getGrantedPermissionList().add(permission);
                            } else {
                                result.getDeniedPermissionList().add(permission);
                                result.setSuccess(false);
                            }
                        }
                        // extra permission
                        for (String permission : request.getExtraPermissionList()) {
                            if (hasPermission(activity, permission)) {
                                result.getGrantedPermissionList().add(permission);
                            } else {
                                result.getDeniedPermissionList().add(permission);
                            }
                        }
                        emitter.emitPermission(result);
                    } else {
                        // request permission
                        sender.requestPermission(emitter);
                    }
                    return emitter;
                }
            }).observeOn(Schedulers.newThread())
                    .doOnNext(new Consumer<RxPermissionEmitter>() {
                        @Override
                        public void accept(RxPermissionEmitter rxPermissionEmitter) throws Exception {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                        }
                    })
                    .map(new Function<RxPermissionEmitter, RxPermissionResult>() {
                        @Override
                        public RxPermissionResult apply(RxPermissionEmitter rxPermissionEmitter) throws Exception {
                            RxPermissionResult result = rxPermissionEmitter.request();
                            if (!result.isSuccess()) {
                                throw new RxPermissionException(result);
                            }
                            return result;
                        }
                    });
        } else {
            RxPermissionResult result = new RxPermissionResult();
            result.setSuccess(true);
            result.getGrantedPermissionList().addAll(request.getForcePermissionList());
            result.getGrantedPermissionList().addAll(request.getExtraPermissionList());
            return Observable.just(result);
        }
    }
}
