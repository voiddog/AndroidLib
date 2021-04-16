package org.voiddog.android.lib.base.recycler.viewholder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * 存储数据的 view holder
 *
 * @author qigengxin
 * @since 2018-07-24 11:02
 */
public abstract class BindViewHolder<T> extends RecyclerView.ViewHolder {

    @Nullable
    protected T data;

    /**
     * 普通构造函数
     * @param itemView 当前 view holder 的 item view
     */
    public BindViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 从 layout id 中 inflate item view
     * @param parent 当前 viewHolder 的父布局
     * @param layoutId 需要 inflate 的 layout id
     */
    public BindViewHolder(@NonNull ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Nullable
    public T getData(){
        return data;
    }

    public void bind(@NonNull T data){
        this.data = data;
        onBindData(data);
    }

    public abstract void onBindData(@NonNull T data);
}
