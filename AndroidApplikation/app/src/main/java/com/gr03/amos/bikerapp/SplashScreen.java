package com.gr03.amos.bikerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.support.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class SplashScreen extends AppCompatActivity {
    private ImageView splashImageView;
    @Override
    protected void onStart() {
        super.onStart();
        Context context = this;

        Glide.with(context)
                .asGif()
                .load(R.mipmap.splash_screen_adrenaline)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(
                            GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(SplashScreen.this, ShowEventActivity.class);
                            SplashScreen.this.startActivity(intent);
                            SplashScreen.this.finish();
                        }, 6000);
                        return false;
                    }
                })
                .into(splashImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashImageView = findViewById(R.id.splashScreenView);
    }
}

