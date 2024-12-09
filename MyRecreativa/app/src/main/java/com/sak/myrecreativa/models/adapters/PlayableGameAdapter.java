package com.sak.myrecreativa.models.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnClickListenner;
import com.sak.myrecreativa.models.GameName;

import java.util.List;

public class PlayableGameAdapter extends RecyclerView.Adapter<PlayableGameAdapter.GameViewHolder> {

    private List<GameName> games;
    private IOnClickListenner listenner;

    public PlayableGameAdapter(List<GameName> games){
        this.games = games;
    }
    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameName game =games.get(position);
        holder.bindGames(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setListenner(IOnClickListenner listenner) {
        this.listenner = listenner;
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvGameName;
        private TextView tvMaxScore;
        private FrameLayout mainLayout;

        private ImageButton favButton;

        private Button playButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvMaxScore = itemView.findViewById(R.id.tvMaxScore);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            favButton = itemView.findViewById(R.id.favButton);
            playButton = itemView.findViewById(R.id.playButton);
            playButton.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        public void bindGames(GameName game){
        try{
            Context context = itemView.getContext();
            Resources res =context.getResources();
            int resId = res.getIdentifier(game.getName().toLowerCase(), "drawable", context.getPackageName());

            if (resId != 0) {
                mainLayout.setBackgroundResource(resId);
            } else {
                mainLayout.setBackgroundResource(R.drawable.side_nav_bar);
            }
        }catch (Exception e){
            mainLayout.setBackgroundResource(R.drawable.side_nav_bar);
        }

        if (game != null){
            tvGameName.setText(game.getName());
            tvMaxScore.setText(String.valueOf(game.getMaxScore()));
            //btPlay.setOnClickListener(listenner);

        }
        }

        @Override
        public void onClick(View v) {
            if(listenner != null){
                listenner.onClick(getAdapterPosition());
            }
        }
    }
}
