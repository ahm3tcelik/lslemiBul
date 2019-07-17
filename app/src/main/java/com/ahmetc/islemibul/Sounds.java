package com.ahmetc.islemibul;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class Sounds {
    private static SoundPool soundPool;
    private static int sound_button;
    private static int sound_true;
    private static int sound_false;
    private static int sound_selected;
    private static int sound_levelup;
    private static int sound_pass;
    private static int sound_hint;
    private static int sound_win;
    private static int sound_lose;
    private static int sound_cd;

    public enum Musics {
        button_sound,
        true_sound,
        false_sound,
        selected_sound,
        levelup_sound,
        pass_sound,
        hint_sound,
        win_sound,
        lose_sound,
        cd_sound
    }
    public Sounds(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(10)
                    .build();
        }
        else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }
        sound_button = soundPool.load(context, R.raw.sound_button, 1);
        sound_true = soundPool.load(context, R.raw.sound_true,1);
        sound_false = soundPool.load(context, R.raw.sound_false,1);
        sound_selected = soundPool.load(context, R.raw.sound_selected,1);
        sound_levelup = soundPool.load(context, R.raw.sound_levelup,1);
        sound_pass = soundPool.load(context, R.raw.sound_pass,1);
        sound_hint = soundPool.load(context, R.raw.sound_hint,1);
        sound_win = soundPool.load(context, R.raw.sound_winner,1);
        sound_lose = soundPool.load(context, R.raw.sound_loser,1);
        sound_cd = soundPool.load(context, R.raw.cd_sec,1);
    }
    public void PlaySound(Musics music) {
        switch (music)
        {
            case button_sound: {
                soundPool.play(sound_button,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case true_sound: {
                soundPool.play(sound_true,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case false_sound: {
                soundPool.play(sound_false,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case selected_sound: {
                soundPool.play(sound_selected,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case levelup_sound: {
                soundPool.play(sound_levelup,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case pass_sound: {
                soundPool.play(sound_pass,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case hint_sound: {
                soundPool.play(sound_hint,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case win_sound: {
                soundPool.play(sound_win,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case lose_sound: {
                soundPool.play(sound_lose,1.0f,1.0f,1,0,1.0f);
                break;
            }
            case cd_sound: {
                soundPool.play(sound_cd,1.0f,1.0f,1,0,1.0f);
                break;
            }
            default:break;
        }
    }
    public void StopSound(Musics music) {
        switch (music)
        {
            case button_sound: {
                soundPool.stop(sound_button);
                break;
            }
            case true_sound: {
                soundPool.stop(sound_true);
                break;
            }
            case false_sound: {
                soundPool.stop(sound_false);
                break;
            }
            case selected_sound: {
                soundPool.stop(sound_selected);
                break;
            }
            case levelup_sound: {
                soundPool.stop(sound_levelup);
                break;
            }
            case pass_sound: {
                soundPool.stop(sound_pass);
                break;
            }
            case hint_sound: {
                soundPool.stop(sound_hint);
                break;
            }
            case cd_sound: {
                soundPool.stop(sound_cd);
                break;
            }
            default:break;
        }
    }
}
