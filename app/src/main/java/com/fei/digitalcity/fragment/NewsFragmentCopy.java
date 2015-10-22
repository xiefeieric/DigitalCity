package com.fei.digitalcity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fei.digitalcity.R;
import com.fei.digitalcity.activity.MainActivity;
import com.fei.digitalcity.model.ChildNewsData;
import com.fei.digitalcity.model.NewsMenuData;
import com.fei.digitalcity.utils.GlobalConstants;
import com.fei.digitalcity.utils.UiUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragmentCopy extends Fragment {


    private ImageView ivNewsImage;
    private ViewPager vpNewsChildren;
    private ImageButton ibNext;
    private NewsMenuData mNewsMenuData;
    private ArrayList<NewsMenuData.NewsMenuItem.NewsMenuItemChilden> mChildrenList;
    private LinearLayout llNews;
    private TabPageIndicator mIndicator;
    private ChildNewsData mChildNewsData;
    private ArrayList<ChildNewsData.ItemChildNewsData.ItemChildTopNews> mTopnewsList;
    private MyNewsPagerAdapter mAdapter;
    private ViewPager mVpNewsTopPics;
    private MyTopPicPagerAdapter mPicAdapter;

    public LinearLayout getLlNews() {
        return llNews;
    }


    public NewsFragmentCopy() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = View.inflate(getActivity(), R.layout.fragment_news, null);
        vpNewsChildren = (ViewPager) view.findViewById(R.id.vpNewsChildren);
        ibNext = (ImageButton) view.findViewById(R.id.ibNext);
        llNews = (LinearLayout) view.findViewById(R.id.llNews);
        ivNewsImage = (ImageView) view.findViewById(R.id.ivNewsImage);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        initData();

        initListeners();


        return view;


    }

    private void initData() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                System.out.println(result);
                Gson gson = new Gson();
                mNewsMenuData = gson.fromJson(result, NewsMenuData.class);
                mChildrenList = mNewsMenuData.getData().get(0).getChildren();

                mAdapter = new MyNewsPagerAdapter();
                vpNewsChildren.setAdapter(mAdapter);

                //set indicator for viewpager
                mIndicator.setViewPager(vpNewsChildren);
                mIndicator.setVisibility(View.VISIBLE);
                ibNext.setVisibility(View.VISIBLE);

                parseData();

//                System.out.println("mChildrenList size: " + mChildrenList.size());
                MainActivity activity = (MainActivity) getActivity();
                LeftMenuFragment leftMenuFragment = activity.getLeftMenuFragment();
                leftMenuFragment.setNewsData(mNewsMenuData);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                e.printStackTrace();
                UiUtils.showToast(getActivity(), s);
            }
        });
    }

    private void initListeners() {

        ivNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.getSlidingMenu().toggle();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = vpNewsChildren.getCurrentItem();
                vpNewsChildren.setCurrentItem(++currentItem);
            }
        });

        vpNewsChildren.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                mAdapter.notifyDataSetChanged();
//                mPicAdapter.notifyDataSetChanged();
                parseData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mVpNewsTopPics != null) {
            mVpNewsTopPics.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    mAdapter.notifyDataSetChanged();
                    mPicAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    private void parseData() {

        String url = mNewsMenuData.getData().get(0).getChildren().get(vpNewsChildren.getCurrentItem()).getUrl();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.SERVER_URL + url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultTopNews = responseInfo.result;
//                    System.out.println(resultTopNews);
                Gson gson = new Gson();
                mChildNewsData = gson.fromJson(resultTopNews, ChildNewsData.class);
//                System.out.println(mChildNewsData.toString());
                mTopnewsList = mChildNewsData.data.topnews;
//                    System.out.println(mTopnewsList.size());
                mPicAdapter = new MyTopPicPagerAdapter();
                mVpNewsTopPics.setAdapter(mPicAdapter);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                UiUtils.showToast(getActivity(), s);
            }
        });
    }




    class MyNewsPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mChildrenList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mChildrenList.get(position).getTitle().toUpperCase();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(getActivity(), R.layout.view_pager_item_news, null);
            mVpNewsTopPics = (ViewPager) view.findViewById(R.id.vpNewsTopPics);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class MyTopPicPagerAdapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public MyTopPicPagerAdapter() {
            mBitmapUtils = new BitmapUtils(getActivity());
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mTopnewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            System.out.println("pic position: "+position);
            ImageView pic = new ImageView(getActivity());
//            pic.setImageResource(R.drawable.news_pic_default);
            mBitmapUtils.display(pic, mTopnewsList.get(position).topimage);
//            System.out.println("pic address: "+mTopnewsList.get(position).topimage);
            pic.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(pic);
            return pic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
