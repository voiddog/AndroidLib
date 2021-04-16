package org.voiddog.android.lib.base.recycler.adapter;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
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
 * 阿里巴巴那个控件写的好傻逼，我这里修复它那个傻逼逻辑
 *
 * @author qigengxin
 * @since 2017-12-13 12:38
 */

public abstract class RecyclablePagerAdapter<VH extends RecyclerView.ViewHolder> extends PagerAdapter {

    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private RecyclerView.Adapter<VH> innerAdapter;

    public RecyclablePagerAdapter() {
        this(new RecyclerView.RecycledViewPool());
    }

    public RecyclablePagerAdapter(RecyclerView.RecycledViewPool pool) {
        this.mRecycledViewPool = pool;
        innerAdapter = new RecyclerView.Adapter<VH>() {
            @Override
            public VH onCreateViewHolder(ViewGroup parent, int viewType) {
                return RecyclablePagerAdapter.this.createViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(VH holder, int position) {
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
    }

    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public abstract int getCount();

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o instanceof RecyclerView.ViewHolder && (((RecyclerView.ViewHolder) o).itemView == view);
    }

    /**
     * Get view from position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int itemViewType = getItemViewType(position);
        RecyclerView.ViewHolder holder = mRecycledViewPool.getRecycledView(itemViewType);

        if (holder == null) {
            holder = innerAdapter.createViewHolder(container, itemViewType);
        }

        onBindViewHolder((VH) holder, position);
        //itemViews' layoutParam will be reused when there are more than one nested ViewPager in one page,
        //so the attributes of layoutParam such as widthFactor and position will also be reused,
        //while these attributes should be reset to default value during reused.
        //Considering ViewPager.LayoutParams has a few inner attributes which could not be modify outside, we provide a new instance here
        container.addView(holder.itemView, new ViewPager.LayoutParams());

        return holder;
    }

    protected abstract void onBindViewHolder(VH holder, int position);

    protected abstract VH createViewHolder(ViewGroup container, int itemViewType);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof RecyclerView.ViewHolder) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) object;
            container.removeView(holder.itemView);
            mRecycledViewPool.putRecycledView(holder);
        }
    }
}
