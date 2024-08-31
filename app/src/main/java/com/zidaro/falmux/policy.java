package com.zidaro.falmux;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class policy extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        findViewById(R.id.backButton).setOnClickListener(this);
        WebView policy = findViewById(R.id.policyText);
        policy.loadUrl("https://telegra.ph/Magic-Stones-06-07");
    }

    @Override
    public void onClick(View view) {
        SoundAndVibration();
        if (view.getId() == R.id.backButton) {
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
            MediaPlayer mediaPlayer = MediaPlayer.create(policy.this, R.raw.click);
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