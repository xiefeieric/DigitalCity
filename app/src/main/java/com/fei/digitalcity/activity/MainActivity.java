package com.fei.digitalcity.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.fei.digitalcity.R;
import com.fei.digitalcity.fragment.GovFragment;
import com.fei.digitalcity.fragment.HomeFragment;
import com.fei.digitalcity.fragment.LeftMenuFragment;
import com.fei.digitalcity.fragment.NewsFragment;
import com.fei.digitalcity.fragment.ServiceFragment;
import com.fei.digitalcity.fragment.SettingFragment;
import com.fei.digitalcity.model.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends SlidingFragmentActivity {
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.rbHome)
    private RadioButton rbHome;
    @ViewInject(R.id.rbNews)
    private RadioButton rbNews;
    @ViewInject(R.id.rbService)
    private RadioButton rbService;
    @ViewInject(R.id.rbGov)
    private RadioButton rbGov;
    @ViewInject(R.id.rbSetting)
    private RadioButton rbSetting;
    @ViewInject(R.id.flContent)
    private FrameLayout flContent;
    @ViewInject(R.id.flLeftMenu)
    private FrameLayout flLeftMenu;
    private FragmentManager mFragmentManager;
    private SlidingMenu mSlidingMenu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        ViewUtils.inject(this);
        mFragmentManager = getSupportFragmentManager();
        setBehindContentView(R.layout.menu_left);
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        mSlidingMenu.setBehindOffset(650);
        rbHome.isChecked();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.flContent,new HomeFragment(),"FragmentHome").commit();
    }

    private void initListeners() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbHome.isChecked()) {
                    rbHome.setTextColor(Color.RED);
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.flContent, new HomeFragment(), "FragmentHome").commit();
                } else {
                    rbHome.setTextColor(Color.WHITE);
                }
                if (rbNews.isChecked()) {
                    rbNews.setTextColor(Color.RED);
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.flLeftMenu, new LeftMenuFragment(), "FragmentLeftMenu");
                    transaction.replace(R.id.flContent, new NewsFragment(), "FragmentNews");
                    transaction.commit();
                } else {
                    rbNews.setTextColor(Color.WHITE);
                }
                if (rbService.isChecked()) {
                    rbService.setTextColor(Color.RED);
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.flContent, new ServiceFragment(), "FragmentService").commit();
                } else {
                    rbService.setTextColor(Color.WHITE);
                }
                if (rbGov.isChecked()) {
                    rbGov.setTextColor(Color.RED);
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.flContent, new GovFragment(), "FragmentGov").commit();
                } else {
                    rbGov.setTextColor(Color.WHITE);
                }
                if (rbSetting.isChecked()) {
                    rbSetting.setTextColor(Color.RED);
                    mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.replace(R.id.flContent, new SettingFragment(), "FragmentSetting").commit();
                } else {
                    rbSetting.setTextColor(Color.WHITE);
                }
            }
        });
    }

    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        LeftMenuFragment fragmentLeftMenu = (LeftMenuFragment) supportFragmentManager.findFragmentByTag("FragmentLeftMenu");
        return fragmentLeftMenu;
    }

    public NewsFragment getNewsFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        NewsFragment newsFragment = (NewsFragment) supportFragmentManager.findFragmentByTag("FragmentNews");
        return newsFragment;
    }

}
