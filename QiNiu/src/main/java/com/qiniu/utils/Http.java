package com.qiniu.utils;

import com.qiniu.conf.Conf;

import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.params.ConnManagerParams;
import cz.msebera.android.httpclient.conn.params.ConnPerRoute;
import cz.msebera.android.httpclient.conn.params.ConnPerRouteBean;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.CoreConnectionPNames;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;

public class Http {
    private static HttpClient httpClient;

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = buildHttpClient();
        }
        return httpClient;
    }

    private static HttpClient buildHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        ConnPerRoute connPerRoute = new ConnPerRouteBean(3);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory
                .getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(
                httpParams, registry);

        HttpClient httpClient = new DefaultHttpClient(cm, httpParams);

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Conf.SO_TIMEOUT);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Conf.CONNECTION_TIMEOUT);

        return httpClient;
    }

}
