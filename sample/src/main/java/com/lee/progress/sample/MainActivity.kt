package com.lee.progress.sample

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animator = ValueAnimator.ofInt(0, 50)
        animator.duration = 2000
        animator.addUpdateListener {
            if (it.animatedValue is Int) {
                pie.progress = it.animatedValue as Int
            }
        }
        animator.start()
    }
}
