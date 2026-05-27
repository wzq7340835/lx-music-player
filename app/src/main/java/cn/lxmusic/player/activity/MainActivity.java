package cn.lxmusic.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.lxmusic.player.R;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.player.PlayerManager;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigation;
    private FrameLayout miniPlayer;
    private TextView songTitle;
    private TextView songArtist;
    private ImageView coverImage;
    private ImageButton playPauseBtn;
    private ImageButton nextBtn;
    private View progressView;
    
    private PlayerManager playerManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupNavigation();
        setupMiniPlayer();
        
        playerManager = new PlayerManager(this);
    }
    
    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        miniPlayer = findViewById(R.id.mini_player);
        songTitle = miniPlayer.findViewById(R.id.songTitle);
        songArtist = miniPlayer.findViewById(R.id.songArtist);
        coverImage = miniPlayer.findViewById(R.id.coverImage);
        playPauseBtn = miniPlayer.findViewById(R.id.playPauseBtn);
        nextBtn = miniPlayer.findViewById(R.id.nextBtn);
        progressView = miniPlayer.findViewById(R.id.progressBar);
    }
    
    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment);
        
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.getNavController());
        }
    }
    
    private void setupMiniPlayer() {
        miniPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            startActivity(intent);
        });
        
        playPauseBtn.setOnClickListener(v -> {
            if (playerManager != null) {
                if (playerManager.isPlaying()) {
                    playerManager.pause();
                } else {
                    playerManager.resume();
                }
            }
        });
        
        nextBtn.setOnClickListener(v -> {
            if (playerManager != null) {
                playerManager.playNext();
            }
        });
        
        songTitle.setText("未播放");
        songArtist.setText("点击搜索音乐");
        
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_my) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
    
    public void updateMiniPlayer(SongInfo song) {
        if (song == null) return;
        
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
        
        com.bumptech.glide.Glide.with(this)
            .load(song.getCoverUrl())
            .placeholder(R.drawable.ic_music_note)
            .into(coverImage);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerManager != null) {
            playerManager.release();
        }
    }
}
