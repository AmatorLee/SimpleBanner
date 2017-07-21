package com.amator.simplebanner.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amator.simplebanner.listener.OnBannerClickListener;

import java.util.List;

/**
 * Created by AmatorLee on 2017/7/20.
 */

public class BannerAdapter extends PagerAdapter {

    private List<View> mViews;
    public static final String TAG = "BannerPagerAdapter";
    private OnBannerClickListener mBannerClickListener;
    private int pos;

    public void setBannerClickListener(OnBannerClickListener bannerClickListener) {
        mBannerClickListener = bannerClickListener;
    }

    public BannerAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mViews == null || mViews.size() == 0) {
            Log.d(TAG, "instantiateItem return");
            return null;
        }
        final int pos = position % mViews.size();
        View view = mViews.get(pos);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "instantiateItem pos is :" + pos);
                if (mBannerClickListener != null) {
                    mBannerClickListener.onBannerClick(pos);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViews == null || mViews.size() == 0) {
            return;
        }
        int pos = position % mViews.size();
        Log.d(TAG, "destroyItempos: " + pos);
        container.removeView(mViews.get(pos));
    }

    @Override
    public int getCount() {
        return mViews == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
