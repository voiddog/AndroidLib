package org.voiddog.android.sample.glidevsfresco

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.base.utils.DensityUtil
import org.voiddog.android.sample.glidevsfresco.sample.GalleryFrescoActivity
import org.voiddog.android.sample.glidevsfresco.sample.GlideGalleryActivity
import org.voiddog.android.sample.glidevsfresco.sample.SimpleListTestActivity


private data class MenuEntry(val name:String, val clazz: Class<*>)
    : MultiTypeBindAdapter.ViewTypeItem {

    override fun getViewType(): Int = 0
}

private class MenuVH(parent: ViewGroup) : BindViewHolder<MenuEntry>(Button(parent.context)) {

    val button = itemView as Button

    init {
        button.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        with(button.layoutParams as RecyclerView.LayoutParams) {
            leftMargin = DensityUtil.dp2px(5f)
            rightMargin = DensityUtil.dp2px(5f)
            topMargin = DensityUtil.dp2px(10f)
            bottomMargin = DensityUtil.dp2px(10f)
        }
        button.setOnClickListener {
            val menuEntry = data
            if (menuEntry != null) {
                it.context.startActivity(Intent(it.context, menuEntry.clazz))
            }
        }
    }

    override fun onBindData(data: MenuEntry) {
        button.text = data.name
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = ListMultiTypeBindAdapter<MenuEntry>()
        adapter.registerItemProvider(0) {
            return@registerItemProvider MenuVH(it)
        }
        adapter.set(arrayListOf(MenuEntry("简单的列表测试", SimpleListTestActivity::class.java),
                MenuEntry("Glide 画廊", GlideGalleryActivity::class.java),
                MenuEntry("Fresco 画廊", GalleryFrescoActivity::class.java)))
        rec_list.layoutManager = LinearLayoutManager(this)
        rec_list.adapter = adapter
    }
}
