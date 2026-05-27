package cn.lxmusic.player.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media3.common.Player;

import com.bumptech.glide.Glide;

import cn.lxmusic.player.R;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.player.PlayerManager;

public class PlayerActivity extends AppCompatActivity {
    
    private ImageView albumCover;
    private TextView songTitle;
    private TextView songArtist;
    private SeekBar progressBar;
    private TextView currentTime;
    private TextView totalTime;
    private ImageButton playPauseBtn;
    private ImageButton prevBtn;
    private ImageButton nextBtn;
    private ImageButton lyricsBtn;
    private ImageButton playlistBtn;
    
    private PlayerManager playerManager;
    private boolean isUserSeeking = false;
    
    private final ActivityResultLauncher<String> notificationPermissionLauncher = 
        registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (!granted) {
                    // 通知权限被拒绝，但仍可以继续播放
                }
            }
        );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        initViews();
        setupListeners();
        
        playerManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager().getPlayerManager();
        if (playerManager == null) {
            playerManager = new PlayerManager(this);
            cn.lxmusic.player.LXMusicApplication.getInstance().setPlayerManager(playerManager);
        }
        
        playerManager.addPlayerListener(new PlayerManager.PlayerListener() {
            @Override
            public void onSongChange(SongInfo song) {
                runOnUiThread(() -> updateSongInfo(song));
            }
            
            @Override
            public void onPlayStateChanged(int state) {
                runOnUiThread(() -> updatePlayState(state));
            }
            
            @Override
            public void onProgressUpdate(long position, long duration) {
                runOnUiThread(() -> updateProgress(position, duration));
            }
            
            @Override
            public void onError(String error) {
            }
        });
        
        requestNotificationPermission();
    }
    
    private void initViews() {
        albumCover = findViewById(R.id.albumCover);
        songTitle = findViewById(R.id.songTitle);
        songArtist = findViewById(R.id.songArtist);
        progressBar = findViewById(R.id.progressBar);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        lyricsBtn = findViewById(R.id.lyricsBtn);
        playlistBtn = findViewById(R.id.playlistBtn);
        
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentTime.setText(formatTime(progress));
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playerManager != null) {
                    playerManager.seekTo(seekBar.getProgress());
                }
                isUserSeeking = false;
            }
        });
    }
    
    private void setupListeners() {
        playPauseBtn.setOnClickListener(v -> togglePlayPause());
        nextBtn.setOnClickListener(v -> {
            if (playerManager != null) {
                playerManager.playNext();
            }
        });
        prevBtn.setOnClickListener(v -> {
            if (playerManager != null) {
                playerManager.playPrevious();
            }
        });
        lyricsBtn.setOnClickListener(v -> {
        });
        playlistBtn.setOnClickListener(v -> {
        });
    }
    
    private void togglePlayPause() {
        if (playerManager == null) return;
        
        if (playerManager.isPlaying()) {
            playerManager.pause();
        } else {
            playerManager.resume();
        }
    }
    
    private void updateSongInfo(SongInfo song) {
        if (song == null) return;
        
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        
        Glide.with(this)
            .load(song.getCoverUrl())
            .placeholder(R.drawable.ic_music_note)
            .into(albumCover);
    }
    
    private void updatePlayState(int state) {
        switch (state) {
            case Player.STATE_READY:
                playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
                break;
            case Player.STATE_BUFFERING:
                break;
            case Player.STATE_ENDED:
            case Player.STATE_IDLE:
                playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                break;
        }
    }
    
    private void updateProgress(long position, long duration) {
        if (isUserSeeking) return;
        
        progressBar.setMax((int) duration);
        progressBar.setProgress((int) position);
        
        currentTime.setText(formatTime(position));
        totalTime.setText(formatTime(duration));
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
    
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerManager != null) {
            playerManager.removePlayerListener(new PlayerManager.PlayerListener() {
                @Override
                public void onSongChange(SongInfo song) {}
                @Override
                public void onPlayStateChanged(int state) {}
                @Override
                public void onProgressUpdate(long position, long duration) {}
                @Override
                public void onError(String error) {}
            });
        }
    }
}
