package cn.lxmusic.player;

import android.app.Application;
import cn.lxmusic.player.player.PlayerManager;
import cn.lxmusic.player.source.MusicSourceManager;

public class LXMusicApplication extends Application {
    private static LXMusicApplication instance;
    private MusicSourceManager sourceManager;
    private PlayerManager playerManager;
    
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
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}
