package org.voiddog.android.lib.design.loadingarch.loadmore;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.android.lib.base.recycler.adapter.BaseAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * 显示在Footer的上拉加载更多 Wrapper
 *
 * @author qigengxin
 * @since 2016-10-18 09:58
 */

public class LoadMoreWrapper extends BaseAdapterWrapper<RecyclerView.ViewHolder> implements ILoadMoreSender{

    /**
     * 底部加载更多的 ViewHolder，实现类需要重写对应的方法
     */
    public static abstract class LoadMoreViewHolder extends RecyclerView.ViewHolder{
        public static class State {
            public static final int NORMAL = 1;
            public static final int LOADING = 2;
            public static final int COMPLETE = 3;
            public static final int ERROR = 4;

            public static final State normal = new State(NORMAL, null);
            public static final State loading = new State(LOADING, null);
            public static final State complete = new State(COMPLETE, null);

            public final int status;
            @Nullable
            public final CharSequence content;

            public State(int status, @Nullable CharSequence content) {
                this.status = status;
                this.content = content;
            }
        }

        /**
         * 当前的状态
         */
        @NonNull
        private State state = State.normal;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 设置状态, 根据状态改变显示方式
         * 子类改变视图方式请在 {@link #updateState(State, State)} 中实现
         * @param state
         */
        public void setState(@NonNull State state){
            State oldState = this.state;
            this.state = state;
            if(oldState != state) {
                updateState(oldState, state);
            }
        }

        @NonNull
        public State getState(){
            return state;
        }

        /**
         * state为错误后 点击错误的回调
         * @param clickListener
         */
        public abstract void setOnErrorViewClickListener(View.OnClickListener clickListener);

        /**
         * 根据状态更新视图
         * @param oldState
         * @param newState
         */
        abstract protected void updateState(@NonNull State oldState, @NonNull State newState);
    }

    public interface CreateLoadMoreViewHolderListener{
        /**
         * load more view holder 的 creator
         * @param parent
         * @return
         */
        LoadMoreViewHolder createLoadMoreViewHolder(@NonNull ViewGroup parent);

        /**
         * 当前 view holder 的 item type
         * @return
         */
        int getItemType();
    }

    protected LoadMoreViewHolder loadMoreViewHolder;
    private RecyclerView recyclerView;
    private HeaderFooterFullSpanHelper spanHelper;
    private LoadMoreViewHolder.State state = LoadMoreViewHolder.State.normal;
    private CreateLoadMoreViewHolderListener createLoadMoreListener;
    private int itemType;
    private List<ILoadMoreListener> loadMoreListenerList = new ArrayList<>();
    private List<View.OnClickListener> errorClickListenerList = new ArrayList<>();

    protected boolean enableLoadMore = true;

    public LoadMoreWrapper(RecyclerView.Adapter adapter, CreateLoadMoreViewHolderListener createLoadMoreListener){
        super(adapter);
        this.createLoadMoreListener = createLoadMoreListener;
        itemType = createLoadMoreListener.getItemType();
        this.spanHelper = new HeaderFooterFullSpanHelper(new HeaderFooterFullSpanHelper.IFullSpanPosition() {
            @Override
            public boolean isFullSpanPosition(int position) {
                return getItemViewType(position) == itemType;
            }
        }, this);
    }

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
        notifyDataSetChanged();
    }

    public void addOnErrorClickListener(View.OnClickListener clickListener) {
        errorClickListenerList.add(clickListener);
    }

    public void removeOnErrorClickListener(View.OnClickListener clickListener) {
        errorClickListenerList.remove(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        if(enableLoadMore && position == getItemCount() - 1){
            return itemType;
        }
        return getInnerAdapter().getItemViewType(wrapperPosition2InnerPosition(position));
    }

    public void setState(LoadMoreViewHolder.State state) {
        this.state = state;
        if(loadMoreViewHolder != null){
            loadMoreViewHolder.setState(state);
        }
    }

    public LoadMoreViewHolder.State getState(){
        return state;
    }

    @Override
    public void addLoadMoreListener(@NonNull ILoadMoreListener listener) {
        loadMoreListenerList.add(listener);
    }

    @Override
    public void removeLoadMoreListener(@NonNull ILoadMoreListener listener) {
        loadMoreListenerList.remove(listener);
    }

    @Override
    public void setLoading(boolean loading) {
        setState(loading ? LoadMoreViewHolder.State.loading : LoadMoreViewHolder.State.normal);
    }

    @Override
    public void setComplete() {
        setState(LoadMoreViewHolder.State.complete);
   }

    @Override
    public void setError(@NonNull Throwable error) {
        setState(new LoadMoreViewHolder.State(LoadMoreViewHolder.State.ERROR, error.getMessage()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        spanHelper.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(scrollListener);
        this.recyclerView = recyclerView;
    }

    @Override
    public int wrapperPosition2InnerPosition(int wrapperPosition) {
        return wrapperPosition;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        spanHelper.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(scrollListener);
        this.recyclerView = null;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        spanHelper.onViewAttachedToWindow(holder);
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int innerPosition2WrapperPosition(int innerPosition) {
        return innerPosition;
    }

    @Override
    public boolean isPositionInWrapper(int position) {
        return position == getItemCount() - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == itemType){
            loadMoreViewHolder = createLoadMoreListener.createLoadMoreViewHolder(parent);
            loadMoreViewHolder.setState(state);
            loadMoreViewHolder.setOnErrorViewClickListener(viewHolderErrorClickListener);
            return loadMoreViewHolder;
        }
        return getInnerAdapter().onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindWrapper(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof LoadMoreViewHolder){
            loadMoreViewHolder = (LoadMoreViewHolder) holder;
            loadMoreViewHolder.setState(state);
        }
    }

    @Override
    public int getItemCount() {
        return getInnerAdapter().getItemCount() + (enableLoadMore ? 1 : 0);
    }

    private Rect visibleRect = new Rect();

    protected boolean needLoadMore() {
        if(!enableLoadMore || recyclerView == null || loadMoreViewHolder == null
                || state == null
                || state.status == LoadMoreViewHolder.State.ERROR
                || state.status == LoadMoreViewHolder.State.COMPLETE
                || state.status == LoadMoreViewHolder.State.LOADING ){
            return false;
        }

        if (recyclerView.getWindowVisibility() != VISIBLE){
            return false;
        }

        View view = recyclerView.getLayoutManager().findViewByPosition(wrapperPosition2TopAdapterPosition(getItemCount() - 1));

        if(view != null && view.getGlobalVisibleRect(visibleRect)){
            return true;
        }

        return false;
    }

    private void dispatchLoadMore() {
        for (ILoadMoreListener listener : loadMoreListenerList) {
            listener.onLoadMore(this);
        }
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (needLoadMore()) {
                setLoading(true);
                dispatchLoadMore();
            }
        }
    };

    private View.OnClickListener viewHolderErrorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (View.OnClickListener clickListener : errorClickListenerList) {
                clickListener.onClick(v);
            }
        }
    };
}
