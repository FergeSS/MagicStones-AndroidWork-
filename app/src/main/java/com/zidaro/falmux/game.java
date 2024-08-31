package com.zidaro.falmux;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class game extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    SharedPreferences sharedPreferences;
    ImageSwitcher fire1Left;
    ImageSwitcher fire2Left;
    ImageSwitcher fire1Right;
    ImageSwitcher fire2Right;
    ImageButton buttonCont;
    ImageButton stone1;
    ImageButton stone2;
    ImageButton stone3;
    ImageButton checkAgain;
    ImageButton continueBut;
    int[] answers = {R.drawable.yes, R.drawable.no, R.drawable.possible};
    Random rand = new Random();
    ImageView text;
    ViewGroup parentLayout;
    View customView;
    View customView1;
    View customView2;
    int counter = 0;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        int[] fire1frames = {R.drawable.fire, R.drawable.fire_1, R.drawable.fire_2};
        int[] fire2frames = {R.drawable.fire2, R.drawable.fire2_1, R.drawable.fire2_2};

        fire1Left = new ImageSwitcher(findViewById(R.id.fire1Left), fire1frames);
        fire2Left = new ImageSwitcher(findViewById(R.id.fire2Left), fire2frames);
        fire1Right = new ImageSwitcher(findViewById(R.id.fire1Right), fire1frames);
        fire2Right = new ImageSwitcher(findViewById(R.id.fire2Right), fire2frames);

        fire1Left.startSwitching();
        fire2Left.startSwitching();
        fire1Right.startSwitching();
        fire2Right.startSwitching();

        findViewById(R.id.backButton).setOnClickListener(this);

        parentLayout = findViewById(R.id.gameLayout);

        LayoutInflater inflater = getLayoutInflater();
        customView = inflater.inflate(R.layout.option, parentLayout, false);
        customView1 = inflater.inflate(R.layout.stones, parentLayout, false);
        customView2 = inflater.inflate(R.layout.answer, parentLayout, false);

        parentLayout.addView(customView);

        buttonCont = findViewById(R.id.buttonContinue);
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttonCont.setOnClickListener(game.this);
                buttonCont.setClickable(true);
                buttonCont.setVisibility(View.VISIBLE);
                SoundAndVibration();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN && (v.getId() == R.id.stone1 || v.getId() == R.id.stone2 || v.getId() == R.id.stone3)) {
            SoundAndVibration();
            v.animate().scaleXBy(0.1f).setDuration(100).start();
            v.animate().scaleYBy(0.1f).setDuration(100).start();
            return true;
        } else if (action == MotionEvent.ACTION_UP && (v.getId() == R.id.stone1 || v.getId() == R.id.stone2 || v.getId() == R.id.stone3)) {
            counter = 0;
            parentLayout.removeView(customView1);
            parentLayout.addView(customView2);

            text = findViewById(R.id.answer);
            checkAgain = findViewById(R.id.checkAgainButton);
            continueBut = findViewById(R.id.continueButton);

            checkAgain.setOnClickListener(this);
            continueBut.setOnClickListener(this);

            text.setImageResource(answers[rand.nextInt(3)]);
            v.animate().cancel();
            v.animate().scaleX(1f).setDuration(300).start();
            v.animate().scaleY(1f).setDuration(300).start();
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        SoundAndVibration();
        if (view.getId() == R.id.backButton) {
            finish();
        }
        if (view.getId() == R.id.buttonContinue) {
            parentLayout.removeView(customView);
            parentLayout.addView(customView1);
            stone1 = findViewById(R.id.stone1);
            stone2 = findViewById(R.id.stone2);
            stone3 = findViewById(R.id.stone3);

            stone1.setOnTouchListener(this);
            stone2.setOnTouchListener(this);
            stone3.setOnTouchListener(this);
        }

        if (view.getId() == R.id.continueButton) {
            parentLayout.removeView(customView2);
            parentLayout.addView(customView);
            radioGroup.clearCheck();
            buttonCont.setClickable(false);
            buttonCont.setVisibility(View.INVISIBLE);

        }
        if (view.getId() == R.id.checkAgainButton) {
            ++counter;
            if (counter < 3) {
                text.setImageResource(answers[rand.nextInt(3)]);
            }
            else {
                Toast.makeText(game.this, "Only 2 attempts for check again!", Toast.LENGTH_SHORT).show();
            }

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
            MediaPlayer mediaPlayer = MediaPlayer.create(game.this, R.raw.click);
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