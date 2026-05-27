package cn.lxmusic.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.lxmusic.player.R;
import cn.lxmusic.player.activity.MyListActivity;
import cn.lxmusic.player.adapter.SongListAdapter;
import cn.lxmusic.player.db.HistoryManager;
import cn.lxmusic.player.db.FavoriteManager;
import cn.lxmusic.player.model.SongInfo;
import java.util.List;

public class HomeFragment extends Fragment {
    
    private RecyclerView recentHistoryList;
    private TextView recentEmptyView;
    private TextView favoriteCountText;
    private LinearLayout favoriteEntryCard;
    
    private SongListAdapter adapter;
    private HistoryManager historyManager;
    private FavoriteManager favoriteManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        
        historyManager = HistoryManager.getInstance(requireContext());
        favoriteManager = FavoriteManager.getInstance(requireContext());
        
        setupAdapter();
        setupListeners();
        loadData();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    
    private void initViews(View view) {
        recentHistoryList = view.findViewById(R.id.recentHistoryList);
        recentEmptyView = view.findViewById(R.id.recentEmptyView);
        favoriteCountText = view.findViewById(R.id.favoriteCountText);
        favoriteEntryCard = view.findViewById(R.id.favoriteEntryCard);
    }
    
    private void setupAdapter() {
        adapter = new SongListAdapter(requireContext());
        recentHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        recentHistoryList.setAdapter(adapter);
        
        adapter.setOnSongActionListener(new SongListAdapter.OnSongActionListener() {
            @Override
            public void onPlay(SongInfo song) {
                // TODO: 播放歌曲
            }

            @Override
            public void onAddToFavorite(SongInfo song) {
                favoriteManager.addToFavorite(song);
            }

            @Override
            public void onRemoveFromFavorite(SongInfo song) {
                favoriteManager.removeFromFavorite(song.getId());
            }
        });
    }
    
    private void setupListeners() {
        favoriteEntryCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyListActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadData() {
        List<SongInfo> history = historyManager.getHistory(10);
        adapter.setSongs(history);
        
        if (history.isEmpty()) {
            recentEmptyView.setVisibility(View.VISIBLE);
            recentHistoryList.setVisibility(View.GONE);
        } else {
            recentEmptyView.setVisibility(View.GONE);
            recentHistoryList.setVisibility(View.VISIBLE);
        }
        
        int favoriteCount = favoriteManager.getTotalCount();
        favoriteCountText.setText(favoriteCount + " 首歌曲");
    }
}
