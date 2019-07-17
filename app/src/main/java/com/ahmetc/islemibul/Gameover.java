package com.ahmetc.islemibul;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Gameover extends AppCompatActivity {

    private ImageView finish_image, finish_again, finish_gamemenu;
    private TextView finish_eski, finish_skor, finish_sure, finish_rekor;
    private Sounds sounds;
    private boolean sound_enable;
    private MediaPlayer mediaPlayerLose, mediaPlayerWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        sound_enable = preferences.getBoolean("SoundEnable", true);

        if (sounds == null && sound_enable) sounds = new Sounds(this);
        if(sound_enable) {
            if(mediaPlayerLose == null) mediaPlayerLose = MediaPlayer.create(this, R.raw.sound_loser);
            if(mediaPlayerWin == null) mediaPlayerWin = MediaPlayer.create(this, R.raw.sound_winner);
        }
        init();

        SharedPreferences sharedPreferences = getSharedPreferences("MaxScore", MODE_PRIVATE);

        int puan = getIntent().getIntExtra("puan",0);
        String sure = getIntent().getStringExtra("time");
        int oldpuan = sharedPreferences.getInt("enyuksek",0);

        finish_skor.setText(getString(R.string.skor).concat(" : ").concat(String.valueOf(puan)));
        finish_eski.setText(getString(R.string.eskirekor).concat("\n").concat(String.valueOf(oldpuan)));
        finish_sure.setText(getString(R.string.sure).concat(" : ").concat(sure));

        if(puan > oldpuan) {
            if(sound_enable)mediaPlayerWin.start();
            finish_rekor.setTextColor(Color.parseColor("#4BD171"));
            finish_rekor.setText(getString(R.string.yeniRekor));
            finish_image.setImageResource(R.drawable.win);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("enyuksek",puan);
            editor.apply();
        }
        else {
            if(sound_enable)mediaPlayerLose.start();
            finish_rekor.setTextColor(Color.parseColor("#FF3B3B"));
            finish_rekor.setText(getString(R.string.textLose));
            finish_image.setImageResource(R.drawable.lose);
        }
        finish_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_enable)sounds.PlaySound(Sounds.Musics.selected_sound);
                startActivity(new Intent(Gameover.this, MainActivity.class));
                finish();
            }
        });
        finish_gamemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_enable)sounds.PlaySound(Sounds.Musics.selected_sound);
                startActivity(new Intent(Gameover.this, GameMenu.class));
                finish();
            }
        });
    }
    public void init() {
        finish_image = findViewById(R.id.finish_image);
        finish_again = findViewById(R.id.finish_again);
        finish_gamemenu = findViewById(R.id.finish_gamemenu);
        finish_eski = findViewById(R.id.finish_eski);
        finish_skor = findViewById(R.id.finish_skor);
        finish_sure = findViewById(R.id.finish_sure);
        finish_rekor = findViewById(R.id.finish_rekor);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayerLose != null && sound_enable) mediaPlayerLose.pause();
        if(mediaPlayerWin != null && sound_enable) mediaPlayerWin.pause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayerWin != null && sound_enable) {
            mediaPlayerWin.release();
            mediaPlayerWin = null;
        }
        if(mediaPlayerLose != null && sound_enable) {
            mediaPlayerLose.release();
            mediaPlayerLose = null;
        }
    }
}
