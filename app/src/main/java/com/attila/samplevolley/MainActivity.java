package com.attila.samplevolley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import cn.qbcbyb.lib.R;

public class MainActivity extends Activity implements OnClickListener {
	Button imageLoader,jsonRequest,gsonParser,loadFromCache,stringRequest;
	static ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_volleytest);
		intializeUI();
		
		imageLoader.setOnClickListener(this);
		jsonRequest.setOnClickListener(this);
		gsonParser.setOnClickListener(this);
		loadFromCache.setOnClickListener(this);
		stringRequest.setOnClickListener(this);
		
		
	}
	
   public void intializeUI() {
	imageLoader = (Button) findViewById(R.id.button_image_loading);
	stringRequest = (Button) findViewById(R.id.button_string_request);
	jsonRequest = (Button) findViewById(R.id.button_json_request);
	gsonParser = (Button) findViewById(R.id.button_gson);
	loadFromCache = (Button) findViewById(R.id.button_cache);
   }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	
	case R.id.button_image_loading: 
		Intent imageIntent = new Intent(MainActivity.this,ImageLoading.class);
		startActivity(imageIntent);		
		break;
		
	case R.id.button_string_request: 
		Intent stringIntent = new Intent(MainActivity.this,SampleStringRequest.class);
		startActivity(stringIntent);		
		break;
		
	case R.id.button_json_request: 
		Intent jsonIntent = new Intent(MainActivity.this,JsonRequest.class);
		startActivity(jsonIntent);
		break;	
		
	case R.id.button_gson: 
//		Intent gsonIntent = new Intent(MainActivity.this,MakeGsonRequest.class);
//		startActivity(gsonIntent);
		break;
		
	case R.id.button_cache: 
		Intent cacheIntent = new Intent(MainActivity.this,CacheHandler.class);
		startActivity(cacheIntent);
		break;
		
	default:
		break;
	}
}

public static void showProgressDialog(Context context) {
	progressDialog = new ProgressDialog(context);
	progressDialog.setTitle("Sample Volley");
	progressDialog.setMessage("Loading...");
	progressDialog.show();
}

public static void dismissProgressDialog() {
	progressDialog.dismiss();
}




}
