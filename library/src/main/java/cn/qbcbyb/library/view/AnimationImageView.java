package cn.qbcbyb.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.qbcbyb.library.R;

public class AnimationImageView extends ImageView {

    public static interface AnimationImageViewFinish {
        public void onFinish();
    }

    public static class AnimationDrawableItem {
        private int drawable;
        private int duration;

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public AnimationDrawableItem(int drawable, int duration) {
            this.drawable = drawable;
            this.duration = duration;
        }
    }

    private List<AnimationDrawableItem> animationDrawable;

    private boolean isRunning = false;
    private int imagePosition = 0;
    private AnimationImageViewFinish animationImageViewFinish;
    private Drawable nowDrawable = null;

    private Runnable run = new Runnable() {

        @Override
        public void run() {
            if (animationDrawable != null) {
                int imageCount = animationDrawable.size();
                if (animationDrawable != null && imagePosition < imageCount) {
                    AnimationDrawableItem animationDrawableItem = animationDrawable.get(imagePosition);
                    if (nowDrawable == null) {
                        AnimationImageView.this.setImageResource(animationDrawableItem.getDrawable());
                    } else {
                        AnimationImageView.this.setImageDrawable(nowDrawable);
                    }
                    nowDrawable = null;

                    if (isRunning) {
                        postDelayed(run, animationDrawableItem.getDuration());
                    }

                    imagePosition++;
                    if (imagePosition >= imageCount) {
                        imagePosition = 0;
                        if (animationImageViewFinish != null) {
                            animationImageViewFinish.onFinish();
                        }
                    }
                    post(new Runnable() {
                        public void run() {
                            nowDrawable = getResources()
                                    .getDrawable(animationDrawable.get(imagePosition).getDrawable());
                        }
                    });
                }
            }
        }

    };

    public AnimationImageViewFinish getAnimationImageViewFinish() {
        return animationImageViewFinish;
    }

    public void setAnimationImageViewFinish(AnimationImageViewFinish animationImageViewFinish) {
        this.animationImageViewFinish = animationImageViewFinish;
    }

    public AnimationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    public AnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public AnimationImageView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AnimationImageView, defStyle,
                    defStyle);
            XmlResourceParser parser = getResources().getXml(
                    a.getResourceId(R.styleable.AnimationImageView_animationDrawable, 0));
            if (parser != null) {
                List<AnimationDrawableItem> animation_list = null;
                AnimationDrawableItem item = null;
                try {
                    int event = parser.getEventType();// 产生第一个事件
                    while (event != XmlResourceParser.END_DOCUMENT) {
                        switch (event) {
                            // case XmlResourceParser.START_DOCUMENT://
                            // 判断当前事件是否是文档开始事件
                            // break;
                            case XmlResourceParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                                if ("item".equals(parser.getName())) {// 判断开始标签元素是否是item
                                    int drawable = parser.getAttributeResourceValue(null, "drawable", 0);
                                    int duration = Integer.parseInt(parser.getAttributeValue(null, "duration"));
                                    item = new AnimationDrawableItem(drawable, duration);
                                } else if ("animation-list".equals(parser.getName())) {
                                    animation_list = new ArrayList<AnimationDrawableItem>();// 初始化animation-list集合
                                }
                                break;
                            case XmlResourceParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                                if ("item".equals(parser.getName())) {// 判断结束标签元素是否是item
                                    animation_list.add(item);
                                }
                                break;
                        }
                        event = parser.next();// 进入下一个元素并触发相应事件
                    }// end while
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setAnimationDrawable(animation_list);
            }
            if (a != null) {
                a.recycle();
            }
        }
    }

    public void setImageList(int[] imageList, int duration) {
        if (imageList != null && imageList.length > 0) {
            List<AnimationDrawableItem> animation = new ArrayList<AnimationDrawableItem>();
            for (int resId : imageList) {
                animation.add(new AnimationDrawableItem(resId, duration));
            }
            setAnimationDrawable(animation);
        }
    }

    public List<AnimationDrawableItem> getAnimationDrawable() {
        return animationDrawable;
    }

    public void setAnimationDrawable(List<AnimationDrawableItem> animationDrawable) {
        this.animationDrawable = animationDrawable;
        start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void start() {
        if (!isRunning) {
            removeCallbacks(run);
            isRunning = true;
            post(run);
        }
    }

    public void stop() {
        removeCallbacks(run);
        isRunning = false;
        imagePosition = 0;
    }

}
