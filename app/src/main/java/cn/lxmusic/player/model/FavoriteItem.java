package cn.lxmusic.player.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favorites")
public class FavoriteItem {
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
    
    @SerializedName("coverUrl")
    private String coverUrl;
    
    @SerializedName("addTime")
    private long addTime;
    
    public FavoriteItem() {
        this.addTime = System.currentTimeMillis();
    }
    
    public FavoriteItem(SongInfo song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.album = song.getAlbum();
        this.platform = song.getPlatform();
        this.sourceId = song.getSourceId();
        this.duration = song.getDuration();
        this.coverUrl = song.getCoverUrl();
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
    
    public long getAddTime() {
        return addTime;
    }
    
    public void setAddTime(long addTime) {
        this.addTime = addTime;
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
        song.setCoverUrl(this.coverUrl);
        return song;
    }
}
