# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-assumenosideeffects class android.util.Log { *; }

-keepattributes Signature

-keep,allowobfuscation class * implements java.io.Serializable {
public void set*(...);
public *** get*(...);
public *** is*(...);
public *** has*(...);
public <init>(...);
}
-keepclassmembers class * implements java.io.Serializable {
public void set*(...);
public *** get*(...);
public *** is*(...);
public *** has*(...);
public <init>(...);
}

-keepclassmembers class cn.qbcbyb.lib.WebViewActivity$JavaCallback {
   public *;
}

-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-dontwarn android.support.**

-keep class com.facebook.** {*;}

-dontwarn com.soundcloud.android.crop.**
-dontwarn com.origamilabs.library.views.**
-dontwarn com.qiniu.**
-dontwarn com.zxing.**
-dontwarn com.zbar.lib.**
-dontwarn com.squareup.**
-dontwarn com.alibaba.fastjson.**
-dontwarn com.nineoldandroids.**
-dontwarn com.fourmob.datetimepicker.**
-dontwarn com.sleepbot.datetimepicker.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.sleepbot.datetimepicker.**
-dontwarn com.handmark.pulltorefresh.library.**
-dontwarn com.etsy.android.grid.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn uk.co.senab.photoview.**
-dontwarn org.codehaus.mojo.**
-dontwarn java.nio.file.**

-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.**{*;}

-dontwarn org.apache.**
-keep class org.apache.** { *;}
-dontwarn com.avos.**
-keep class com.avos.** { *;}

-keep class com.tencent.mm.sdk.** { *; }

-dontwarn com.tencent.weibo.sdk.android.**

-keep class com.sina.** { *; }

-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
-keep class com.yuntongxun.ecsdk.**  {* ;}
-keep class org.webrtc.**  {* ;}