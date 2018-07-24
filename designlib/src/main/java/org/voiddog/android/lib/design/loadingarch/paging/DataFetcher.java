package org.voiddog.android.lib.design.loadingarch.paging;

import android.support.annotation.NonNull;

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
 * @author qigengxin
 * @since 2018-04-27 20:09
 */
public abstract class DataFetcher<T> {

    public interface Callback<T> {

        /**
         * next data
         * @param data
         */
        void onSuccess(T data);

        /**
         * error data
         * @param t
         */
        void onError(Throwable t);
    }

    private boolean isCancel = false;

    public void cancel() {
        isCancel = true;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public abstract void fetchData(@NonNull Callback<T> callback);
}
