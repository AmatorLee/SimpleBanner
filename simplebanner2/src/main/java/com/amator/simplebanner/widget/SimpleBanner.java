package com.amator.simplebanner.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amator.simplebanner.R;
import com.amator.simplebanner.adapter.BannerAdapter;
import com.amator.simplebanner.listener.OnBannerClickListener;
import com.amator.simplebanner.listener.OnBannerPageChangeListener;
import com.amator.simplebanner.util.AnimateUtil;
import com.amator.simplebanner.util.BannerDirection;
import com.amator.simplebanner.util.DisplayUtil;
import com.amator.simplebanner.util.ImageLoader;
import com.amator.simplebanner.util.IndicatorPosition;
import com.amator.simplebanner.util.IndicatorShape;
import com.amator.simplebanner.util.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.amator.simplebanner.util.IndicatorPosition.RIGHT;
import static com.amator.simplebanner.util.IndicatorShape.OVAL;

/**
 * Created by AmatorLee on 2017/7/21.
 */

public class SimpleBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {

    public static final String TAG = "SimpleBanner";
    private int mPositionValue;
    private int mShapeVAlue;

    private IndicatorPosition mIndicatorPosition = IndicatorPosition.RIGHT;

    /**
     * ViewPager
     */
    private BannerViewPager mBannerViewPager;

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 指示器背景颜色
     */
    private int mIndicatorBackColor = getResources().getColor(android.R.color.white);
    /**
     * 指示器前景颜色
     */
    private int mIndicatorForeColor = getResources().getColor(android.R.color.holo_blue_light);
    /**
     * 指示器半径
     */
    private int mIndicatorWidth = 20;
    /**
     * 是否显示banner提示
     */
    private boolean isShowBannerTips;
    /**
     * 是否显示指示器
     */
    private boolean isShowBannerIndicator;

    /**
     * banner indicator container（包括文字提示与指示器容器）
     */
    private RelativeLayout mBannerIndicatorContainer;
    /**
     * banner indicator
     */
    private RelativeLayout mBannerContainer;
    /**
     * 指示器提示
     */
    private TextView mBannerTips;

    /**
     * 资源
     */
    private List<View> mViews;

    /**
     * 数据适配器
     */
    private BannerAdapter mAdapter;
    /**
     * 提示资源
     */
    private List<String> mTips;
    /**
     * 指示器形状，OVAL，RECT
     */
    private IndicatorShape mIndicatorShape = OVAL;
    /**
     * 指示器高度，当为OVAL时与mIndicatorWidth相同
     */
    private int mIndicatorHeight = 20;
    /**
     * 未选中图层
     */
    private LayerDrawable unSelectedDrawable;
    /**
     * 选中图层
     */
    private LayerDrawable selectedDrawable;
    /**
     * 指示器容器
     */
    private LinearLayout indicatorContainer;
    /**
     * 指示器容器margin
     */
    private int indicatorMargin = 8;
    /**
     * 指示器padding
     */
    private int indicatorSpace = 5;
    /**
     * 未选中图层id
     */
    public static final int UNSELECTDRAWABLEID = 0x1;
    /**
     * 选中图层id
     */
    public static final int SELECTEDDRAWABLEID = 0x2;
    /**
     * 方向
     */
    private BannerDirection mBannerDirection = BannerDirection.LEFT;
    /**
     * 展示时间
     */
    private int showTime;
    /**
     * BannerPage滑动监听
     */
    private OnBannerPageChangeListener mOnBannerPageChangeListener;
    private boolean isShowBannerAnimate = true;


