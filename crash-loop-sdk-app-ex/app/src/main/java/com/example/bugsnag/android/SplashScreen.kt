package com.example.bugsnag.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bugsnag.android.Bugsnag
import com.example.foo.FeatureMgr
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // We used the postDelayed(Runnable, time) method to send a the runtime exception with a
        // delayed time, to allow the splash screen to show before crashing.
        Handler().postDelayed({
            if (FeatureMgr.turnoff("") == true) {
                throw RuntimeException("oops launch crash")
            }

            //See https://docs.bugsnag.com/platforms/android/identifying-crashes-at-launch/#controlling-the-launch-period
            Bugsnag.markLaunchCompleted()
            TimeUnit.SECONDS.sleep(2L)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}