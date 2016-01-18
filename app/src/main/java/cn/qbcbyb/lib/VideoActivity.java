package cn.qbcbyb.lib;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.Random;

import cn.qbcbyb.library.util.StringUtils;

public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {

    private VideoPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video);

        final Random random = new Random(System.currentTimeMillis());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                query(1);
            }
        }, 5000);
        videoPlayer = new VideoPlayer(this);

        setContentView(videoPlayer);

        videoPlayer.setOnErrorListener(this);
        videoPlayer.setOnInfoListener(this);

//        videoPlayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    videoPlayer.setDataSource("http://flv2.mtvxz.cn:83/57380.mp4");//"http://58.30.207.39/158/20/24/letv-gug/17/ver_00_22-33209058-avc-588111-aac-64365-14960-1245045-fad6e6b58fbc875aae9c1cae595fcd5b-1443508561040.letv?crypt=67aa7f2e131400&b=1314&nlh=3072&nlt=45&bf=8000&p2p=1&video_type=mp4&termid=0&tss=no&geo=CN-1-12-15&platid=100&splatid=10000&its=0&qos=5&path=111.206.215.40&proxy=2016684967,2016680317,467484306&keyitem=rxWmhiz4nvsenbC4B_PU1Ho7JhJWOKmfhaz0NA..&ntm=1475912400&nkey=2d31ce24cb1d370bbd517acc4f667d99&nkey2=fdd49e4ef26caadc5ebc5b2fc17b5ae8&mltag=1&gugtype=1&mmsid=35489010&type=pc_gaoqing_mp4&errc=0&gn=383&buss=1&cips=180.78.97.76");//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 1000);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("VideoActivity----", "onError called");
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v("VideoActivity----", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.v("VideoActivity----", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                Log.v("VideoActivity----", "MEDIA_ERROR_OTHER");
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {// 当一些特定信息出现或者警告时触发
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.v("VideoActivity----", "MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Log.v("VideoActivity----", "MEDIA_INFO_METADATA_UPDATE");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.v("VideoActivity----", "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.v("VideoActivity----", "MEDIA_INFO_NOT_SEEKABLE");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                Log.v("VideoActivity----", "MEDIA_INFO_VIDEO_RENDERING_START");
                break;
            default:
                Log.v("VideoActivity----", "MEDIA_INFO_OTHER");
                break;
        }
        return false;
    }

    class VideoPlayer extends RelativeLayout implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

        private LayoutParams surfaceViewLayoutParams;
        private final ProgressBar progressBar;
        private final SurfaceView surfaceView;
        private final ImageView imgPlayBtn;
        private final MediaPlayer player;
        private boolean hasVideoPathSetted;
        private boolean hasSurfaceCreated;
        private boolean hasVideoPrepared;

        public VideoPlayer(Context context) {
            this(context, null);
        }

        public VideoPlayer(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setBackgroundColor(0xff000000);
            surfaceView = new SurfaceView(context);
            surfaceViewLayoutParams = new LayoutParams(-1, -1);
            surfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(surfaceView, surfaceViewLayoutParams);

            progressBar = new ProgressBar(context);
            final LayoutParams params = new LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(progressBar, params);

            imgPlayBtn = new ImageView(context);
            imgPlayBtn.setScaleType(ImageView.ScaleType.CENTER);
            imgPlayBtn.setImageResource(android.R.drawable.ic_media_play);
            addView(imgPlayBtn, new LayoutParams(-1, -1));

            hasVideoPathSetted = false;
            hasSurfaceCreated = false;
            setHasVideoPrepared(false);

            //给SurfaceView添加CallBack监听
            SurfaceHolder holder = surfaceView.getHolder();
            holder.addCallback(this);
            //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            //下面开始实例化MediaPlayer对象
            player = new MediaPlayer();
            player.setOnPreparedListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnCompletionListener(this);

            surfaceView.setOnClickListener(this);

        }

        public void setHasVideoPrepared(boolean hasVideoPrepared) {
            this.hasVideoPrepared = hasVideoPrepared;
            progressBar.setVisibility(hasVideoPrepared ? GONE : VISIBLE);
            imgPlayBtn.setVisibility(!hasVideoPrepared ? GONE : VISIBLE);
        }

        public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
            player.setOnCompletionListener(listener);
        }

        public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener) {
            player.setOnSeekCompleteListener(listener);
        }

        public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
            player.setOnErrorListener(listener);
        }

        public void setOnInfoListener(MediaPlayer.OnInfoListener listener) {
            player.setOnInfoListener(listener);
        }

        public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
            if (StringUtils.isEmpty(path)) {
                throw new IllegalArgumentException("path is null");
            }
            setHasVideoPrepared(false);
            hasVideoPathSetted = true;
            player.setDataSource(path);
            if (hasSurfaceCreated) {
                Log.v("VideoActivity----", "start playing in setDataSource");
                player.prepareAsync();
            }
        }

        public void setVideoSize(int width, int height) {
            surfaceViewLayoutParams.width = width;
            surfaceViewLayoutParams.height = height;
            requestLayout();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 当SurfaceView中的Surface被创建的时候被调用
            //在这里我们指定MediaPlayer在当前的Surface中进行播放
            player.setDisplay(holder);
            setHasVideoPrepared(false);

            hasSurfaceCreated = true;
            //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
            if (hasVideoPathSetted) {
                Log.v("VideoActivity----", "start playing in surfaceCreated");
                player.prepareAsync();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stop();
//            holder.removeCallback(this);
//            player.release();
        }

        @Override
        public void onPrepared(MediaPlayer mp) {// 当prepare完成后，该方法触发，在这里我们播放视频
            setHasVideoPrepared(true);
            player.setScreenOnWhilePlaying(true);
            //然后开始播放视频
            Log.v("VideoActivity----", "onPrepared");
        }


        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

            int maxWidth = getWidth();
            int maxHeight = getHeight();

            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            float wRatio = (float) width / (float) maxWidth;
            float hRatio = (float) height / (float) maxHeight;

            //选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);
            final int toWidth = (int) Math.ceil((float) width / ratio);
            final int toHeight = (int) Math.ceil((float) height / ratio);

            setVideoSize(toWidth, toHeight);
            Log.v("VideoActivity----", toWidth + ",onVideoSizeChanged," + toHeight);
        }

        @Override
        public void onClick(View v) {
            if (v == surfaceView && hasVideoPrepared) {
                if (!player.isPlaying()) {
                    play();
                } else {
                    pause();
                }
            }
        }

        private void play() {
            Log.v("VideoActivity----", "play:" + player.getCurrentPosition());
            player.start();
            imgPlayBtn.setVisibility(GONE);
        }

        private void pause() {
            Log.v("VideoActivity----", "pause:" + player.getCurrentPosition());
            player.pause();
            imgPlayBtn.setVisibility(VISIBLE);
        }

        private void stop() {
            Log.v("VideoActivity----", "stop:" + player.getCurrentPosition());
            player.stop();
            imgPlayBtn.setVisibility(VISIBLE);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v("VideoActivity----", "onCompletion:" + player.getCurrentPosition());
            imgPlayBtn.setVisibility(VISIBLE);
        }
    }
}
