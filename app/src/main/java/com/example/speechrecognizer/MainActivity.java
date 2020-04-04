package com.example.speechrecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  //  Regardless of the kind of application you create, speech recognition can improve accessibility by providing users with an alternative way to interact with your app. For example, people with mobility, dexterity, or sight issues may find it easier to navigate mobile applications using voice commands, rather than the touchscreen or keyboard. Plus, according to the World Health Organization (WHO), over a billion people have some form of disability, which equates to around 15% of the world’s population. Adding accessibility features to your applications can significantly increase your potential audience.

    private static final int REQ_AUDIO =32 ;
    Switch aSwitch;
    TextView t ;
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private Button b;

    private static int REQ = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.switch1);
        t = findViewById(R.id.textView);
        b = findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        Log.d("Locale Default",Locale.getDefault().toString()+" "+Locale.getISOCountries().toString());


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),AllSongsActivity.class);
                startActivity(i);
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
                t.setText(res.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


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
                    speechRecognizer.startListening(speechIntent);
                     t.setText("On");

                }
            }
            else {
                t.setText("off");
            }
        }
    });
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
