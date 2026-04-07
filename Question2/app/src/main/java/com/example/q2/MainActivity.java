package com.example.q2;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {
    MediaPlayer audioPlayer;
    VideoView videoView;
    TextView tvStatus;
    private static final int PICK_AUDIO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // AUDIO
        Button btnOpenAudio = findViewById(R.id.btnOpenAudio);
        Button btnPlay = findViewById(R.id.btnAudioPlay);
        Button btnPause = findViewById(R.id.btnAudioPause);
        Button btnStop = findViewById(R.id.btnAudioStop);
        Button btnRestart = findViewById(R.id.btnAudioRestart);
        tvStatus = findViewById(R.id.tvAudioStatus);
        btnOpenAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_AUDIO);
        });
        btnPlay.setOnClickListener(v -> {
            if (audioPlayer != null) {
                audioPlayer.start();
                tvStatus.setText("Playing ");
            } else {
                Toast.makeText(this, "Select audio first!", Toast.LENGTH_SHORT).show();
            }
        });
        btnPause.setOnClickListener(v -> {
            if (audioPlayer != null && audioPlayer.isPlaying()) {
                audioPlayer.pause();
                tvStatus.setText("Paused ");
            }
        });
        btnStop.setOnClickListener(v -> {
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer.release();
                audioPlayer = null;
                tvStatus.setText("Stopped ");
            }
        });
        btnRestart.setOnClickListener(v -> {
            if (audioPlayer != null) {
                audioPlayer.seekTo(0);
                audioPlayer.start();
                tvStatus.setText("Restarted ");
            }
        });
        // VIDEO
        videoView = findViewById(R.id.videoView);
        EditText etUrl = findViewById(R.id.etVideoUrl);
        Button btnLoad = findViewById(R.id.btnLoadUrl);
        btnLoad.setOnClickListener(v -> {
            String url = etUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                videoView.setVideoURI(Uri.parse(url));
                videoView.start();
            } else {
                Toast.makeText(this, "Enter video URL!", Toast.LENGTH_SHORT).show();
            }
        });
        Button vPlay = findViewById(R.id.btnVideoPlay);
        Button vPause = findViewById(R.id.btnVideoPause);
        Button vStop = findViewById(R.id.btnVideoStop);
        Button vRestart = findViewById(R.id.btnVideoRestart);
        vPlay.setOnClickListener(v -> videoView.start());
        vPause.setOnClickListener(v -> videoView.pause());
        vStop.setOnClickListener(v -> videoView.stopPlayback());
        vRestart.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO && resultCode == RESULT_OK && data != null) {
            Uri audioUri = data.getData();
            try {
                if (audioPlayer != null) {
                    audioPlayer.release();
                }
                audioPlayer = new MediaPlayer();
                audioPlayer.setDataSource(this, audioUri);
                audioPlayer.prepare();
                tvStatus.setText("Audio Loaded ");
                Toast.makeText(this, "Audio Loaded", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading audio!", Toast.LENGTH_SHORT).show();
            }}}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
        }
    }
}