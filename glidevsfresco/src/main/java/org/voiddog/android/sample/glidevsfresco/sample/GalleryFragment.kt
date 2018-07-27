package org.voiddog.android.sample.glidevsfresco.sample

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_simple_list.*
import org.voiddog.android.lib.base.recycler.adapter.ListMultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.adapter.MultiTypeBindAdapter
import org.voiddog.android.lib.base.recycler.viewholder.BindViewHolder
import org.voiddog.android.lib.base.utils.DensityUtil
import org.voiddog.android.lib.rx.permission.RxPermissionEmitter
import org.voiddog.android.lib.rx.permission.RxPermissionHandlerHelper
import org.voiddog.android.lib.rx.permission.RxPermissionRequest
import org.voiddog.android.lib.rx.permission.RxPermissionUtil
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
 *
 * 本地图片画廊
 *
 * @author qigengxin
 * @since 2018-07-27 17:53
 */

val GLIDE_TYPE = 1
val FRESCO_TYPE = 2

private data class ImgItem(val uri:String, val itemType: Int) : MultiTypeBindAdapter.ViewTypeItem {
    override fun getViewType(): Int = itemType
}

private class GalleryGlideVH(parent: ViewGroup) : BindViewHolder<ImgItem>(parent, R.layout.rec_gallery_glide_item) {

    val img = itemView.findViewById<ImageView>(R.id.img)

    override fun onBindData(data: ImgItem) {
        Glide.with(img)
                .load(data.uri)
                .apply(RequestOptions.centerCropTransform())
                .into(img)
    }
}

private class GalleryFrescoVH(parent: ViewGroup, val itemSize: Int) : BindViewHolder<ImgItem>(parent, R.layout.rec_gallery_fresco_item) {

    val drawee = itemView.findViewById<SimpleDraweeView>(R.id.drawee)

    override fun onBindData(data: ImgItem) {
        drawee.controller = Fresco.newDraweeControllerBuilder().apply {
            oldController = drawee.controller
            autoPlayAnimations = true
            imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.uri))
                    .setResizeOptions(ResizeOptions(itemSize, itemSize))
                    .build()
        }.build()
    }
}

class GalleryFragment : Fragment(){

    companion object {
        fun newInstance(itemType: Int) : GalleryFragment {
            val ret = GalleryFragment()
            ret.itemType = itemType
            return ret
        }
    }

    private var itemType = GLIDE_TYPE
    private var helper: RxPermissionHandlerHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helper = RxPermissionHandlerHelper(activity)
        // init view
        rec_list.layoutManager = GridLayoutManager(context, 4)
        val adapter = ListMultiTypeBindAdapter<ImgItem>()
        adapter.registerItemProvider(GLIDE_TYPE) {
            return@registerItemProvider  GalleryGlideVH(it)
        }
        adapter.registerItemProvider(FRESCO_TYPE) {
            return@registerItemProvider GalleryFrescoVH(it, DensityUtil.getScreenWidth(it.context)/4)
        }
        rec_list.adapter = adapter

        RxPermissionUtil.checkPermission(activity!!, helper!!, RxPermissionRequest.newRequest()
                .addForcePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .observeOn(Schedulers.io())
                .map {
                    return@map scanGallery()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.set(it!!.map { ImgItem(it, itemType) })
                }, {
                    it.printStackTrace()
                    if (context != null) {
                        Toast.makeText(context, "扫描画廊异常", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        helper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun scanGallery(): List<String>? {
        val context = this.context ?: return null
        val minSize = 30*1024L
        val galleryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val mineType = MediaStore.Images.Media.MIME_TYPE
        val selection = "($mineType=? OR $mineType=? OR $mineType=?) AND ${MediaStore.Images.Media.SIZE}>?"
        val selectionArgs = arrayOf("image/jpeg", "image/png", "image/gif", minSize.toString())
        val cursor = context.contentResolver.query(galleryUri, null, selection, selectionArgs
                , MediaStore.Images.Media.DATE_ADDED + " DESC") ?: return null
        val ret = ArrayList<String>()
        while(cursor.moveToNext()) {
            val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
            if (size < minSize) {
                continue
            }
            ret.add("file://${cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))}")
        }
        cursor.close()
        return ret
    }
}