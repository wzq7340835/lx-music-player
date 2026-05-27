package cn.lxmusic.player.util;

import android.util.Log;
import cn.lxmusic.player.model.LyricsInfo;

public class LyricsParser {
    private static final String TAG = "LyricsParser";
    
    public static LyricsInfo parse(String content) {
        if (content == null || content.isEmpty()) {
            return new LyricsInfo();
        }
        
        LyricsInfo lyricsInfo = new LyricsInfo();
        String[] lines = content.split("\\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            if (line.contains("ti:")) {
                lyricsInfo.setTitle(extractTagValue(line, "ti:"));
            } else if (line.contains("ar:")) {
                lyricsInfo.setArtist(extractTagValue(line, "ar:"));
            } else if (line.contains("al:")) {
                lyricsInfo.setAlbum(extractTagValue(line, "al:"));
            } else if (line.startsWith("[") && line.contains("]")) {
                LyricsInfo.LyricsLine lyricsLine = parseLine(line);
                if (lyricsLine != null) {
                    lyricsInfo.addLine(lyricsLine.getTime(), lyricsLine.getContent());
                }
            }
        }
        
        return lyricsInfo;
    }
    
    private static LyricsInfo.LyricsLine parseLine(String line) {
        if (line == null || !line.matches("^\\[\\d{2}:\\d{2}\\.\\d{2,3}\\].*$")) {
            return null;
        }
        
        int endBracketIndex = line.indexOf(']');
        String timeStr = line.substring(1, endBracketIndex);
        String content = line.substring(endBracketIndex + 1).trim();
        
        long time = parseTime(timeStr);
        return new LyricsInfo.LyricsLine(time, content);
    }
    
    private static long parseTime(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            if (parts.length != 2) {
                return 0;
            }
            
            int minutes = Integer.parseInt(parts[0]);
            double seconds = Double.parseDouble(parts[1]);
            
            return minutes * 60L * 1000L + (long)(seconds * 1000L);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Parse time failed: " + timeStr, e);
            return 0;
        }
    }
    
    private static String extractTagValue(String line, String tag) {
        if (!line.contains(tag)) {
            return null;
        }
        
        int tagIndex = line.indexOf(tag);
        int start = tagIndex + tag.length();
        int end = line.indexOf(']', start);
        
        if (end == -1) {
            end = line.length();
        }
        
        return line.substring(start, end).trim();
    }
}
