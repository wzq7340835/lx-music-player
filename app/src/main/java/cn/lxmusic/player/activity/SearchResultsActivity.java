package cn.lxmusic.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import cn.lxmusic.player.R;
import cn.lxmusic.player.adapter.SearchResultAdapter;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.source.MusicSourceManager;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    
    private RecyclerView searchResultList;
    private TextView emptyView;
    private ProgressBar loadingProgressBar;
    private TextView searchKeyword;
    private TabLayout tabLayout;
    
    private SearchResultAdapter adapter;
    private List<SongInfo> allResults;
    
    private String keyword;
    private MusicSourceManager sourceManager;
    
    public static final String EXTRA_KEYWORD = "keyword";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        
        keyword = getIntent().getStringExtra(EXTRA_KEYWORD);
        
        initViews();
        setupListeners();
        
        sourceManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager();
        allResults = new ArrayList<>();
        
        if (keyword != null) {
            searchKeyword.setText("搜索：" + keyword);
            performSearch(keyword);
        }
    }
    
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        searchResultList = findViewById(R.id.searchResultList);
        emptyView = findViewById(R.id.emptyView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        searchKeyword = findViewById(R.id.searchKeyword);
        tabLayout = findViewById(R.id.tabLayout);
        
        adapter = new SearchResultAdapter();
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchResultList.setAdapter(adapter);
        
        adapter.setOnItemClickListener(song -> {
            // TODO: 播放歌曲
            // playerManager.play(song);
        });
    }
    
    private void setupListeners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterResults(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void performSearch(String keyword) {
        showLoading(true);
        
        List<MusicSourceManager.SourceCallback<SongInfo>> callbacks = new ArrayList<>();
        
        sourceManager.search(keyword, getEnabledSourceIds(), new MusicSourceManager.SourceCallback<List<SongInfo>>() {
            @Override
            public void onSuccess(List<SongInfo> results) {
                showLoading(false);
                allResults.clear();
                allResults.addAll(results);
                adapter.setSongs(results);
                updateEmptyState();
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                updateEmptyState();
            }
        });
    }
    
    private List<String> getEnabledSourceIds() {
        List<String> ids = new ArrayList<>();
        for (cn.lxmusic.player.model.MusicSource source : sourceManager.getEnabledSources()) {
            ids.add(source.getId());
        }
        return ids;
    }
    
    private void filterResults(int tabIndex) {
        if (tabIndex == 0) {
            adapter.setSongs(allResults);
        } else {
            // TODO: 过滤歌手结果
            adapter.setSongs(new ArrayList<>());
        }
    }
    
    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(show ? View.GONE : View.GONE);
        searchResultList.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void updateEmptyState() {
        if (allResults.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            searchResultList.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            searchResultList.setVisibility(View.VISIBLE);
        }
    }
}
