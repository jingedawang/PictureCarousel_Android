package com.ucas.developer.picturecarousel_android;

import java.util.List;

import com.ucas.developer.picturecarousel_android.CarouselerView.CarouselerViewListener;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

class ViewPagerAdapter extends PagerAdapter {

	private List<View> views;
	private CarouselerViewListener carouselerViewListener;
	
	public ViewPagerAdapter (List<View> views) {
		this.views = views;
	}
	
	
	public ViewPagerAdapter (List<View> views, CarouselerViewListener carouselerViewListener){
		this.views = views;
		this.carouselerViewListener = carouselerViewListener;
	}

	//销毁position位置的界面
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}

	//获得当前界面数
	@Override
	public int getCount() {
		return views.size();
	}

	//初始化position位置的界面
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view =  views.get(position);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (carouselerViewListener == null) {
					return;
				}
				int realPosition;
				if (position == 0) {
					realPosition = views.size() - 3;
				}
				else if (position == views.size() - 1) {
					realPosition = 0;
				}
				else {
					realPosition = position - 1;
				}
				carouselerViewListener.onImageClick(realPosition, v);
			}
		});
		container.addView(view);
		return view;
	}

	//判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

}
