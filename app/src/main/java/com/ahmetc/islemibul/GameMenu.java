package com.ahmetc.islemibul;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameMenu extends AppCompatActivity {

    private Button buttonBasla, buttonNasil, buttonAyarlar;
    private MediaPlayer mediaPlayer;
    private Sounds sound;
    private boolean sound_enable, music_enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        music_enable = preferences.getBoolean("MusicEnable",true);
        sound_enable = preferences.getBoolean("SoundEnable",true);

        if(mediaPlayer == null && music_enable) {
            mediaPlayer = MediaPlayer.create(this,R.raw.sound_gamemenu);
        }
        if(music_enable) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        if(sound == null && sound_enable)sound = new Sounds(this);

        init();

        buttonBasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_enable)sound.PlaySound(Sounds.Musics.selected_sound);
                startActivity(new Intent(GameMenu.this, MainActivity.class));
            }
        });
        buttonNasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_enable)sound.PlaySound(Sounds.Musics.selected_sound);
                startActivity(new Intent(GameMenu.this, Slider.class));
            }
        });

        buttonAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_enable)sound.PlaySound(Sounds.Musics.selected_sound);
                startActivity(new Intent(GameMenu.this, Settings.class));
            }
        });
    }
    public void init() {
        buttonBasla = findViewById(R.id.buttonBasla);
        buttonNasil = findViewById(R.id.buttonNasil);
        buttonAyarlar = findViewById(R.id.buttonAyarlar);
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        sound_enable = preferences.getBoolean("SoundEnable",true);
        music_enable = preferences.getBoolean("MusicEnable",true);

        if(sound == null)sound = new Sounds(this);

        if(mediaPlayer == null && music_enable) {
            mediaPlayer = MediaPlayer.create(this,R.raw.sound_gamemenu);
        }
        if(music_enable) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null && music_enable) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null && music_enable) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
