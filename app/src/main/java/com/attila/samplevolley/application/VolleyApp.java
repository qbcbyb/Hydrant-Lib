package com.attila.samplevolley.application;

import com.android.volley.toolbox.Volley;

import android.app.Application;

public class VolleyApp extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		VolleyHandler.intializeRequestQueue(VolleyApp.this);
	}
	

}
