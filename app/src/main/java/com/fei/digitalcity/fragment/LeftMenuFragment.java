package com.fei.digitalcity.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fei.digitalcity.R;
import com.fei.digitalcity.activity.MainActivity;
import com.fei.digitalcity.model.NewsMenuData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends Fragment {

    private ListView lvFragmentLeftMenu;
    private ArrayList<NewsMenuData.NewsMenuItem> mMenuList;
    private int currentPosition = 0;
    private MyLeftMenuAdapter mAdapter;
    private FragmentManager mFm;
    private MainActivity mMainActivity;
    private NewsFragment newsFragment;
//    private FrameLayout mFlNewsContent;
    private LinearLayout mLlNews;

    public LeftMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFm = getActivity().getSupportFragmentManager();
        mMainActivity = (MainActivity) getActivity();
        View view = View.inflate(getActivity(), R.layout.fragment_left_menu, null);
        lvFragmentLeftMenu = (ListView) view.findViewById(R.id.lvFragmentLeftMenu);
        lvFragmentLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println(i);
                currentPosition = i;
                mAdapter.notifyDataSetChanged();
                newsFragment = mMainActivity.getNewsFragment();
//                mFlNewsContent = newsFragment.getFlNewsContent();
                mLlNews = newsFragment.getLlNews();

                if (i == 0) {
                    if (mLlNews!=null) {
                        mLlNews.removeAllViews();}
                    FragmentTransaction fragmentTransaction = mFm.beginTransaction();
                    fragmentTransaction.replace(R.id.llNews, new NewsFragment()).commit();
                }

                if (i == 1) {
                    if (mLlNews!=null) {
                        mLlNews.removeAllViews();
                    }
                    FragmentTransaction fragmentTransaction = mFm.beginTransaction();
                    fragmentTransaction.replace(R.id.llNews, new FoucsTopicFragment(), "FragmentFocusTopic").commit();
                }
                if (i == 2) {
                    if (mLlNews!=null) {
                        mLlNews.removeAllViews();
                    }
                    FragmentTransaction fragmentTransaction = mFm.beginTransaction();
                    fragmentTransaction.replace(R.id.llNews, new PhotoFragment(), "FragmentPhoto").commit();
                }
                if (i == 3) {
                    if (mLlNews!=null) {
                        mLlNews.removeAllViews();
                    }
                    FragmentTransaction fragmentTransaction = mFm.beginTransaction();
                    fragmentTransaction.replace(R.id.llNews, new ForumFragment(), "FragmentForum").commit();
                }

                mMainActivity.getSlidingMenu().toggle();
            }
        });

        return view;
    }

    public void setNewsData(NewsMenuData data) {
//        System.out.println("get data:" + data.toString());
        mMenuList = data.getData();
//        System.out.println(mMenuList.size());
        if (mMenuList != null) {
            mAdapter = new MyLeftMenuAdapter();
            lvFragmentLeftMenu.setAdapter(mAdapter);
        }
    }


    class MyLeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public Object getItem(int i) {
            return mMenuList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(getActivity(), R.layout.list_view_item_left_menu, null);
            TextView tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivItemArrow);

//            System.out.println(currentPosition);

            if (i == currentPosition) {
                imageView.setImageResource(R.drawable.menu_arr_select);
                tvItemTitle.setTextColor(Color.RED);
            } else {
                imageView.setImageResource(R.drawable.menu_arr_normal);
                tvItemTitle.setTextColor(Color.WHITE);
            }

            tvItemTitle.setText(mMenuList.get(i).getTitle());
            return view;
        }
    }
}
