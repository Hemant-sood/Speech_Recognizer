package com.example.speechrecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class PlaySongActivity extends AppCompatActivity {
    private static final int REQ_AUDIO =323;
    private ArrayList<File> allSongs;
    private String songName;
    private String[] allSongName;
    private int position;
    private TextView name;
    private Switch aSwitch;
    private ImageView pre, next, pausePlay;
    private MediaPlayer mediaPlayer = null;
    private Uri uri;
    private boolean pausePlayBoolean = true;    // true means play and false means to stop playing song
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        name = findViewById(R.id.textView2);
        aSwitch = findViewById(R.id.switch3);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        pausePlay = findViewById(R.id.pause);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(PlaySongActivity.this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        setSongs();


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if( isChecked == true){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if( checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String []{Manifest.permission.RECORD_AUDIO},REQ_AUDIO);
                            return;
                        }
                    }

                    speechRecognizer.startListening(speechIntent);


                }
                else {

                }
            }
        });



        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Toast.makeText(getApplicationContext(),res.get(0),Toast.LENGTH_LONG).show();
             }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mediaPlayer.isPlaying() ){
                    pausePlay.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else{
                    pausePlay.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });


        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevoisSong();
            }
        });


    }

    private void playNextSong() {
        position = (position + 1) % allSongs.size();
        mediaPlayer.release();

        name.setText(allSongName[position]);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),getUriFromAllSong(position));
        mediaPlayer.start();
    }

    private void playPrevoisSong() {

        position = (position - 1) ;
        if (position < 0){
            position = allSongs.size()- 1 ;
        }

        name.setText(allSongName[position]);
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), getUriFromAllSong(position));
        mediaPlayer.start();

    }

    private void setSongs() {

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        allSongs = (ArrayList) bundle.getParcelableArrayList("All Songs");
        allSongName = bundle.getStringArray("Song Name");
        position = bundle.getInt("Song Position", 0);

        name.setText(allSongName[position]);

        uri = getUriFromAllSong(position);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();


    }

    private Uri getUriFromAllSong( int position ){
        return uri.parse(String.valueOf(allSongs.get(position)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == REQ_AUDIO && grantResults.length >= 1 && grantResults[0] == RESULT_OK){
            Toast.makeText(getApplicationContext(),"Permission granted...",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission denied...",Toast.LENGTH_SHORT).show();
        }
    }

 }
