package org.voiddog.android.test.lib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.base.utils.DensityUtil.dp2px
import org.voiddog.android.test.lib.sample.*

private data class MenuEntry(val name:String, val clazz: Class<*>)
    : MultiTypeBindAdapter.ViewTypeItem {

    override fun getViewType(): Int = 0
}

private class MenuVH(parent: ViewGroup) : BindViewHolder<MenuEntry>(Button(parent.context)) {

    val button = itemView as Button

    init {
        button.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        with(button.layoutParams as RecyclerView.LayoutParams) {
            leftMargin = dp2px(5f)
            rightMargin = dp2px(5f)
            topMargin = dp2px(10f)
            bottomMargin = dp2px(10f)
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
            MenuVH(it)
        }
        val menuList = arrayListOf(MenuEntry("测试物理动画效果", PhysicAnimTestActivity::class.java),
                MenuEntry("测试 SPManager", SpTestActivity::class.java),
                MenuEntry("测试 loading arch", LoadingArchTestActivity::class.java),
                MenuEntry("测试 List Multi item", MultiTypeTestActivity::class.java),
                MenuEntry("测试权限", PermissionTestActivity::class.java))
        rec_list.adapter = adapter
        rec_list.layoutManager = LinearLayoutManager(this)
        adapter.set(menuList)
    }
}
