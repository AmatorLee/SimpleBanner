# SimpleBanner
一个简单的广告轮播库
##使用
1.  在你project的```build.gradle```的```repositories```里面添加：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}```
2. 在你所需的module的```build.gradle```里面添加依赖：
```
dependencies {
	       compile 'com.github.AmatorLee:SimpleBanner:1.0.0'
	}```
3. 在你需要展示Banner数据的xml文件中引入：
```  
  <com.amator.simplebanner.widget.SimpleBanner
        android:id="@+id/banner_simple"
        android:layout_width="match_parent"
        android:layout_height="200dp">
    </com.amator.simplebanner.widget.SimpleBanner>```
4. 在Activity或者Fragment中配置SimpleBanner：
```
 /**
     * 测试本地图片资源，图片较大，造成开销大
     */
    private int[] imaRes = {R.drawable.a123, R.drawable.color, R.drawable.flash, R.drawable.zhixiao, R.drawable.xiaozhi};
    private SimpleBanner mBanner;
    /**
     * 默认图片资源(图片URL地址)
     */
    private String[] defaultUrl = new String[]{
            "http://g.hiphotos.baidu.com/imgad/pic/item/a8773912b31bb051be533b24317adab44aede043.jpg",
            "http://g.hiphotos.baidu.com/imgad/pic/item/c75c10385343fbf22c362d2fb77eca8065388fa0.jpg",
            "http://liaoning.sinaimg.cn/2014/1111/U10435P1195DT20141111220802.jpg",
            "http://photocdn.sohu.com/20151124/mp43786429_1448294862260_4.jpeg",
            "http://h.hiphotos.baidu.com/image/pic/item/faedab64034f78f0b00507c97e310a55b3191cf9.jpg"};
    /**
     * 测试文字提示
     */
    private String[] titleRes = {"深色", "浅色", "动物", "宋智孝", "宋智孝"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载本地图片
        List<Integer> mDatas = new ArrayList<>();
        for (int i = 0; i < imaRes.length; i++) {
            mDatas.add(imaRes[i]);
        }
        //加载网络图片
        List<String> mImageRes = new ArrayList<>();
        for (int i = 0; i < defaultUrl.length; i++) {
            mImageRes.add(defaultUrl[i]);
        }
        List<String> tips = new ArrayList<>();
        for (int i = 0; i < titleRes.length; i++) {
            tips.add(titleRes[i]);
        }
        //1.写在布局文件中
        mBanner = (SimpleBanner) findViewById(R.id.banner_simple);
        mBanner.setImageRes(mImageRes)
                .start();//调用则表示允许自动循环播放
        //2.代码中直接使用
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_banner_container);
        mBanner = SimpleBanner.createBanner(this)
                .setImageRes(mImageRes);
        mBanner.start();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(this, 200));
        linearLayout.addView(mBanner, params);

    }
    @Override
    protected void onPause() {
        super.onPause();
        //释放资源避免内存泄漏
        mBanner.stop();
    }```
就这样就可以实现以下效果

![simplebanner](http://upload-images.jianshu.io/upload_images/2605454-0fedbe57178ce488.gif?imageMogr2/auto-orient/strip)
可能有人会问，那么切换效果呢？，其实切换效果的实现很简单，但是实现炫酷的切换效果需要大量是时间进行计算，同时对于部分APP来说是不合适的，所以我不打算提供多种切换效果，当然你可以自己实现然后调用```setBannerPageTranFormer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer)```即可。同时我推荐一个开源库[android-viewpager-transformers](https://github.com/geftimov/android-viewpager-transformers)，感谢。
