package cn.lxmusic.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import cn.lxmusic.player.R;
import cn.lxmusic.player.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<SongInfo> songs;
    private SongInfo currentSong;
    private OnSongActionListener listener;
    private Context context;
    
    public interface OnSongActionListener {
        void onPlay(SongInfo song);
        void onRemove(SongInfo song, int position);
    }
    
    public PlaylistAdapter(Context context) {
        this.context = context;
        this.songs = new ArrayList<>();
    }
    
    public void setSongs(List<SongInfo> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
    
    public void setCurrentSong(SongInfo song) {
        this.currentSong = song;
        notifyDataSetChanged();
    }
    
    public void setOnSongActionListener(OnSongActionListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_playlist_song, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongInfo song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        
        Glide.with(holder.itemView.getContext())
            .load(song.getCoverUrl())
            .placeholder(R.drawable.ic_music_note)
            .into(holder.coverImage);
        
        if (currentSong != null && currentSong.getId().equals(song.getId())) {
            holder.playingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.playingIndicator.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlay(song);
            }
        });
        
        holder.removeBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemove(song, position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return songs.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView title;
        TextView artist;
        ImageView playingIndicator;
        ImageButton removeBtn;
        
        ViewHolder(View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.coverImage);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            playingIndicator = itemView.findViewById(R.id.playingIndicator);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}
