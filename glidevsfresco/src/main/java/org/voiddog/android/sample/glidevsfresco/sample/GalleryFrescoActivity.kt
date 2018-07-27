package org.voiddog.android.sample.glidevsfresco.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.voiddog.android.sample.glidevsfresco.R

class GalleryFrescoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_fresco)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fra_content, GalleryFragment.newInstance(FRESCO_TYPE))
                .commit()
    }
}
