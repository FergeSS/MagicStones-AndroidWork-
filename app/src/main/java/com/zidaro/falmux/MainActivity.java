package com.zidaro.falmux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton start;
    ImageButton policy;
    ImageButton settings;
    ImageButton exit;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        start = findViewById(R.id.buttonStart);
        policy = findViewById(R.id.buttonPolicy);
        settings = findViewById(R.id.buttonSettings);
        exit = findViewById(R.id.buttonExit);

        start.setOnClickListener(this);
        policy.setOnClickListener(this);
        settings.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SoundAndVibration();
        if (view.getId() == R.id.backButton) {
            finish();
        }
        Intent intent;
        if (view.getId() == R.id.buttonStart) {
            intent = new Intent(this, game.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.buttonPolicy) {
            intent = new Intent(this, policy.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.buttonSettings) {
            intent = new Intent(this, settings.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.buttonExit) {
            finish();
        }
    }
    public void SoundAndVibration() {
        if (sharedPreferences.getBoolean("vibro_enabled", true)) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        }

        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.click);
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