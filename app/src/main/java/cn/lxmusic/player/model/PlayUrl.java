package cn.lxmusic.player.model;

public class PlayUrl {
    private String url;
    private int quality;
    private String format;
    private long size;
    private boolean vip;
    
    public PlayUrl() {
    }
    
    public PlayUrl(String url, int quality, String format) {
        this.url = url;
        this.quality = quality;
        this.format = format;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getQuality() {
        return quality;
    }
    
    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public boolean isVip() {
        return vip;
    }
    
    public void setVip(boolean vip) {
        this.vip = vip;
    }
    
    public String getQualityText() {
        switch (quality) {
            case 0: return "标准";
            case 1: return "高质量";
            case 2: return "无损";
            default: return "未知";
        }
    }
}
