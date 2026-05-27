package cn.lxmusic.player;

import android.app.Application;
import android.content.Context;
import cn.lxmusic.player.source.MusicSourceManager;

public class LXMusicApplication extends Application {
    private static LXMusicApplication instance;
    private MusicSourceManager sourceManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sourceManager = new MusicSourceManager(this);
    }
    
    public static LXMusicApplication getInstance() {
        return instance;
    }
    
    public MusicSourceManager getSourceManager() {
        return sourceManager;
    }
}
