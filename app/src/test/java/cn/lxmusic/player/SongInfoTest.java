package cn.lxmusic.player;

import cn.lxmusic.player.model.SongInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class SongInfoTest {
    
    @Test
    public void testSongInfoConstructor() {
        SongInfo song = new SongInfo(
            "123",
            "Title",
            "Artist",
            "Album",
            "netease",
            "source1",
            240000L,
            "http://cover.jpg"
        );
        
        assertEquals("123", song.getId());
        assertEquals("Title", song.getTitle());
        assertEquals("Artist", song.getArtist());
        assertEquals("Album", song.getAlbum());
        assertEquals("netease", song.getPlatform());
        assertEquals("source1", song.getSourceId());
        assertEquals(240000L, song.getDuration());
        assertEquals("http://cover.jpg", song.getCoverUrl());
    }
    
    @Test
    public void testGetDisplayName() {
        SongInfo song = new SongInfo();
        song.setTitle("My Song");
        song.setArtist("My Artist");
        
        assertEquals("My Song - My Artist", song.getDisplayName());
    }
    
    @Test
    public void testSettersAndGetters() {
        SongInfo song = new SongInfo();
        
        song.setId("456");
        assertEquals("456", song.getId());
        
        song.setTitle("New Title");
        assertEquals("New Title", song.getTitle());
        
        song.setDuration(180000L);
        assertEquals(180000L, song.getDuration());
    }
    
    @Test
    public void testAddTime() {
        SongInfo song = new SongInfo();
        long addTime = song.getAddTime();
        
        assertTrue(addTime > 0);
        assertTrue(addTime <= System.currentTimeMillis());
    }
}
