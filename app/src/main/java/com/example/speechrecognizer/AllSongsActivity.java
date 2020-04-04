package com.example.speechrecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllSongsActivity extends AppCompatActivity {

    private String [] allSongs;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);
        listView = findViewById(R.id.listView);

        runTimePermission();

     }

    private void runTimePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private ArrayList<File> readSongs( File file )  {

        ArrayList<File> result = new ArrayList<>();

        File[] allFiles = file.listFiles();

        for( File individual : allFiles ){
            if( !individual.isHidden() && individual.isDirectory() ){
                result.addAll(readSongs(individual));
            }
            else{
                if( individual.getName().endsWith(".mp3") || individual.getName().endsWith(".aac") || individual.getName().endsWith(".wma")){
                    result.add(individual);
                }
            }
        }

        return result;
    }

    void displaySong(){

        final ArrayList<File> song = readSongs(Environment.getExternalStorageDirectory());

        allSongs = new String[song.size()];

        for(int i = 0 ;i < song.size() ; i++){
            allSongs[i] = song.get(i).getName();
        }

        arrayAdapter = new ArrayAdapter<String>(AllSongsActivity.this,android.R.layout.simple_list_item_1,allSongs);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent( getApplicationContext(), PlaySongActivity.class);
                i.putExtra("All Songs",song);
                i.putExtra("Song Name",allSongs);
                i.putExtra("Song Position",position);
                startActivity(i);

            }
        });


    }

}
