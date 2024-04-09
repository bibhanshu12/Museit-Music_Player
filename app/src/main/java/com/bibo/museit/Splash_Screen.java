package com.bibo.museit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textview=findViewById(R.id.txtview);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.alpha);
        textview.setAnimation(animation);

        Intent home= new Intent(Splash_Screen.this,LogIn_Screen.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(home);
                finish();

            }
        },2000);
    }
}