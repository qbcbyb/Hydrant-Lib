package cn.qbcbyb.lib;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.qbcbyb.library.activity.BaseActivity;
import cn.qbcbyb.library.util.Msg;
import cn.qbcbyb.library.view.ProgressDialogCustom;

/**
 * Created by qbcby on 2016/1/8.
 */
public class WebViewActivity extends BaseActivity {

    ProgressBar progressWebView;
    WebView webView;

    @Override
    protected void doCreate() {
        setContentView(R.layout.activity_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressWebView = (ProgressBar) findViewById(R.id.progressWebView);
        webView = (WebView) findViewById(R.id.webView);

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webView.addJavascriptInterface(new JavaCallback(), "BCWebContainer");

        loadUrl("http://115.28.110.211:3001/test/redirect/joinOrg");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            webView.reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Msg.confirm(context, url, message, new Msg.MsgCallback() {
                @Override
                public void callback(DialogInterface dialogInterface, Msg.Which which) {
                    result.confirm();
                }
            });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Msg.confirm(context, url, message, new Msg.MsgCallback() {
                @Override
                public void callback(DialogInterface dialogInterface, Msg.Which which) {
                    if (which == Msg.Which.POSITIVE) {
                        result.confirm();
                    } else {
                        result.cancel();
                    }
                }
            });
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return true;
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressWebView.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == progressWebView.getVisibility()) {
                    progressWebView.setVisibility(View.VISIBLE);
                }
                progressWebView.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class JavaCallback {
        @JavascriptInterface
        public void uploadFile(final String callbackMethod) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ProgressDialogCustom dialogCustom = new ProgressDialogCustom(context);
                    dialogCustom.show();
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:" + callbackMethod + "('" + "testFileName" + "');");
                            dialogCustom.dismiss();
                        }
                    }, 2000);
                }
            });
        }
    }
}
