package com.igalata.bubblepickerdemo
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
                {
                    // After the splash screen duration, route to the right activities
                    val intent = Intent(this,DemoActivity::class.java)
                    startActivity(intent)
                },
                splashScreenDuration
        )
    }

    private fun getSplashScreenDuration() = 2000L



}