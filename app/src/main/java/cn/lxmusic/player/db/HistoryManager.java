package cn.lxmusic.player.db;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final int MAX_HISTORY = 100;
    private static HistoryManager instance;
    private HistoryDao historyDao;
    
    private HistoryManager(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.historyDao = db.historyDao();
    }
    
    public static synchronized HistoryManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public void addHistory(final cn.lxmusic.player.model.SongInfo song) {
        new Thread(() -> {
            cn.lxmusic.player.model.HistoryItem existingItem = historyDao.getById(song.getId());
            
            if (existingItem != null) {
                historyDao.incrementPlayCount(song.getId(), System.currentTimeMillis());
            } else {
                cn.lxmusic.player.model.HistoryItem newItem = new cn.lxmusic.player.model.HistoryItem(song);
                historyDao.insert(newItem);
                
                int count = historyDao.getCount();
                if (count > MAX_HISTORY) {
                    List<cn.lxmusic.player.model.HistoryItem> allHistory = historyDao.getHistory(count);
                    if (allHistory.size() > MAX_HISTORY) {
                        cn.lxmusic.player.model.HistoryItem oldestItem = allHistory.get(allHistory.size() - 1);
                        historyDao.delete(oldestItem);
                    }
                }
            }
        }).start();
    }
    
    public List<cn.lxmusic.player.model.SongInfo> getHistory(int limit) {
        List<cn.lxmusic.player.model.HistoryItem> items = historyDao.getHistory(limit);
        List<cn.lxmusic.player.model.SongInfo> songs = new ArrayList<>();
        
        for (cn.lxmusic.player.model.HistoryItem item : items) {
            songs.add(item.toSongInfo());
        }
        
        return songs;
    }
    
    public void removeHistory(String songId) {
        new Thread(() -> {
            historyDao.deleteById(songId);
        }).start();
    }
    
    public void clearHistory() {
        new Thread(() -> {
            historyDao.clearAll();
        }).start();
    }
    
    public int getTotalCount() {
        return historyDao.getCount();
    }
}
