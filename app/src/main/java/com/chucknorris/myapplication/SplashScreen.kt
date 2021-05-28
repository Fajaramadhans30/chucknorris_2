package com.chucknorris.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.chucknorris.myapplication.ui.CategoryActivity
import com.trncic.library.DottedProgressBar
import kotlinx.android.synthetic.main.splash_screen.view.*

class SplashScreen: AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=3000 // 3 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, CategoryActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)

    }


}
