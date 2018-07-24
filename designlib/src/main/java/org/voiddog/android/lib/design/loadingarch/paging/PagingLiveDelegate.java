package org.voiddog.android.lib.design.loadingarch.paging;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.voiddog.android.lib.base.widget.LazyField;

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
 * @since 2018-04-27 19:19
 */
public class PagingLiveDelegate<R, L> {

    private MutableLiveData<PagingPageState> pageStateLiveData;

    private LazyField<StateLiveData<R>> refreshStateLiveData = new LazyField<StateLiveData<R>>() {
        @NonNull
        @Override
        protected StateLiveData<R> createNew() {
            return new StateLiveData<>();
        }
    };

    private LazyField<StateLiveData<L>> loadMoreStateLiveData = new LazyField<StateLiveData<L>>() {
        @NonNull
        @Override
        protected StateLiveData<L> createNew() {
            return new StateLiveData<>();
        }
    };

    private LiveData<R> refreshLiveData;

    private LiveData<L> loadMoreLiveData;

    @Nullable
    public R getRefreshData() {
        StateData<R> retStateData = refreshStateLiveData.getOrCreate().getValue();
        return retStateData == null ? null : retStateData.data;
    }

    private StateData<R> emptyRefreshStateData;
    @NonNull
    public StateData<R> getRefreshStateData() {
        StateData<R> ret = refreshStateLiveData.getOrCreate().getValue();
        if (ret != null) {
            return ret;
        }
        if (emptyRefreshStateData == null) {
            emptyRefreshStateData = new StateData<>(0, null, null);
        }
        return emptyRefreshStateData;
    }

    @Nullable
    public L getLoadMoreData() {
        StateData<L> retStateData = loadMoreStateLiveData.getOrCreate().getValue();
        return retStateData == null ? null : retStateData.data;
    }

    private StateData<L> emptyLoadMoreStateData;
    @NonNull
    public StateData<L> getLoadMoreStateData() {
        StateData<L> ret = loadMoreStateLiveData.getOrCreate().getValue();
        if (ret != null) {
            return ret;
        }
        if (emptyLoadMoreStateData == null) {
            emptyLoadMoreStateData = new StateData<>(0, null, null);
        }
        return emptyLoadMoreStateData;
    }

    @Nullable
    public PagingPageState getPageState() {
        return pageStateLiveData == null ? null : pageStateLiveData.getValue();
    }

    public MutableLiveData<PagingPageState> getPageStateLiveData() {
        pageStateLiveData = pageStateLiveData == null ? new MutableLiveData<PagingPageState>() : pageStateLiveData;
        return pageStateLiveData;
    }

    public LiveData<R> getRefreshLiveData() {
        if (refreshLiveData == null) {
            final MediatorLiveData<R> liveData = new MediatorLiveData<>();
            liveData.addSource(getRefreshStateLiveData(), new Observer<StateData<R>>() {
                @Override
                public void onChanged(@Nullable StateData<R> rStateData) {
                    if (rStateData != null && rStateData.hasNewData()) {
                        liveData.setValue(rStateData.data);
                    }
                }
            });
            refreshLiveData = liveData;
        }
        return refreshLiveData;
    }

    public LiveData<L> getLoadMoreLiveData() {
        if (loadMoreLiveData == null) {
            final MediatorLiveData<L> liveData = new MediatorLiveData<>();
            liveData.addSource(getLoadMoreStateLiveData(), new Observer<StateData<L>>() {
                @Override
                public void onChanged(@Nullable StateData<L> lStateData) {
                    if (lStateData != null && lStateData.hasNewData()) {
                        liveData.setValue(lStateData.data);
                    }
                }
            });
            loadMoreLiveData = liveData;
        }
        return loadMoreLiveData;
    }

    public StateLiveData<R> getRefreshStateLiveData() {
        return refreshStateLiveData.getOrCreate();
    }

    public StateLiveData<L> getLoadMoreStateLiveData() {
        return loadMoreStateLiveData.getOrCreate();
    }
}
