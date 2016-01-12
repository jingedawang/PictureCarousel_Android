package com.ucas.developer.picturecarousel_android;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ucas.developer.picturecarousel_android.CarouselerView.CarouselerViewListener;

public class MainActivity extends Activity {

	private CarouselerView carouselerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		carouselerView = (CarouselerView) findViewById(R.id.carouseler);
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye1));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye2));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye3));
		carouselerView.setImageResources(bitmaps, new CarouselerViewListener() {
			@Override
			public void onImageClick(int position, View imageView) {
				//在此写图片单击事件
				Toast.makeText(getApplicationContext(), "第"+position+"张图片", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
