package cn.lxmusic.player.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import cn.lxmusic.player.R;

public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        
        setupSettings();
    }
    
    private void setupSettings() {
        CardView sourceSettingCard = findViewById(R.id.sourceSettingCard);
        CardView themeSettingCard = findViewById(R.id.themeSettingCard);
        CardView aboutSettingCard = findViewById(R.id.aboutSettingCard);
        
        sourceSettingCard.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, SettingsSourceActivity.class);
            startActivity(intent);
        });
        
        themeSettingCard.setOnClickListener(v -> {
        });
        
        aboutSettingCard.setOnClickListener(v -> {
            showAboutDialog();
        });
    }
    
    private void showAboutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("关于 LX Music")
            .setMessage("LX Music Player\n\n版本：1.0.0 (开发中)\n\n一个基于 Android 原生开发的音乐播放软件，支持导入自定义音乐源脚本。\n\n本项目仅供学习交流使用。")
            .setPositiveButton("确定", null)
            .show();
    }
}
