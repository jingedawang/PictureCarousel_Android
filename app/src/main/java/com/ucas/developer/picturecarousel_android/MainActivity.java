package com.ucas.developer.picturecarousel_android;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private CarouselerView carouselerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button start = (Button) findViewById(R.id.button1);
		Button stop = (Button) findViewById(R.id.btn_autoSlide);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				carouselerView.setAutoSlide(true);
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				carouselerView.setAutoSlide(false);
			}
		});
		
		carouselerView = (CarouselerView) findViewById(R.id.carouseler);
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye1));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye2));
		bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.faye3));
		carouselerView.setImageResources(bitmaps);
		carouselerView.setAutoSlide(false);
	}
	
}