    private SimpleBanner(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public SimpleBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleBanner);
            try {
                mIndicatorBackColor = ta.getColor(R.styleable.SimpleBanner_indicator_unselected_color, getResources().getColor(android.R.color.white));
                mIndicatorForeColor = ta.getColor(R.styleable.SimpleBanner_indicator_selected_color, getResources().getColor(android.R.color.holo_red_light));
                mIndicatorWidth = ta.getDimensionPixelSize(R.styleable.SimpleBanner_indicator_width, 20);
                mIndicatorHeight = ta.getDimensionPixelSize(R.styleable.SimpleBanner_indicator_height, mIndicatorWidth);
                isShowBannerIndicator = ta.getBoolean(R.styleable.SimpleBanner_isShowBannerIndicator, true);
                isShowBannerTips = ta.getBoolean(R.styleable.SimpleBanner_isShowBannerTips, false);
                indicatorMargin = ta.getDimensionPixelSize(R.styleable.SimpleBanner_indicator_margin, 10);
                indicatorSpace = ta.getDimensionPixelSize(R.styleable.SimpleBanner_indicator_padding, 5);
                isShowBannerAnimate = ta.getBoolean(R.styleable.SimpleBanner_isShowBannerContainerAnimate, true);
                mShapeVAlue = ta.getInteger(R.styleable.SimpleBanner_indicator_shape, OVAL.ordinal());
                for (IndicatorShape shape : IndicatorShape.values()) {
                    if (shape.ordinal() == mShapeVAlue) {
                        Logger.d(TAG, "ShapeOrdinal===" + shape.ordinal() + "shapeValue===" + mShapeVAlue);
                        mIndicatorShape = shape;
                        break;
                    }
                }

                mPositionValue = ta.getInteger(R.styleable.SimpleBanner_indicator_position, RIGHT.ordinal());

                for (IndicatorPosition pos : IndicatorPosition.values()) {
                    if (pos.ordinal() == mPositionValue) {
                        Logger.d(TAG, "PosOrdinal===" + pos.ordinal() + "positionValue===" + mPositionValue);
                        mIndicatorPosition = pos;
                    }
                }

            } finally {
                ta.recycle();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }


    public static SimpleBanner createBanner(Context c) {
        return new SimpleBanner(c).init();
    }


    private SimpleBanner init() {

        showLog(true);

        mBannerContainer = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_simple_banner, null);
        mBannerIndicatorContainer = (RelativeLayout) mBannerContainer.findViewById(R.id.rl_banner_indicator_container);
        //初始化指示器父布局，当用作引导界面时可能改变
//        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.addRule(Gravity.BOTTOM);
//        mBannerIndicatorContainer = new RelativeLayout(mContext);

//        mBannerIndicatorContainer.setBackground(new ColorDrawable(0x80323232));

//        mBannerContainer.addView(mBannerContainer, params);

        mBannerViewPager = (BannerViewPager) mBannerContainer.findViewById(R.id.viewPager_simple_banner);
        setIndicatorTips();


        /***
         * 绘制指示器
         */
        setIndicator();

