package org.voiddog.android.lib.design.loadingarch.paging;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.view.View;

import org.voiddog.android.lib.design.loadingarch.loadmore.ILoadMoreListener;
import org.voiddog.android.lib.design.loadingarch.loadmore.ILoadMoreSender;
import org.voiddog.android.lib.design.loadingarch.refresh.IRefreshListener;
import org.voiddog.android.lib.design.loadingarch.refresh.IRefreshSender;

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
 * 分页框架
 *
 * @author qigengxin
 * @since 2018-04-27 17:28
 */
public class PagingArch<R, L> {

    /**
     * 设置内容视图，必选项
     * @param contentView
     * @return
     */
    public PagingArch<R, L> setContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    /**
     * paging 数据逻辑流，必选项
     * @param callback
     * @return
     */
    public PagingArch<R, L> setCallback(IPagingCallback<R, L> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置页面状态 live data 代理，必选项
     * @param pagingLiveDelegate
     * @return
     */
    public PagingArch<R, L> setPagingLiveDelegate(PagingLiveDelegate<R, L> pagingLiveDelegate) {
        this.pagingLiveDelegate = pagingLiveDelegate;
        return this;
    }

    /**
     * 设置 loading 层视图，非必须
     * @param loadingView
     * @return
     */
    public PagingArch<R, L> setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    /**
     * 设定错误层视图，非必须
     * @param errorView
     * @return
     */
    public PagingArch<R, L> setErrorView(View errorView) {
        this.errorView = errorView;
        return this;
    }

    /**
     * 设置没有内动的视图，非必须
     * @param emptyView
     * @return
     */
    public PagingArch<R, L> setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    /**
     * 配置刷新事件发送者，非必须
     * @param refreshSender
     * @return
     */
    public PagingArch<R, L> setRefreshSender(IRefreshSender refreshSender) {
        this.refreshSender = refreshSender;
        return this;
    }

    /**
     * 配置加载更多发送者，非必须
     * @param loadMoreSender
     * @return
     */
    public PagingArch<R, L> setLoadMoreSender(ILoadMoreSender loadMoreSender) {
        this.loadMoreSender = loadMoreSender;
        return this;
    }

    /**
     * 页面的切换动画，非必须
     * @param switchAnim
     * @return
     */
    public PagingArch<R, L> setSwitchAnim(IPageSwitchAnim switchAnim) {
        this.switchAnim = switchAnim;
        return this;
    }

    public View getContentView() {
        return contentView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public PagingLiveDelegate<R, L> getPagingLiveDelegate() {
        return pagingLiveDelegate;
    }

    public IRefreshSender getRefreshSender() {
        return refreshSender;
    }

    public ILoadMoreSender getLoadMoreSender() {
        return loadMoreSender;
    }

    public IPagingCallback<R, L> getCallback() {
        return callback;
    }

    public IPageSwitchAnim getSwitchAnim() {
        return switchAnim;
    }

    public PagingPageState.State getPagingState() {
        if (loadingView != null && loadingView.isShown() && loadingView.getAlpha() > 0) {
            return PagingPageState.State.LOADING;
        }
        return PagingPageState.State.NORMAL;
    }

    /**
     * 取消加载和重置状态，用于状态异常的时候使用
     * @param resetState 是否重置 state live data 的状态
     */
    public void cancelLoading(boolean resetState) {
        if(lastRefreshFetcher != null && !lastRefreshFetcher.isCancel()) {
            lastRefreshFetcher.cancel();
            lastRefreshFetcher = null;
        }
        if (lastLoadMoreFetcher != null && !lastLoadMoreFetcher.isCancel()) {
            lastLoadMoreFetcher.cancel();
            lastLoadMoreFetcher = null;
        }
        if (resetState) {
            StateData stateData = pagingLiveDelegate.getRefreshStateLiveData().getValue();
            if (stateData != null && stateData.isLoading()) {
                pagingLiveDelegate.getRefreshStateLiveData().setValue(null);
            }
            stateData = pagingLiveDelegate.getLoadMoreStateLiveData().getValue();
            if (stateData != null && stateData.isLoading()) {
                pagingLiveDelegate.getLoadMoreStateLiveData().setValue(null);
            }
        }
    }

    /**
     * 安装到 LifecycleOwner
     * @param owner
     */
    public void install(LifecycleOwner owner) {
        checkArgs();
        if (refreshSender != null) {
            refreshSender.addRefreshListener(refreshListener);
        }

        if (loadMoreSender != null) {
            loadMoreSender.addLoadMoreListener(loadMoreListener);
        }

        View currentView = null;
        PagingPageState.State state = pagingLiveDelegate.getPageState() == null ? getPagingState()
                : pagingLiveDelegate.getPageState().state;
        switch (state) {
            case EMPTY:
                currentView = emptyView;
                break;
            case ERROR:
                currentView = errorView;
                break;
            case LOADING:
                currentView = loadingView;
                break;
            case NORMAL:
                currentView = contentView;
                break;
        }
        switchAnim.setupCurrentView(currentView);

        pagingLiveDelegate.getPageStateLiveData().observe(owner, new Observer<PagingPageState>() {
            @Override
            public void onChanged(@Nullable PagingPageState pagingPageState) {
                if (pagingPageState == null) {
                    return;
                }
                View view = null;
                switch (pagingPageState.state) {
                    case EMPTY:
                        view = emptyView;
                        break;
                    case ERROR:
                        view = errorView;
                        break;
                    case LOADING:
                        view = loadingView;
                        break;
                    case NORMAL:
                        view = contentView;
                        break;
                }
                if (view != null) {
                    switchAnim.switchTo(view);
                }
            }
        });

        pagingLiveDelegate.getRefreshStateLiveData().observe(owner, new Observer<StateData<R>>() {
            @Override
            public void onChanged(@Nullable StateData<R> data) {
                if (data == null) {
                    refresh(false);
                    return;
                }
                if (data.isLoading()){
                    return;
                }
                refresh(false);
                switch (data.getLoadingFlag()) {
                    case StateData.FLAG_ERROR:
                        errorLoadMore(data.error);
                        pagingLiveDelegate.getPageStateLiveData().setValue(PagingPageState.error(data.error));
                        pagingLiveDelegate.getLoadMoreStateLiveData().setValue(StateData.error((L)null, data.error));
                        break;
                    case StateData.FLAG_SUCCESS:
                        if (pagingLiveDelegate.getLoadMoreStateData().isLoading()) {
                            pagingLiveDelegate.getLoadMoreStateLiveData().setValue(null);
                        }
                        R r = data.data;
                        pagingLiveDelegate.getPageStateLiveData().setValue(PagingPageState.normal());
                        if (callback.isEmpty(r)) {
                            pagingLiveDelegate.getPageStateLiveData().setValue(PagingPageState.empty());
                            completeLoadMore();
                            return;
                        }
                        if (callback.isRefreshComplete(r)) {
                            completeLoadMore();
                        }
                        break;
                }
            }
        });

        pagingLiveDelegate.getLoadMoreStateLiveData().observe(owner, new Observer<StateData<L>>() {
            @Override
            public void onChanged(@Nullable StateData<L> data) {
                if (data == null) {
                    loading(false);
                    return;
                }
                switch (data.getLoadingFlag()) {
                    case StateData.FLAG_ERROR:
                        errorLoadMore(data.error);
                        break;
                    case StateData.FLAG_SUCCESS:
                        L l = data.data;
                        if (callback.isLoadMoreComplete(l)) {
                            completeLoadMore();
                        } else {
                            loading(false);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 手动刷新
     * @param applySender 是否把刷新状态应用到 sender 上
     */
    public void performRefresh(boolean applySender) {
        if (applySender && refreshSender != null) {
            refreshSender.setRefreshing(true);
        }
        refresh(callback.requestRefresh(this));
    }

    /**
     * 手动加载更多
     * @param applySender 是否把加载状态应用到 sender 上
     */
    public void performLoadMore(boolean applySender) {
        if (applySender && loadMoreSender != null) {
            loadMoreSender.setLoading(true);
        }
        loadMore(callback.requestLoadMore(this));
    }

    protected IRefreshListener refreshListener = new IRefreshListener() {
        @Override
        public void onRefresh(IRefreshSender sender) {
            refresh(callback.requestRefresh(PagingArch.this));
        }
    };

    protected ILoadMoreListener loadMoreListener = new ILoadMoreListener() {
        @Override
        public void onLoadMore(ILoadMoreSender sender) {
            loadMore(callback.requestLoadMore(PagingArch.this));
        }
    };

    protected void loading(boolean loading) {
        if (loadMoreSender != null) {
            loadMoreSender.setLoading(loading);
        }
    }

    protected void refresh(boolean refresh) {
        if (refreshSender != null) {
            refreshSender.setRefreshing(refresh);
        }
    }

    protected void completeLoadMore() {
        if (loadMoreSender != null) {
            loadMoreSender.setComplete();
        }
    }

    protected void errorLoadMore(Throwable error) {
        if (loadMoreSender != null) {
            loadMoreSender.setError(error);
        }
    }

    private DataFetcher<R> lastRefreshFetcher;
    protected void refresh(final DataFetcher<R> dataFetcher) {
        pagingLiveDelegate.getRefreshStateLiveData().setValue(StateData.loading((R)null));
        pagingLiveDelegate.getLoadMoreStateLiveData().setValue(null);
        if (lastRefreshFetcher != null && !lastRefreshFetcher.isCancel()) {
            // 上一次的刷新还没有结束
            return;
        }
        if (lastLoadMoreFetcher != null && !lastLoadMoreFetcher.isCancel()) {
            lastLoadMoreFetcher.cancel();
            lastLoadMoreFetcher = null;
        }
        lastRefreshFetcher = dataFetcher;
        dataFetcher.fetchData(new DataFetcher.Callback<R>() {

            @Override
            public void onSuccess(R data) {
                if (lastRefreshFetcher == dataFetcher) {
                    lastRefreshFetcher = null;
                }
                if (dataFetcher.isCancel()) {
                    return;
                }
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    pagingLiveDelegate.getRefreshStateLiveData().setValue(StateData.newSuccess(data));
                } else {
                    pagingLiveDelegate.getRefreshStateLiveData().postValue(StateData.newSuccess(data));
                }
            }

            @Override
            public void onError(Throwable t) {
                if (lastRefreshFetcher == dataFetcher) {
                    lastRefreshFetcher = null;
                }
                if (dataFetcher.isCancel()) {
                    return;
                }
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    pagingLiveDelegate.getRefreshStateLiveData().setValue(StateData.error((R)null, t));
                } else {
                    pagingLiveDelegate.getRefreshStateLiveData().postValue(StateData.error((R)null, t));
                }
            }
        });
    }

    private DataFetcher<L> lastLoadMoreFetcher;
    protected void loadMore(final DataFetcher<L> dataFetcher) {
        pagingLiveDelegate.getLoadMoreStateLiveData().setValue(StateData.loading((L)null));
        if (lastRefreshFetcher != null && !lastRefreshFetcher.isCancel()) {
            // 如果下拉刷新还没有结束，不执行加载更多
            if (lastLoadMoreFetcher != null) {
                lastLoadMoreFetcher.cancel();
                lastLoadMoreFetcher = null;
            }
            return;
        }
        if (lastLoadMoreFetcher != null && !lastLoadMoreFetcher.isCancel()) {
            // 上一次的加载更多还没结束
            return;
        }
        lastLoadMoreFetcher = dataFetcher;
        dataFetcher.fetchData(new DataFetcher.Callback<L>() {


            @Override
            public void onSuccess(L data) {
                if (lastLoadMoreFetcher == dataFetcher) {
                    lastLoadMoreFetcher = null;
                }
                if (dataFetcher.isCancel()) {
                    return;
                }
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    pagingLiveDelegate.getLoadMoreStateLiveData().setValue(StateData.newSuccess(data));
                } else {
                    pagingLiveDelegate.getLoadMoreStateLiveData().postValue(StateData.newSuccess(data));
                }
            }

            @Override
            public void onError(Throwable t) {
                if (lastLoadMoreFetcher == dataFetcher) {
                    lastLoadMoreFetcher = null;
                }
                if (dataFetcher.isCancel()) {
                    return;
                }
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    pagingLiveDelegate.getLoadMoreStateLiveData().setValue(StateData.error((L)null, t));
                } else {
                    pagingLiveDelegate.getLoadMoreStateLiveData().postValue(StateData.error((L)null, t));
                }
            }
        });
    }

    protected void checkArgs() {
        if (pagingLiveDelegate == null) {
            throw new IllegalArgumentException("pagingDelegate should not be null");
        }
        if (contentView == null) {
            throw new IllegalArgumentException("content view should not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("listener should not be null");
        }
        if (switchAnim == null) {
            switchAnim = new DefaultPageSwitfchAnim();
        }
    }

    private View contentView;

    private View loadingView;

    private View errorView;

    private View emptyView;

    private PagingLiveDelegate<R, L> pagingLiveDelegate;

    private IRefreshSender refreshSender;

    private ILoadMoreSender loadMoreSender;

    private IPagingCallback<R, L> callback;

    private IPageSwitchAnim switchAnim;

    public PagingArch() {}
}
