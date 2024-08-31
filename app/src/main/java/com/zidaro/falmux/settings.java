package com.zidaro.falmux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.Objects;

public class settings extends AppCompatActivity implements View.OnClickListener {
    Switch soundSwitch;
    Switch vibroSwitch;
    ImageButton but;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Objects.requireNonNull(getWindow().getDecorView().getWindowInsetsController()).hide(WindowInsets.Type.statusBars());
        }
        setContentView(R.layout.activity_settings);

        //Выключение системного звука
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        soundSwitch = findViewById(R.id.switchSound);
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);
        soundSwitch.setChecked(isSoundEnabled);

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SoundAndVibration();
                editor.putBoolean("sound_enabled", isChecked);
                editor.apply();
            }
        });

        vibroSwitch = findViewById(R.id.switchVibro);
        boolean isVibroEnabled = sharedPreferences.getBoolean("vibro_enabled", true);
        vibroSwitch.setChecked(isVibroEnabled);

        vibroSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SoundAndVibration();
                editor.putBoolean("vibro_enabled", isChecked);
                editor.apply();
            }
        });

        but = findViewById(R.id.backButton);
        but.setOnClickListener(this);
    }

    public void SoundAndVibration() {
        if (sharedPreferences.getBoolean("vibro_enabled", true)) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        }

        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(settings.this, R.raw.click);
            mediaPlayer.setVolume(0.4f, 0.4f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        SoundAndVibration();
        if (view.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundAndVibration();
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}