        int padding = DisplayUtil.dp2px(mContext, 10);
        mBannerIndicatorContainer.setPadding(padding, padding, padding, padding);
        mViews = new ArrayList<>();
        addView(mBannerContainer);
        return this;
    }

    private void setIndicatorTips() {
        mBannerTips = new TextView(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (mIndicatorPosition) {
            case LEFT:
                params.addRule(CENTER_VERTICAL);
                params.addRule(ALIGN_PARENT_RIGHT);
                break;
            case CENTER:
                break;
            case RIGHT:
                params.addRule(CENTER_VERTICAL);
                params.addRule(ALIGN_PARENT_LEFT);
                break;
        }
        mBannerTips.setTextColor(getResources().getColor(android.R.color.white));
        mBannerTips.setTextSize(14);
        mBannerIndicatorContainer.addView(mBannerTips, params);
    }

    private void setIndicator() {
        //绘制未选中状态图形
        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();

        //绘制选中状态图形
        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();


        switch (mIndicatorShape) {
            case RECT:
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case OVAL:
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                break;
        }
        unSelectedGradientDrawable.setColor(mIndicatorBackColor);
        unSelectedGradientDrawable.setSize(mIndicatorWidth, mIndicatorHeight);
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});

        unSelectedDrawable = unSelectedLayerDrawable;
        unSelectedDrawable.setId(0, UNSELECTDRAWABLEID);

        selectedGradientDrawable.setColor(mIndicatorForeColor);
        selectedGradientDrawable.setSize(mIndicatorWidth, mIndicatorHeight);
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});

        selectedDrawable = selectedLayerDrawable;
        selectedDrawable.setId(0, SELECTEDDRAWABLEID);

        //初始化indicatorContainer
        indicatorContainer = new LinearLayout(mContext);
        indicatorContainer.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        switch (mIndicatorPosition) {
            case LEFT:
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_LEFT);
                break;
            case CENTER:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                if (isShowBannerTips) {
                    mBannerTips.setVisibility(GONE);
                }
                break;
            case RIGHT:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
        }
        //设置margin
        params.setMargins(indicatorMargin, indicatorMargin / 2, indicatorMargin, indicatorMargin / 2);
        //添加指示器容器布局到SliderLayout
        mBannerIndicatorContainer.addView(indicatorContainer, params);
    }

    public SimpleBanner setImageRes(List<?> imageRes) {
        return setImageRes(imageRes, null);
    }

    public SimpleBanner setImageRes(List<?> imageRes, List<String> tips) {
        if (tips != null) {
            mTips = tips;
        }
        Logger.d(TAG, "setImageRes excute====");
        for (int i = 0; i < imageRes.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.loadImage(mContext, imageRes.get(i), imageView);
            mViews.add(imageView);
        }

        //初始化指示器，并添加到指示器容器布局
        for (int j = 0; j < imageRes.size(); j++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(indicatorSpace, 0, indicatorSpace, 0);
            indicator.setImageDrawable(unSelectedDrawable);
            indicatorContainer.addView(indicator);
        }
        mAdapter = new BannerAdapter(mViews);
        mBannerViewPager.setAdapter(mAdapter);
        //设置当前item到Integer.MAX_VALUE中间的一个值，看起来像无论是往前滑还是往后滑都是ok的
        //如果不设置，用户往左边滑动的时候已经划不动了
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mViews.size();
        Logger.d(TAG, "targetItemPosition=====" + targetItemPosition);
        mBannerViewPager.setCurrentItem(targetItemPosition);
        int currentPos = targetItemPosition % mViews.size();
        if (tips != null) {
            mBannerTips.setText(tips.get(currentPos));
            mBannerTips.setVisibility(isShowBannerTips ? VISIBLE : GONE);
        }
        switchIndicator(currentPos);
        mBannerViewPager.addOnPageChangeListener(this);
        return this;
    }

    public void update(List<?> imageRes) {
        mViews.clear();
        indicatorContainer.removeAllViews();
        for (int i = 0; i < imageRes.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.loadImage(mContext, imageRes.get(i), imageView);
            mViews.add(imageView);

        }

        for (int j = 0; j < imageRes.size(); j++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(indicatorSpace, 0, indicatorSpace, 0);
            indicator.setImageDrawable(unSelectedDrawable);
            indicatorContainer.addView(indicator);
        }

        if (mAdapter == null) {
            mAdapter = new BannerAdapter(mViews);
            mBannerViewPager.setAdapter(mAdapter);
            mBannerViewPager.addOnPageChangeListener(this);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    public SimpleBanner start() {
        mBannerViewPager.start();
        return this;
    }

    public SimpleBanner stop() {
        mBannerViewPager.stop();
        return this;
    }

    public SimpleBanner setOnBannerPageChangeListener(OnBannerPageChangeListener onBannerPageChangeListener) {
        mOnBannerPageChangeListener = onBannerPageChangeListener;
        return this;
    }

    public SimpleBanner setBannerDirection(BannerDirection bannerDirection) {
        mBannerDirection = bannerDirection;
        mBannerViewPager.setBannerDirection(bannerDirection);
        return this;
    }

    public SimpleBanner setBannerShowTime(int showTime) {
        this.showTime = showTime;
        mBannerViewPager.setDelayTime(showTime);
        return this;
    }


    public SimpleBanner setIndicatorPosition(IndicatorPosition indicatorPosition) {

        mIndicatorPosition = indicatorPosition;
        mBannerIndicatorContainer.removeView(indicatorContainer);
        mBannerIndicatorContainer.removeView(mBannerTips);
        RelativeLayout.LayoutParams indicatorParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams indicatortipsParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        switch (mIndicatorPosition) {
            case LEFT:
                indicatorParams.addRule(RelativeLayout.CENTER_VERTICAL);
                indicatorParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                indicatortipsParams.addRule(CENTER_VERTICAL);
                indicatortipsParams.addRule(ALIGN_PARENT_RIGHT);
                break;
            case CENTER:
                indicatorParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                indicatortipsParams.addRule(CENTER_IN_PARENT);
                setShowBannerTips(false);
                break;
            case RIGHT:
                indicatorParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                indicatorParams.addRule(RelativeLayout.CENTER_VERTICAL);
                indicatortipsParams.addRule(ALIGN_PARENT_LEFT);
                indicatortipsParams.addRule(CENTER_VERTICAL);
                break;
        }

        //设置margin
//        indicatorParams.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
        //添加指示器容器布局到SliderLayout
        mBannerIndicatorContainer.addView(indicatorContainer, indicatorParams);
        mBannerIndicatorContainer.addView(mBannerTips, indicatortipsParams);
        return this;

    }

    public SimpleBanner setIndicatorShape(IndicatorShape indicatorShape) {
        mIndicatorShape = indicatorShape;
        GradientDrawable unSelectedGradientDrawable = (GradientDrawable) unSelectedDrawable.getDrawable(0);
        GradientDrawable selectedGradientDrawable = (GradientDrawable) selectedDrawable.getDrawable(0);
        switch (mIndicatorShape) {
            case RECT:
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case OVAL:
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                break;
        }
        unSelectedDrawable.setDrawableByLayerId(UNSELECTDRAWABLEID, unSelectedGradientDrawable);
        selectedDrawable.setDrawableByLayerId(SELECTEDDRAWABLEID, selectedGradientDrawable);

        return this;
    }


    public SimpleBanner setAuto(boolean isAuto) {
        mBannerViewPager.setAuto(isAuto);
        return this;
    }


    public SimpleBanner setIndicatorBackColor(int indicatorBackColor) {
        mIndicatorBackColor = indicatorBackColor;
        GradientDrawable drawable = (GradientDrawable) unSelectedDrawable.getDrawable(0);
        drawable.setColor(indicatorBackColor);
        unSelectedDrawable.setDrawableByLayerId(UNSELECTDRAWABLEID, drawable);
        return this;
    }

    public SimpleBanner setIndicatorForeColor(int indicatorForeColor) {
        mIndicatorForeColor = indicatorForeColor;
        GradientDrawable drawable = (GradientDrawable) selectedDrawable.getDrawable(0);
        drawable.setColor(indicatorForeColor);
        unSelectedDrawable.setDrawableByLayerId(SELECTEDDRAWABLEID, drawable);
        return this;
    }


    public SimpleBanner setShowBannerTips(boolean showBannerTips) {
        this.isShowBannerTips = showBannerTips;
        mBannerTips.setVisibility(showBannerTips ? VISIBLE : GONE);
        return this;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mViews != null) {
            position = position % mViews.size();
        }
        if (mOnBannerPageChangeListener != null) {
            mOnBannerPageChangeListener.onBannerPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        Logger.d(TAG, "onPageScrolled =====");
    }

    @Override
    public void onPageSelected(int position) {
        if (mViews != null) {
            position = position % mViews.size();
        }
        if (mOnBannerPageChangeListener != null) {
            mOnBannerPageChangeListener.onPageSelected(position);
        }
        Logger.d(TAG, "onPageSelected: =======" + mTips);
        if (mTips != null) {
            Logger.d(TAG, "SetBannerTip excute====");
            setBannerTip(mTips.get(position));
        }
        switchIndicator(position);
        if (isShowBannerIndicator) {
            AnimateUtil.showView(mBannerIndicatorContainer);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Logger.d(TAG, "onPageScrollStateChanged state====" + state);
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            start();
        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            stop();
        }

        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            if (isShowBannerIndicator) {
                AnimateUtil.hideView(mBannerIndicatorContainer);
            }
        }

        if (mOnBannerPageChangeListener != null) {
            mOnBannerPageChangeListener.onBannerPageScrollStateChanged(state);
        }

    }

    public void setBannerTip(String bannerTip) {
        if (isShowBannerTips) {
            mBannerTips.setText(bannerTip);
        }
    }

    public SimpleBanner showLog(boolean debug) {
        Logger.DEBUG = debug;
        return this;
    }

    public SimpleBanner setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        if (bannerClickListener != null) {
            mAdapter.setBannerClickListener(bannerClickListener);
        }
        return this;
    }

    /**
     * 切换指示器状态
     *
     * @param currentPosition 当前位置
     */
    private void switchIndicator(int currentPosition) {
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            ((ImageView) indicatorContainer.getChildAt(i)).setImageDrawable(i == currentPosition ? selectedDrawable : unSelectedDrawable);
        }
    }

    public SimpleBanner setShowBannerIndicator(boolean showBannerIndicator) {
        isShowBannerIndicator = showBannerIndicator;
        mBannerIndicatorContainer.setVisibility(showBannerIndicator ? VISIBLE : GONE);
        Logger.d(TAG, "isBannershow====" + mBannerIndicatorContainer.getVisibility());
        return this;
    }

    public SimpleBanner setBannerIndicatorContainerColor(int color) {
        if (isShowBannerIndicator) {
            mBannerIndicatorContainer.setBackgroundColor(color);
        }
        return this;
    }

    public SimpleBanner setBannerPageTranFormer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        mBannerViewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public SimpleBanner setBannerContainerAnimate(boolean showBannerAnimate) {
        isShowBannerAnimate = showBannerAnimate;
        AnimateUtil.canShowAnimate = isShowBannerAnimate;
        return this;
    }
}
