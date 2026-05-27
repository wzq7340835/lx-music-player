package cn.lxmusic.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import cn.lxmusic.player.R;
import cn.lxmusic.player.model.MusicSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MusicSourceAdapter extends RecyclerView.Adapter<MusicSourceAdapter.ViewHolder> {
    private List<MusicSource> sources;
    private OnSourceActionListener listener;
    private SimpleDateFormat dateFormat;
    
    public interface OnSourceActionListener {
        void onToggleEnable(MusicSource source, boolean enable);
        void onDelete(MusicSource source);
    }
    
    public MusicSourceAdapter() {
        this.sources = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    
    public void setSources(List<MusicSource> sources) {
        this.sources = sources;
        notifyDataSetChanged();
    }
    
    public void setOnSourceActionListener(OnSourceActionListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_music_source, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicSource source = sources.get(position);
        
        holder.name.setText(source.getName());
        holder.version.setText("v" + source.getVersion());
        holder.platforms.setText(source.getPlatformText());
        holder.enableSwitch.setChecked(source.isEnabled());
        holder.updateTime.setText("更新于：" + dateFormat.format(new Date(source.getUpdateTime())));
        
        holder.enableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleEnable(source, isChecked);
            }
        });
        
        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(source);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return sources.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView version;
        TextView platforms;
        SwitchMaterial enableSwitch;
        TextView updateTime;
        ImageButton deleteBtn;
        
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sourceName);
            version = itemView.findViewById(R.id.sourceVersion);
            platforms = itemView.findViewById(R.id.sourcePlatforms);
            enableSwitch = itemView.findViewById(R.id.sourceEnableSwitch);
            updateTime = itemView.findViewById(R.id.updateTime);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
