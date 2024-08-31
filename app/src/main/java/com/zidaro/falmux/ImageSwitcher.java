package com.zidaro.falmux;

import android.os.Handler;
import android.widget.ImageView;

import java.util.Random;

public class ImageSwitcher {
    private final ImageView imageView;
    private final int[] images;
    Random random = new Random();
    private int currentIndex = 0;
    private final Handler handler = new Handler();
    private Runnable runnable;

    public ImageSwitcher(ImageView imageView, int[] images) {
        this.images = images.clone();
        this.imageView = imageView;
        this.currentIndex = random.nextInt(images.length);
    }

    public void startSwitching() {
        runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(images[currentIndex]);
                currentIndex = (currentIndex + 1) % images.length;
                handler.postDelayed(this, 75);
            }
        };
        handler.post(runnable);
    }
    public void stopSwitching() {
        handler.removeCallbacks(runnable);
    }
}