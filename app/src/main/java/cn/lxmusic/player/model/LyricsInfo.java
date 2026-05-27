package cn.lxmusic.player.model;

import java.util.ArrayList;
import java.util.List;

public class LyricsInfo {
    private String title;
    private String artist;
    private String album;
    private List<LyricsLine> lines;
    
    public LyricsInfo() {
        this.lines = new ArrayList<>();
    }
    
    public LyricsInfo(String title, String artist, String album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.lines = new ArrayList<>();
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
    
    public List<LyricsLine> getLines() {
        return lines;
    }
    
    public void setLines(List<LyricsLine> lines) {
        this.lines = lines;
    }
    
    public void addLine(long time, String content) {
        this.lines.add(new LyricsLine(time, content));
    }
    
    public boolean hasLines() {
        return lines != null && !lines.isEmpty();
    }
    
    public int getCurrentLineIndex(long currentTime) {
        if (lines == null || lines.isEmpty()) {
            return -1;
        }
        
        for (int i = 0; i < lines.size() - 1; i++) {
            long currentTimeStamp = lines.get(i).getTime();
            long nextTimeStamp = lines.get(i + 1).getTime();
            
            if (currentTime >= currentTimeStamp && currentTime < nextTimeStamp) {
                return i;
            }
        }
        
        if (currentTime >= lines.get(lines.size() - 1).getTime()) {
            return lines.size() - 1;
        }
        
        return 0;
    }
    
    public static class LyricsLine {
        private long time;
        private String content;
        
        public LyricsLine() {
        }
        
        public LyricsLine(long time, String content) {
            this.time = time;
            this.content = content;
        }
        
        public long getTime() {
            return time;
        }
        
        public void setTime(long time) {
            this.time = time;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
        
        public String getDisplayString() {
            return content.trim();
        }
    }
}
