package ccom.timark.zhouvlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.videolan.integrate.MediaView;

public class MainActivity extends AppCompatActivity {

    private MediaView mMediaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaView = (MediaView) findViewById(R.id.media);

        mMediaView.setIStateLisenter(new MediaView.IStateLisenter() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onBufferReady() {

            }
        });
        mMediaView.playByNet("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
    }
}
