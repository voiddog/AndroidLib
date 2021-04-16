package org.voiddog.android.lib.design.loadingarch.paging;

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
 * @since 2017-09-01 13:40
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
public class StateData<T> {
    public final static int FLAG_SUCCESS = 1;
    public final static int FLAG_LOADING = 2;
    public final static int FLAG_ERROR = 4;
    public final static int FLAG_NEW_DATA = 8;

    public final int stateFlag;

    @Nullable
    public final Throwable error;

    @Nullable
    public final T data;

    public StateData(@NonNull int flag, @Nullable T data, @Nullable Throwable error) {
        this.stateFlag = flag;
        this.data = data;
        this.error = error;
    }

    public int getLoadingFlag() {
        return stateFlag&7;
    }

    public boolean hasNewData() {
        return hasFlag(FLAG_NEW_DATA);
    }

    public boolean isLoading() {
        return hasFlag(FLAG_LOADING);
    }

    public boolean isSuccess() {
        return hasFlag(FLAG_SUCCESS);
    }

    public boolean isError() {
        return hasFlag(FLAG_ERROR);
    }

    public boolean hasFlag(int flag) {
        return (stateFlag&flag) == flag;
    }

    public static <T> StateData<T> success(@Nullable T data) {
        return new StateData<>(FLAG_SUCCESS, data, null);
    }

    public static <T> StateData<T> newSuccess(@Nullable T data) {
        return new StateData<>(FLAG_SUCCESS|FLAG_NEW_DATA, data, null);
    }

    public static <T> StateData<T> error(@Nullable T data, Throwable error) {
        return new StateData<>(FLAG_ERROR, data, error);
    }

    public static <T> StateData<T> newError(@Nullable T data, Throwable error) {
        return new StateData<>(FLAG_ERROR|FLAG_NEW_DATA, data, error);
    }

    public static <T> StateData<T> loading(@Nullable T data) {
        return new StateData<>(FLAG_LOADING, data, null);
    }

    public static <T> StateData<T> newLoading(@Nullable T data) {
        return new StateData<>(FLAG_LOADING|FLAG_NEW_DATA, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StateData<?> stateData = (StateData<?>) o;

        if (stateFlag != stateData.stateFlag) {
            return false;
        }
        if (error != null ? !error.equals(stateData.error) : stateData.error != null) {
            return false;
        }
        return data != null ? data.equals(stateData.data) : stateData.data == null;
    }

    @Override
    public int hashCode() {
        int result = stateFlag;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StateData{" +
                "status=" + stateFlag +
                ", message='" + (error == null ? null : error.getMessage()) + '\'' +
                ", data=" + data +
                '}';
    }
}