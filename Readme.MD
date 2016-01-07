图片轮播控件CarouselerView
==========================
控件功能
--------------
+ **图片轮播展示**
+ **自动循环播放**
+ **横竖屏自动适配**

最新版本
-------
**v0.1 2016-1-7**

效果展示
--------
![控件实际效果](http://d.pcs.baidu.com/thumbnail/32be4a76a00f8249c25a442189e880bc?fid=2300734070-250528-616526256315670&time=1452153600&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-ZgUz%2F4waK2v%2FhalUvXKNvILV5ug%3D&rt=sh&expires=2h&r=962719008&sharesign=unknown&size=c710_u500&quality=100)

起步
----
1. **从GitHub下载[源码](),下载的源码就是一个完整的工程，自带demo，可以直接运行看效果**

2. **拷贝以下文件到工程的相应位置**

    + `src/com/ucas/developer/picturecarousel_android/CarouselerView.java`
    + `src/com/ucas/developer/picturecarousel_android/CarouselException.java`
    + `src/com/ucas/developer/picturecarousel_android/ViewPagerAdapter.java`
    + `res/drawable-hdpi/dot.xml`
    + `res/drawable-hdpi/dark_dot.png`
    + `res/drawable-hdpi/white_dot.png`
    + `res/layout/carouseler.xml`
    + `libs/android-support-v4.jar`
    
3. **在你的Activity中使用该控件**

    在xml布局中直接使用本控件
    
        <com.ucas.developer.picturecarousel_android.CarouselerView
            android:id="@+id/carouseler"
    	    android:layout_width="match_parent"
    	    android:layout_height="match_parent" />
    
    并在Activity中获取到该控件
    
        carouselerView = (CarouselerView) findViewById(R.id.carouseler);
    
    然后为其设置图片资源，以四个图片为例
    
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic3));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic4));
        carouselerView.setImageResources(bitmaps);
    
    之后可以选择性的设置是否开启或关闭自动播放，默认开启，若不想开启，只需调用如下方法传入参数false即可
    
        carouselerView.setAutoSlide(false);
        
4. **运行你的程序试试吧~**
