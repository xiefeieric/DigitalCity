package com.fei.digitalcity.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.fei.digitalcity.R;

public class SplashActivity extends Activity {

    private ImageView ivSplash;
    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSp = getSharedPreferences("config",MODE_PRIVATE);
        initViews();
    }

    private void initViews() {
        ivSplash = (ImageView) findViewById(R.id.ivSplash);

        AnimationSet animSet = new AnimationSet(false);

        RotateAnimation anim2 = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim2.setDuration(1000);
        animSet.addAnimation(anim2);
        ScaleAnimation anim3 = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f);
        anim3.setDuration(1000);
        animSet.addAnimation(anim3);
        AlphaAnimation anim1 = new AlphaAnimation(0.5f,1.0f);
        anim1.setDuration(2000);
        animSet.addAnimation(anim1);
        ivSplash.startAnimation(animSet);

        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean is_new_user = mSp.getBoolean("is_new_user", true);
                if (is_new_user) {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
