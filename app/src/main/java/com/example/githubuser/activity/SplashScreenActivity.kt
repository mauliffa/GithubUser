package com.example.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.githubuser.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logo:ImageView = findViewById(R.id.logo_splash_screen)

        logo.alpha = 0f
        logo.animate().setDuration(3000).alpha(1f).withEndAction {
            val moveToMainActivity = Intent (this, MainActivity::class.java)
            startActivity(moveToMainActivity)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}