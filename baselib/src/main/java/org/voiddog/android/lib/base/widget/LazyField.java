package org.voiddog.android.lib.base.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 * 懒加载属性
 *
 * @author qigengxin
 * @since 2018-07-24 19:18
 */

public abstract class LazyField<T> {

    private T value;

    @Nullable
    public T get() {
        return value;
    }

    @NonNull
    public T getOrCreate() {
        if (value == null) {
            value = createNew();
        }
        return value;
    }

    @NonNull
    protected abstract T createNew();
}
