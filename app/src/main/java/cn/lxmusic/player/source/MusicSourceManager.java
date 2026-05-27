package cn.lxmusic.player.source;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import cn.lxmusic.player.db.SourceDatabase;
import cn.lxmusic.player.model.MusicSource;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.model.PlayUrl;
import cn.lxmusic.player.model.LyricsInfo;
import cn.lxmusic.player.util.LyricsParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicSourceManager {
    private static final String TAG = "MusicSourceManager";
    private static final String SCRIPT_TAG = "// Music Source Script";
    
    private Context context;
    private SourceDatabase database;
    private OkHttpClient httpClient;
    private List<SourceCallback> callbacks;
    
    public interface SourceCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
    
    public MusicSourceManager(Context context) {
        this.context = context;
        this.database = Room.databaseBuilder(context, SourceDatabase.class, "lxmusic_sources.db").build();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.callbacks = new ArrayList<>();
    }
    
    public void importSource(String url, final SourceCallback<Boolean> callback) {
        Request request = new Request.Builder().url(url).build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Download source script failed", e);
                callback.onError("下载音乐源失败：" + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("下载失败，HTTP 状态码：" + response.code());
                    return;
                }
                
                String script = response.body().string();
                validateAndSaveScript(script, url, callback);
            }
        });
    }
    
    public void importSource(File scriptFile, final SourceCallback<Boolean> callback) {
        if (!scriptFile.exists()) {
            callback.onError("文件不存在");
            return;
        }
        
        try {
            String script = android.os.FileUtils.readFullFile(scriptFile);
            validateAndSaveScript(script, null, callback);
        } catch (IOException e) {
            callback.onError("读取文件失败：" + e.getMessage());
        }
    }
    
    private void validateAndSaveScript(String script, String url, final SourceCallback<Boolean> callback) {
        script = script.trim();
        
        if (script.isEmpty()) {
            callback.onError("空脚本");
            return;
        }
        
        try {
            MusicSource source = QuickJsExecutor.extractSourceInfo(script);
            if (source == null) {
                callback.onError("脚本格式无效");
                return;
            }
            
            source.setScriptUrl(url);
            source.setUpdateTime(System.currentTimeMillis());
            
            new Thread(() -> {
                database.sourceDao().insertOrUpdate(source);
                callback.onSuccess(true);
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Validate script failed", e);
            callback.onError("脚本解析失败：" + e.getMessage());
        }
    }
    
    public void search(String keyword, List<String> sourceIds, SourceCallback<List<SongInfo>> callback) {
        List<SongInfo> results = new ArrayList<>();
        int[] completedCount = {0};
        List<IOException> errors = new ArrayList<>();
        
        for (String sourceId : sourceIds) {
            QuickJsExecutor.search(context, database, sourceId, keyword, new SourceCallback<SongInfo>() {
                @Override
                public void onSuccess(SongInfo song) {
                    synchronized (results) {
                        if (song != null) {
                            results.add(song);
                        }
                    }
                    checkCompletion();
                }
                
                @Override
                public void onError(String error) {
                    errors.add(new IOException(error));
                    checkCompletion();
                }
                
                private void checkCompletion() {
                    completedCount[0]++;
                    if (completedCount[0] >= sourceIds.size()) {
                        callback.onSuccess(results);
                    }
                }
            });
        }
    }
    
    public void getPlayUrl(SongInfo song, SourceCallback<PlayUrl> callback) {
        QuickJsExecutor.getPlayUrl(context, database, song.getSourceId(), song.getId(), callback);
    }
    
    public void getLyrics(SongInfo song, SourceCallback<LyricsInfo> callback) {
        QuickJsExecutor.getLyrics(context, database, song.getSourceId(), song.getId(), new SourceCallback<String>() {
            @Override
            public void onSuccess(String lrc) {
                LyricsInfo lyrics = LyricsParser.parse(lrc);
                callback.onSuccess(lyrics);
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
    
    public List<MusicSource> getAllSources() {
        return database.sourceDao().getAll();
    }
    
    public void enableSource(String sourceId, boolean enable) {
        new Thread(() -> {
            MusicSource source = database.sourceDao().getById(sourceId);
            if (source != null) {
                source.setEnabled(enable);
                database.sourceDao().update(source);
            }
        }).start();
    }
    
    public List<MusicSource> getEnabledSources() {
        return database.sourceDao().getEnabled();
    }
    
    public void deleteSource(String sourceId) {
        new Thread(() -> {
            database.sourceDao().deleteById(sourceId);
        }).start();
    }
}
