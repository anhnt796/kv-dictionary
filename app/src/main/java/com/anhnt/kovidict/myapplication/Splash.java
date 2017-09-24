package com.anhnt.kovidict.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
    private static final int SPLASH_DURATION = 2000;
    private boolean mIsBackButtonPressed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Splash.this.finish();
                if (!Splash.this.mIsBackButtonPressed) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("type_dict", 0);
                    Splash.this.startActivity(intent);
                }
            }
        }, SPLASH_DURATION);
    }

    public void onBackPressed() {
        this.mIsBackButtonPressed = true;
        super.onBackPressed();
    }
}