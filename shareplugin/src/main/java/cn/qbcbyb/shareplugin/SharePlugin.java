package cn.qbcbyb.shareplugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.weibo.sdk.android.api.UserAPI;
import com.tencent.weibo.sdk.android.api.util.SharePersistent;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.PublishBitmapActivity;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;

import java.text.MessageFormat;

/**
 * Created by 秋云 on 2014/9/16.
 */
public class SharePlugin {

    public static class ShareKey {
        private String appKey;
        private String appSecret;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public static ShareKey fromString(String keyString) {
            if (keyString == null) {
                return null;
            }
            ShareKey shareKey = new ShareKey();
            String[] keyStrs = keyString.split("\n");
            shareKey.setAppKey(keyStrs[0]);
            if (keyStrs.length > 1) {
                shareKey.setAppSecret(keyStrs[1]);
            }
            return shareKey;
        }
    }

    private static final Object lock = new Object();
    private static SharePlugin instance;

    public static SharePlugin getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new SharePlugin();
            }
            return instance;
        }
    }

    private IWeiboShareAPI mWeiboShareAPI = null;
    private IWXAPI mWeixinShareAPI = null;

    private SharePlugin() {
    }

    public void sendWeixinLoginRequest(Context context) {
        if (mWeixinShareAPI == null) {
            throw new IllegalArgumentException("WXAPI is null");
        }
        if (!mWeixinShareAPI.isWXAppInstalled()) {
            throw new IllegalAccessError("wx is not installed");
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = context.getApplicationContext().getPackageName();
        mWeixinShareAPI.sendReq(req);
    }

    public void registerTencent(final Context context) {
        Util.AppAuthConfig config = Util.getConfig(context);
        final Long appid = config.getApp_key();
        String app_secket = config.getApp_key_sec();
        if (appid == null || app_secket == null) {
            return;
        }

        AuthHelper.register(context, appid, app_secket, new OnAuthListener() {

            //如果当前设备没有安装腾讯微博客户端，走这里
            @Override
            public void onWeiBoNotInstalled() {
//                Toast.makeText(context, "onWeiBoNotInstalled", 1000).show();
                AuthHelper.unregister(context);
                Intent i = new Intent(context, Authorize.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

            //如果当前设备没安装指定版本的微博客户端，走这里
            @Override
            public void onWeiboVersionMisMatch() {
//                Toast.makeText(context, "onWeiboVersionMisMatch",1000).show();
                AuthHelper.unregister(context);
                Intent i = new Intent(context, Authorize.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

            //如果授权失败，走这里
            @Override
            public void onAuthFail(int result, String err) {
//                Toast.makeText(context, "result : " + result, 1000).show();
                AuthHelper.unregister(context);
            }

            //授权成功，走这里
            //授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
            //在这里，存放到了applicationcontext中
            @Override
            public void onAuthPassed(String name, WeiboToken token) {
//                Toast.makeText(context, "passed", 1000).show();
                //
                Util.saveSharePersistent(context, "ACCESS_TOKEN", token.accessToken);
                Util.saveSharePersistent(context, "EXPIRES_IN", String.valueOf(token.expiresIn));
                Util.saveSharePersistent(context, "OPEN_ID", token.openID);
//				Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
                Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
//				Util.saveSharePersistent(context, "NAME", name);
//				Util.saveSharePersistent(context, "NICK", name);
                Util.saveSharePersistent(context, "CLIENT_ID", appid);
                Util.saveSharePersistent(context, "AUTHORIZETIME",
                        String.valueOf(System.currentTimeMillis() / 1000l));
                AuthHelper.unregister(context);
            }
        });

        AuthHelper.auth(context, "");
    }

    public void registerWeibo(Context context) {
        String keyString = context.getString(R.string.share_weibo_app_key);
        String appKey = ShareKey.fromString(keyString).getAppKey();
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appKey);
        mWeiboShareAPI.registerApp();
    }

    public void registerWeixin(Context context) {
        String keyString = context.getString(R.string.share_weixin_app_key);
        String appKey = ShareKey.fromString(keyString).getAppKey();
        mWeixinShareAPI = WXAPIFactory.createWXAPI(context, appKey);
        mWeixinShareAPI.registerApp(appKey);
    }

    public void handleResponse(Intent intent, IWeiboHandler.Response response, IWXAPIEventHandler iwxapiEventHandler) {
        if (intent != null) {
            if (mWeiboShareAPI != null) {
                mWeiboShareAPI.handleWeiboResponse(intent, response);
            }
            if (mWeixinShareAPI != null) {
                mWeixinShareAPI.handleIntent(intent, iwxapiEventHandler);
            }
        }
    }

    public void shareToWeiXin(String title, String description, String webUrl, Bitmap bitmap) {
        shareToWeiXin(SendMessageToWX.Req.WXSceneSession, title, description, webUrl, bitmap);
    }

    public void shareToPengYouQuan(String title, String description, String webUrl, Bitmap bitmap) {
        shareToWeiXin(SendMessageToWX.Req.WXSceneTimeline, title, description, webUrl, bitmap);
    }

    private void shareToWeiXin(int secene, String title, String description, String webUrl, Bitmap bitmap) {
        if (mWeixinShareAPI == null) {
            throw new IllegalArgumentException("WXAPI is null");
        }
        if (!mWeixinShareAPI.isWXAppInstalled()) {
            throw new IllegalAccessError("wx is not installed");
        }
        if (!mWeixinShareAPI.isWXAppSupportAPI()) {
            throw new IllegalAccessError("wxapi is not support");
        }
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = webUrl;
//        WXTextObject wxTextObject=new WXTextObject();
//        wxTextObject.text=webUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxWebpageObject;//wxTextObject;//
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("shareurl");
        req.message = msg;
        req.scene = secene;
        mWeixinShareAPI.sendReq(req);
    }

    public void shareToWeiBo(Context context, String title, String description, String webUrl, Bitmap bitmap) {
        registerWeibo(context);
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            return;
//                        mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//                            @Override
//                            public void onCancel() {
//                            }
//                        });
        }
        if (mWeiboShareAPI.checkEnvironment(true)) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();

                // 1. 初始化微博的分享消息
                WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = title;
                mediaObject.description = description;

                // 设置 Bitmap 类型的图片到对象里
                mediaObject.setThumbImage(bitmap);
                mediaObject.actionUrl = webUrl;
                mediaObject.defaultText = "";

                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {

                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.mediaObject = mediaObject;

                    // 2. 初始化从第三方到微博的消息请求
                    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                    // 用transaction唯一标识一个请求
                    request.transaction = String.valueOf(System.currentTimeMillis());
                    request.multiMessage = weiboMessage;

                    // 3. 发送请求消息到微博，唤起微博分享界面
                    mWeiboShareAPI.sendRequest(request);
                } else {
                    WeiboMessage weiboMessage = new WeiboMessage();
                    weiboMessage.mediaObject = mediaObject;
                    // 2. 初始化从第三方到微博的消息请求
                    SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
                    // 用transaction唯一标识一个请求
                    request.transaction = buildTransaction("shareurl");
                    request.message = weiboMessage;

                    // 3. 发送请求消息到微博，唤起微博分享界面
                    mWeiboShareAPI.sendRequest(request);
                }
            } else {
// TODO           Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareToTecent(Context context, String title, String description, String webUrl, Bitmap bitmap) {
        try {
            AccountModel accountModel = SharePersistent.getInstance().getAccount(context);
            UserAPI userAPI = new UserAPI(accountModel);
            if (userAPI.isAuthorizeExpired(context)) {
                registerTencent(context);
                return;
            }
        } catch (Exception e) {
            registerTencent(context);
            return;
        }

        Intent i = new Intent(context, PublishBitmapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("content", MessageFormat.format("#{0}#{1}{2}", title, description, webUrl));
        if (bitmap != null) {
            bundle.putParcelable("pic", bitmap);
        }
        i.putExtras(bundle);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
