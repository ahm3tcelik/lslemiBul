package com.ahmetc.islemibul;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Splash extends AppCompatActivity {
    Animation alpha_animasyon,toplama,cikarma,carpma,soru;
    private RelativeLayout intro_tasarim;
    private ImageView intro_cikarma, intro_toplama, intro_carpma, intro_soru;
    private LinearLayout intro_yazi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        alpha_animasyon = AnimationUtils.loadAnimation(Splash.this,R.anim.islemler_alpha);
        toplama = AnimationUtils.loadAnimation(Splash.this,R.anim.translate_toplama);
        cikarma = AnimationUtils.loadAnimation(Splash.this,R.anim.translate_cikarma);
        carpma = AnimationUtils.loadAnimation(Splash.this,R.anim.translate_carpma);
        soru = AnimationUtils.loadAnimation(Splash.this,R.anim.translate_soru);

        cikarma.setFillAfter(true);
        toplama.setFillAfter(true);
        carpma.setFillAfter(true);
        soru.setFillAfter(true);
        alpha_animasyon.setFillAfter(true);

        intro_toplama.setAnimation(toplama);
        intro_cikarma.setAnimation(cikarma);
        intro_carpma.setAnimation(carpma);
        intro_soru.setAnimation(soru);
        intro_yazi.setAnimation(alpha_animasyon);
        intro_tasarim.setAnimation(alpha_animasyon);
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Splash.this, GameMenu.class));
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    public void init() {
        intro_tasarim = findViewById(R.id.intro_tasarim);
        intro_cikarma = findViewById(R.id.intro_cikarma);
        intro_soru = findViewById(R.id.intro_soru);
        intro_toplama = findViewById(R.id.intro_toplama);
        intro_carpma = findViewById(R.id.intro_carpma);
        intro_yazi = findViewById(R.id.intro_yazi);
    }
}
