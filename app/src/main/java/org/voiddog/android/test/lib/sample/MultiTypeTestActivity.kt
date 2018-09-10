package org.voiddog.android.test.lib.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_multi_type_test.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.base.utils.DensityUtil
import org.voiddog.android.test.lib.R

class MultiTypeTestActivity : AppCompatActivity() {

    data class ItemA(val content: String) : MultiTypeBindAdapter.ViewTypeItem {
        override fun getViewType(): Int {
            return 1
        }
    }

    data class ItemB(val content: String) : MultiTypeBindAdapter.ViewTypeItem {
        override fun getViewType(): Int {
            return 2
        }
    }

    class VHA(parent: ViewGroup) : BindViewHolder<ItemA>(TextView(parent.context)) {

        private val textView: TextView = itemView as TextView

        init {
            val padding = DensityUtil.dp2px(10f)
            textView.setPadding(padding, padding, padding, padding)
            textView.setTextColor(textView.resources.getColor(R.color.colorAccent))
        }

        override fun onBindData(data: ItemA) {
            textView.text = data.content
        }
    }

    class VHB(parent: ViewGroup) : BindViewHolder<ItemB>(TextView(parent.context)) {

        private val textView: TextView = itemView as TextView

        init {
            val padding = DensityUtil.dp2px(10f)
            textView.setPadding(padding, padding, padding, padding)
            textView.setTextColor(textView.resources.getColor(R.color.colorPrimaryDark))
        }

        override fun onBindData(data: ItemB) {
            textView.text = data.content
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type_test)
        val adapter = ListMultiTypeBindAdapter<MultiTypeBindAdapter.ViewTypeItem>()
        adapter.registerItemProvider(1) { parent -> VHA(parent) }
        adapter.registerItemProvider(2) { parent -> VHB(parent) }
        rec_list.layoutManager = LinearLayoutManager(this)
        rec_list.adapter = adapter
        adapter.set(arrayListOf(
                ItemA("我是A"),
                ItemB("我是B")
        ))
    }
}
