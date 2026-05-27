package cn.lxmusic.player.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import cn.lxmusic.player.R;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    
    private static final String PREF_SEARCH_HISTORY = "search_history";
    private static final int MAX_HISTORY = 20;
    
    private EditText searchInput;
    private ImageButton searchBtn;
    private RecyclerView searchHistoryList;
    private TextView emptyView;
    private View loadingProgressBar;
    
    private SearchHistoryAdapter historyAdapter;
    private List<String> searchHistory;
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        initViews();
        loadSearchHistory();
        setupListeners();
    }
    
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        searchHistoryList = findViewById(R.id.searchHistoryList);
        emptyView = findViewById(R.id.emptyView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        
        historyAdapter = new SearchHistoryAdapter(searchHistory);
        searchHistoryList.setLayoutManager(new LinearLayoutManager(this));
        searchHistoryList.setAdapter(historyAdapter);
    }
    
    private void setupListeners() {
        searchBtn.setOnClickListener(v -> performSearch());
        
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });
        
        historyAdapter.setOnItemClickListener(keyword -> {
            searchInput.setText(keyword);
            searchInput.setSelection(keyword.length());
            performSearch();
        });
    }
    
    private void performSearch() {
        String keyword = searchInput.getText().toString().trim();
        
        if (TextUtils.isEmpty(keyword)) {
            searchInput.setError("请输入搜索内容");
            return;
        }
        
        saveToHistory(keyword);
        
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
        
        // TODO: 调用搜索服务进行搜索
        // MusicSourceManager.search(keyword, callback);
    }
    
    private void loadSearchHistory() {
        prefs = getSharedPreferences(PREF_SEARCH_HISTORY, MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet("history", new LinkedHashSet<>());
        searchHistory = new ArrayList<>(historySet);
    }
    
    private void saveToHistory(String keyword) {
        searchHistory.remove(keyword);
        searchHistory.add(0, keyword);
        
        if (searchHistory.size() > MAX_HISTORY) {
            searchHistory = searchHistory.subList(0, MAX_HISTORY);
        }
        
        Set<String> historySet = new LinkedHashSet<>(searchHistory);
        prefs.edit().putStringSet("history", historySet).apply();
        
        historyAdapter.notifyDataSetChanged();
    }
    
    public void clearHistory() {
        searchHistory.clear();
        prefs.edit().putStringSet("history", new LinkedHashSet<>()).apply();
        historyAdapter.notifyDataSetChanged();
    }
    
    private class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
        private List<String> items;
        private OnItemClickListener listener;
        
        public SearchHistoryAdapter(List<String> items) {
            this.items = items;
        }
        
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_history, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(items.get(position));
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(items.get(position));
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return items.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            
            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.searchHistoryItem);
            }
        }
    }
    
    interface OnItemClickListener {
        void onItemClick(String keyword);
    }
}
