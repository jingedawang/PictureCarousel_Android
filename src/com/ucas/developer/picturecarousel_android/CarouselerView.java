package com.ucas.developer.picturecarousel_android;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 图片轮播控件类
 * <br>
 * <pre>
 *使用方法：
 *在xml布局中直接使用本控件
 *	&lt;com.ucas.developer.picturecarousel_android.CarouselerView
 *		android:id="@+id/carouseler"
 *		android:layout_width="match_parent"
 *		android:layout_height="match_parent" /&gt;
 *并在Activity中获取到该控件
 *	carouselerView = (CarouselerView) findViewById(R.id.carouseler);
 *然后为其设置图片资源，以四个图片为例
 *	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
 *	bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic1));
 *	bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic2));
 *	bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic3));
 *	bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pic4));
 *	carouselerView.setImageResources(bitmaps);
 *之后可以选择性的设置是否开启或关闭自动播放，默认开启，若不想开启，只需调用如下方法传入参数false即可
 *	carouselerView.setAutoSlide(false);
 * </pre>
 * @author 金戈大王
 * @version 0.1
 *
 */
public class CarouselerView extends LinearLayout {

	private Context context;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private RelativeLayout rtvContainer;
	private List<View> views = new ArrayList<View>();
	private ImageView[] dots;
	private int currentIndex;
	private int currentPosition;
	private ArrayList<Bitmap> bitmaps;
	private Timer timer;
	private TimerHandler timerHandler;
	
	/*
	 * Listener interface, which you can implement to handle onClick event of the images
	 */
	public interface CarouselerViewListener {
		void onImageClick(int position, View imageView);
	}
	
/**
 * 用户代码构造方式的构造方法
 * @param context 视图所在的上下文对象
 */
	public CarouselerView(Context context) {
		super(context);
		this.context = context;
		
		//initial views
		initViews();
		
		//initial auto slide
		initAutoSlide();
	}
	
/**
 * xml方式的构造方法，系统自动调用
 * @param context 视图所在的上下文对象
 * @param attrs xml中的属性对象
 */
	public CarouselerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		//inflate from xml
		LayoutInflater.from(context).inflate(R.layout.carouseler, this);
		
		//initial views
		initViews();
		
		//initial auto slide
		initAutoSlide();
	}

	private void initViews() {
		//控件初始化
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		rtvContainer = (RelativeLayout) findViewById(R.id.rtv_container);
	}
	
	private void initAutoSlide() {
		timerHandler = new TimerHandler();
		timer = new Timer();
	}
	
	private class TimerHandler extends Handler {
		public void handleMessage(android.os.Message msg) {
			int currPos = viewPager.getCurrentItem();
			if(currPos + 1 < views.size()) {
				++currPos;
			}
			else {
				currPos = 0;
			}
			viewPager.setCurrentItem(currPos, true);
			int index = currPos;
			if (index == views.size() - 1) {
				setCurDot(0);
			}
			else if (index == 0) {
				setCurDot(views.size() - 2);
			}
			else {
				setCurDot(index - 1);
			}
			
		}
	}
	
	private class SlideTimerTask extends TimerTask {
		@Override
		public void run() {
			timerHandler.sendEmptyMessage(0);
		}
	}

