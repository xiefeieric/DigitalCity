package com.fei.digitalcity.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fei.digitalcity.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fei on 12/10/15.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    public static final int PULL_TO_REFRESH = 0;
    public static final int RELEASE_TO_REFRESH = 1;
    public static final int REFRESHING = 2;

    private View mViewHead;
    private int mStartY = -1;
    private int mMeasuredHeight;
    private int currentState = PULL_TO_REFRESH;
    private TextView tvRefreshListHint;
    private ImageView ivRefreshListArr;
    private TextView tvRefreshListTime;
    private ProgressBar pbRefreshList;
    private RotateAnimation mAnimUp;
    private RotateAnimation mAnimDown;
    private View mViewFoot;
    private int mMeasuredFootHeight;


    public RefreshListView(Context context) {
        super(context);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }



    public void initView() {

        //inflate head view
        mViewHead = View.inflate(getContext(), R.layout.refresh_list_view_head, null);
        //inflate foot view
        mViewFoot = View.inflate(getContext(), R.layout.refresh_list_view_foot, null);
        //find components from head view
        tvRefreshListHint = (TextView) mViewHead.findViewById(R.id.tvRefreshListHint);
        ivRefreshListArr = (ImageView) mViewHead.findViewById(R.id.ivRefreshListArr);
        tvRefreshListTime = (TextView) mViewHead.findViewById(R.id.tvRefreshListTime);
        pbRefreshList = (ProgressBar) mViewHead.findViewById(R.id.pbRefreshList);
        //add head view to list
        addHeaderView(mViewHead);
        //add foot view to list
        addFooterView(mViewFoot);
        //measure head view height
        mViewHead.measure(0, 0);
        mMeasuredHeight = mViewHead.getMeasuredHeight();
        //set head view padding
        mViewHead.setPadding(0, -mMeasuredHeight, 0, 0);
        //measure foot view height
        mViewFoot.measure(0, 0);
        mMeasuredFootHeight = mViewFoot.getMeasuredHeight();
        //set foot view padding
        mViewFoot.setPadding(0,-mMeasuredFootHeight,0,0);
        initAnims();

        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getRawY();
//                System.out.println("startY: "+mStartY);
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentState == REFRESHING) {
                    break;
                }

                if (mStartY==-1) {
                    mStartY = (int) ev.getRawY();
                }
                int currentY = (int) ev.getRawY();
                int offsetY = currentY-mStartY;
                int padding = offsetY-mMeasuredHeight;
//                System.out.println("offsetY: "+offsetY + "; MesureY: "+mMeasuredHeight + "; currentstate: "+currentState + "; padding:"+padding);
                if (getFirstVisiblePosition()==0 && offsetY>0) {
                    mViewHead.setPadding(0,padding,0,0);
                }

//                System.out.println("padding:"+padding);
                if (padding>0 && currentState!=RELEASE_TO_REFRESH) {
                    currentState = RELEASE_TO_REFRESH;
                    refreshState();
                } else if (padding<0 && currentState!=PULL_TO_REFRESH) {
                    currentState = PULL_TO_REFRESH;
                    refreshState();
                }

                break;

            case MotionEvent.ACTION_UP:
                mStartY = -1;
//                System.out.println(currentState);
                if (currentState == RELEASE_TO_REFRESH){
                    currentState = REFRESHING;
                    refreshState();
                } else if (currentState == PULL_TO_REFRESH) {
                    mViewHead.setPadding(0, -mMeasuredHeight, 0, 0);
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void refreshState() {

        switch (currentState) {

            case PULL_TO_REFRESH:
                tvRefreshListHint.setText("Pull to Refresh");
                ivRefreshListArr.setVisibility(VISIBLE);
                ivRefreshListArr.startAnimation(mAnimDown);
                pbRefreshList.setVisibility(INVISIBLE);
                break;

            case RELEASE_TO_REFRESH:
                tvRefreshListHint.setText("Release to Refresh");
                ivRefreshListArr.setVisibility(VISIBLE);
                ivRefreshListArr.startAnimation(mAnimUp);
                pbRefreshList.setVisibility(INVISIBLE);
                break;

            case REFRESHING:
                tvRefreshListHint.setText("Refreshing......");
                mViewHead.setPadding(0, 0, 0, 0);
                ivRefreshListArr.clearAnimation();
                ivRefreshListArr.setVisibility(INVISIBLE);
                pbRefreshList.setVisibility(VISIBLE);

                if (mListener!=null){
                    mListener.onRefresh();
                    onRefreshComplete();
//                    mViewHead.setPadding(0, -mMeasuredHeight, 0, 0);
//                    currentState = PULL_TO_REFRESH;
//                    tvRefreshListTime.setText("Last refreshed: "+getCurrentTime());
//                    ivRefreshListArr.setVisibility(VISIBLE);
//                    pbRefreshList.setVisibility(INVISIBLE);

                }
                break;
        }

    }

    private void initAnims() {

        mAnimUp = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        mAnimUp.setDuration(300);
        mAnimUp.setFillAfter(true);

        mAnimDown = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        mAnimUp.setDuration(300);
        mAnimUp.setFillAfter(true);
    }


    OnRefreshListener mListener;
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }



    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }

    public void onRefreshComplete(){

        mViewFoot.setPadding(0,-mMeasuredFootHeight,0,0);

        if (isLoadingMore) {
            mViewFoot.setPadding(0,-mMeasuredFootHeight,0,0);
            isLoadingMore = false;
        } else {
            currentState = PULL_TO_REFRESH;
            tvRefreshListHint.setText("Pull to Refresh");
            ivRefreshListArr.setVisibility(VISIBLE);
            pbRefreshList.setVisibility(INVISIBLE);
            mViewHead.setPadding(0, -mMeasuredHeight, 0, 0);
            tvRefreshListTime.setText("Last refreshed: " + getCurrentTime());
//        System.out.println("onRefreshComplete");
        }
    }

    public String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        return currentTime;
    }

    private boolean isLoadingMore = false;
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

        if (i==SCROLL_STATE_FLING || i==SCROLL_STATE_IDLE) {
            if (getLastVisiblePosition() == getCount()-1 && !isLoadingMore) {
                mViewFoot.setPadding(0,0,0,0);
                setSelection(getCount());
                isLoadingMore = true;
                if (mListener!=null) {
                    mListener.onLoadMore();
                    mViewFoot.setPadding(0,-mMeasuredFootHeight,0,0);
                }
            }


        }

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }


}
