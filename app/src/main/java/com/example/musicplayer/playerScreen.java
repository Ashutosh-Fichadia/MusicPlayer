package com.example.musicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.musicplayer.R.drawable.icon_pause;
import static com.example.musicplayer.R.drawable.icon_play;

public class playerScreen extends AppCompatActivity {

    private Handler mHandler;
    ImageButton btnplaylist,btnadd,btnrepeat,btnnxt,btnplay,btnprev,btnshuffle,btnback;
    Button btndown,btnmore;
    TextView songname,artistname;
    SeekBar sb;
    Uri uri;
    int cpos=0;
    MediaPlayer mPlayer;
    Boolean play=false;
    Boolean pause=false;
    MediaMetadataRetriever metaRetriver;
    Runnable mUpdateSeekbar;
    ImageView album_art;
    byte[] art;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_player_screen);
        sb= (SeekBar) findViewById(R.id.seek);
        album_art = (ImageView) findViewById(R.id.album_art);
        btnadd = (ImageButton) findViewById(R.id.btnadd);
        songname = (TextView) findViewById(R.id.songname);

        btnplay = (ImageButton) findViewById(R.id.btnplay);
        btnnxt  = (ImageButton) findViewById(R.id.btnnxt);
        btnprev  = (ImageButton) findViewById(R.id.btnprev);
        btnback = (ImageButton) findViewById(R.id.btnback);



        listeners();

    }
    void listeners()
    {
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null)
                {
                    sb.setProgress(0);
                    btnplay.setImageResource(icon_play);
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                    play=false;


                }
                chooseSong();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null)
                {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                    songname.setText("Select New Song");
                    btnplay.setImageResource(icon_play);
                    sb.setProgress(0);

                    pause=false;
                    play = false;
                }
            }
        });

        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null)
                {
                    //sb.setProgress(sb.getProgress()+10);
                    mPlayer.seekTo(mPlayer.getCurrentPosition() + 1000);
                }

            }
        });
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null)
                {
                    //sb.setProgress(sb.getProgress()+10);
                    mPlayer.seekTo(mPlayer.getCurrentPosition() - 1000);
                }

            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    if(mPlayer!=null)
                    {
                        mPlayer.seekTo(i);
                    }


                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void playSong()
    {
        if(mPlayer!=null && mPlayer.getDuration()==sb.getProgress())
        {
            mPlayer.seekTo(0);
            mHandler.removeCallbacks(mUpdateSeekbar);
            btnplay.setImageResource(icon_play);
            play=false;
            cpos=0;

        }
        if(play==true)
        {
            if(mPlayer!=null && mPlayer.isPlaying()){
                cpos= mPlayer.getCurrentPosition();
                mPlayer.pause();
                mHandler.removeCallbacks(mUpdateSeekbar);
                btnplay.setImageResource(icon_play);
                pause = true;
                play=false;
            }

        }
        else
        {

            if(pause==true)
            {
                if(mPlayer!=null)
                {
                    mPlayer.seekTo(cpos);
                    btnplay.setImageResource(icon_pause);
                    mPlayer.start();
                    pause=false;
                    play=true;
                }


            }
            else {


                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mPlayer.setDataSource(getApplicationContext(), uri);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                }
                sb.setMax(mPlayer.getDuration());

                mHandler = new Handler();
                //Make sure you update Seekbar on UI thread
                mUpdateSeekbar = new Runnable() {
                    @Override
                    public void run() {
                        if(mPlayer!=null)
                        {
                            sb.setProgress(mPlayer.getCurrentPosition());
                            if (sb.getProgress() == mPlayer.getDuration()) {
                                btnplay.setImageResource(icon_play);
                                mPlayer.pause();
                                sb.setProgress(0);
                                play = false;
                            }
                            mHandler.postDelayed(this, 50);

                        }

                    }
                };

                mPlayer.start();
                mHandler.postDelayed(mUpdateSeekbar, 0);
                btnplay.setImageResource(icon_pause);
                play = true;
            }
        }

    }
    void chooseSong()
    {

        pause=false;
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                uri = data.getData();
                File f = new File(uri.getPath());
                songname.setText(f.getName());

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}