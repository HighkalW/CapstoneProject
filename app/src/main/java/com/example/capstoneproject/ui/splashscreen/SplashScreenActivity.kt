package com.example.capstoneproject.ui.splashscreen


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.capstoneproject.R

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var topAnimation: Animation
    private lateinit var buttomAnimation: Animation
    private lateinit var imageLogo: ImageView
    private lateinit var imagetext : ImageView
    private lateinit var slogan : TextView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )





        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        buttomAnimation = AnimationUtils.loadAnimation(this, R.anim.buttom_animation)

        imageLogo = findViewById(R.id.logo)
        slogan = findViewById(R.id.slogan_text)

        imageLogo.startAnimation(topAnimation)
        imagetext.startAnimation(buttomAnimation)
        slogan.startAnimation(buttomAnimation)
    }
}