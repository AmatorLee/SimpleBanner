package com.amator.bannerDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amator.simplebanner.listener.OnBannerClickListener;
import com.amator.simplebanner.util.IndicatorPosition;
import com.amator.simplebanner.util.IndicatorShape;
import com.amator.simplebanner.widget.SimpleBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 测试本地图片资源，图片较大，造成开销大
     */
    private int[] imaRes = {R.drawable.a123,R.drawable.color,R.drawable.flash,R.drawable.zhixiao,R.drawable.xiaozhi};
    private SimpleBanner mBanner;
    /**
     * 默认图片资源(图片URL地址)
     */
    private String [] defaultUrl = new String[] {
            "http://g.hiphotos.baidu.com/imgad/pic/item/a8773912b31bb051be533b24317adab44aede043.jpg",
            "http://g.hiphotos.baidu.com/imgad/pic/item/c75c10385343fbf22c362d2fb77eca8065388fa0.jpg",
            "http://liaoning.sinaimg.cn/2014/1111/U10435P1195DT20141111220802.jpg",
            "http://photocdn.sohu.com/20151124/mp43786429_1448294862260_4.jpeg",
            "http://h.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b00507c97e310a55b3191cf9.jpg" };
    /**
     * 测试文字提示
     */
    private String[] titleRes = {"深色", "浅色", "动物", "宋智孝", "宋智孝"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Integer> mDatas = new ArrayList<>();
        for (int i = 0; i < imaRes.length; i++) {
            mDatas.add(imaRes[i]);
        }

        List<String> mImageRes = new ArrayList<>();
        for (int i = 0; i < defaultUrl.length; i++) {
            mImageRes.add(defaultUrl[i]);
        }

        List<String> tips = new ArrayList<>();
        for (int i = 0; i < titleRes.length; i++) {
            tips.add(titleRes[i]);
        }
        mBanner = (SimpleBanner) findViewById(R.id.banner_simple);
        mBanner.setImageRes(mImageRes,tips)
                .setIndicatorForeColor(getResources().getColor(android.R.color.holo_blue_light))
                .setIndicatorShape(IndicatorShape.OVAL)
                .setShowBannerTips(true)
                .setIndicatorPosition(IndicatorPosition.LEFT)
                .setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void onBannerClick(int pos) {
                        Toast.makeText(MainActivity.this, "点击了第" + (pos + 1) + "个广告", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBanner.stop();
    }
}
