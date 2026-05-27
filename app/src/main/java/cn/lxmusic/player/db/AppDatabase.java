package cn.lxmusic.player.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import cn.lxmusic.player.model.FavoriteItem;
import cn.lxmusic.player.model.HistoryItem;
import cn.lxmusic.player.model.MusicSource;

@Database(entities = {MusicSource.class, FavoriteItem.class, HistoryItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SourceDao sourceDao();
    public abstract FavoriteDao favoriteDao();
    public abstract HistoryDao historyDao();
    
    private static volatile AppDatabase instance;
    
    public static AppDatabase getInstance(android.content.Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = androidx.room.Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "lxmusic_app.db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
