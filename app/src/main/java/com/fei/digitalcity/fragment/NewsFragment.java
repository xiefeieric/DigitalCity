package com.fei.digitalcity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fei.digitalcity.R;
import com.fei.digitalcity.activity.MainActivity;
import com.fei.digitalcity.activity.ReadNewsActivity;
import com.fei.digitalcity.model.ChildNewsData;
import com.fei.digitalcity.model.NewsMenuData;
import com.fei.digitalcity.utils.GlobalConstants;
import com.fei.digitalcity.utils.UiUtils;
import com.fei.digitalcity.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private ImageView ivNewsImage;
    private ViewPager vpNewsChildren;
    private ImageButton ibNext;
    private NewsMenuData mNewsMenuData;
    private ArrayList<NewsMenuData.NewsMenuItem.NewsMenuItemChilden> mChildrenList;
    private LinearLayout llNews;
    private TabPageIndicator mIndicator;
    private CirclePageIndicator indicatorPic;
    private ChildNewsData mChildNewsData;
    private ArrayList<ChildNewsData.ItemChildNewsData.ItemChildTopNews> mTopnewsList;
    private MyNewsPagerAdapter mAdapter;
    private ViewPager mVpNewsTopPics;
    private MyTopPicPagerAdapter mPicAdapter;
    private TextView tvTopNewsPicTitle;
    private MainActivity activity;
    private RefreshListView mLvNews;
    private ArrayList<ChildNewsData.ItemChildNewsData.ItemChildNewsDetail> mChildNewsList;
    private View mHeadView;
    private String mUrlMore;
    private MyChildNewsListAdapter mChildNewsListAdapter;

    public LinearLayout getLlNews() {
        return llNews;
    }


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = View.inflate(getActivity(), R.layout.fragment_news, null);
        mHeadView = View.inflate(getActivity(), R.layout.view_pager_item_news_head, null);
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

        getDataFromServer();
    }


    private void initListeners() {

        ivNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                parseData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getDataFromServer() {
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

                parseData();

                //set indicator for viewpager
                mIndicator.setViewPager(vpNewsChildren);
                mIndicator.setVisibility(View.VISIBLE);
                ibNext.setVisibility(View.VISIBLE);
//                System.out.println("mChildrenList size: " + mChildrenList.size());

                activity = (MainActivity) getActivity();
                LeftMenuFragment leftMenuFragment = activity.getLeftMenuFragment();
                leftMenuFragment.setNewsData(mNewsMenuData);

//                mLvNews.onRefreshComplete();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                UiUtils.showToast(getActivity(), s);
//                mLvNews.onRefreshComplete();
            }
        });
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
                //get top news data
                mTopnewsList = mChildNewsData.data.topnews;
                //get news data
                mChildNewsList = mChildNewsData.data.news;
                //get more data url
                String moreUrl = mChildNewsData.data.more;
                if (moreUrl!=null) {
                    mUrlMore = GlobalConstants.SERVER_URL+moreUrl;
                } else {
                    mUrlMore = null;
                }
//                    System.out.println(mTopnewsList.size());
                mPicAdapter = new MyTopPicPagerAdapter();
                mVpNewsTopPics.setAdapter(mPicAdapter);
                tvTopNewsPicTitle.setText(mTopnewsList.get(0).title);
                mVpNewsTopPics.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        tvTopNewsPicTitle.setText(mTopnewsList.get(position).title);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                indicatorPic.setViewPager(mVpNewsTopPics);
                indicatorPic.setSnap(true);

                mChildNewsListAdapter = new MyChildNewsListAdapter();
                mLvNews.setAdapter(mChildNewsListAdapter);

                //set self defined refresh listener
                mLvNews.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        getDataFromServer();
//                        UiUtils.showToast(getActivity(), "Load New Data from Server!");

                    }

                    @Override
                    public void onLoadMore() {
                        if (mUrlMore!=null){
                            parseMoreData();
//                            UiUtils.showToast(getActivity(),"No more data!");
                            mLvNews.onRefreshComplete();
                        } else {
                            UiUtils.showToast(getActivity(),"No more data!");
                            mLvNews.onRefreshComplete();
                        }
                    }
                });

                mLvNews.onRefreshComplete();

                //set on item click listener
                mLvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        System.out.println(i);
                        Intent intent = new Intent(getActivity(), ReadNewsActivity.class);
                        String newsUrl = mChildNewsList.get(i - 2).url;
                        intent.putExtra("newsUrl",newsUrl);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                UiUtils.showToast(getActivity(), s);
            }
        });
    }

    private void parseMoreData() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrlMore, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultTopNews = responseInfo.result;
                Gson gson = new Gson();
                mChildNewsData = gson.fromJson(resultTopNews, ChildNewsData.class);
                mChildNewsList.addAll(mChildNewsData.data.news);
                String moreUrl = mChildNewsData.data.more;
                if (moreUrl!=null) {
                    mUrlMore = GlobalConstants.SERVER_URL+moreUrl;
                } else {
                    mUrlMore = null;
                }
                mChildNewsListAdapter.notifyDataSetChanged();
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
            mVpNewsTopPics = (ViewPager) mHeadView.findViewById(R.id.vpNewsTopPics);
            indicatorPic = (CirclePageIndicator) mHeadView.findViewById(R.id.indicatorPic);
            tvTopNewsPicTitle = (TextView) mHeadView.findViewById(R.id.tvTopNewsPicTitle);
            mLvNews = (RefreshListView) view.findViewById(R.id.lvNews);
            mLvNews.addHeaderView(mHeadView);
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
            mBitmapUtils = new BitmapUtils(activity);
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
        public ImageView instantiateItem(ViewGroup container, int position) {
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


    class MyChildNewsListAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public MyChildNewsListAdapter() {
            mBitmapUtils = new BitmapUtils(getActivity());
        }

        @Override
        public int getCount() {
            return mChildNewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return mChildNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.list_view_item_child_news, null);
                holder.ivNewsPic = (ImageView) view.findViewById(R.id.ivNewsPic);
                holder.tvNewsTitle = (TextView) view.findViewById(R.id.tvNewsTitle);
                holder.tvNewsPubDate = (TextView) view.findViewById(R.id.tvNewsPubDate);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ChildNewsData.ItemChildNewsData.ItemChildNewsDetail itemChildNewsDetail = mChildNewsList.get(i);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
            mBitmapUtils.display(holder.ivNewsPic, itemChildNewsDetail.listimage);
            holder.tvNewsTitle.setText(itemChildNewsDetail.title);
            holder.tvNewsPubDate.setText(itemChildNewsDetail.pubdate);

            return view;
        }
    }

    static class ViewHolder {
        ImageView ivNewsPic;
        TextView tvNewsTitle;
        TextView tvNewsPubDate;
    }


}
