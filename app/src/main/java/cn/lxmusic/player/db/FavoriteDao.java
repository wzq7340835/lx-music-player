package cn.lxmusic.player.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import cn.lxmusic.player.model.FavoriteItem;
import cn.lxmusic.player.model.HistoryItem;
import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteItem item);
    
    @Update
    void update(FavoriteItem item);
    
    @Delete
    void delete(FavoriteItem item);
    
    @Query("SELECT * FROM favorites ORDER BY add_time DESC")
    List<FavoriteItem> getAll();
    
    @Query("SELECT * FROM favorites WHERE id = :id")
    FavoriteItem getById(String id);
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    boolean isFavorite(String id);
    
    @Query("DELETE FROM favorites WHERE id = :id")
    void deleteById(String id);
    
    @Query("SELECT COUNT(*) FROM favorites")
    int getCount();
}
