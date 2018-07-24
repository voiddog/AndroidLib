package org.voiddog.android.test.lib.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_physic_anim_test.*
import org.voiddog.android.lib.base.utils.DensityUtil.dp2px
import org.voiddog.android.lib.design.animator.PhysicsSpringAnimSet
import org.voiddog.android.test.lib.R

class PhysicAnimTestActivity : AppCompatActivity() {

    var x = 0f
    var y = 0f
    var scale = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physic_anim_test)
        val moveDis = dp2px(100f)
        btn_move_right.setOnClickListener {
            x += moveDis
            PhysicsSpringAnimSet.from(img_sample)
                    .translationX(x)
        }
        btn_move_left_top.setOnClickListener {
            x -= moveDis
            y -= moveDis
            PhysicsSpringAnimSet.from(img_sample)
                    .translationX(x)
                    .translationY(y)
        }
        btn_move_down_zoom_in.setOnClickListener {
            y += moveDis
            scale += 0.1f
            PhysicsSpringAnimSet.from(img_sample)
                    .translationY(y)
                    .scale(scale)
        }
        btn_zoom_out.setOnClickListener {
            scale -= 0.5f
            PhysicsSpringAnimSet.from(img_sample)
                    .scale(scale)
        }
    }
}
