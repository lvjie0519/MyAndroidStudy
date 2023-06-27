package com.example.rtsp.client.player;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.example.rtsp.client.player.anim.ScaleRotateEndAnimator;
import com.example.rtsp.client.player.controller.VideoPlayController;

import java.io.IOException;

/**
 * MediaPlayer + TextureView 播放视频
 * <p>
 *
 * @author yinxuming
 * @date 2020/7/6
 */
public class SimpleTextureViewPlayer extends TextureView implements TextureView.SurfaceTextureListener,
        VideoPlayController {
    private static final String TAG = "SimpleTextureViewPlayer";

    private MediaPlayer mMediaPlayer;
    SurfaceTexture mSurfaceTexture;
    Surface mSurface;
    String mPlayUrl;
    private MediaController mMediaController;


    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    private int mCurrentDegree = 0;
    private int mVideoWidth = 473, mVideoHeight = 840;


    public SimpleTextureViewPlayer(Context context) {
        this(context, null);
    }

    public SimpleTextureViewPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTextureViewPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setSurfaceTextureListener(this);
    }

    private void initMedia() {

        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaPlayer = null;
        }

        mMediaPlayer = new MediaPlayer();
        updateSurface(mSurface);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                if (getVisibility() != VISIBLE) {
//                    setVisibility(View.VISIBLE);
////                    mTextureView.requestLayout(mp.getVideoWidth(), mp.getVideoHeight());
//                }
//                mp.start();

                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared(mp);
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError " + mp + ", what=" + what + ", " + extra);

                return true;
            }
        });

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //此方法获取的是缓冲的状态
                Log.e(TAG, "缓冲中:" + percent);
            }
        });

        //播放完成的监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion " + mp);
//                    mState = VideoState.init;
                if (mOnCompletionListener != null) {
                    mOnCompletionListener.onCompletion(mp);
                }
            }
        });

        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d(TAG, "onVideoSizeChanged w=" + width + ", h=" + height);
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                if (mOnVideoSizeChangedListener != null) {
                    mOnVideoSizeChangedListener.onVideoSizeChanged(mp, width, height);
                }/* else {
                    updateVideoSize(mVideoWidth, mVideoHeight);
                }*/
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        Log.i(TAG, "onMeasure call, width: " + width + ", height: " + height+", "+(mCurrentDegree % 180));
//
//        if (mCurrentDegree % 180 == 0) {
//            setMeasuredDimension(mVideoWidth, mVideoHeight);
////            super.onMeasure(MeasureSpec.makeMeasureSpec(mVideoWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mVideoHeight, MeasureSpec.EXACTLY));
//        } else {
////            super.onMeasure(MeasureSpec.makeMeasureSpec(mVideoHeight, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mVideoWidth, MeasureSpec.EXACTLY));
//            setMeasuredDimension(mVideoHeight, mVideoWidth);
//        }

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable surface=" + surface + ", w=" + width + ", h=" + height);
        mSurfaceTexture = surface;
        mSurface = new Surface(mSurfaceTexture);
        updateSurface(mSurface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureSizeChanged surface=" + surface + ", w=" + width + ", h=" + height);

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureDestroyed surface=" + surface);
        updateSurface(null);
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        Log.d(TAG, "onSurfaceTextureUpdated surface=" + surface); // 会不断回调

    }

    private void updateSurface(Surface surface) {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void start() {
        if (isPlayerReady()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        return isPlayerReady() ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        return isPlayerReady() ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void seekTo(int pos) {
        if (isPlayerReady()) {
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlayerReady() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

//    public void bindTextureView(HkTextureView textureView) {
//        mTextureView = textureView;
//        mTextureView.setSurfaceTextureListener(this);
//    }

    public void startPlay(String url) {
        mPlayUrl = url;
        try {
            initMedia();
            mMediaPlayer.setDataSource(mPlayUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
        updateSurface(mSurface);
    }

    public String getPlayUrl() {
        return mPlayUrl;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        mOnPreparedListener = onPreparedListener;
    }

    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        mOnVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    /**
     * 缩放画面尺寸：根据视频匡高比例，以及显示区域匡高比例，取最小比例值，缩放
     *
     * @param width
     * @param height
     */
    public void updateVideoSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        int surWidth = getWidth();
        int surHeight = getHeight();
        if (surHeight <= 0 || surHeight <= 0) {
            return;
        }
        Log.d(TAG, "video:(" + width + ", " + height + "), view:(" + surWidth + ", " + surHeight + ")");
        // 等比例缩放
        int wSca = surWidth / width;
        int hSca = surHeight / height;

        int scale = Math.min(wSca, hSca);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = scale * width;
        params.height = scale * height;
        setLayoutParams(params);
    }


    // 按键相关
    public void setMediaController(MediaController controller) {
        mMediaController = controller;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                } else {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer.isPlaying()) {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisiblity();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                && isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return super.onTrackballEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                && isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return super.onTouchEvent(ev);
    }

    private boolean isInPlaybackState() {
        return true;
    }

    private void toggleMediaControlsVisiblity() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show();
        }
    }

    private boolean isPlayerReady() {
        return mMediaPlayer != null;
    }

    public void postRotate(float rotateDegree) {
//        Matrix matrix = getTransformMatrix();
//        matrix.postRotate(rotateDegree, getWidth()/2, getHeight()/2);
//        updateMatrixToTexture(matrix);

        mCurrentDegree = (int) rotateDegree;
        postAnima(rotateDegree);
    }

    private Matrix getTransformMatrix() {
        if (isAvailable()) {
            return getTransform(null);
        }
        return null;
    }

    private void updateMatrixToTexture(Matrix newMatrix) {
        if (isAvailable()) {
            setTransform(newMatrix);
            if (!isPlaying()) {
                invalidate();
            }
        }
    }

    public void postAnima(float rotateDegree) {
        Matrix currentTransformMatrix = getTransform(null);

        Matrix endAnimMatrix = new Matrix();
        RectF videoRectF = new RectF(0, 0, getWidth(), getHeight());
        PointF center = new PointF(videoRectF.right / 2, videoRectF.bottom / 2);
        endAnimMatrix.set(currentTransformMatrix);
        endAnimMatrix.postRotate(rotateDegree, center.x, center.y);
//        endAnimMatrix.postRotate(rotateDegree, 0, 0);

        RectF currentLocationRectF = new RectF(0, 0, getWidth(), getHeight());
        // 测量画面最终应该进行的矩阵变换位置
        endAnimMatrix.mapRect(currentLocationRectF);

//        endAnimMatrix.postTranslate(transAnimX, transAnimY);

        ScaleRotateEndAnimator animator = new ScaleRotateEndAnimator() {
            @Override
            protected void updateMatrixToView(Matrix transMatrix) {
                Log.i(TAG, "updateMatrixToView call, "+transMatrix.toString());
                setTransform(transMatrix);
            }

            @Override
            protected void onFixEndAnim(ValueAnimator animator, float fixEndDegrees) {
                Log.i(TAG, "onFixEndAnim call, "+fixEndDegrees);
                animator = null;
//                requestLayout();
            }
        };
        animator.setScaleEndAnimParams(currentTransformMatrix, endAnimMatrix, 90);
        animator.start();
    }
}
