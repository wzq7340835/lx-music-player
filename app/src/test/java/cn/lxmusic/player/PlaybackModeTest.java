package cn.lxmusic.player;

import cn.lxmusic.player.player.PlayerManager.PlaybackMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testPlaybackModeValues() {
        PlaybackMode[] modes = PlaybackMode.values();
        
        assertEquals(3, modes.length);
        assertEquals(PlaybackMode.REPEAT_SINGLE, modes[0]);
        assertEquals(PlaybackMode.REPEAT_ALL, modes[1]);
        assertEquals(PlaybackMode.SHUFFLE, modes[2]);
    }
    
    @Test
    public void testPlaybackModeValueOf() {
        assertEquals(PlaybackMode.REPEAT_SINGLE, PlaybackMode.valueOf("REPEAT_SINGLE"));
        assertEquals(PlaybackMode.REPEAT_ALL, PlaybackMode.valueOf("REPEAT_ALL"));
        assertEquals(PlaybackMode.SHUFFLE, PlaybackMode.valueOf("SHUFFLE"));
    }
}
