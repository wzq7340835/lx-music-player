package cn.lxmusic.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.lxmusic.player.R;
import cn.lxmusic.player.model.LyricsInfo;

public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.ViewHolder> {
    private LyricsInfo lyricsInfo;
    private int currentLineIndex = -1;
    
    public void setLyrics(LyricsInfo lyricsInfo) {
        this.lyricsInfo = lyricsInfo;
        notifyDataSetChanged();
    }
    
    public void setCurrentLine(int lineIndex) {
        if (currentLineIndex != lineIndex) {
            currentLineIndex = lineIndex;
            notifyDataSetChanged();
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_lyrics_line, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LyricsInfo.LyricsLine line = lyricsInfo.getLines().get(position);
        holder.lyricsText.setText(line.getContent());
        
        if (position == currentLineIndex) {
            holder.lyricsText.setTextColor(holder.itemView.getContext().getColor(R.color.lyrics_current));
            holder.lyricsText.setTextSize(18);
        } else {
            holder.lyricsText.setTextColor(holder.itemView.getContext().getColor(R.color.lyrics_normal));
            holder.lyricsText.setTextSize(16);
        }
    }
    
    @Override
    public int getItemCount() {
        return lyricsInfo != null && lyricsInfo.getLines() != null ? lyricsInfo.getLines().size() : 0;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lyricsText;
        
        ViewHolder(View itemView) {
            super(itemView);
            lyricsText = itemView.findViewById(R.id.lyricsLineText);
        }
    }
}
