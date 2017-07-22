package com.amator.simplebanner.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.amator.simplebanner.util.BannerDirection;

import static com.amator.simplebanner.util.BannerDirection.LEFT;

/**
 * Created by AmatorLee on 2017/7/21.
 */

public class BannerViewPager extends ViewPager {

    private int delayTime = 3000;//设置展示时间
    private BannerDirection mBannerDirection = LEFT;//试着轮播方向
    private boolean isAuto = true;//是否为自动轮播

    public void setAuto(boolean auto) {
        isAuto = auto;
    }


    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setBannerDirection(BannerDirection bannerDirection) {
        mBannerDirection = bannerDirection;
    }

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void start() {
        if (isAuto) {
            //每次开始前先remove掉避免重复调用
            removeCallbacks(mBannerTask);
            postDelayed(mBannerTask, delayTime);
        }
    }

    public void stop() {
        removeCallbacks(mBannerTask);
    }


    private Runnable mBannerTask = new Runnable() {
        @Override
        public void run() {
            bannerPlay();
        }
    };

    private void bannerPlay() {
        PagerAdapter adapter = getAdapter();
        if (adapter != null) {
            int count = adapter.getCount();
            int currentItem = getCurrentItem();
            switch (mBannerDirection) {
                case LEFT:
                    currentItem++;
                    if (currentItem >= count) {
                        currentItem = 0;
                    }
                    break;
                case RIGHT:
                    currentItem--;
                    if (currentItem < 0) {
                        currentItem = count - 1;
                    }
                    break;
            }
            setCurrentItem(currentItem);
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(mBannerTask);
        super.onDetachedFromWindow();
    }

}
