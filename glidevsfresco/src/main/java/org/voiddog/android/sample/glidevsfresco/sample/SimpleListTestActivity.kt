package org.voiddog.android.sample.glidevsfresco.sample

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.activity_simple_list_test.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.base.utils.DensityUtil
import org.voiddog.android.sample.glidevsfresco.R
import org.voiddog.android.sample.glidevsfresco.SimpleFragmentPagerAdapter
import org.voiddog.android.sample.glidevsfresco.generateNextCdnUrl
import org.voiddog.android.sample.glidevsfresco.generateNextGifUrl

private class RoundParams {
    var isCircle = false
    var radius = 0f
}

private data class SimpleItem(val hint: String, val url:String, val itemType: Int) : MultiTypeBindAdapter.ViewTypeItem{

    var roundParams: RoundParams? = null
    var scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER

    override fun getViewType(): Int {
        return itemType
    }
}

private class GlideVH(parent: ViewGroup)
    : BindViewHolder<SimpleItem>(parent, R.layout.rec_simple_glide_item) {

    private val img : ImageView = itemView.findViewById(R.id.img)
    private val txtHint: TextView = itemView.findViewById(R.id.txt_hint)

    override fun onBindData(data: SimpleItem) {
        txtHint.text = data.hint
        val requestOptions = RequestOptions.placeholderOf(R.drawable.loading_anim)
        val transList = ArrayList<Transformation<Bitmap>>()
        data.roundParams?.apply {
            if (isCircle) {
                transList.add(CircleCrop())
            } else if (radius > 0) {
                transList.add(RoundedCorners(radius.toInt()))
            }
        }
        when (data.scaleType) {
            ImageView.ScaleType.CENTER_CROP -> transList.add(CenterCrop())
            ImageView.ScaleType.CENTER_INSIDE -> transList.add(CenterInside())
            ImageView.ScaleType.FIT_CENTER -> transList.add(FitCenter())
            else -> {}
        }
        if (!transList.isEmpty()) {
            requestOptions.transform(MultiTransformation<Bitmap>(transList))
        }
        Glide.with(img)
                .load(data.url)
                .apply(requestOptions)
                .into(img)
    }
}

private class FrescoVH(parent: ViewGroup)
    : BindViewHolder<SimpleItem>(parent, R.layout.rec_simple_fresco_item) {

    private val drawee = itemView.findViewById<SimpleDraweeView>(R.id.drawee)
    private val txtHint = itemView.findViewById<TextView>(R.id.txt_hint)

    override fun onBindData(data: SimpleItem) {
        txtHint.text = data.hint
        drawee.hierarchy.roundingParams = null
        data.roundParams?.apply {
            if (isCircle) {
                drawee.hierarchy.roundingParams = RoundingParams.asCircle().setOverlayColor(Color.WHITE)
            } else if (radius > 0) {
                drawee.hierarchy.roundingParams = RoundingParams.fromCornersRadius(radius).setOverlayColor(Color.WHITE)
            }
        }
        when(data.scaleType) {
            ImageView.ScaleType.CENTER -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER
            ImageView.ScaleType.CENTER_CROP -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
            ImageView.ScaleType.CENTER_INSIDE -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_INSIDE
            ImageView.ScaleType.FIT_XY -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_XY
            ImageView.ScaleType.FIT_CENTER -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
            else -> drawee.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
        }
        drawee.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.url))
                        .setResizeOptions(ResizeOptions(DensityUtil.getScreenWidth(itemView.context)
                                , DensityUtil.getScreenWidth(itemView.context)))
                        .build())
                .setAutoPlayAnimations(true)
                .build()
    }
}

class SimpleListTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list_test)
        tab_layout.setupWithViewPager(view_pager)
        val dataList = arrayListOf(SimpleItem("fitCenter", generateNextCdnUrl(), 0),
                SimpleItem("center", generateNextCdnUrl(), 0).apply {
                    scaleType = ImageView.ScaleType.CENTER
                },
                SimpleItem("centerInside", generateNextCdnUrl(), 0).apply {
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                },
                SimpleItem("centerCrop", generateNextCdnUrl(), 0).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                },
                SimpleItem("fitXY", generateNextCdnUrl(), 0).apply {
                    scaleType = ImageView.ScaleType.FIT_XY
                },
                SimpleItem("circle", generateNextCdnUrl(), 0).apply {
                    roundParams = RoundParams().apply { isCircle=true }
                },
                SimpleItem("roundParams", generateNextCdnUrl(), 0).apply {
                    roundParams = RoundParams().apply { radius = DensityUtil.dp2px(16f).toFloat() }
                },
                SimpleItem("Gif Circle", generateNextGifUrl(), 0).apply {
                    roundParams = RoundParams().apply { isCircle = true }
                })
        val adapter = SimpleFragmentPagerAdapter<SimpleListFragment<SimpleItem>>(supportFragmentManager)
        adapter.fragmentList.add(SimpleListFragment.newInstance("Glide", dataList, object:AdapterCallback<SimpleItem> {
            override fun createAdapter(): ListMultiTypeBindAdapter<SimpleItem> {
                val ret = ListMultiTypeBindAdapter<SimpleItem>()
                ret.registerItemProvider(0) {
                    return@registerItemProvider GlideVH(it)
                }
                return ret
            }
        }))
        adapter.fragmentList.add(SimpleListFragment.newInstance("Empty", ArrayList(), null))
        adapter.fragmentList.add(SimpleListFragment.newInstance("Fresco", dataList, object:AdapterCallback<SimpleItem> {
            override fun createAdapter(): ListMultiTypeBindAdapter<SimpleItem> {
                val ret = ListMultiTypeBindAdapter<SimpleItem>()
                ret.registerItemProvider(0) {
                    return@registerItemProvider FrescoVH(it)
                }
                return ret
            }
        }))
        view_pager.adapter = adapter
    }
}
