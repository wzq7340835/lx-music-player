package cn.lxmusic.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import cn.lxmusic.player.R;
import cn.lxmusic.player.activity.MyListActivity;
import cn.lxmusic.player.activity.SettingsActivity;
import cn.lxmusic.player.db.FavoriteManager;
import cn.lxmusic.player.db.HistoryManager;

public class MyFragment extends Fragment {
    
    private CardView favoriteCard;
    private CardView historyCard;
    private CardView settingsCard;
    private TextView favoriteCountText;
    private TextView historyCountText;
    
    private FavoriteManager favoriteManager;
    private HistoryManager historyManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        
        favoriteManager = FavoriteManager.getInstance(requireContext());
        historyManager = HistoryManager.getInstance(requireContext());
        
        setupListeners();
        updateCounts();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateCounts();
    }
    
    private void initViews(View view) {
        favoriteCard = view.findViewById(R.id.favoriteCard);
        historyCard = view.findViewById(R.id.historyCard);
        settingsCard = view.findViewById(R.id.settingsCard);
        favoriteCountText = view.findViewById(R.id.favoriteCountText);
        historyCountText = view.findViewById(R.id.historyCountText);
    }
    
    private void setupListeners() {
        favoriteCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyListActivity.class);
            startActivity(intent);
        });
        
        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyListActivity.class);
            intent.putExtra("tab", 1);
            startActivity(intent);
        });
        
        settingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
    
    private void updateCounts() {
        int favoriteCount = favoriteManager.getTotalCount();
        int historyCount = historyManager.getTotalCount();
        
        favoriteCountText.setText(favoriteCount + " 首歌曲");
        historyCountText.setText(historyCount + " 首歌曲");
    }
}
