package com.tencent.weibo.sdk.android.api.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.io.InputStream;

public class BackGroudSeletor {
    static int[] PRESSED_ENABLED_STATE_SET = {16842910, 16842919};// debug出的该变量在view中的值
    static int[] ENABLED_STATE_SET = {16842910};
    static int[] EMPTY_STATE_SET = {};

    private BackGroudSeletor() {
    }

    /**
     * 该方法主要是构建StateListDrawable对象，以StateListDrawable来设置图片状态，来表现View的各中状态：未选中，按下
     * ，选中效果
     *
     * @param imagename 选中和未选中使用的两张图片名称
     * @param context   上下文
     */
    public static StateListDrawable createBgByImageIds(String[] imagename,
                                                       Context context) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = getdrawble(imagename[0], context);
        Drawable pressed = getdrawble(imagename[1], context);
        bg.addState(PRESSED_ENABLED_STATE_SET, pressed);
        bg.addState(ENABLED_STATE_SET, normal);
        bg.addState(EMPTY_STATE_SET, normal);
        return bg;
    }

    /**
     * 该方法主要根据图片名称获取可用的 Drawable
     *
     * @param imagename 选中和未选中使用的两张图片名称
     * @param context   上下文
     * @return 可用的Drawable
     */
    public static Drawable getdrawble(String imagename, Context context) {
        Drawable drawable = null;
        int id = context.getResources().getIdentifier(imagename, "drawable", context.getPackageName());
        drawable = context.getResources().getDrawable(id);
        return drawable;
    }

    public static InputStream zipPic(InputStream is) {

        return null;
    }


}
