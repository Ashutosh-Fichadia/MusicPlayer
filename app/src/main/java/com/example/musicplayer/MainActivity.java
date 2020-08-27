package com.example.musicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.util.Pair;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class MainActivity extends AppCompatActivity {
TextView slogan;
Animation topanim,botanim;
private static int DURATION=5000;
CircleImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);
        slogan = findViewById(R.id.slogan);
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        botanim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        image = findViewById(R.id.album_art);
        image.setAnimation(topanim);
        slogan.setAnimation(botanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,playerScreen.class);
               /* Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(image,"logo_image");
               ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(i,options.toBundle());*/
               startActivity(i);
                finish();
            }
        },DURATION);
    }
}