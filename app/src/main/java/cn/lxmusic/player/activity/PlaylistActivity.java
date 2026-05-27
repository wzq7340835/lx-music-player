package cn.lxmusic.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import cn.lxmusic.player.R;
import cn.lxmusic.player.adapter.PlaylistAdapter;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.player.PlayerManager;

import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    
    private RecyclerView playlistRecyclerView;
    private TextView emptyView;
    private TextView nowPlayingText;
    private ImageButton clearBtn;
    private ImageButton playModeBtn;
    private ImageView playingIndicator;
    
    private PlaylistAdapter adapter;
    private PlayerManager playerManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        
        initViews();
        
        playerManager = cn.lxmusic.player.LXMusicApplication.getInstance().getPlayerManager();
        
        setupAdapter();
        setupListeners();
        loadPlaylist();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadPlaylist();
        updateNowPlaying();
    }
    
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        playlistRecyclerView = findViewById(R.id.playlistRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        nowPlayingText = findViewById(R.id.nowPlayingText);
        clearBtn = findViewById(R.id.clearBtn);
        playModeBtn = findViewById(R.id.playModeBtn);
    }
    
    private void setupAdapter() {
        adapter = new PlaylistAdapter(this);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        playlistRecyclerView.setAdapter(adapter);
        
        adapter.setOnSongActionListener(new PlaylistAdapter.OnSongActionListener() {
            @Override
            public void onPlay(SongInfo song) {
                // TODO: Play song
            }

            @Override
            public void onRemove(SongInfo song, int position) {
                playerManager.removeFromQueue(position);
                loadPlaylist();
            }
        });
    }
    
    private void setupListeners() {
        clearBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("清空播放队列")
                .setMessage("确定要清空播放队列吗？")
                .setPositiveButton("清空", (dialog, which) -> {
                    playerManager.clearQueue();
                    loadPlaylist();
                })
                .setNegativeButton("取消", null)
                .show();
        });
        
        playModeBtn.setOnClickListener(v -> {
            PlayerManager.PlaybackMode currentMode = playerManager.getPlaybackMode();
            PlayerManager.PlaybackMode newMode;
            
            switch (currentMode) {
                case REPEAT_SINGLE:
                    newMode = PlayerManager.PlaybackMode.SHUFFLE;
                    break;
                case REPEAT_ALL:
                    newMode = PlayerManager.PlaybackMode.REPEAT_SINGLE;
                    break;
                case SHUFFLE:
                default:
                    newMode = PlayerManager.PlaybackMode.REPEAT_ALL;
                    break;
            }
            
            playerManager.setPlaybackMode(newMode);
            updatePlayModeIcon(newMode);
            
            String modeText = "";
            switch (newMode) {
                case REPEAT_SINGLE:
                    modeText = "单曲循环";
                    break;
                case REPEAT_ALL:
                    modeText = "列表循环";
                    break;
                case SHUFFLE:
                    modeText = "随机播放";
                    break;
            }
            
            Toast.makeText(this, modeText, Toast.LENGTH_SHORT).show();
        });
    }
    
    private void loadPlaylist() {
        List<SongInfo> queue = playerManager.getQueue();
        adapter.setSongs(queue);
        adapter.setCurrentSong(playerManager.getCurrentSong());
        
        if (queue.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            playlistRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            playlistRecyclerView.setVisibility(View.VISIBLE);
        }
        
        updateNowPlaying();
    }
    
    private void updateNowPlaying() {
        SongInfo currentSong = playerManager.getCurrentSong();
        if (currentSong != null) {
            nowPlayingText.setText("正在播放：" + currentSong.getTitle());
        } else {
            nowPlayingText.setText("正在播放：未播放");
        }
        adapter.notifyDataSetChanged();
    }
    
    private void updatePlayModeIcon(PlayerManager.PlaybackMode mode) {
        // Icon already set in button
    }
}
