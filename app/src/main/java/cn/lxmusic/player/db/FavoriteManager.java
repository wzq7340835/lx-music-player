package cn.lxmusic.player.db;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    private static FavoriteManager instance;
    private FavoriteDao favoriteDao;
    
    private FavoriteManager(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.favoriteDao = db.favoriteDao();
    }
    
    public static synchronized FavoriteManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public boolean addToFavorite(final cn.lxmusic.player.model.SongInfo song) {
        if (isFavorite(song.getId())) {
            return false;
        }
        
        new Thread(() -> {
            cn.lxmusic.player.model.FavoriteItem item = new cn.lxmusic.player.model.FavoriteItem(song);
            favoriteDao.insert(item);
        }).start();
        
        return true;
    }
    
    public boolean removeFromFavorite(String songId) {
        new Thread(() -> {
            cn.lxmusic.player.model.FavoriteItem item = favoriteDao.getById(songId);
            if (item != null) {
                favoriteDao.delete(item);
            }
        }).start();
        
        return true;
    }
    
    public boolean isFavorite(String songId) {
        return favoriteDao.isFavorite(songId);
    }
    
    public List<cn.lxmusic.player.model.SongInfo> getAllFavorites() {
        List<cn.lxmusic.player.model.FavoriteItem> items = favoriteDao.getAll();
        List<cn.lxmusic.player.model.SongInfo> songs = new ArrayList<>();
        
        for (cn.lxmusic.player.model.FavoriteItem item : items) {
            songs.add(item.toSongInfo());
        }
        
        return songs;
    }
    
    public int getTotalCount() {
        return favoriteDao.getCount();
    }
}
