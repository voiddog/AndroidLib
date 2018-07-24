package org.voiddog.android.lib.base.recycler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

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
 * 基本的适配器 wrapper
 *
 * @author qigengxin
 * @since 2018-07-24 11:09
 */
public abstract class BaseAdapterWrapper<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements IAdapterWrapper{

    private RecyclerView.Adapter innerAdapter;
    private IAdapterWrapper wrapper;

    public BaseAdapterWrapper(RecyclerView.Adapter adapter){
        this.innerAdapter = adapter;
        if(innerAdapter instanceof IAdapterWrapper){
            ((IAdapterWrapper) innerAdapter).setWrapper(this);
        }
        innerAdapter.registerAdapterDataObserver(innerObserver);
    }

    @Override
    public void setWrapper(IAdapterWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public IAdapterWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public RecyclerView.Adapter getInnerAdapter() {
        return innerAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (isPositionInWrapper(position)) {
            onBindWrapper((VH) holder, position);
        } else {
            innerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (isPositionInWrapper(position)) {
            onBindWrapper((VH) holder, position, payloads);
        } else {
            innerAdapter.onBindViewHolder(holder, position, payloads);
        }
    }


    public abstract void onBindWrapper(@NonNull VH holder, int position);

    public void onBindWrapper(@NonNull VH holder, int position, @NonNull List payloads) {
        onBindWrapper(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        innerAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        innerAdapter.onDetachedFromRecyclerView(recyclerView);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if(!isPositionInWrapper(position)){
            innerAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if(!isPositionInWrapper(position)){
            innerAdapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public long getItemId(int position) {
        if(isPositionInWrapper(position)){
            return super.getItemId(position);
        }
        return innerAdapter.getItemId(position);
    }

    @Override
    public int wrapperPosition2TopAdapterPosition(int position) {
        IAdapterWrapper wrapper = getWrapper();
        while(wrapper != null){
            position = wrapper.innerPosition2WrapperPosition(position);
            wrapper = wrapper.getWrapper();
        }
        return position;
    }

    private RecyclerView.AdapterDataObserver innerObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(innerPosition2WrapperPosition(positionStart), itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            notifyItemRangeChanged(innerPosition2WrapperPosition(positionStart), itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(innerPosition2WrapperPosition(positionStart), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(innerPosition2WrapperPosition(positionStart), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(innerPosition2WrapperPosition(fromPosition), innerPosition2WrapperPosition(toPosition));
        }
    };
}
