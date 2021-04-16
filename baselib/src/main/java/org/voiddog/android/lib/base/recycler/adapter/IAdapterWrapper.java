package org.voiddog.android.lib.base.recycler.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
 * 适配器 wrapper
 *
 * @author qigengxin
 * @since 2018-07-24 11:08
 */
public interface IAdapterWrapper {

    RecyclerView.Adapter getInnerAdapter();

    void setWrapper(IAdapterWrapper wrapper);

    IAdapterWrapper getWrapper();

    long getItemId(int position);

    void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder);

    void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder);

    void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView);

    void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView);

    void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer);

    void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer);

    /**
     * 外部position转为内部position
     * @param wrapperPosition
     * @return
     */
    int wrapperPosition2InnerPosition(int wrapperPosition);

    /**
     * 内部adapter的position转换为外部的position
     * @param innerPosition
     * @return
     */
    int innerPosition2WrapperPosition(int innerPosition);

    /**
     * positon是否落在wrapper内
     * @param position
     * @return
     */
    boolean isPositionInWrapper(int position);

    /**
     * wrapper的position转换到根Adapter的位置
     * @param position
     * @return
     */
    int wrapperPosition2TopAdapterPosition(int position);
}
