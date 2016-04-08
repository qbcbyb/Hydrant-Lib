package com.attila.samplevolley;



import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.attila.samplevolley.application.VolleyHandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.qbcbyb.lib.R;

public class ImageLoading extends Activity{
	ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_loader);
		imageView = (ImageView) findViewById(R.id.imageview_image_loader);
		//mRequestQueue.getCache().get("key");
		//MainActivity.showProgressDialog(ImageLoading.this);
//		BitmapLruCache bitmapLruCache = new BitmapLruCache(VolleyHandler.cacheSize);
//		Bitmap image = bitmapLruCache.getBitmap(Constants.IMAGE_URL);
//		
//		if(image != null) {
//			imageView.setVisibility(View.VISIBLE);
//			imageView.setImageBitmap(image);
//		}
		
		//else {
		
		ImageLoader imgLoader = VolleyHandler.getImageLoader();
		imgLoader.get(Constants.IMAGE_URL, 
				ImageLoader.getImageListener(imageView, 
                                                     R.drawable.no_avatar, 
                                                     R.drawable.warning));
		
		//}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

}
