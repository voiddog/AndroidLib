package org.voiddog.android.lib.design.loadingarch.paging;

import androidx.annotation.NonNull;

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
 * @since 2018-02-02 14:11
 */

public class PagingPageState {

    public static PagingPageState error() {
        return new PagingPageState(State.ERROR, null, null);
    }

    public static PagingPageState loading() {
        return new PagingPageState(State.LOADING, null, null);
    }

    public static PagingPageState empty() {
        return new PagingPageState(State.EMPTY, null, null);
    }

    public static PagingPageState error(Throwable error) {
        return new PagingPageState(State.ERROR, null, error);
    }

    public static PagingPageState loading(CharSequence message) {
        return new PagingPageState(State.LOADING, message, null);
    }

    public static PagingPageState empty(CharSequence message) {
        return new PagingPageState(State.EMPTY, message, null);
    }

    public static PagingPageState normal() {
        return new PagingPageState(State.NORMAL, null, null);
    }

    public enum State {
        NORMAL,
        LOADING,
        EMPTY,
        ERROR
    }

    @NonNull
    public final State state;
    public final CharSequence message;
    public final Throwable error;

    public PagingPageState(@NonNull State state, CharSequence message, Throwable error) {
        this.state = state;
        this.message = message;
        this.error = error;
    }
}
