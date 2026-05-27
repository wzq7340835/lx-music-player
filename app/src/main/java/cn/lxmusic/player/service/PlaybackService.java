package cn.lxmusic.player.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.Player;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import cn.lxmusic.player.R;
import cn.lxmusic.player.activity.PlayerActivity;
import cn.lxmusic.player.player.PlayerManager;

public class PlaybackService extends MediaSessionService {
    private static final String CHANNEL_ID = "lx_music_playback_channel";
    private static final int NOTIFICATION_ID = 1001;
    
    private MediaSession mediaSession;
    private PlayerManager playerManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        createNotificationChannel();
        
        playerManager = new PlayerManager(this);
        playerManager.addPlayerListener(new PlayerManager.PlayerListener() {
            @Override
            public void onSongChange(cn.lxmusic.player.model.SongInfo song) {
                updateNotification(song);
            }
            
            @Override
            public void onPlayStateChanged(int state) {
                updatePlaybackState(state);
            }
            
            @Override
            public void onProgressUpdate(long position, long duration) {
            }
            
            @Override
            public void onError(String error) {
            }
        });
        
        MediaSessionCompat.Builder sessionBuilder = new MediaSessionCompat.Builder(this, "LXMusicSession");
        mediaSession = new MediaSession.Builder(this, new androidx.media3.exoplayer.ExoPlayer.Builder(this).build()).build();
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "播放控制",
            NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription("音乐播放器后台播放控制");
        channel.setShowBadge(false);
        
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }
    
    private void updateNotification(cn.lxmusic.player.model.SongInfo song) {
        if (song == null) {
            return;
        }
        
        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        Bitmap albumArt = null;
        
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(song.getTitle())
            .setContentText(song.getArtist())
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(albumArt)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(createPlayPauseAction())
            .addAction(createNextAction())
            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.getSessionCompat().getToken()))
            .build();
        
        startForeground(NOTIFICATION_ID, notification);
    }
    
    private NotificationCompat.Action createPlayPauseAction() {
        Intent intent = new Intent(this, PlaybackService.class);
        intent.setAction("TOGGLE_PLAY_PAUSE");
        
        PendingIntent pendingIntent = PendingIntent.getForegroundService(
            this, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        int icon = playerManager.isPlaying() ? 
            android.R.drawable.ic_media_pause : 
            android.R.drawable.ic_media_play;
            
        return new NotificationCompat.Action.Builder(icon, "播放/暂停", pendingIntent).build();
    }
    
    private NotificationCompat.Action createNextAction() {
        Intent intent = new Intent(this, PlaybackService.class);
        intent.setAction("PLAY_NEXT");
        
        PendingIntent pendingIntent = PendingIntent.getForegroundService(
            this, 2, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        return new NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_next, "下一首", pendingIntent
        ).build();
    }
    
    private void updatePlaybackState(int state) {
        if (mediaSession == null) {
            return;
        }
        
        int playbackState;
        switch (state) {
            case Player.STATE_READY:
                playbackState = PlaybackStateCompat.STATE_PLAYING;
                break;
            case Player.STATE_BUFFERING:
                playbackState = PlaybackStateCompat.STATE_BUFFERING;
                break;
            case Player.STATE_ENDED:
                playbackState = PlaybackStateCompat.STATE_STOPPED;
                break;
            case Player.STATE_IDLE:
                playbackState = PlaybackStateCompat.STATE_NONE;
                break;
            default:
                playbackState = PlaybackStateCompat.STATE_NONE;
        }
        
        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder()
            .setState(playbackState, playerManager.getCurrentPosition(), 1.0f);
            
        mediaSession.setPlaybackState(builder.build());
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (playerManager != null) {
            playerManager.release();
        }
        
        if (mediaSession != null) {
            mediaSession.release();
            mediaSession = null;
        }
    }
    
    @Override
    public MediaSession onGetSession(android.content.Intent intent) {
        return mediaSession;
    }
}