/**
 * 设置CarouselerView的图像资源，设置后自动从第一张开始播放
 * @param bitmaps Bitmap数组作为图像资源
 */
	public void setImageResources(final ArrayList<Bitmap> bitmaps) {
		setImageResources(bitmaps, null);
	}

	/*
	 * Set images to this view. Pass a CarouselerViewListener to this view. After this method finished, the images will show, and click event enables.
	 */
	public void setImageResources(final ArrayList<Bitmap> bitmaps, CarouselerViewListener carouselerViewListener) {
		
		this.bitmaps = bitmaps;
		
		//handle exception
		if (bitmaps == null) {
			throw new BitmapsNullException("parameter 'bitmaps' must not be null");
		}
		
		//add bitmaps to the view
		//attention: two more ImageView than Bitmap, one in the front and another in the back, used for infinite circular.
		views = new ArrayList<View>();
		ImageView imageView = new ImageView(context);
		imageView.setImageBitmap(bitmaps.get(bitmaps.size() - 1));
		imageView.setTag(bitmaps.size() - 1);
		views.add(imageView);
		for(int i=0; i<bitmaps.size(); ++i) {
			imageView = new ImageView(context);
			imageView.setImageBitmap(bitmaps.get(i));
			imageView.setTag(i);
			views.add(imageView);
		}
		imageView = new ImageView(context);
		imageView.setImageBitmap(bitmaps.get(0));
		imageView.setTag(0);
		views.add(imageView);
		
		//以下代码用于在控件加载完成后调整它的大小，当本控件高度小于宽度时，按横屏模式处理（即横向同时铺展多个图片）
		//adjust the size of viewPager. If its height is less than its width, we handle it as horizontal mode(more than one image in the screen at the same time)
		ViewTreeObserver vto = viewPager.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int height = viewPager.getHeight();
				int width = viewPager.getWidth();
				RelativeLayout.LayoutParams viewPagerLayoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
				if(height < width) {
					viewPagerLayoutParams.leftMargin = (width - height) / 2;
					viewPagerLayoutParams.rightMargin = (width - height) / 2;
					viewPager.setLayoutParams(viewPagerLayoutParams);
				}
			}
		});
		
		//为多个图片同时显示在viewPager中做准备，主要是显示超出控件的部分和touch事件分发
		viewPagerAdapter = new ViewPagerAdapter(views, carouselerViewListener);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setPageMargin(10);
		rtvContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return viewPager.dispatchTouchEvent(event);
			}
		});
		
        //绑定回调
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		
		//初始化底部圆点
		initDots();

		//设置初始位置
		viewPager.setCurrentItem(1, false);
		//默认启动自动滚动
		setAutoSlide(true);
	}
	
/**
 * 用于设置是否启动自动滚动模式。请在setImageResources之后调用该方法，否则设置不生效
 * @param autoSlide true则启动自动滚动，false则关闭自动滚动
 */
	public void setAutoSlide(boolean autoSlide) {
		if (autoSlide) {
			timer.cancel();
			timer = new Timer();
			timer.schedule(new SlideTimerTask(), 3000, 3000);
		}
		else {
			timer.cancel();
		}
	}
	
	private void initDots() {
		LinearLayout dotContainer = (LinearLayout) findViewById(R.id.dot_container);

		dots = new ImageView[bitmaps.size()];

		//循环取得小点图片
		for (int i = 0; i < bitmaps.size(); i++) {
			ImageView dot = new ImageView(context);
			dot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			dot.setImageResource(R.drawable.dot);
			int padding = 15;
			dot.setPadding(padding, padding, padding, padding);
			
			dot.setEnabled(true);//都设为灰色
			dot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCurView((int) v.getTag());
					setCurDot((int) v.getTag());
				}
			});
			dot.setTag(i);//设置位置tag，方便取出与当前位置对应
			dotContainer.addView(dot);
			dots[i] = dot;
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

	private void setCurDot(int index) {
		if (index < 0 || index > bitmaps.size() - 1 || currentIndex == index) {
			return;
		}
		dots[index].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = index;
	}

	private void setCurView(int index) {
		if (index < 0 || index >= bitmaps.size()) {
			return;
		}
		viewPager.setCurrentItem(index);
	}

	/*
	 * ViewPager的页面改变监听器，该监听器中实现了循环播放的功能
	 */
	private final class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				if (currentPosition == views.size() - 1) {
					viewPager.setCurrentItem(1, false);
					setCurDot(0);
				}
				else if (currentPosition == 0) {
					viewPager.setCurrentItem(views.size() - 2, false);
					setCurDot(bitmaps.size() - 1);
				}
				else {
					setCurDot(currentPosition - 1);
				}
			}
		}

		@Override
		public void onPageScrolled(int scrolledPosition, float percent, int pixels) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == views.size() - 1) {
				setCurDot(0);
			}
			else if (position == 0) {
				setCurDot(bitmaps.size() - 1);
			}
			else {
				setCurDot(position - 1);
			}
			currentPosition = position;
		}

	}
	
}
