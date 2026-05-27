package cn.lxmusic.player.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import cn.lxmusic.player.model.SongInfo;
import cn.lxmusic.player.source.MusicSourceManager;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static final String TAG = "PlayerManager";
    private static final long PROGRESS_UPDATE_INTERVAL = 500L;
    
    public enum PlaybackMode {
        REPEAT_SINGLE,
        REPEAT_ALL,
        SHUFFLE
    }
    
    public interface PlayerListener {
        void onSongChange(SongInfo song);
        void onPlayStateChanged(int state);
        void onProgressUpdate(long position, long duration);
        void onError(String error);
    }
    
    private Context context;
    private ExoPlayer player;
    private SongInfo currentSong;
    private List<SongInfo> playQueue;
    private int currentQueueIndex;
    private PlaybackMode playbackMode;
    private List<PlayerListener> listeners;
    private Handler progressHandler;
    private Runnable progressRunnable;
    
    public PlayerManager(Context context) {
        this.context = context.getApplicationContext();
        this.playQueue = new ArrayList<>();
        this.currentQueueIndex = -1;
        this.playbackMode = PlaybackMode.REPEAT_ALL;
        this.listeners = new ArrayList<>();
        this.progressHandler = new Handler(Looper.getMainLooper());
        
        initPlayer();
    }
    
    private void initPlayer() {
        player = new ExoPlayer.Builder(context).build();
        player.setRepeatMode(getExoRepeatMode());
        
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                handlePlaybackStateChanged(state);
            }
            
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e(TAG, "Playback error", error);
                notifyError(error.getMessage());
                
                if (playbackMode == PlaybackMode.REPEAT_ALL) {
                    playNext();
                }
            }
            
            @Override
            public void onEvents(Player player, Player.Events events) {
                if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)) {
                    notifyProgressUpdate();
                }
            }
        });
        
        startProgressUpdates();
    }
    
    private void handlePlaybackStateChanged(int state) {
        for (PlayerListener listener : listeners) {
            listener.onPlayStateChanged(state);
        }
        
        if (state == Player.STATE_ENDED) {
            handleSongEnded();
        }
    }
    
    private void handleSongEnded() {
        switch (playbackMode) {
            case REPEAT_SINGLE:
                seekTo(0);
                play();
                break;
            case REPEAT_ALL:
                playNext();
                break;
            case SHUFFLE:
                playRandom();
                break;
        }
    }
    
    public void setSurface(android.view.Surface surface) {
        player.setVideoSurface(surface);
    }
    
    public void play(String url, SongInfo song) {
        try {
            currentSong = song;
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
            
            notifySongChange(song);
        } catch (Exception e) {
            Log.e(TAG, "Play failed", e);
            notifyError("播放失败：" + e.getMessage());
        }
    }
    
    public void play(SongInfo song, String url) {
        play(url, song);
    }
    
    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }
    
    public void resume() {
        if (player != null) {
            player.play();
        }
    }
    
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }
    
    public void seekTo(long position) {
        if (player != null) {
            player.seekTo(position);
        }
    }
    
    public long getCurrentPosition() {
        return player != null ? player.getCurrentPosition() : 0;
    }
    
    public long getDuration() {
        return player != null ? player.getDuration() : 0;
    }
    
    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }
    
    public void setVolume(float volume) {
        if (player != null) {
            player.setVolume(volume);
        }
    }
    
    public void setPlaybackMode(PlaybackMode mode) {
        this.playbackMode = mode;
        if (player != null) {
            player.setRepeatMode(getExoRepeatMode());
        }
    }
    
    public PlaybackMode getPlaybackMode() {
        return playbackMode;
    }
    
    @Player.RepeatMode
    private int getExoRepeatMode() {
        switch (playbackMode) {
            case REPEAT_SINGLE:
                return Player.REPEAT_MODE_ONE;
            case REPEAT_ALL:
                return Player.REPEAT_MODE_ALL;
            case SHUFFLE:
                return Player.REPEAT_MODE_ALL;
            default:
                return Player.REPEAT_MODE_ALL;
        }
    }
    
    public void addToQueue(SongInfo song) {
        playQueue.add(song);
    }
    
    public void removeFromQueue(int position) {
        if (position >= 0 && position < playQueue.size()) {
            playQueue.remove(position);
            if (currentQueueIndex >= position) {
                currentQueueIndex--;
            }
        }
    }
    
    public void clearQueue() {
        playQueue.clear();
        currentQueueIndex = -1;
        currentSong = null;
    }
    
    public List<SongInfo> getQueue() {
        return new ArrayList<>(playQueue);
    }
    
    public int getCurrentQueueIndex() {
        return currentQueueIndex;
    }
    
    public SongInfo getCurrentSong() {
        return currentSong;
    }
    
    public void playNext() {
        if (playQueue.isEmpty()) {
            return;
        }
        
        if (playbackMode == PlaybackMode.SHUFFLE) {
            playRandom();
            return;
        }
        
        currentQueueIndex = (currentQueueIndex + 1) % playQueue.size();
        if (currentQueueIndex < 0) {
            currentQueueIndex = 0;
        }
        SongInfo nextSong = playQueue.get(currentQueueIndex);
        
        MusicSourceManager sourceManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager();
        sourceManager.getPlayUrl(nextSong, new MusicSourceManager.SourceCallback<cn.lxmusic.player.model.PlayUrl>() {
            @Override
            public void onSuccess(cn.lxmusic.player.model.PlayUrl playUrl) {
                play(playUrl.getUrl(), nextSong);
            }
            
            @Override
            public void onError(String error) {
                notifyError("获取播放链接失败：" + error);
                playNext();
            }
        });
    }
    
    public void playPrevious() {
        if (playQueue.isEmpty()) {
            return;
        }
        
        currentQueueIndex = (currentQueueIndex - 1 + playQueue.size()) % playQueue.size();
        if (currentQueueIndex < 0) {
            currentQueueIndex = 0;
        }
        SongInfo previousSong = playQueue.get(currentQueueIndex);
        
        MusicSourceManager sourceManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager();
        sourceManager.getPlayUrl(previousSong, new MusicSourceManager.SourceCallback<cn.lxmusic.player.model.PlayUrl>() {
            @Override
            public void onSuccess(cn.lxmusic.player.model.PlayUrl playUrl) {
                play(playUrl.getUrl(), previousSong);
            }
            
            @Override
            public void onError(String error) {
                notifyError("获取播放链接失败：" + error);
                playPrevious();
            }
        });
    }
    
    private void playRandom() {
        if (playQueue.isEmpty()) {
            return;
        }
        
        int randomIndex = (int)(Math.random() * playQueue.size());
        currentQueueIndex = randomIndex;
        SongInfo randomSong = playQueue.get(currentQueueIndex);
        
        MusicSourceManager sourceManager = cn.lxmusic.player.LXMusicApplication.getInstance().getSourceManager();
        sourceManager.getPlayUrl(randomSong, new MusicSourceManager.SourceCallback<cn.lxmusic.player.model.PlayUrl>() {
            @Override
            public void onSuccess(cn.lxmusic.player.model.PlayUrl playUrl) {
                play(playUrl.getUrl(), randomSong);
            }
            
            @Override
            public void onError(String error) {
                notifyError("获取播放链接失败：" + error);
                playRandom();
            }
        });
    }
    
    public void addPlayerListener(PlayerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removePlayerListener(PlayerListener listener) {
        listeners.remove(listener);
    }
    
    private void notifySongChange(SongInfo song) {
        for (PlayerListener listener : listeners) {
            listener.onSongChange(song);
        }
    }
    
    private void notifyProgressUpdate() {
        long position = getCurrentPosition();
        long duration = getDuration();
        
        for (PlayerListener listener : listeners) {
            listener.onProgressUpdate(position, duration);
        }
    }
    
    private void notifyError(String error) {
        for (PlayerListener listener : listeners) {
            listener.onError(error);
        }
    }
    
    private void startProgressUpdates() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    notifyProgressUpdate();
                }
                progressHandler.postDelayed(this, PROGRESS_UPDATE_INTERVAL);
            }
        };
        progressHandler.post(progressRunnable);
    }
    
    public void release() {
        if (progressHandler != null && progressRunnable != null) {
            progressHandler.removeCallbacks(progressRunnable);
        }
        
        if (player != null) {
            player.release();
            player = null;
        }
        
        listeners.clear();
        playQueue.clear();
    }
}
