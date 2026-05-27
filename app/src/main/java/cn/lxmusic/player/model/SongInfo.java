package cn.lxmusic.player.model;

import com.google.gson.annotations.SerializedName;

public class SongInfo {
    @SerializedName("id")
    private String id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("artist")
    private String artist;
    
    @SerializedName("album")
    private String album;
    
    @SerializedName("platform")
    private String platform;
    
    @SerializedName("sourceId")
    private String sourceId;
    
    @SerializedName("duration")
    private long duration;
    
    @SerializedName("coverUrl")
    private String coverUrl;
    
    @SerializedName("quality")
    private int quality;
    
    @SerializedName("addTime")
    private long addTime;
    
    public SongInfo() {
        this.addTime = System.currentTimeMillis();
    }
    
    public SongInfo(String id, String title, String artist, String album, 
                    String platform, String sourceId, long duration, String coverUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.platform = platform;
        this.sourceId = sourceId;
        this.duration = duration;
        this.coverUrl = coverUrl;
        this.quality = 0;
        this.addTime = System.currentTimeMillis();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public String getAlbum() {
        return album;
    }
    
    public void setAlbum(String album) {
        this.album = album;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    
    public int getQuality() {
        return quality;
    }
    
    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    public long getAddTime() {
        return addTime;
    }
    
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
    
    public String getDisplayName() {
        return title + " - " + artist;
    }
}
