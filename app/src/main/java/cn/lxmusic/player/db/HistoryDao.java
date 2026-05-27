package cn.lxmusic.player.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import cn.lxmusic.player.model.HistoryItem;
import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryItem item);
    
    @Update
    void update(HistoryItem item);
    
    @Delete
    void delete(HistoryItem item);
    
    @Query("SELECT * FROM history ORDER BY last_play_time DESC LIMIT :limit")
    List<HistoryItem> getHistory(int limit);
    
    @Query("SELECT * FROM history WHERE id = :id")
    HistoryItem getById(String id);
    
    @Query("DELETE FROM history WHERE id = :id")
    void deleteById(String id);
    
    @Query("DELETE FROM history")
    void clearAll();
    
    @Query("SELECT COUNT(*) FROM history")
    int getCount();
    
    @Query("UPDATE history SET play_count = play_count + 1, last_play_time = :playTime WHERE id = :id")
    void incrementPlayCount(String id, long playTime);
}
