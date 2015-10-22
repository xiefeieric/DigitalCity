package com.fei.digitalcity.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fei.digitalcity.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    private ViewPager vpWelcome;
    private ImageView ivDotRed;
    private int[] pages = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> mViewList;
    private Button btnWelcomeStart;
    private LinearLayout llDots;
    private SharedPreferences mSp;
    private int mOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mSp = getSharedPreferences("config", MODE_PRIVATE);
        initViews();
        initListeners();

        llDots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llDots.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int x0 = (int) llDots.getChildAt(0).getX();
                int x1 = (int) llDots.getChildAt(1).getX();
                mOffset = x1-x0;
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();

//    }

    private void initViews() {

        vpWelcome = (ViewPager) findViewById(R.id.vpWelcome);
        btnWelcomeStart = (Button) findViewById(R.id.btnWelcomeStart);
        llDots = (LinearLayout) findViewById(R.id.llDots);
        ivDotRed = (ImageView) findViewById(R.id.ivDotRed);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);


        for (int i = 0; i < pages.length; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.shape_dot_grey);
            if (i > 0) {
                params.leftMargin = 30;
            }
            view.setLayoutParams(params);
            llDots.addView(view);
        }

        mViewList = new ArrayList<>();

        for (int i = 0; i < pages.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(pages[i]);
            mViewList.add(imageView);
        }

        vpWelcome.setAdapter(new MyWelcomePagerAdapter());
    }

    private void initListeners() {

        vpWelcome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDotRed.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                System.out.println(positionOffset);

                layoutParams.leftMargin = (int) (leftMargin + mOffset*positionOffset + position*mOffset);
                ivDotRed.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == pages.length - 1) {
                    btnWelcomeStart.setVisibility(View.VISIBLE);
                } else {
                    btnWelcomeStart.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnWelcomeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                mSp.edit().putBoolean("is_new_user", false).apply();
                finish();
            }
        });

    }


    class MyWelcomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
