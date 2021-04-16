package org.voiddog.android.test.lib.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.voiddog.android.lib.base.utils.DensityUtil.dp2px
import org.voiddog.android.lib.design.animator.PhysicsSpringAnimSet
import org.voiddog.android.test.lib.databinding.ActivityPhysicAnimTestBinding

class PhysicAnimTestActivity : AppCompatActivity() {

    var x = 0f
    var y = 0f
    var scale = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val moveDis = dp2px(100f)
        val binding = ActivityPhysicAnimTestBinding.inflate(layoutInflater)

        binding.btnMoveRight.setOnClickListener {
            x += moveDis
            PhysicsSpringAnimSet.from(binding.imgSample)
                    .translationX(x)
        }
        binding.btnMoveLeftTop.setOnClickListener {
            x -= moveDis
            y -= moveDis
            PhysicsSpringAnimSet.from(binding.imgSample)
                    .translationX(x)
                    .translationY(y)
        }
        binding.btnMoveDownZoomIn.setOnClickListener {
            y += moveDis
            scale += 0.1f
            PhysicsSpringAnimSet.from(binding.imgSample)
                    .translationY(y)
                    .scale(scale)
        }
        binding.btnZoomOut.setOnClickListener {
            scale -= 0.5f
            PhysicsSpringAnimSet.from(binding.imgSample)
                    .scale(scale)
        }

        setContentView(binding.root)
    }
}
