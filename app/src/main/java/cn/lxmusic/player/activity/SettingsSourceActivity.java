package cn.lxmusic.player.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import cn.lxmusic.player.R;
import cn.lxmusic.player.adapter.MusicSourceAdapter;
import cn.lxmusic.player.model.MusicSource;
import cn.lxmusic.player.source.MusicSourceManager;

import java.util.List;

public class SettingsSourceActivity extends AppCompatActivity {
    
    private RecyclerView sourceList;
    private TextView emptyView;
    private FloatingActionButton addSourceFab;
    
    private MusicSourceAdapter adapter;
    private MusicSourceManager sourceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_source);
        
        initViews();
        
        sourceManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager();
        
        setupAdapter();
        loadSources();
        setupListeners();
    }
    
    private void initViews() {
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        
        sourceList = findViewById(R.id.sourceList);
        emptyView = findViewById(R.id.emptyView);
        addSourceFab = findViewById(R.id.addSourceFab);
    }
    
    private void setupAdapter() {
        adapter = new MusicSourceAdapter();
        sourceList.setLayoutManager(new LinearLayoutManager(this));
        sourceList.setAdapter(adapter);
        
        adapter.setOnSourceActionListener(new MusicSourceAdapter.OnSourceActionListener() {
            @Override
            public void onToggleEnable(MusicSource source, boolean enable) {
                sourceManager.enableSource(source.getId(), enable);
            }
            
            @Override
            public void onDelete(MusicSource source) {
                new AlertDialog.Builder(SettingsSourceActivity.this)
                    .setTitle("删除音乐源")
                    .setMessage("确定要删除 " + source.getName() + " 吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        sourceManager.deleteSource(source.getId());
                        loadSources();
                    })
                    .setNegativeButton("取消", null)
                    .show();
            }
        });
    }
    
    private void loadSources() {
        List<MusicSource> sources = sourceManager.getAllSources();
        adapter.setSources(sources);
        
        if (sources.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            sourceList.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            sourceList.setVisibility(View.VISIBLE);
        }
    }
    
    private void setupListeners() {
        addSourceFab.setOnClickListener(v -> showImportDialog());
    }
    
    private void showImportDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_import_source, null);
        TextInputLayout urlInputLayout = dialogView.findViewById(R.id.urlInputLayout);
        TextInputEditText urlInput = dialogView.findViewById(R.id.urlInput);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .create();
        
        dialogView.findViewById(R.id.cancelBtn).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.importBtn).setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            
            if (url.isEmpty()) {
                urlInputLayout.setError("请输入音乐源 URL");
                return;
            }
            
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                urlInputLayout.setError("URL 必须以 http://或 https://开头");
                return;
            }
            
            dialog.dismiss();
            importSource(url);
        });
        
        dialog.show();
    }
    
    private void importSource(String url) {
        sourceManager.importSource(url, new MusicSourceManager.SourceCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                runOnUiThread(() -> {
                    Toast.makeText(SettingsSourceActivity.this, 
                        R.string.source_import_success, Toast.LENGTH_SHORT).show();
                    loadSources();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SettingsSourceActivity.this, 
                        R.string.source_import_failed + ": " + error, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
