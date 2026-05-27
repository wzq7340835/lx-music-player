package cn.lxmusic.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import cn.lxmusic.player.R;
import cn.lxmusic.player.adapter.SearchResultAdapter;
import cn.lxmusic.player.db.FavoriteManager;
import cn.lxmusic.player.db.HistoryManager;
import cn.lxmusic.player.model.SongInfo;
import java.util.List;

public class MyListActivity extends AppCompatActivity {
    
    private RecyclerView listRecyclerView;
    private TextView emptyView;
    private TabLayout tabLayout;
    
    private SearchResultAdapter adapter;
    private FavoriteManager favoriteManager;
    private HistoryManager historyManager;
    
    private boolean isFavoriteTab = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        
        initViews();
        
        favoriteManager = FavoriteManager.getInstance(this);
        historyManager = HistoryManager.getInstance(this);
        
        setupAdapter();
        setupTabs();
        loadData();
    }
    
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        listRecyclerView = findViewById(R.id.listRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        tabLayout = findViewById(R.id.tabLayout);
    }
    
    private void setupAdapter() {
        adapter = new SearchResultAdapter();
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(song -> {
            // TODO: 播放歌曲
        });
    }
    
    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isFavoriteTab = tab.getPosition() == 0;
                updateTitle();
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void updateTitle() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(isFavoriteTab ? R.string.home_favorite : R.string.home_recent);
        
        emptyView.setText(isFavoriteTab ? "暂无收藏" : "暂无播放记录");
    }
    
    private void loadData() {
        List<SongInfo> songs;
        if (isFavoriteTab) {
            songs = favoriteManager.getAllFavorites();
        } else {
            songs = historyManager.getHistory(50);
        }
        
        adapter.setSongs(songs);
        
        if (songs.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            listRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
