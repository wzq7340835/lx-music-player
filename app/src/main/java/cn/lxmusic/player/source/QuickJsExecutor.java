package cn.lxmusic.player.source;

import android.content.Context;
import android.util.Log;
import cn.lxmusic.player.db.SourceDatabase;
import cn.lxmusic.player.model.MusicSource;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.model.PlayUrl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;

public class QuickJsExecutor {
    private static final String TAG = "QuickJsExecutor";
    private static Gson gson = new Gson();
    
    public static MusicSource extractSourceInfo(String script) {
        try {
            String infoJson = extractModuleExports(script);
            if (infoJson == null || infoJson.isEmpty()) {
                return null;
            }
            
            JsonObject jsonObj = JsonParser.parseString(infoJson).getAsJsonObject();
            if (!jsonObj.has("info")) {
                return null;
            }
            
            JsonObject info = jsonObj.getAsJsonObject("info");
            String name = info.has("name") ? info.get("name").getAsString() : "未知源";
            String version = info.has("version") ? info.get("version").getAsString() : "0.0.1";
            
            MusicSource source = new MusicSource();
            source.setId(generateSourceId(name));
            source.setName(name);
            source.setVersion(version);
            source.setEnabled(true);
            
            return source;
        } catch (Exception e) {
            Log.e(TAG, "Extract source info failed", e);
            return null;
        }
    }
    
    public static void search(Context context, SourceDatabase database, String sourceId, 
                             String keyword, MusicSourceManager.SourceCallback<SongInfo> callback) {
        try {
            MusicSource source = database.sourceDao().getById(sourceId);
            if (source == null) {
                callback.onError("音乐源不存在");
                return;
            }
            
            Map<String, Object> result = executeScript(context, source, "search", keyword);
            if (result != null) {
                SongInfo song = parseSong(result);
                if (song != null) {
                    callback.onSuccess(song);
                } else {
                    callback.onError("解析搜索结果失败");
                }
            } else {
                callback.onError("脚本执行失败");
            }
        } catch (Exception e) {
            Log.e(TAG, "Search failed", e);
            callback.onError("搜索失败：" + e.getMessage());
        }
    }
    
    public static void getPlayUrl(Context context, SourceDatabase database, String sourceId, 
                                  String songId, MusicSourceManager.SourceCallback<PlayUrl> callback) {
        try {
            MusicSource source = database.sourceDao().getById(sourceId);
            if (source == null) {
                callback.onError("音乐源不存在");
                return;
            }
            
            Map<String, Object> result = executeScript(context, source, "getPlayUrl", songId);
            if (result != null) {
                PlayUrl playUrl = parsePlayUrl(result);
                if (playUrl != null) {
                    callback.onSuccess(playUrl);
                } else {
                    callback.onError("解析播放链接失败");
                }
            } else {
                callback.onError("脚本执行失败");
            }
        } catch (Exception e) {
            Log.e(TAG, "Get play url failed", e);
            callback.onError("获取播放链接失败：" + e.getMessage());
        }
    }
    
    public static void getLyrics(Context context, SourceDatabase database, String sourceId, 
                                 String songId, MusicSourceManager.SourceCallback<String> callback) {
        try {
            MusicSource source = database.sourceDao().getById(sourceId);
            if (source == null) {
                callback.onError("音乐源不存在");
                return;
            }
            
            Map<String, Object> result = executeScript(context, source, "getLyrics", songId);
            if (result != null) {
                String lrc = (String) result.get("lrc");
                callback.onSuccess(lrc != null ? lrc : "");
            } else {
                callback.onError("脚本执行失败");
            }
        } catch (Exception e) {
            Log.e(TAG, "Get lyrics failed", e);
            callback.onError("获取歌词失败：" + e.getMessage());
        }
    }
    
    private static String extractModuleExports(String script) {
        int moduleIndex = script.indexOf("module.exports");
        if (moduleIndex == -1) {
            return null;
        }
        
        int assignIndex = script.indexOf("=", moduleIndex);
        if (assignIndex == -1) {
            return null;
        }
        
        int startBraceIndex = script.indexOf("{", assignIndex);
        if (startBraceIndex == -1) {
            return null;
        }
        
        int braceCount = 1;
        int endBraceIndex = startBraceIndex + 1;
        
        while (braceCount > 0 && endBraceIndex < script.length()) {
            char c = script.charAt(endBraceIndex);
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;
            endBraceIndex++;
        }
        
        return script.substring(startBraceIndex, endBraceIndex);
    }
    
    private static String generateSourceId(String name) {
        return String.valueOf(Math.abs(name.hashCode()));
    }
    
    private static SongInfo parseSong(Map<String, Object> data) {
        try {
            String json = gson.toJson(data);
            return gson.fromJson(json, SongInfo.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static PlayUrl parsePlayUrl(Map<String, Object> data) {
        try {
            String json = gson.toJson(data);
            return gson.fromJson(json, PlayUrl.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Map<String, Object> executeScript(Context context, MusicSource source, 
                                                     String functionName, String arg) {
        try {
            String script = loadScript(context, source.getScriptUrl());
            if (script == null) {
                return null;
            }
            
            String jsCode = buildScriptCall(script, functionName, arg);
            String result = "result";
            
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 0);
            resultMap.put("data", result);
            
            return resultMap;
        } catch (Exception e) {
            Log.e(TAG, "Execute script failed: " + e.getMessage());
            return null;
        }
    }
    
    private static String loadScript(Context context, String url) {
        try {
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            Log.e(TAG, "Load script failed", e);
        }
        return null;
    }
    
    private static String buildScriptCall(String script, String functionName, String arg) {
        return script + ";\n" + 
               functionName + "('\"'" + arg + "'\"'");
    }
}
