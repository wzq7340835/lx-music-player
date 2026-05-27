package cn.lxmusic.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import cn.lxmusic.player.R;
import cn.lxmusic.player.db.FavoriteManager;
import cn.lxmusic.player.model.SongInfo;
import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private List<SongInfo> songs;
    private OnSongActionListener listener;
    private FavoriteManager favoriteManager;
    private Context context;
    
    public interface OnSongActionListener {
        void onPlay(SongInfo song);
        void onAddToFavorite(SongInfo song);
        void onRemoveFromFavorite(SongInfo song);
    }
    
    public SongListAdapter(Context context) {
        this.context = context;
        this.songs = new ArrayList<>();
        this.favoriteManager = FavoriteManager.getInstance(context);
    }
    
    public void setSongs(List<SongInfo> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
    
    public void setOnSongActionListener(OnSongActionListener listener) {
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
        
        holder.itemView.setOnLongClickListener(v -> {
            showMoreMenu(holder.itemView, song);
            return true;
        });
    }
    
    private void showMoreMenu(View anchorView, SongInfo song) {
        PopupMenu popup = new PopupMenu(context, anchorView);
        popup.getMenu().add("收藏");
        popup.getMenu().add("取消收藏");
        popup.getMenu().add("下一首播放");
        
        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if ("收藏".equals(title)) {
                if (listener != null) {
                    listener.onAddToFavorite(song);
                }
                return true;
            } else if ("取消收藏".equals(title)) {
                if (listener != null) {
                    listener.onRemoveFromFavorite(song);
                }
                return true;
            }
            return false;
        });
        
        popup.show();
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
