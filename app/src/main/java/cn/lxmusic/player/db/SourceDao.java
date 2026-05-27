package cn.lxmusic.player.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import cn.lxmusic.player.model.MusicSource;
import java.util.List;

@Dao
public interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MusicSource source);
    
    @Update
    void update(MusicSource source);
    
    @Delete
    void delete(MusicSource source);
    
    @Query("SELECT * FROM music_sources")
    List<MusicSource> getAll();
    
    @Query("SELECT * FROM music_sources WHERE id = :id")
    MusicSource getById(String id);
    
    @Query("SELECT * FROM music_sources WHERE enabled = 1")
    List<MusicSource> getEnabled();
    
    @Query("DELETE FROM music_sources WHERE id = :id")
    void deleteById(String id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(MusicSource source);
}
