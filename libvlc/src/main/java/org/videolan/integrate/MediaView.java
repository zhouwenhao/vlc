package org.videolan.integrate;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

/**
 * Created by zhou on 2018/4/2.
 */

public class MediaView extends SurfaceView implements SurfaceHolder.Callback, MediaPlayer.EventListener{

    private SurfaceHolder mHolder;
    private LibVLC mLivVLC;
    private MediaPlayer mPlayer;
    private IVLCVout mIVLCVout;
    private Media mMedia;
    private String mUrl;
    private IStateLisenter mLisenter;

    public MediaView(Context context) {
        this(context, null);
    }

    public MediaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if (mPlayer == null){
            reCreatePlayer();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        mIVLCVout.setWindowSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        releasePlayer();
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        switch (event.type) {
            case MediaPlayer.Event.MediaChanged:
                break;
            case MediaPlayer.Event.Stopped:
                if (mLisenter != null){
                    mLisenter.onStop();
                }
                break;
            case MediaPlayer.Event.EndReached:
                break;
            case MediaPlayer.Event.EncounteredError:
                if (mLisenter != null){
                    mLisenter.onError();
                }
                break;
            case MediaPlayer.Event.Opening:
                break;
            case MediaPlayer.Event.Buffering:
                if (mLisenter != null && event.getBuffering() >= 100.0f){
                    mLisenter.onBufferReady();
                }
                break;
            case MediaPlayer.Event.Playing:
                if (mLisenter != null){
                    mLisenter.onPlaying();
                }
                break;
            case MediaPlayer.Event.Paused:
                break;
            case MediaPlayer.Event.TimeChanged:
                break;
            case MediaPlayer.Event.LengthChanged:
                break;
            case MediaPlayer.Event.PositionChanged:
                break;
            case MediaPlayer.Event.Vout:
                break;
            case MediaPlayer.Event.ESAdded:
                break;
            case MediaPlayer.Event.ESDeleted:
                break;
            case MediaPlayer.Event.ESSelected:
                break;
            case MediaPlayer.Event.SeekableChanged:
                break;
            case MediaPlayer.Event.PausableChanged:
                break;
        }
    }

    private void init(){
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        reCreatePlayer();
    }

    public void playByNet(String uri){
        mUrl = uri;
        mMedia = new Media(mLivVLC, Uri.parse(mUrl));
        mMedia.setHWDecoderEnabled(true, true);
        mPlayer.setMedia(mMedia);
        mPlayer.play();
    }

    public void playByLocal(String filePath){
        mUrl = filePath;
        mMedia = new Media(mLivVLC, mUrl);
        mMedia.setHWDecoderEnabled(true, true);
        mPlayer.setMedia(mMedia);
        mPlayer.play();
    }

    public void setIStateLisenter(IStateLisenter lisenter){
        mLisenter = lisenter;
    }

    public void rePlay(){
        if (mMedia != null && mPlayer != null && !mPlayer.isPlaying()){
            stop();
            mPlayer.play();
        }
    }

    public void stop(){
        if (mPlayer != null){
            mPlayer.stop();
        }
    }

    private void reCreatePlayer(){
        mLivVLC = new LibVLC(getContext());
        mPlayer = new MediaPlayer(mLivVLC);
        mPlayer.setEventListener(this);
        mIVLCVout = mPlayer.getVLCVout();
        mIVLCVout.setVideoView(this);
        mIVLCVout.attachViews();
    }

    public void releasePlayer(){
        if (mIVLCVout != null) {
            mIVLCVout.detachViews();
        }
        if (mMedia != null) {
            mMedia.release();
        }
        if (mPlayer != null) {
            mPlayer.release();
        }
        if (mLivVLC != null) {
            mLivVLC.release();
        }
        mIVLCVout = null;
        mPlayer = null;
        mLivVLC = null;
        mMedia = null;
    }

    public interface IStateLisenter{
        public void onPlaying();
        public void onStop();
        public void onError();
        public void onBufferReady();
    }
}
