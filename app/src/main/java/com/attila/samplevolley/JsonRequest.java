package com.attila.samplevolley;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.attila.samplevolley.application.VolleyHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cn.qbcbyb.lib.R;

public class JsonRequest extends Activity {
    TextView tvName;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_request);
        tvName = (TextView) findViewById(R.id.textview_name);

        queue = VolleyHandler.getRequestQueue();

        MainActivity.showProgressDialog(JsonRequest.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Method.GET,
                Constants.JSON_URL,
                (String) null,
                SuccessListener(),
                ErrorListener()){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    final Cache.Entry entry = new Cache.Entry();
                    entry.data=response.data;
                    return Response.success(new JSONObject(jsonString),entry);
//                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };

        jsonRequest.setTag("jsonrequestTag");
        queue.add(jsonRequest);
    }


    private Response.Listener<JSONObject> SuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MainActivity.dismissProgressDialog();
                try {
                    tvName.setText(response.getString("title"));
                } catch (JSONException e) {
                    tvName.setText("error");
                }
            }
        };
    }


    private Response.ErrorListener ErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.dismissProgressDialog();
                tvName.setText(error.getMessage());
            }
        };
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        queue.cancelAll("jsonrequestTag");
    }


}
