package com.bibo.museit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MusIt_Player_Screen extends AppCompatActivity {

    TextView Songname,lefttiming ,righttiming;


    MediaPlayer mediaPlayer;
    LottieAnimationView lottieAnimationView;
    Button playPause, leftSong,Rightsong,Skipleft,Skipright;
    String currentSongFilePath;
    SeekBar seekBar;



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







        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();



        // Get extras from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Retrieve the current song title and file path
            String currentSongTitle = extras.getString("currentSongTitle");
            currentSongFilePath = extras.getString("currentSongFilePath");

            // Set the song title to the TextView
            Songname.setText(currentSongTitle);


            try {
                playPause.setBackgroundResource(R.drawable.pause);

                lottieAnimationView.playAnimation();
                // Set the data source to the current song file path
                mediaPlayer.setDataSource(currentSongFilePath);
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
        second=mili/6000;
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