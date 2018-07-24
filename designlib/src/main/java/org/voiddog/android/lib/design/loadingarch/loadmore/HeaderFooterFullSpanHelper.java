package org.voiddog.android.lib.design.loadingarch.loadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import org.voiddog.android.lib.base.recycler.adapter.IAdapterWrapper;

/**
 * RecycleView 头部 底部 横跨全部
 *
 * @author qigengxin
 * @since 2016-10-18 10:40
 */


public class HeaderFooterFullSpanHelper {

    private SpanSizeLookup spanSizeLookup;
    private IFullSpanPosition fullSpanPosition;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    public HeaderFooterFullSpanHelper(IFullSpanPosition fullSpanPosition, RecyclerView.Adapter adapter){
        this.fullSpanPosition = fullSpanPosition;
        this.adapter = adapter;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            spanSizeLookup = new SpanSizeLookup(gridLayoutManager, gridLayoutManager.getSpanSizeLookup());
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        }
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager && spanSizeLookup != null){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup.oldSpanSizeLookup);
            spanSizeLookup = null;
        }
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(recyclerView != null){
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            while(adapter != this.adapter){
                if(adapter instanceof IAdapterWrapper){
                    position = ((IAdapterWrapper) adapter).wrapperPosition2InnerPosition(position);
                    adapter = ((IAdapterWrapper) adapter).getInnerAdapter();
                } else {
                    break;
                }
            }
        }
        if(fullSpanPosition.isFullSpanPosition(position)){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if(lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public interface IFullSpanPosition{
        boolean isFullSpanPosition(int position);
    }

    private class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

        GridLayoutManager gridLayoutManager;
        GridLayoutManager.SpanSizeLookup oldSpanSizeLookup;

        public SpanSizeLookup(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup oldSpanSizeLookup){
            this.gridLayoutManager = gridLayoutManager;
            this.oldSpanSizeLookup = oldSpanSizeLookup;
        }

        @Override
        public int getSpanSize(int position) {
            if(fullSpanPosition.isFullSpanPosition(position) && gridLayoutManager != null){
                return gridLayoutManager.getSpanCount();
            }
            if(oldSpanSizeLookup != null){
                return oldSpanSizeLookup.getSpanSize(position);
            }
            return 1;
        }
    }
}
