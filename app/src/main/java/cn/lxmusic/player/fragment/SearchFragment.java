package cn.lxmusic.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.lxmusic.player.R;
import cn.lxmusic.player.activity.SearchResultsActivity;

public class SearchFragment extends Fragment {
    
    private EditText searchInput;
    private RecyclerView searchHistoryList;
    private TextView emptyView;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        searchInput = view.findViewById(R.id.searchInput);
        searchHistoryList = view.findViewById(R.id.searchHistoryList);
        emptyView = view.findViewById(R.id.emptyView);
        
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });
    }
    
    private void performSearch() {
        String keyword = searchInput.getText().toString().trim();
        
        if (keyword.isEmpty()) {
            searchInput.setError("请输入搜索内容");
            return;
        }
        
        Intent intent = new Intent(getContext(), SearchResultsActivity.class);
        intent.putExtra(SearchResultsActivity.EXTRA_KEYWORD, keyword);
        startActivity(intent);
        
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }
}
