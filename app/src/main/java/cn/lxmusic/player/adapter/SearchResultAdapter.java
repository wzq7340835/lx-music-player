package cn.lxmusic.player.adapter;

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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<SongInfo> songs;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onPlay(SongInfo song);
    }
    
    public SearchResultAdapter() {
        this.songs = new ArrayList<>();
    }
    
    public void setSongs(List<SongInfo> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_search_result, parent, false);
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
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlay(song);
            }
        });
        
        holder.playBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlay(song);
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
        ImageButton playBtn;
        
        ViewHolder(View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.coverImage);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            playBtn = itemView.findViewById(R.id.playBtn);
        }
    }
}
