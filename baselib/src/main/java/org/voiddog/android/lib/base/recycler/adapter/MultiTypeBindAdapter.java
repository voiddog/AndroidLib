package org.voiddog.android.lib.base.recycler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder;

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
 * 绑定多类型的 adapter
 *
 * @author qigengxin
 * @since 2018-07-24 11:35
 */
public abstract class MultiTypeBindAdapter <T extends MultiTypeBindAdapter.ViewTypeItem>
        extends RecyclerView.Adapter<BindViewHolder<? super T>> {
    /**
     * 有具体类型的 item
     */
    public interface ViewTypeItem {
        /**
         * 或趋视图类型
         * @return
         */
        int getViewType();
    }

    public interface ItemProvider<V extends ViewTypeItem>{
        /**
         * 创建 ViewHolder
         * @param parent
         * @return
         */
        BindViewHolder<? super V> onCreateViewHolder(@NonNull ViewGroup parent);
    }


    /**
     * 记录 itemType 到 ViewHolder Provider 的稀疏数组
     */
    private SparseArray<ItemProvider<T>> itemProviderSparseArray = new SparseArray<>();

    /**
     * 注册 item 提供器
     * @param viewType
     * @param itemProvider
     */
    public void registerItemProvider(int viewType, @NonNull ItemProvider<T> itemProvider){
        itemProviderSparseArray.put(viewType, itemProvider);
    }

    /**
     * 清除注册的 item 提供器
     */
    public void clearItemProvider(){
        itemProviderSparseArray.clear();
    }

    /**
     * 注销 item 提供器
     * @param viewType
     */
    public void unRegisterItemProvider(int viewType){
        itemProviderSparseArray.remove(viewType);
    }

    @NonNull
    public abstract T getItemAtIndex(int position);

    @Override
    public int getItemViewType(int position) {
        return getItemAtIndex(position).getViewType();
    }

    @Override
    @NonNull
    public BindViewHolder<? super T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindViewHolder<? super T> res = itemProviderSparseArray.get(viewType).onCreateViewHolder(parent);
        if(res == null){
            throw new IllegalArgumentException("the create view holder return null");
        }
        return res;
    }

    @Override
    public void onBindViewHolder(@NonNull BindViewHolder<? super T> holder, int position) {
        holder.itemView.setAlpha(1.0f);
        holder.bind(getItemAtIndex(position));
    }
}
