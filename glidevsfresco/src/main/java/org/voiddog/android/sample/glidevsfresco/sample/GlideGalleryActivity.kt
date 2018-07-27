package org.voiddog.android.sample.glidevsfresco.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.voiddog.android.sample.glidevsfresco.R

class GlideGalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_gallery)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fra_content, GalleryFragment.newInstance(GLIDE_TYPE))
                .commit()
    }
}
