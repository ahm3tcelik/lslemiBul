package com.ahmetc.islemibul;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Sounds sounds;
    private Calculate calculate = new Calculate();
    private Dialog dialog_ipucu, dialog_pes, dialog_pas, dialog_cd, dialog_levelup;
    private PopupMenu popupIslem;

    private View view;
    private ImageView ipucu, gonder, pes, pas;
    private ImageView islem_1, islem_2, islem_3, islem_4;
    private TextView sayi_1, sayi_2, sayi_3, sayi_4, sayi_5;
    private TextView text_ipucu, text_pas, text_seviye, text_skor, text_gerisayim;
    private Chronometer chronometer;
    private CountDownTimer countDownTimer;
    private long timeLeftinMilis;
    private boolean cdIsRunning, completed = false;

    /************* GAME CONFIG ***********************/

    private int[]Answer_ScoresT = {
            8,  // Seviye 1 : Doğru Cevap Skoru
            12,  // Seviye 2 : Doğru Cevap Skoru
            18, // Seviye 3: Doğru Cevap Skoru
            26, // Seviye 4: Doğru Cevap Skoru
    };
    private int[]Answer_ScoresF = {
            3,  // Seviye 1 : Yanlış Cevap - Skoru
            4,  // Seviye 2 : Yanlış - Cevap Skoru
            5,  // Seviye 3: Yanlış Cevap - Skoru
            9   // Seviye 4 : Yanluş Cevap - Skoru
    };
    private int[]AwardsI = {
            2, // Seviye 1 Up : + İpucu hakkı
            5, // Seviye 2 Up : + İpucu hakkı
            9, // Seviye 3 Up : + İpucu hakkı
            2  // Seviye 4    :  + İpucu Hakkı (Her 3 doğru soruda bir)
    };
    private int[]AwardsP = {
            2, // Seviye 1 Up : + Pas hakkı
            5, // Seviye 2 Up : + Pas hakkı
            9, // Seviye 3 Up :  + Pas hakkı
            2  // Seviye 4    :  + İpucu Hakkı (Her 3 doğru soruda bir)
    };
    private int[] Level_MaxScores = {
            79, // Seviye1 Max Skor
            189, // Seviye2 Max Skor
            300, // Seviye3 Max Skor
            999999

    };
    private int[][] Level_ZorlukCountDown = {
            {4, 8, 12},
            {12, 16, 4},
            {25, 33, 41},
            {40,48,56}
    };
    private int sayilar[] = new int[5];
    private char islemler[] = {'+', '-', '*'};
    private char cevap[] = new char[4];
    private int sonuc = 0, maxReward = 0;
    private int MAXREWARD = 3; // Maximum reward reklam gosterme sayisi

    /***************   END  ********************

    /******** User Configs ********************/

    private int puan = 0;
    private int hak_ipucu = 5;
    private int hak_pas = 5;
    private int currentSeviye = 1;
    private int odulSoru = 0;
    private char g_cevap[] = new char[4];
    private boolean sound_enable, music_enable;
    private int zorluk;
    private int alreadyIpucu;
    private long lastPause;

    /******************   END  *******************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        sound_enable = preferences.getBoolean("SoundEnable", true);
        music_enable = preferences.getBoolean("MusicEnable", true);
        zorluk = preferences.getInt("Zorluk",2);
        if (sounds == null && sound_enable) sounds = new Sounds(MainActivity.this);
        startChronometer();
        soruHazirlik();

        /* Button Handles */
        islem_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupIslem = new PopupMenu(MainActivity.this, islem_1);
                popupIslem.getMenuInflater().inflate(R.menu.cevap_tasarim, popupIslem.getMenu());

                popupIslem.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                        switch (item.getItemId()) {
                            case R.id.action_toplama:
                                islem_1.setImageResource(R.mipmap.arti);
                                g_cevap[0] = '+';
                                return true;

                            case R.id.action_cikarma:
                                islem_1.setImageResource(R.mipmap.eksi);
                                g_cevap[0] = '-';
                                return true;

                            case R.id.action_carpma:
                                islem_1.setImageResource(R.mipmap.carpi);
                                g_cevap[0] = '*';
                                return true;
                        }
                        return false;
                    }
                });
                popupIslem.show();
            }
        });
        islem_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupIslem = new PopupMenu(MainActivity.this, islem_2);
                popupIslem.getMenuInflater().inflate(R.menu.cevap_tasarim, popupIslem.getMenu());
                popupIslem.show();

                popupIslem.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                        switch (item.getItemId()) {
                            case R.id.action_toplama:
                                islem_2.setImageResource(R.mipmap.arti);
                                g_cevap[1] = '+';
                                return true;

                            case R.id.action_cikarma:
                                islem_2.setImageResource(R.mipmap.eksi);
                                g_cevap[1] = '-';
                                return true;

                            case R.id.action_carpma:
                                islem_2.setImageResource(R.mipmap.carpi);
                                g_cevap[1] = '*';
                                return true;
                        }
                        return false;
                    }
                });
            }
        });
        islem_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupIslem = new PopupMenu(MainActivity.this, islem_3);
                popupIslem.getMenuInflater().inflate(R.menu.cevap_tasarim, popupIslem.getMenu());
                popupIslem.show();

                popupIslem.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                        switch (item.getItemId()) {
                            case R.id.action_toplama:
                                islem_3.setImageResource(R.mipmap.arti);
                                g_cevap[2] = '+';
                                return true;

                            case R.id.action_cikarma:
                                islem_3.setImageResource(R.mipmap.eksi);
                                g_cevap[2] = '-';
                                return true;

                            case R.id.action_carpma:
                                islem_3.setImageResource(R.mipmap.carpi);
                                g_cevap[2] = '*';
                                return true;
                        }
                        return false;
                    }
                });
            }
        });
        islem_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupIslem = new PopupMenu(MainActivity.this, islem_4);
                popupIslem.getMenuInflater().inflate(R.menu.cevap_tasarim, popupIslem.getMenu());
                popupIslem.show();

                popupIslem.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                        switch (item.getItemId()) {
                            case R.id.action_toplama:
                                islem_4.setImageResource(R.mipmap.arti);
                                g_cevap[3] = '+';
                                return true;

                            case R.id.action_cikarma:
                                islem_4.setImageResource(R.mipmap.eksi);
                                g_cevap[3] = '-';
                                return true;

                            case R.id.action_carpma:
                                islem_4.setImageResource(R.mipmap.carpi);
                                g_cevap[3] = '*';
                                return true;
                        }
                        return false;
                    }
                });
            }
        });
        ipucu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                if (currentSeviye <= alreadyIpucu)
                    Toast.makeText(MainActivity.this, getString(R.string.alreadyIpucuOK), Toast.LENGTH_SHORT).show();
                else if (hak_ipucu > 0) {
                    // Onaylama Sorusu
                    dialog_ipucu = new Dialog(MainActivity.this);

                    dialog_ipucu.setContentView(R.layout.custom_popup);
                    TextView popupText = dialog_ipucu.findViewById(R.id.popupText);
                    Button popupPositive = dialog_ipucu.findViewById(R.id.popupPositive);
                    Button popupNegative = dialog_ipucu.findViewById(R.id.popupNegative);
                    ImageView popupImage = dialog_ipucu.findViewById(R.id.popupImage);
                    ImageView popupExit = dialog_ipucu.findViewById(R.id.popupExit);

                    popupText.setText(getString(R.string.ipucuOnay));
                    popupImage.setImageResource(R.drawable.ipucu);

                    popupExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_ipucu.dismiss();
                        }
                    });
                    popupNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_ipucu.dismiss();
                        }
                    });
                    popupPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sound_enable) sounds.PlaySound(Sounds.Musics.hint_sound);
                            hak_ipucu--;
                            alreadyIpucu++;
                            text_ipucu.setText(getString(R.string.ipucuHakki).concat(" : ").concat(String.valueOf(hak_ipucu)));
                            islemAc();
                            dialog_ipucu.dismiss();
                            Toast.makeText(MainActivity.this, getString(R.string.ipucuOK), Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog_ipucu.show();
                } else
                    Toast.makeText(MainActivity.this, getString(R.string.yetersizIpucu), Toast.LENGTH_SHORT).show();
            }
        });
        pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                if (hak_pas > 0) {
                    // Onaylama Sorusu
                    dialog_pas = new Dialog(MainActivity.this);

                    dialog_pas.setContentView(R.layout.custom_popup);
                    TextView popupText = dialog_pas.findViewById(R.id.popupText);
                    Button popupPositive = dialog_pas.findViewById(R.id.popupPositive);
                    Button popupNegative = dialog_pas.findViewById(R.id.popupNegative);
                    ImageView popupImage = dialog_pas.findViewById(R.id.popupImage);
                    ImageView popupExit = dialog_pas.findViewById(R.id.popupExit);

                    popupText.setText(getString(R.string.pasOnay));
                    popupImage.setImageResource(R.drawable.pas);

                    popupExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_pas.dismiss();
                        }
                    });
                    popupNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_pas.dismiss();
                        }
                    });
                    popupPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sound_enable) sounds.PlaySound(Sounds.Musics.pass_sound);
                            hak_pas--;
                            text_pas.setText(getString(R.string.atlamaHakki).concat(" : ").concat(String.valueOf(hak_pas)));
                            pauseGerisayim();
                            soruHazirlik();
                            dialog_pas.dismiss();
                            Toast.makeText(MainActivity.this, getString(R.string.pasOK), Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog_pas.show();
                } else
                    Toast.makeText(MainActivity.this, getString(R.string.yetersizPas), Toast.LENGTH_SHORT).show();
            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Cevabi Gonderdiginde
                boolean isEmpty = false;
                for (int i = 0; i < currentSeviye; i++) {
                    if (g_cevap[i] == ' ')
                        isEmpty = true;
                }
                if (isEmpty) Toast.makeText(MainActivity.this, getString(R.string.islemSec), Toast.LENGTH_SHORT).show();
                else if (isTrue()) { // Cevap Dogruysa

                    pauseGerisayim();
                    puan += Answer_ScoresT[currentSeviye-1]- 3*zorluk;

                    if(currentSeviye == 4) {
                        odulSoru++;
                        if(odulSoru == 3) {

                            hak_pas += AwardsI[currentSeviye-1];
                            hak_ipucu += AwardsP[currentSeviye-1];
                            Snackbar.make(view, getString(R.string.awards3tebir)+AwardsI[currentSeviye-1] + getString(R.string.ipucu) + ", " + AwardsP[currentSeviye-1] + getString(R.string.pas),Snackbar.LENGTH_LONG).show();
                            odulSoru = 0;
                        }
                    }
                        if (puan >= Level_MaxScores[currentSeviye - 1]) { // LevelUp
                            if (sound_enable) sounds.PlaySound(Sounds.Musics.levelup_sound);
                            hak_pas += AwardsP[currentSeviye - 1];
                            hak_ipucu += AwardsI[currentSeviye - 1];
                            LevelUp();
                    }
                    else {
                        if(sound_enable)sounds.StopSound(Sounds.Musics.cd_sound);
                        if (sound_enable) sounds.PlaySound(Sounds.Musics.true_sound);
                        soruHazirlik();
                    }
                }
                else {
                    if (sound_enable) sounds.PlaySound(Sounds.Musics.false_sound);

                    puan -= Answer_ScoresF[currentSeviye-1];
                    if(puan < 0) puan = 0;

                    for (int i = 0; i < currentSeviye; i++)
                        g_cevap[i] = ' ';
                    islem_1.setImageResource(R.mipmap.soru);
                    islem_2.setImageResource(R.mipmap.soru);
                    islem_3.setImageResource(R.mipmap.soru);
                    islem_4.setImageResource(R.mipmap.soru);

                    text_skor.setText(getString(R.string.skor).concat(" : ").concat(String.valueOf(puan)));
                }
            }
        });
        pes.setOnClickListener(new View.OnClickListener() { // Oyunu sonlandır
            @Override
            public void onClick(View v) {
                if (sound_enable) sounds.PlaySound(Sounds.Musics.selected_sound);
                // Onaylama Sorusu
                dialog_pes = new Dialog(MainActivity.this);

                dialog_pes.setContentView(R.layout.custom_popup);
                TextView popupText = dialog_pes.findViewById(R.id.popupText);
                Button popupPositive = dialog_pes.findViewById(R.id.popupPositive);
                Button popupNegative = dialog_pes.findViewById(R.id.popupNegative);
                ImageView popupImage = dialog_pes.findViewById(R.id.popupImage);
                ImageView popupExit = dialog_pes.findViewById(R.id.popupExit);

                popupText.setText(getString(R.string.pesOnay));
                popupImage.setImageResource(R.drawable.peset);

                popupExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_pes.dismiss();
                    }
                });
                popupNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_pes.dismiss();
                    }
                });
                popupPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_pes.dismiss();
                        Intent intent = new Intent(MainActivity.this, Gameover.class);
                        intent.putExtra("time",chronometer.getText());
                        intent.putExtra("puan",puan);
                        pauseGerisayim();
                        pauseChronometer();
                        resetChronometer();
                        startActivity(intent);
                        finish();

                    }
                });
                dialog_pes.show();
            }
        });
    }
    public boolean isTrue() {
        int ss = 0;
        switch (currentSeviye) {
            case 1:
                ss = calculate.Sonuc(String.valueOf(sayilar[0]).concat(String.valueOf(g_cevap[0])).concat(String.valueOf(sayilar[1])));
                break;
            case 2:
                ss = calculate.Sonuc(String.valueOf(sayilar[0]).concat(String.valueOf(g_cevap[0])).concat(String.valueOf(sayilar[1])).concat(String.valueOf(g_cevap[1])).concat(String.valueOf(sayilar[2])));
                break;
            case 3:
                ss = calculate.Sonuc(String.valueOf(sayilar[0]).concat(String.valueOf(g_cevap[0])).concat(String.valueOf(sayilar[1])).concat(String.valueOf(g_cevap[1])).concat(String.valueOf(sayilar[2])).concat(String.valueOf(g_cevap[2])).concat(String.valueOf(sayilar[3])));
                break;
            case 4:
                ss = calculate.Sonuc(String.valueOf(sayilar[0]).concat(String.valueOf(g_cevap[0])).concat(String.valueOf(sayilar[1])).concat(String.valueOf(g_cevap[1])).concat(String.valueOf(sayilar[2])).concat(String.valueOf(g_cevap[2])).concat(String.valueOf(sayilar[3])).concat(String.valueOf(g_cevap[3])).concat(String.valueOf(sayilar[4])));
                break;
        }
        return (ss == sonuc);
    }
    public void soruHazirlik() {
        soruSor();
        startGeriSayim();
        alreadyIpucu = 0;
        switch (currentSeviye) {
            case 1:
                sayi_1.setText(String.valueOf(sayilar[0]));
                sayi_2.setText(String.valueOf(sayilar[1]).concat(" = ").concat(String.valueOf(sonuc)));
                islem_1.setImageResource(R.mipmap.soru);
                sayi_3.setVisibility(View.GONE);
                sayi_4.setVisibility(View.GONE);
                sayi_5.setVisibility(View.GONE);
                islem_2.setVisibility(View.GONE);
                islem_3.setVisibility(View.GONE);
                islem_4.setVisibility(View.GONE);
                break;
            case 2:
                islem_2.setVisibility(View.VISIBLE);
                sayi_3.setVisibility(View.VISIBLE);
                sayi_1.setText(String.valueOf(sayilar[0]));
                sayi_2.setText(String.valueOf(sayilar[1]));
                sayi_3.setText(String.valueOf(sayilar[2]).concat(" = ").concat(String.valueOf(sonuc)));
                islem_1.setImageResource(R.mipmap.soru);
                islem_2.setImageResource(R.mipmap.soru);
                sayi_4.setVisibility(View.GONE);
                sayi_5.setVisibility(View.GONE);
                islem_3.setVisibility(View.GONE);
                islem_4.setVisibility(View.GONE);
                break;
            case 3:
                islem_3.setVisibility(View.VISIBLE);
                sayi_4.setVisibility(View.VISIBLE);
                sayi_1.setText(String.valueOf(sayilar[0]));
                sayi_2.setText(String.valueOf(sayilar[1]));
                sayi_3.setText(String.valueOf(sayilar[2]));
                sayi_4.setText(String.valueOf(sayilar[3]).concat(" = ").concat(String.valueOf(sonuc)));
                islem_1.setImageResource(R.mipmap.soru);
                islem_2.setImageResource(R.mipmap.soru);
                islem_3.setImageResource(R.mipmap.soru);
                sayi_5.setVisibility(View.GONE);
                islem_4.setVisibility(View.GONE);
                break;
            case 4:
                islem_4.setVisibility(View.VISIBLE);
                sayi_5.setVisibility(View.VISIBLE);
                sayi_1.setText(String.valueOf(sayilar[0]));
                sayi_2.setText(String.valueOf(sayilar[1]));
                sayi_3.setText(String.valueOf(sayilar[2]));
                sayi_4.setText(String.valueOf(sayilar[3]));
                sayi_5.setText(String.valueOf(sayilar[4]).concat(" = ").concat(String.valueOf(sonuc)));
                islem_1.setImageResource(R.mipmap.soru);
                islem_2.setImageResource(R.mipmap.soru);
                islem_3.setImageResource(R.mipmap.soru);
                islem_4.setImageResource(R.mipmap.soru);
                break;
        }

        text_skor.setText(getString(R.string.skor).concat(" : ").concat(String.valueOf(puan)));
        text_seviye.setText(getString(R.string.seviye).concat(" : ").concat(String.valueOf(currentSeviye)));
        text_pas.setText(getString(R.string.atlamaHakki).concat(" : ").concat(String.valueOf(hak_pas)));
        text_ipucu.setText(getString(R.string.ipucuHakki).concat(" : ").concat(String.valueOf(hak_ipucu)));

        for (int i = 0; i < currentSeviye; i++) {
            g_cevap[i] = ' ';
            Log.e("islem", String.valueOf(cevap[i]));
        }
        Log.e("islem2", "soru end");
    }
    public void soruSor() {
        Random rand = new Random();
        for (int i=0; i<currentSeviye+1; i++) sayilar[i] = rand.nextInt(20)+1;
        for (int j=0; j<currentSeviye; j++) cevap[j] = islemler[Math.abs(rand.nextInt()%3)];

        switch (currentSeviye) {
            case 1: {
                sonuc = calculate.Sonuc(String.valueOf(sayilar[0]) + cevap[0] + String.valueOf(sayilar[1]));
                break;
            }
            case 2: {
                sonuc = calculate.Sonuc(String.valueOf(sayilar[0]) + cevap[0] + String.valueOf(sayilar[1]) + cevap[1] + String.valueOf(sayilar[2]));
                break;
            }
            case 3: {
                sonuc = calculate.Sonuc(String.valueOf(sayilar[0]) + cevap[0] + String.valueOf(sayilar[1]) + cevap[1] + String.valueOf(sayilar[2]) + cevap[2] + sayilar[3]);
                break;
            }
            case 4: {
                sonuc = calculate.Sonuc(String.valueOf(sayilar[0]) + cevap[0] + String.valueOf(sayilar[1]) + cevap[1] + String.valueOf(sayilar[2]) + cevap[2] + sayilar[3] + cevap[3] + sayilar[4]);
                break;
            }
            default:break;
        }
    }
    public void islemAc() {
        Random rand = new Random();
        int i = Math.abs(rand.nextInt(currentSeviye));
        Log.e("islemac", String.valueOf(i) + String.valueOf(cevap[i]));
        g_cevap[i] = cevap[i];

        switch(i) {
            case 0:
                switch (cevap[i]) {
                    case '+':
                        islem_1.setImageResource(R.mipmap.arti);
                        break;
                    case '-':
                        islem_1.setImageResource(R.mipmap.eksi);
                        break;
                    case '*':
                        islem_1.setImageResource(R.mipmap.carpi);
                        break;
                }
                break;
            case 1:
                switch (cevap[i]) {
                    case '+':
                        islem_2.setImageResource(R.mipmap.arti);
                        break;
                    case '-':
                        islem_2.setImageResource(R.mipmap.eksi);
                        break;
                    case '*':
                        islem_2.setImageResource(R.mipmap.carpi);
                        break;
                }
                break;
            case 2:
                switch (cevap[i]) {
                    case '+':
                        islem_3.setImageResource(R.mipmap.arti);
                        break;
                    case '-':
                        islem_3.setImageResource(R.mipmap.eksi);
                        break;
                    case '*':
                        islem_3.setImageResource(R.mipmap.carpi);
                        break;
                }
                break;
            case 3:
                switch (cevap[i]) {
                    case '+':
                        islem_4.setImageResource(R.mipmap.arti);
                        break;
                    case '-':
                        islem_4.setImageResource(R.mipmap.eksi);
                        break;
                    case '*':
                        islem_4.setImageResource(R.mipmap.carpi);
                        break;
                }
                break;
        }
    }
    public void updateCdText() {
        //int minutes = (int) (timeLeftinMilis / 1000) / 60;
        int seconds = (int) (timeLeftinMilis /1000);
        String cdFormat = String.format(Locale.getDefault(),"%02d",seconds);
        if(seconds < 5) text_gerisayim.setTextColor(getResources().getColor(R.color.kirmizi2));
        else text_gerisayim.setTextColor(getResources().getColor(R.color.colorPrimary));
        text_gerisayim.setText(cdFormat);

    }
    public void startGeriSayim() {

        timeLeftinMilis = Level_ZorlukCountDown[currentSeviye-1][zorluk] *1000;
        if(music_enable)sounds.PlaySound(Sounds.Musics.cd_sound);
        countDownTimer = new CountDownTimer(timeLeftinMilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMilis = millisUntilFinished;
                if(music_enable)sounds.PlaySound(Sounds.Musics.cd_sound);

                int seconds = (int) (timeLeftinMilis /1000);
                String cdFormat = String.format(Locale.getDefault(),"%02d",seconds);
                if(seconds < 5) text_gerisayim.setTextColor(getResources().getColor(R.color.kirmizi2));
                else text_gerisayim.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_gerisayim.setText(cdFormat);
            }

            @Override
            public void onFinish() {  //Sure Doldu

                if(music_enable)sounds.PlaySound(Sounds.Musics.false_sound);
                timeLeftinMilis = 0;
                updateCdText();
                cdIsRunning = false;
                pauseGerisayim();
                pauseChronometer();
                // Game Over POPUP

                dialog_cd = new Dialog(MainActivity.this);
                dialog_cd.show();
                final Intent intent = new Intent(MainActivity.this, Gameover.class);
                intent.putExtra("time",chronometer.getText());
                intent.putExtra("puan",puan);

                dialog_cd.setContentView(R.layout.custom_popup);
                TextView popupText = dialog_cd.findViewById(R.id.popupText);
                Button popupPositive = dialog_cd.findViewById(R.id.popupPositive);
                Button popupNegative = dialog_cd.findViewById(R.id.popupNegative);
                ImageView popupImage = dialog_cd.findViewById(R.id.popupImage);
                ImageView popupExit = dialog_cd.findViewById(R.id.popupExit);
                popupPositive.setVisibility(View.GONE);
                popupText.setText(getString(R.string.sureBitti));
                popupNegative.setText(getString(R.string.ok));
                popupImage.setImageResource(R.drawable.gerisayim);

                popupExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_cd.dismiss();
                        resetChronometer();
                        startActivity(intent);
                        finish();
                    }
                });
                popupNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_cd.dismiss();
                        resetChronometer();
                        startActivity(intent);
                        finish();
                    }
                });
                dialog_cd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                       dialog_cd.dismiss();
                        resetChronometer();
                       startActivity(intent);
                       finish();
                    }
                });
                countDownTimer = null;
            }
        }.start();
        cdIsRunning = true;
    }
    public void pauseGerisayim() {
        countDownTimer.cancel();
        cdIsRunning = false;

    }
    public void resetChronometer() {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        lastPause = 0;
    }
    public void startChronometer() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/blackspace.ttf");
        chronometer.setTypeface(typeface);
        if(lastPause != 0) chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
        else chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
    public void pauseChronometer() {
        lastPause = SystemClock.elapsedRealtime();
        chronometer.stop();
    }
    public void init() {
        view = findViewById(R.id.constraintLayout);
        ipucu = findViewById(R.id.ipucu);
        gonder = findViewById(R.id.gonder);
        pes = findViewById(R.id.pes);
        pas = findViewById(R.id.pas);
        islem_1 = findViewById(R.id.islem_1);
        islem_2 = findViewById(R.id.islem_2);
        islem_3 = findViewById(R.id.islem_3);
        islem_4 = findViewById(R.id.islem_4);
        sayi_1 = findViewById(R.id.sayi_1);
        sayi_2 = findViewById(R.id.sayi_2);
        sayi_3 = findViewById(R.id.sayi_3);
        sayi_4 = findViewById(R.id.sayi_4);
        sayi_5 = findViewById(R.id.sayi_5);
        text_ipucu = findViewById(R.id.text_ipucu);
        text_pas = findViewById(R.id.text_pas);
        text_seviye = findViewById(R.id.text_seviye);
        text_skor = findViewById(R.id.text_skor);
        chronometer = findViewById(R.id.chronometer);
        text_gerisayim = findViewById(R.id.text_gerisayim);
    }
    /*
    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        sound_enable = preferences.getBoolean("SoundEnable",true);
        music_enable = preferences.getBoolean("MusicEnable",true);

        if(sounds == null)sounds = new Sounds(this);

        if(mediaPlayer == null && music_enable) {
            mediaPlayer = MediaPlayer.create(this,R.raw.sound_gamemenu);
        }
        if(music_enable) {
            mediaPlayer.start();
        }
    }*/
    @Override
    public void onPause() {
        super.onPause();
        //if(mediaPlayer != null && music_enable) {//mediaPlayer.pause();}
        if(dialog_cd != null)dialog_cd.dismiss();
        if(dialog_pes != null)dialog_pes.dismiss();
        if(dialog_pas != null)dialog_pas.dismiss();
        if(dialog_levelup != null)dialog_levelup.dismiss();
        if(dialog_ipucu != null) dialog_ipucu.dismiss();
        if(popupIslem != null) popupIslem.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(cdIsRunning)pauseGerisayim();
    }
    public void LevelUp() {

        dialog_levelup = new Dialog(MainActivity.this);

        dialog_levelup.setContentView(R.layout.custom_popup);
        TextView popupText = dialog_levelup.findViewById(R.id.popupText);
        Button popupPositive = dialog_levelup.findViewById(R.id.popupPositive);
        Button popupNegative = dialog_levelup.findViewById(R.id.popupNegative);
        ImageView popupImage = dialog_levelup.findViewById(R.id.popupImage);
        ImageView popupExit = dialog_levelup.findViewById(R.id.popupExit);

        popupText.setText(getString(R.string.levelUp));
        popupImage.setImageResource(R.drawable.tebrikler);
        popupPositive.setVisibility(View.GONE);
        popupNegative.setText(getString(R.string.levelupOK));

        popupExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_levelup.dismiss();
                soruHazirlik();
                Snackbar.make(view, getString(R.string.award).concat(" ").concat(String.valueOf(AwardsI[currentSeviye - 1]).concat(" ").concat(getString(R.string.ipucu).concat(", ").concat(String.valueOf(AwardsP[currentSeviye - 1]).concat(" ").concat(getString(R.string.pas))))), Snackbar.LENGTH_LONG).show();
            }
        });
        popupNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_levelup.dismiss();
                soruHazirlik();
                Snackbar.make(view, getString(R.string.award).concat(String.valueOf(AwardsI[currentSeviye - 1]).concat(" ").concat(getString(R.string.ipucu).concat(", ").concat(String.valueOf(AwardsP[currentSeviye - 1]).concat(" ").concat(getString(R.string.pas))))), Snackbar.LENGTH_LONG).show();
            }
        });
        dialog_levelup.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                soruHazirlik();
                Snackbar.make(view, getString(R.string.award).concat(String.valueOf(AwardsI[currentSeviye - 1]).concat(" ").concat(getString(R.string.ipucu).concat(", ").concat(String.valueOf(AwardsP[currentSeviye - 1]).concat(" ").concat(getString(R.string.pas))))), Snackbar.LENGTH_LONG).show();
            }
        });
        currentSeviye++;
        dialog_levelup.show();
    }
}
