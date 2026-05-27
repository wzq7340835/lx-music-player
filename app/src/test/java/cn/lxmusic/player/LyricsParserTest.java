package cn.lxmusic.player;

import cn.lxmusic.player.model.LyricsInfo;
import cn.lxmusic.player.util.LyricsParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class LyricsParserTest {
    
    @Test
    public void testParseEmptyLyrics() {
        LyricsInfo lyrics = LyricsParser.parse("");
        assertNotNull(lyrics);
        assertTrue(lyrics.getLines().isEmpty());
    }
    
    @Test
    public void testParseSimpleLyrics() {
        String lrc = "[00:00.00] Line 1\n[00:05.00] Line 2\n[00:10.00] Line 3";
        LyricsInfo lyrics = LyricsParser.parse(lrc);
        
        assertNotNull(lyrics);
        assertEquals(3, lyrics.getLines().size());
        assertEquals("Line 1", lyrics.getLines().get(0).getContent());
        assertEquals(0L, lyrics.getLines().get(0).getTime());
        assertEquals("Line 2", lyrics.getLines().get(1).getContent());
        assertEquals(5000L, lyrics.getLines().get(1).getTime());
    }
    
    @Test
    public void testParseLyricsWithMetadata() {
        String lrc = "[ti:Song Title]\n[ar:Artist Name]\n[al:Album]\n[00:00.00] Line 1";
        LyricsInfo lyrics = LyricsParser.parse(lrc);
        
        assertEquals("Song Title", lyrics.getTitle());
        assertEquals("Artist Name", lyrics.getArtist());
        assertEquals("Album", lyrics.getAlbum());
        assertEquals(1, lyrics.getLines().size());
    }
    
    @Test
    public void testGetCurrentLineIndex() {
        String lrc = "[00:00.00] Line 1\n[00:05.00] Line 2\n[00:10.00] Line 3";
        LyricsInfo lyrics = LyricsParser.parse(lrc);
        
        assertEquals(0, lyrics.getCurrentLineIndex(0L));
        assertEquals(0, lyrics.getCurrentLineIndex(2500L));
        assertEquals(1, lyrics.getCurrentLineIndex(5000L));
        assertEquals(1, lyrics.getCurrentLineIndex(7500L));
        assertEquals(2, lyrics.getCurrentLineIndex(10000L));
    }
    
    @Test
    public void testGetCurrentLineIndexOutOfBounds() {
        String lrc = "[00:00.00] Line 1";
        LyricsInfo lyrics = LyricsParser.parse(lrc);
        
        assertEquals(0, lyrics.getCurrentLineIndex(-1000L));
    }
}
