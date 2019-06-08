package com.example.lab4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {
    MediaPlayer player;
    private Uri audioFileUri = null;
    private EditText audioFilePathEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        audioFilePathEditor = findViewById(R.id.editText);
        setTitle("Lab 4 - Audio Window");
    }

    public void play(View v) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.song);
            Toast.makeText(this, "Audio player playing", Toast.LENGTH_SHORT).show();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
            Toast.makeText(this, "Audio player paused", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(View v) {
        stopPlayer();
    }

    public void internet(View v) {
        initAudioPlayer();
    }

    public void localStorage(View view) {
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(AudioActivity.this, requirePermission, 2);
        } else {
            local();
        }
    }

    public void local() {
        Intent selectAudioIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectAudioIntent.setType("audio/*");
        startActivityForResult(selectAudioIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                audioFileUri=data.getData();
                b();
            }
        }
    }

    public void b() {
        try {
            player = new MediaPlayer();
            player.setDataSource(getApplicationContext(), audioFileUri);
            player.prepare();
        } catch (IOException ex) {
            Toast.makeText(this, "Something happened", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Audio player stopped and released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }


    private void initAudioPlayer() {
        try {
            player = new MediaPlayer();

            String audioFilePath = audioFilePathEditor.getText().toString().trim();

            if (audioFilePath.toLowerCase().startsWith("http://")) {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(audioFilePath);
            } else {
                if (audioFileUri != null) {
                    player.setDataSource(getApplicationContext(), audioFileUri);
                }
            }
            player.prepare();
        } catch (IOException ex) {
            Toast.makeText(this, "Something happened", Toast.LENGTH_SHORT).show();
        }
    }
}