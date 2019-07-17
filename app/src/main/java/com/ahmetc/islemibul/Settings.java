package com.ahmetc.islemibul;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity {
    private RadioButton radio_kolay, radio_orta, radio_zor;
    private SwitchCompat switch_sound, switch_music;
    private boolean stateSound, stateMusic;
    private int zorluk;
    private SharedPreferences preferences;
    private ImageView image_settings;
    private Animation settings_animation_r, settings_animation_l;
    private Sounds sounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        switch_sound = findViewById(R.id.switch_sound);
        switch_music = findViewById(R.id.switch_music);
        radio_kolay = findViewById(R.id.radio_kolay);
        radio_orta = findViewById(R.id.radio_orta);
        radio_zor = findViewById(R.id.radio_zor);
        image_settings = findViewById(R.id.image_settings);
        sounds = new Sounds(this);

        stateMusic = preferences.getBoolean("MusicEnable",true);
        stateSound = preferences.getBoolean("SoundEnable",true);
        zorluk = preferences.getInt("Zorluk",2);

        switch_music.setChecked(stateMusic);
        switch_sound.setChecked(stateSound);

        switch (zorluk) {
            case 0:
                radio_zor.setChecked(true);
                break;
            case 1:
                radio_orta.setChecked(true);
                break;
            case 2:
                radio_kolay.setChecked(true);
                break;
        }
        radio_kolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_kolay.setChecked(true);
                if(stateSound) sounds.PlaySound(Sounds.Musics.selected_sound);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Zorluk",2);
                editor.apply();
            }
        });
        radio_orta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_orta.setChecked(true);
                if(stateSound) sounds.PlaySound(Sounds.Musics.selected_sound);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Zorluk",1);
                editor.apply();
            }
        });
        radio_zor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_zor.setChecked(true);
                if(stateSound) sounds.PlaySound(Sounds.Musics.selected_sound);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Zorluk",0);
                editor.apply();
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/blackspace.ttf");

        switch_sound.setTypeface(typeface);
        switch_music.setTypeface(typeface);

        settings_animation_r = AnimationUtils.loadAnimation(Settings.this, R.anim.settings_animation_r);
        settings_animation_r.setFillAfter(true);

        settings_animation_l = AnimationUtils.loadAnimation(Settings.this, R.anim.settings_animation_l);
        settings_animation_l.setFillAfter(true);


        switch_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSound = !stateSound;
                switch_sound.setChecked(stateSound);
                if(stateSound) {
                    sounds.PlaySound(Sounds.Musics.selected_sound);
                    image_settings.startAnimation(settings_animation_r);
                }
                else image_settings.startAnimation(settings_animation_l);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("SoundEnable",stateSound);
                editor.apply();
            }
        });
        switch_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_settings.startAnimation(settings_animation_r);
                stateMusic = !stateMusic;
                switch_music.setChecked(stateMusic);

                if(stateMusic) image_settings.startAnimation(settings_animation_r);
                else image_settings.startAnimation(settings_animation_l);

                if(stateSound) sounds.PlaySound(Sounds.Musics.selected_sound);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("MusicEnable",stateMusic);
                editor.apply();
            }
        });
    }
}
