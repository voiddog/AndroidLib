package org.voiddog.android.lib.rx.permission;

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
 * Rx权限请求
 *
 * @author qigengxin
 * @since 2018-07-25 11:34
 */
public class RxPermissionRequest {
    private static int PERMISSION_REQUEST_CODE = 37;

    public static synchronized int obtainNewRequestCode(){
        return PERMISSION_REQUEST_CODE++;
    }

    public RxPermissionRequest() {
        this.requestCode = obtainNewRequestCode();
    }

    public static RxPermissionRequest newRequest(){
        return new RxPermissionRequest();
    }

    public RxPermissionRequest addExtraPermission(String extraPermission){
        extraPermissionList.add(extraPermission);
        return this;
    }

    /**
     * {@link android.Manifest.permission}
     * @param forcePermission
     */
    public RxPermissionRequest addForcePermission(String forcePermission){
        forcePermissionList.add(forcePermission);
        return this;
    }

    public List<String> getForcePermissionList() {
        return forcePermissionList;
    }

    public List<String> getExtraPermissionList() {
        return extraPermissionList;
    }

    public int getRequestCode() {
        return requestCode;
    }

    // 必须需要授予的权限
    private List<String> forcePermissionList = new ArrayList<>();
    // 可以不授予的权限
    private List<String> extraPermissionList = new ArrayList<>();
    private int requestCode;
}
