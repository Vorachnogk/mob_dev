package com.example.lab4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView videoPlayer;
    private Uri VideoUri = null;
    String videoUrl = "http://www.youtubemaza.com/files/data/366/Tom%20And%20Jerry%20055%20Casanova%20Cat%20(1951).mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setTitle("Lab 4 - Video Window");

        videoPlayer = findViewById(R.id.videoView);
    }

    public void play(View view) {
        videoPlayer.start();
    }

    public void pause(View view) {
        videoPlayer.pause();
    }

    public void stop(View view) {
        videoPlayer.stopPlayback();
        videoPlayer.resume();
    }

    public void internete(View view) {
        Uri video = Uri.parse(videoUrl);
        videoPlayer.setVideoURI(video);
    }

    public void packageStorage(View view) {
        Uri myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gu);
        videoPlayer.setVideoURI(myVideoUri);
    }

    public void localeStorage(View view) {
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, requirePermission, 2);
        } else {
            selectVideoFile();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                VideoUri = data.getData();
                intVideoPlayer();
            }
        }
    }

    private void selectVideoFile() {
        Intent selectVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectVideoIntent.setType("video/*");
        startActivityForResult(selectVideoIntent, 1);
    }

    private void intVideoPlayer() {
        videoPlayer.setVideoURI(VideoUri);
    }
}