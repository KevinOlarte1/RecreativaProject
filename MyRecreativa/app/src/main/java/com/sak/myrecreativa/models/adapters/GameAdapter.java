package com.sak.myrecreativa.models.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.GameName;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private final List<GameName> games;

    public GameAdapter(List<GameName> games) {
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_mission, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameName game = games.get(position);
        holder.gameName.setText(game.getName());

        MissionAdapter missionAdapter = new MissionAdapter(game.getMissions());
        holder.missionsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.missionsRecyclerView.setAdapter(missionAdapter);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        private TextView gameName;
        private RecyclerView missionsRecyclerView;

        GameViewHolder(View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.game_name);
            missionsRecyclerView = itemView.findViewById(R.id.missions_recycler_view);
        }
    }
}
