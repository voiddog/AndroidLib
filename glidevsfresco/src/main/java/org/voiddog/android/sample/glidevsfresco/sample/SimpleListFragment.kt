package org.voiddog.android.sample.glidevsfresco.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_simple_list.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.sample.glidevsfresco.IPageName
import org.voiddog.android.sample.glidevsfresco.R

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
 * @author qigengxin
 * @since 2018-07-27 14:53
 */

interface AdapterCallback<T> where T : MultiTypeBindAdapter.ViewTypeItem{
    fun createAdapter() : ListMultiTypeBindAdapter<T>
}

class SimpleListFragment<T> : Fragment(), IPageName where T : MultiTypeBindAdapter.ViewTypeItem{

    companion object {
        fun <T> newInstance(name:String, list: List<T>, callback: AdapterCallback<T>?) : SimpleListFragment<T>
                where T : MultiTypeBindAdapter.ViewTypeItem{
            val fragment = SimpleListFragment<T>()
            fragment.name = name
            fragment.itemList.addAll(list)
            fragment.callback = callback
            return fragment
        }
    }

    private var name: String? = null
    private val itemList = ArrayList<T>()
    private var callback: AdapterCallback<T>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun getPageName(): String? = name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init view
        callback?.apply {
            rec_list.layoutManager = LinearLayoutManager(context)
            val adapter= createAdapter()
            rec_list.adapter = adapter
            adapter.set(itemList)
        }
    }
}
