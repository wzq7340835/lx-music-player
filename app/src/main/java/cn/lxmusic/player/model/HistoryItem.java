package cn.lxmusic.player.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "history")
public class HistoryItem {
    @PrimaryKey
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
    
    @SerializedName("playCount")
    private int playCount;
    
    @SerializedName("lastPlayTime")
    private long lastPlayTime;
    
    public HistoryItem() {
        this.playCount = 1;
        this.lastPlayTime = System.currentTimeMillis();
    }
    
    public HistoryItem(SongInfo song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.album = song.getAlbum();
        this.platform = song.getPlatform();
        this.sourceId = song.getSourceId();
        this.duration = song.getDuration();
        this.playCount = 1;
        this.lastPlayTime = System.currentTimeMillis();
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
    
    public int getPlayCount() {
        return playCount;
    }
    
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
    
    public long getLastPlayTime() {
        return lastPlayTime;
    }
    
    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
    
    public SongInfo toSongInfo() {
        SongInfo song = new SongInfo();
        song.setId(this.id);
        song.setTitle(this.title);
        song.setArtist(this.artist);
        song.setAlbum(this.album);
        song.setPlatform(this.platform);
        song.setSourceId(this.sourceId);
        song.setDuration(this.duration);
        return song;
    }
}
