package org.happysanta.messenger.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.happysanta.messenger.R;
import org.happysanta.messenger.audio.AudioPlayingStatus;
import org.happysanta.messenger.audio.PlayPauseDrawable;
import org.happysanta.messenger.core.util.TimeUtils;

import java.io.IOException;

/**
 * Created by insidefun on 28.04.2015.
 */
public class VideoPlayerActivity extends Activity implements
        OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
        OnVideoSizeChangedListener, SurfaceHolder.Callback {
    int videoId;
    private String videoUrl;
    private String videoPhoto;
    private int videoDuration;
    private static final String EXTRA_VIDEO_URL = "extra_video_url";
    private static final String EXTRA_VIDEO_ID = "extra_video_id";
    private static final String EXTRA_VIDEO_PHOTO = "extra_video_photo";
    private static final String EXTRA_VIDEO_DURATION = "extra_video_duration";

    private MediaPlayer videoPlayer;
    private AudioPlayingStatus currentPlayerStatus = AudioPlayingStatus.PLAYING;
    private boolean initialStage = true;

    private ImageView playpauseView;
    private TextView currentPlayingPositionView;
    private TextView durationView;
    private SeekBar seekbar;

    private int currentPlayingPosition = 0;
    private int bufferedProgress;
    private PlayPauseDrawable playpauseDrawable;
    private Thread timerTask;
    private VideoView videoView;

    private SurfaceHolder holder;
    private String path;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private static final String STREAM_VIDEO = "extra_stream";
    private Bundle extras;
    private SurfaceView videoPreview;
    private PlayerTask playerTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoPreview = (SurfaceView) findViewById(R.id.surface);

        //Play button
        playpauseView = (ImageView) findViewById(R.id.play_pause);
        playpauseDrawable = new PlayPauseDrawable(this);
        playpauseView.setImageDrawable(playpauseDrawable);

        //SeekBar
        currentPlayingPositionView = (TextView) findViewById(R.id.duration_count);
        durationView = (TextView) findViewById(R.id.duration);
        seekbar = (SeekBar) findViewById(R.id.seek_bar);

        //Intents
        videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        videoPhoto = getIntent().getStringExtra(EXTRA_VIDEO_PHOTO);
        videoId = getIntent().getIntExtra(EXTRA_VIDEO_ID, 0);
        videoDuration = getIntent().getIntExtra(EXTRA_VIDEO_DURATION, 0);
        extras = getIntent().getExtras();

        processVideo();
    }

    private void processVideo() {
        durationView.setText(TimeUtils.formatDuration(videoDuration));
        seekbar.setMax(videoDuration);
        playpauseView.setOnClickListener(playViewClickListener);
        seekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        // начинаем загрузку
        videoPlayer = new MediaPlayer();
        videoPlayer.setDisplay(holder);
        videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        videoPlayer.setOnVideoSizeChangedListener(this);
        videoPlayer.setOnBufferingUpdateListener(this);
        videoPlayer.setOnCompletionListener(this);
        videoPlayer.setOnPreparedListener(this);
        playerTask = new PlayerTask();
        playerTask.execute(videoUrl);

        //Surface
        holder = videoPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // тут стартанем таймер
        startTimer();
    }

    private View.OnClickListener playViewClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (currentPlayerStatus){
                case PLAYING:
                    pause();
                    break;
                case PAUSED:
                    play();
                    break;
            }

        }
    };

    private void play() {
        currentPlayerStatus = AudioPlayingStatus.PLAYING;
        playpauseDrawable.pause();
        if (!videoPlayer.isPlaying())
            videoPlayer.start();
        startTimer();

    }

    private void pause() {
        currentPlayerStatus = AudioPlayingStatus.PAUSED;
        playpauseDrawable.play();
        if (videoPlayer.isPlaying())
            videoPlayer.pause();
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){
        public int value;

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            Log.d("VKPlayer", "progress changed" + i + " " + fromUser);

            if (fromUser) {
                value = i;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d("VKPlayer", "startTrackingTouch");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            videoPlayer.seekTo(value * 1000);// мы же max у сикбара поставили другой, теперь он возвращает в секундах, а не в процентах
            Log.d("VKPlayer", "stopTrackingTouch");
        }
    };

    //Tread for update duration counters
    private void startTimer() {
        if(timerTask!=null){
            timerTask.interrupt();
            timerTask = null;
        }
        timerTask = new Thread(new Runnable() {
            @Override
            public void run() {
                while(currentPlayerStatus == AudioPlayingStatus.PLAYING){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTimer();
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
        timerTask.start();
    }

    private void updateTimer() {
        currentPlayingPosition = videoPlayer.getCurrentPosition();
        currentPlayingPositionView.setText(TimeUtils.formatDuration(currentPlayingPosition / 1000));
        seekbar.setProgress(currentPlayingPosition / 1000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlayer != null) {
            videoPlayer.reset();
            videoPlayer.release();
            videoPlayer = null;
        }
    }


    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onProgressUpdate(Void... values) {
            seekbar.setSecondaryProgress(bufferedProgress * videoDuration / 100);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                Log.d("VKPlayer", "Background started");
                videoPlayer.setDataSource(params[0]);
                videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        initialStage = true;
                        currentPlayerStatus = AudioPlayingStatus.PAUSED;
                        playpauseDrawable.play();
                        videoPlayer.stop();
                    }
                });

                videoPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        Log.d("VKPlayer", "Buffered: " + i);
                        bufferedProgress = i;
                        publishProgress();


                    }
                });
                videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.d("VKPlayer", "Prepared");
                    }
                });
                videoPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        Log.d("VKPlayer", "Seeked");

                    }
                });
                Log.d("VKPlayer", "Preparing started");
                videoPlayer.prepare();
                Log.d("VKPlayer", "Preparing ended");

                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            videoPlayer.start();

            // вот тут ты почему-то подумал, что длительность, которую мы передали и длительность, которую получил плеер - разная. хз как ты так подумал, но не страшно.
            Log.d("VKPlayer","Audio duration is " + videoDuration +"s, however audioplayer told its " + videoPlayer.getDuration()+"ms. Should we worry about it?");
            seekbar.setProgress(currentPlayingPosition);
            initialStage = false;
        }

        public PlayerTask() { progress = new ProgressDialog(VideoPlayerActivity.this); }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();

        }



    }


    public static Intent openVideo(Context context, String player, int videoid, String photo_640, int duration) {
        return new Intent(context, VideoPlayerActivity.class)
                .putExtra(EXTRA_VIDEO_URL, player)
                .putExtra(EXTRA_VIDEO_ID, videoid)
                .putExtra(EXTRA_VIDEO_PHOTO, photo_640)
                .putExtra(EXTRA_VIDEO_DURATION, duration);
    }

    //SurfaceHolder
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("VKPlayer", "surfaceCreated called");
        processVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("VKPlayer", "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("VKPlayer", "surfaceDestroyed called");
    }


    //Video methods
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("VKPlayer", "onCompletion called");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v("VKPlayer", "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e("VKPlayer", "invalid video width(" + width + ") or height(" + height
                    + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d("VKPlayer", "onBufferingUpdate percent:" + percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("VKPlayer", "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
            doCleanUp();
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v("VKPlayer", "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        videoPlayer.start();
    }
}
