package com.example.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static int AUDIO_PERMISSION = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAudioPresence()){
            askAudioPermission();
        }
    }
    public void buttonRecord(View view){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(recordedFile());
            mediaRecorder.prepare();
            mediaRecorder.start();
            mediaRecorder.setMaxDuration(300000);
            mediaRecorder.setOnInfoListener((MediaRecorder.OnInfoListener) this);
            Toast.makeText(this, "Recording Begins", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void buttonStop(View view){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();

    }
    public void buttonPlay(View view){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(recordedFile());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Recording playing", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private boolean checkAudioPresence(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else
            return false;
    }
    private void askAudioPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.RECORD_AUDIO},AUDIO_PERMISSION );

        }
    }
    private String recordedFile(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File downloadDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDirectory, "RecordedFile " + ".mp3");
        return file.getPath();
    }
}
