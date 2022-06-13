package com.example.capstoneproject.ui.splashscreen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.capstoneproject.R
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.main.MainActivity
import com.example.capstoneproject.ui.onboarding.OnBoardingActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var topAnimation: Animation
    private lateinit var buttomAnimation: Animation
    private lateinit var imageLogo: ImageView
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
        slogan.startAnimation(buttomAnimation)
        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()}
        }, 2000)
    }
}