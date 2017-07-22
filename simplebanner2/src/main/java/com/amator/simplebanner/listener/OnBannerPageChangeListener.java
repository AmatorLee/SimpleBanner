package com.amator.simplebanner.listener;

/**
 * Created by AmatorLee on 2017/7/22.
 */

public interface OnBannerPageChangeListener {


    void onBannerPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onBannerPageScrollStateChanged(int state);

    void onPageSelected(int position);
}
