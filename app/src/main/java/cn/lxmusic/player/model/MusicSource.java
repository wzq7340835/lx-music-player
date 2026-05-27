package cn.lxmusic.player.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MusicSource {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("version")
    private String version;
    
    @SerializedName("scriptUrl")
    private String scriptUrl;
    
    @SerializedName("platforms")
    private List<String> platforms;
    
    @SerializedName("enabled")
    private boolean enabled;
    
    @SerializedName("updateTime")
    private long updateTime;
    
    public MusicSource() {
    }
    
    public MusicSource(String id, String name, String version, String scriptUrl) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.scriptUrl = scriptUrl;
        this.enabled = true;
        this.updateTime = System.currentTimeMillis();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getScriptUrl() {
        return scriptUrl;
    }
    
    public void setScriptUrl(String scriptUrl) {
        this.scriptUrl = scriptUrl;
    }
    
    public List<String> getPlatforms() {
        return platforms;
    }
    
    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getPlatformText() {
        if (platforms == null || platforms.isEmpty()) {
            return "未知平台";
        }
        return String.join(", ", platforms);
    }
}
