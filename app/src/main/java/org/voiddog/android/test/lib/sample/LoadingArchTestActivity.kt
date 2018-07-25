package org.voiddog.android.test.lib.sample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_loading_arch_test.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.design.loadingarch.loadmore.DefaultLoadMoreViewHolder
import org.voiddog.android.lib.design.loadingarch.loadmore.LoadMoreWrapper
import org.voiddog.android.lib.design.loadingarch.paging.*
import org.voiddog.android.test.lib.R

data class ItemData(val type: Int, val content: String) : MultiTypeBindAdapter.ViewTypeItem {
    override fun getViewType(): Int = type
}

data class LoadDataBean(val dataList : MutableList<ItemData>)

class TestPagingViewModel : ViewModel() , IPagingCallback<LoadDataBean, LoadDataBean> {

    val pagingLiveDelegate by lazy {
        PagingLiveDelegate<LoadDataBean, LoadDataBean>()
    }

    override fun requestRefresh(arch: PagingArch<LoadDataBean, LoadDataBean>): DataFetcher<LoadDataBean> {
        return object : DataFetcher<LoadDataBean>() {
            override fun fetchData(callback: Callback<LoadDataBean>) {
                Thread(Runnable {
                    // 模拟数据获取时间
                    Thread.sleep(2000)
                    var ret = LoadDataBean(ArrayList())
                    for (i in 1..20) {
                        if (Math.random() < 0.5f) {
                            ret.dataList.add(ItemData(R.layout.rec_item_paging_item_a, "我是刷新的 A 类型数据: $i"))
                        } else {
                            ret.dataList.add(ItemData(R.layout.rec_item_paging_item_b, "我是刷新的 B 类型数据: $i"))
                        }
                    }
                    if (Math.random() < 0.2f) {
                        // 20% 的概率失败
                        callback.onError(IllegalArgumentException(""))
                    } else {
                        callback.onSuccess(ret)
                    }
                }).start()
            }
        }
    }

    override fun requestLoadMore(arch: PagingArch<LoadDataBean, LoadDataBean>): DataFetcher<LoadDataBean> {
        return object : DataFetcher<LoadDataBean>() {
            override fun fetchData(callback: Callback<LoadDataBean>) {
                Thread(Runnable {
                    // 模拟数据获取时间
                    Thread.sleep(1000)
                    var ret = LoadDataBean(ArrayList())
                    for (i in 1..20) {
                        if (Math.random() < 0.5f) {
                            ret.dataList.add(ItemData(R.layout.rec_item_paging_item_a, "我是加载更多的 A 类型数据: $i"))
                        } else {
                            ret.dataList.add(ItemData(R.layout.rec_item_paging_item_b, "我是加载更多的 B 类型数据: $i"))
                        }
                    }
                    if (Math.random() < 0.2f) {
                        // 20% 的概率失败
                        callback.onError(IllegalArgumentException(""))
                    } else {
                        callback.onSuccess(ret)
                    }
                }).start()
            }
        }
    }

    override fun isEmpty(dataBean: LoadDataBean?): Boolean {
        return false
    }

    override fun isRefreshComplete(dataBean: LoadDataBean?): Boolean {
        return false
    }

    override fun isLoadMoreComplete(dataBean: LoadDataBean?): Boolean {
        return false
    }
}


private class VHA(parent: ViewGroup) : BindViewHolder<ItemData>(parent, R.layout.rec_item_paging_item_a) {

    val txtContent = itemView.findViewById<TextView>(R.id.txt_content)

    override fun onBindData(data: ItemData) {
        txtContent.text = data.content
    }
}

private class VHB(parent: ViewGroup) : BindViewHolder<ItemData>(parent, R.layout.rec_item_paging_item_b) {

    val txtContent = itemView.findViewById<TextView>(R.id.txt_content)

    override fun onBindData(data: ItemData) {
        txtContent.text = data.content
    }
}

class LoadingArchTestActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProviders.of(this).get(TestPagingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_arch_test)
        val adapter = ListMultiTypeBindAdapter<ItemData>()
        adapter.registerItemProvider(R.layout.rec_item_paging_item_a) { parent -> VHA(parent) }
        adapter.registerItemProvider(R.layout.rec_item_paging_item_b) { parent -> VHB(parent) }
        val loadMoreWrapper = LoadMoreWrapper(adapter, object : LoadMoreWrapper.CreateLoadMoreViewHolderListener {
            override fun createLoadMoreViewHolder(parent: ViewGroup): LoadMoreWrapper.LoadMoreViewHolder {
                return DefaultLoadMoreViewHolder(parent)
            }

            override fun getItemType(): Int {
                return R.layout.void_design_layout_default_load_more
            }
        })
        val arch = PagingArch<LoadDataBean, LoadDataBean>()
                .setContentView(refresh_layout)
                .setLoadingView(loading)
                .setEmptyView(loading)
                .setErrorView(loading)
                .setPagingLiveDelegate(viewModel.pagingLiveDelegate)
                .setLoadMoreSender(loadMoreWrapper)
                .setRefreshSender(refresh_layout)
                .setCallback(viewModel)
        viewModel.pagingLiveDelegate.pageStateLiveData.value = PagingPageState.loading()
        arch.install(this)
        arch.performRefresh(false)
        viewModel.pagingLiveDelegate.refreshLiveData.observe(this, Observer {
            adapter.set(it?.dataList)
        })
        viewModel.pagingLiveDelegate.loadMoreLiveData.observe(this, Observer {
            adapter.add(it?.dataList)
        })
        loadMoreWrapper.addOnErrorClickListener {
            arch.performLoadMore(true)
        }
        rec_paging_list.adapter = loadMoreWrapper
        rec_paging_list.layoutManager = LinearLayoutManager(this)
    }
}
