package com.bibo.museit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MusIt_Player_Screen extends AppCompatActivity {

    TextView Songname,lefttiming ,righttiming;


    MediaPlayer mediaPlayer;
    Thread updateseekbutton;
    LottieAnimationView lottieAnimationView;
    Button playPause, leftSong,Rightsong,Skipleft,Skipright;
    String currentSongFilePath;
    SeekBar seekBar;
    int songpositon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mus_it_player_screen);
        Songname=findViewById(R.id.songname);
        lefttiming=findViewById(R.id.leftseekbarnumber);
        righttiming=findViewById(R.id.rightseekbarnumber);
        playPause=findViewById(R.id.pauseplay);
        leftSong=findViewById(R.id.changeleft);
        Rightsong=findViewById(R.id.chanegright);
        Skipleft=findViewById(R.id.skipleft);
        Skipright=findViewById(R.id.chanegright);
        lottieAnimationView=findViewById(R.id.musicImage);
        seekBar=findViewById(R.id.Seekbar);






        // Get extras from the intent
        Bundle extras = getIntent().getExtras();

        Intent intent= getIntent();
        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        // Retrieve the current song title and file path
        String currentSongTitle = extras.getString("currentSongTitle");
        currentSongFilePath = extras.getString("currentSongFilePath");
        songpositon=intent.getIntExtra("position",0);





        if (extras != null) {

            // Set the song title to the TextView
            Songname.setText(currentSongTitle);


            try {
                playPause.setBackgroundResource(R.drawable.pause);

                lottieAnimationView.playAnimation();
                // Set the data source to the current song file path
                mediaPlayer.setDataSource(currentSongFilePath );
                // Prepare the MediaPlayer
                mediaPlayer.prepare();
                // Start playing the song
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int min=0,second=0,mili=0;
        mili=mediaPlayer.getDuration();
        min=mili/60000;
        second = mili%(1000*60)/1000;
        righttiming.setText(min+":"+second);

        seekBar.setMax(mili);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());

        updateseekbutton = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();

                        int mili = currentPosition;
                        int minute = mili / 60000;
                        int second = mili % (1000 * 60) / 1000;
                        String formattedSecond = String.format("%02d", second);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                lefttiming.setText(minute + ":" + formattedSecond);
                            }
                        });

                        sleep(800); // Experiment with smaller sleep times
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        updateseekbutton.start();




        Skipleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle skip back 10 seconds functionality
                int currentPosition = mediaPlayer.getCurrentPosition();
                int newPosition = currentPosition - 10000; // Skip back 10 seconds
                if (newPosition < 0) {
                    newPosition = 0;
                }
                mediaPlayer.seekTo(newPosition);
            }
        });

        Skipright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle skip forward 10 seconds functionality
                int currentPosition = mediaPlayer.getCurrentPosition();
                int newPosition = currentPosition + 10000; // Skip forward 10 seconds
                int duration = mediaPlayer.getDuration();
                if (newPosition > duration) {
                    newPosition = duration;
                }
                mediaPlayer.seekTo(newPosition);
            }
        });







        playPause.setOnClickListener(new View.OnClickListener() {
            boolean isplaying = true; // Move the declaration inside the OnClickListener

            @Override
            public void onClick(View v) {
                if (isplaying) {

                    lottieAnimationView.pauseAnimation();
                    playPause.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();

                } else {
                    lottieAnimationView.playAnimation();
                    playPause.setBackgroundResource(R.drawable.pause);

                    mediaPlayer.start();



                    
                }
                isplaying = !isplaying;
            }
        });







    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }





}