package com.mundosica.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityHelper extends Activity {
	
	public ProgressDialog simpleWaitDialog;
	int alto  = 0;
	int ancho = 0;
	
	public  int porciento(int cien, int porciento) {
		return (int)((cien / 100) * porciento);
	}
	
	public  Button button(FrameLayout frame,
			int left,int top,int x,int y,String texto) {
		FrameLayout.LayoutParams params = frameParams (left, top,x,y);
		Button boton = new Button(this);
		boton.setText(texto);
		boton.setLayoutParams(params);
		//boton.setTypeface(Typeface.createFromAsset(actividad.getAssets(), fontPathRegular));
		frame.addView(boton,params);
		return boton;
	}
	
	public  Button button(RelativeLayout frame,
			int left,int top,int x,int y,String texto) {
		RelativeLayout.LayoutParams params = relativeParams (left, top,x,y);
		Button boton = new Button(this);
		boton.setText(texto);
		boton.setLayoutParams(params);
		//boton.setTypeface(Typeface.createFromAsset(actividad.getAssets(), fontPathRegular));
		frame.addView(boton,params);
		return boton;
	}
	
	public void tamanoDisplay(Activity activity) {
		DisplayMetrics dm = activity.getResources().getDisplayMetrics(); 
		ancho = dm.widthPixels;
		alto = dm.heightPixels;
	}
	public  RelativeLayout.LayoutParams relativeParams(int left, int top, int x, int y) {
		tamanoDisplay(this);
		x = porciento(ancho,x);
		y = porciento(alto,y);
		left = porciento(ancho,left);
		top = porciento(alto,top);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (x,y);
		params.setMargins(left, top, 0, 0);
		return params;
	}
	
	public  FrameLayout.LayoutParams frameParams(int left, int top, int x, int y) {
		tamanoDisplay(this);
		x = porciento(ancho,x);
		y = porciento(alto,y);
		left = porciento(ancho,left);
		top = porciento(alto,top);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (x,y);
		params.setMargins(left, top, 0, 0);
		return params;
	}
	
	public  TextView textView(FrameLayout frame,
			int left,int top,int x,int y,String texto) {
		FrameLayout.LayoutParams params = frameParams (left, top,x,y);
		TextView textView = new TextView(this);
		textView.setLayoutParams(params);
		textView.setText(texto);
		//textView.setBackgroundColor(Color.BLUE);
		//textView.setTypeface(Typeface.createFromAsset(actividad.getAssets(), fontPathRegular));
		frame.addView(textView,params);
		return textView;
	}
	public  TextView textView(RelativeLayout frame,
			int left,int top,int x,int y,String texto) {
		RelativeLayout.LayoutParams params = relativeParams(left,top,x,y);
		TextView textView = new TextView(this);
		textView.setLayoutParams(params);
		textView.setText(texto);
		//textView.setTypeface(Typeface.createFromAsset(actividad.getAssets(), fontPathRegular));
		frame.addView(textView,params);
		return textView;
	}
	
	public  EditText editTextView(FrameLayout frame,
			int left,int top,int x,int y) {
		FrameLayout.LayoutParams params = frameParams (left, top,x,y);
		EditText text = new EditText(this);
		text.setLayoutParams(params);
		//text.setBackgroundColor(Color.BLUE);
		frame.addView(text,params);
		return text;
	}
	
	public  EditText editTextView(RelativeLayout frame,
			int left,int top,int x,int y) {
		RelativeLayout.LayoutParams params = relativeParams (left, top,x,y);
		EditText text = new EditText(this);
		text.setLayoutParams(params);
		//text.setBackgroundColor(Color.BLUE);
		frame.addView(text,params);
		return text;
	}
	
	public ImageView imageView(RelativeLayout frame,int resource,
	int left,int top,int x,int y) {
		RelativeLayout.LayoutParams params = relativeParams (left, top,x,y);
		ImageView image = new ImageView(this);
		image.setLayoutParams(params);
		image.setImageResource(resource);
		//text.setBackgroundColor(Color.BLUE);
		frame.addView(image,params);
		return image;
	}
	
}
