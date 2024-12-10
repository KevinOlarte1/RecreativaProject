package com.sak.myrecreativa.models.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.Mission;

import java.util.List;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {
    private final List<Mission> missions;

    public MissionAdapter(List<Mission> missions) {
        this.missions = missions;
    }

    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission, parent, false);
        return new MissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionViewHolder holder, int position) {
        Mission mission = missions.get(position);
        holder.missionTitle.setText(mission.getTitle());

        if (mission.getCurrentPoints() >= mission.getTotalPoints()) {
            // Misión completada: muestra el tick
            holder.progressBar.setVisibility(View.GONE);
            holder.progressText.setVisibility(View.GONE);
            holder.completedTick.setVisibility(View.VISIBLE);
        } else {
            // Actualiza la barra de progreso normalmente
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressText.setVisibility(View.VISIBLE);
            holder.completedTick.setVisibility(View.GONE);

            holder.progressBar.setMax(mission.getTotalPoints());
            holder.progressBar.setProgress(mission.getCurrentPoints());
            holder.progressText.setText(mission.getCurrentPoints() + "/" + mission.getTotalPoints() + " pts");
        }
    }

    @Override
    public int getItemCount() {
        return missions.size();
    }

    static class MissionViewHolder extends RecyclerView.ViewHolder {
        private TextView missionTitle;
        private ProgressBar progressBar;
        private TextView progressText;
        private ImageView completedTick;

        public MissionViewHolder(View itemView) {
            super(itemView);
            missionTitle = itemView.findViewById(R.id.mission_title);
            progressBar = itemView.findViewById(R.id.progress_bar);
            progressText = itemView.findViewById(R.id.progress_text);
            completedTick = itemView.findViewById(R.id.completed_tick);
        }
    }
}
