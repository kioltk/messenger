package org.happysanta.messenger.audio;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.TimeUtils;

import java.io.IOException;

/**
 * Created by admin on 21.04.2015.
 */
public class PlayerActivity extends BaseActivity {
    int audioId;
    private String audioUrl;
    private String audioTitle;
    private String audioArtist;
    private int audioDuration;
    private static final String EXTRA_AUDIOS_URL = "extra_audiourl";
    private static final String EXTRA_AUDIO_ID = "extra_audioid";
    private static final String EXTRA_AUDIO_TITLE = "extra_audiotitle";
    private static final String EXTRA_AUDIO_ARTIST = "extra_audioartist";
    private static final String EXTRA_AUDIO_DURATION = "extra_audioduration";
    // как так? Просто можно получать длительность еще до загрузки песни
    // хорошо, и куда ты ее заносишь? справа от сикбара
    // а в какой переменной ты хранишь длительность?

    private MediaPlayer audioPlayer;
    private AudioPlayingStatus currentPlayerStatus = AudioPlayingStatus.PLAYING;
    private boolean initialStage = true;

    private ImageView playpauseView;
    private TextView titleView, subTitleView;
    private TextView currentPlayingPositionView;
    private TextView durationView;
    private SeekBar seekbar;

    private int currentPlayingPosition = 0;
    private PlayerTask playerTask;
    private int bufferedProgress;
    private PlayPauseDrawable playpauseDrawable;
    private Thread timerTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        titleView = (TextView) findViewById(R.id.title);
        subTitleView = (TextView) findViewById(R.id.subtitle);

        playpauseView = (ImageView) findViewById(R.id.playpause);
        playpauseView.setOnClickListener(playViewClickListener);
        playpauseDrawable = new PlayPauseDrawable(this);
        playpauseView.setImageDrawable(playpauseDrawable);

        currentPlayingPositionView = (TextView) findViewById(R.id.duration_count);
        durationView = (TextView) findViewById(R.id.duration);
        seekbar = (SeekBar) findViewById(R.id.seek_bar);
        seekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        audioTitle = getIntent().getStringExtra(EXTRA_AUDIO_TITLE);
        audioArtist = getIntent().getStringExtra(EXTRA_AUDIO_ARTIST);
        audioUrl = getIntent().getStringExtra(EXTRA_AUDIOS_URL);
        audioId = getIntent().getIntExtra(EXTRA_AUDIO_ID, 0);
        audioDuration = getIntent().getIntExtra(EXTRA_AUDIO_DURATION, 0);

        processAudio();
        // создали активити и получили и нашли все данные, с которыми будем работать
    }

    private void processAudio() {
        // начало процесса работы с аудио
        // показываем данные
        titleView.setText(audioTitle);
        subTitleView.setText(audioArtist);
        durationView.setText(TimeUtils.formatDuration(audioDuration));
        seekbar.setMax(audioDuration);

        // начинаем загрузку
        audioPlayer = new MediaPlayer();
        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playerTask = new PlayerTask();
        playerTask.execute(audioUrl);

        // тут стартанем таймер
        startTimer();
    }

    private void startTimer() {
        if (timerTask != null) {
            timerTask.interrupt();
            timerTask = null;
        }
        timerTask = new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentPlayerStatus == AudioPlayingStatus.PLAYING) {
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
        currentPlayingPosition = audioPlayer.getCurrentPosition();
        currentPlayingPositionView.setText(TimeUtils.formatDuration(currentPlayingPosition / 1000));
        seekbar.setProgress(currentPlayingPosition / 1000);
    }

    private View.OnClickListener playViewClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (currentPlayerStatus) {
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
        if (!audioPlayer.isPlaying())
            audioPlayer.start();
        startTimer();

    }

    private void pause() {
        currentPlayerStatus = AudioPlayingStatus.PAUSED;
        playpauseDrawable.play();
        if (audioPlayer.isPlaying())
            audioPlayer.pause();
    }
    // что это?
    // объясни? Тут новый ранейбл с хендлером, что бы запросы были раз в секунду и занесение текста в таймер. А снизу запуск
    // а почему там запуск? Нам же не нужно видеть таймер до загрузки песни
    // чет темно у тебя тут

    private OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
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
            audioPlayer.seekTo(value * 1000);// мы же max у сикбара поставили другой, теперь он возвращает в секундах, а не в процентах
            Log.d("VKPlayer", "stopTrackingTouch");
        }
    };


    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onProgressUpdate(Void... values) {
            seekbar.setSecondaryProgress(bufferedProgress * audioDuration / 100);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                Log.d("VKPlayer", "Background started");
                audioPlayer.setDataSource(params[0]);
                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        initialStage = true;
                        currentPlayerStatus = AudioPlayingStatus.PAUSED;
                        playpauseDrawable.play();
                        audioPlayer.stop();
                    }
                });

                audioPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        Log.d("VKPlayer", "Buffered: " + i);
                        bufferedProgress = i;
                        publishProgress();


                    }
                });
                audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.d("VKPlayer", "Prepared");
                    }
                });
                audioPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        Log.d("VKPlayer", "Seeked");

                    }
                });
                Log.d("VKPlayer", "Preparing started");
                audioPlayer.prepare();
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

        // работает?
        // после нескольких перемоток сломалось и сам сикбар не перемещается на нужную позицию, только таймер
        // значит перемудрили. что именно сломалось? там затуп где-то с переодом с мили. у меян перемотка работает только в небольшой части сикбар.а.в какой? меньше половины
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.d("Prepared", "//" + result);
            audioPlayer.start();

            // вот тут ты почему-то подумал, что длительность, которую мы передали и длительность, которую получил плеер - разная. хз как ты так подумал, но не страшно.
            Log.d("VKPlayer", "Audio duration is " + audioDuration + "s, however audioplayer told its " + audioPlayer.getDuration() + "ms. Should we worry about it?");
            seekbar.setProgress(currentPlayingPosition);
            initialStage = false;
        }


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (audioPlayer != null) {
            audioPlayer.reset();
            audioPlayer.release();
            audioPlayer = null;
        }
    }


    public static Intent openAudio(Context context, int audioid, String audiourl, String audiotitle, String audioartist, int audioduration) {
        return new Intent(context, PlayerActivity.class)
                .putExtra(EXTRA_AUDIO_ID, audioid)
                .putExtra(EXTRA_AUDIOS_URL, audiourl)
                .putExtra(EXTRA_AUDIO_TITLE, audiotitle)
                .putExtra(EXTRA_AUDIO_ARTIST, audioartist)
                .putExtra(EXTRA_AUDIO_DURATION, audioduration);
    }
}
