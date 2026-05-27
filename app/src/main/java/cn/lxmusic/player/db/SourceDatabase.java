package cn.lxmusic.player.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import cn.lxmusic.player.model.MusicSource;

@Database(entities = {MusicSource.class}, version = 1)
public abstract class SourceDatabase extends androidx.room.RoomDatabase {
    public abstract SourceDao sourceDao();
    
    private static volatile SourceDatabase instance;
    
    public static SourceDatabase getInstance(android.content.Context context) {
        if (instance == null) {
            synchronized (SourceDatabase.class) {
                if (instance == null) {
                    instance = androidx.room.Room.databaseBuilder(
                        context.getApplicationContext(),
                        SourceDatabase.class,
                        "lxmusic_sources.db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
