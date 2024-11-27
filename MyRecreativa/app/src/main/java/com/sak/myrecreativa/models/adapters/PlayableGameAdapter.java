package com.sak.myrecreativa.models.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.myrecreativa.interfaces.IOnClickListenner;
import com.sak.myrecreativa.models.Game;

import java.util.List;

public class PlayableGameAdapter extends RecyclerView.Adapter<PlayableGameAdapter.GameViewHolder> {

    private List<Game> games;
    private IOnClickListenner listenner;

    public PlayableGameAdapter(List<Game> games){
        this.games = games;
    }
    @NonNull
    @Override
    public PlayableGameAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayableGameAdapter.GameViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setListenner(IOnClickListenner listenner) {
        this.listenner = listenner;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
