package com.example.pma_project

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.example.pma_project.R
import android.view.WindowManager
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Handler
import android.widget.ImageView
import com.example.pma_project.MainActivity

class SplashActivity : AppCompatActivity() {
    var nasaLogo: ImageView? = null
    var textView: TextView? = null
    var charSequence: CharSequence? = null
    var index = 0
    var delay: Long = 200
    var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        nasaLogo = findViewById(R.id.iv_nasalogo)
        textView = findViewById(R.id.text_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                nasaLogo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        )
        objectAnimator.duration = 500
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.start()
        animateText("Powered by")
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity,
                    MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }, 4000)
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            textView!!.text = charSequence!!.subSequence(0, index++)
            if (index <= charSequence!!.length) {
                handler.postDelayed(this, delay)
            }
        }
    }

    private fun animateText(cs: CharSequence?) {
        charSequence = cs
        index = 0
        textView!!.text = ""
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delay)
    }
}