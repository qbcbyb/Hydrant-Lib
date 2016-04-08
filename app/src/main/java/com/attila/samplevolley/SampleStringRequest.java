package com.attila.samplevolley;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.attila.samplevolley.application.VolleyHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.qbcbyb.lib.R;

public class SampleStringRequest extends Activity implements OnClickListener{
	TextView tvStringRequest;
	Button btnMakeRequest;
	RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_string_request);
		tvStringRequest = (TextView) findViewById(R.id.textview_string_request);
		btnMakeRequest = (Button) findViewById(R.id.button_make_string_request);
		btnMakeRequest.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		queue = VolleyHandler.getRequestQueue();
		StringRequest stringRequest = new StringRequest(Method.POST, "http://httpbin.org/post", new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	tvStringRequest.setText(response); // We set the response data in the TextView
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        System.out.println("Error ["+error+"]");

		    }
		}) ;
		stringRequest.setTag("stringRequestTag");
		queue.add(stringRequest);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		queue.cancelAll("stringRequestTag");
	}

}
