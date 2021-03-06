package org.voiddog.android.lib.base.recycler.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
public abstract class MultiTypeBindAdapter <T>
        extends RecyclerView.Adapter<BindViewHolder<? super T>> {

    private static final int DEFAULT_ITEM_TYPE = 0;

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

    public interface ItemProvider<V>{
        /**
         * 创建 ViewHolder
         * @param parent
         * @return
         */
        BindViewHolder onCreateViewHolder(@NonNull ViewGroup parent);
    }


    /**
     * 记录 itemType 到 ViewHolder Provider 的稀疏数组
     */
    private SparseArray<ItemProvider> itemProviderSparseArray = new SparseArray<>();

    /**
     * 注册 item 默认提供器 {@link #DEFAULT_ITEM_TYPE}
     * @param itemProvider
     */
    public void registerItemProvider(@NonNull ItemProvider<? extends T> itemProvider) {
        registerItemProvider(DEFAULT_ITEM_TYPE, itemProvider);
    }

    /**
     * 注册 item 提供器
     * @param viewType
     * @param itemProvider
     */
    public void registerItemProvider(int viewType, @NonNull ItemProvider<? extends T> itemProvider){
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
        T ret = getItemAtIndex(position);
        if (ret instanceof ViewTypeItem) {
            return ((ViewTypeItem) ret).getViewType();
        }
        return DEFAULT_ITEM_TYPE;
    }

    @Override
    @NonNull
    public BindViewHolder<? super T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindViewHolder<? super T> res = ((ItemProvider<T>)(itemProviderSparseArray.get(viewType)))
                .onCreateViewHolder(parent);
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
