package com.example.pma_project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView nasaLogo;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        nasaLogo = findViewById(R.id.iv_nasalogo);
        textView = findViewById(R.id.text_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                nasaLogo,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );

        objectAnimator.setDuration(500);

        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);

        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);

        objectAnimator.start();

        animateText("Powered by");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        },4000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(charSequence.subSequence(0,index++));
            if(index <= charSequence.length())
            {
                handler.postDelayed(runnable, delay);
            }
        }
    };

    public void animateText(CharSequence cs)
    {
        charSequence = cs;
        index = 0;
        textView.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }
}