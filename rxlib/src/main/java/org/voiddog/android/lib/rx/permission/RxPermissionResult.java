package org.voiddog.android.lib.rx.permission;

import androidx.annotation.NonNull;

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
 * Rx权限处理请求回调
 *
 * @author qigengxin
 * @since 2018-07-25 11:24
 */
public class RxPermissionResult {

    private boolean success;
    @NonNull
    private List<String> grantedPermissionList = new ArrayList<>();
    @NonNull
    private List<String> deniedPermissionList = new ArrayList<>();

    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 后去请求成功地权限
     * @return
     */
    @NonNull
    public List<String> getGrantedPermissionList() {
        return grantedPermissionList;
    }

    public void setGrantedPermissionList(@NonNull List<String> grantedPermissionList) {
        this.grantedPermissionList = grantedPermissionList;
    }

    /**
     * 获取被拒绝的权限
     * @return
     */
    @NonNull
    public List<String> getDeniedPermissionList() {
        return deniedPermissionList;
    }

    public void setDeniedPermissionList(@NonNull List<String> deniedPermissionList) {
        this.deniedPermissionList = deniedPermissionList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("generatePermissions:").append(grantedPermissionList).append('\n')
                .append("deniedPermissions:").append(deniedPermissionList).append('\n');
        return sb.toString();
    }
}
