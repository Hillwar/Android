package com.hillwar.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.hillwar.animation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val objectAnimator1: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.word, View.ALPHA, 0f, 1F)
        objectAnimator1.duration = 2000
        val objectAnimator2: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.word, View.ALPHA, 1F, 0F)
        objectAnimator2.duration = 2000
        val animatorSet = AnimatorSet()
        animatorSet.play(objectAnimator1).before(objectAnimator2)
        animatorSet.doOnEnd { animatorSet.start() }
        animatorSet.start()
    }
}
