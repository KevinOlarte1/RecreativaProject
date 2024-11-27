package com.sak.myrecreativa.models.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public PlayableGameAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayableGameAdapter.GameViewHolder holder, int position) {
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

        private ImageView imgBackGround;
        private TextView tvGameName;
        private TextView tvMaxScore;
        private Button btPlay;
        private ImageView imgFavorito;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackGround = itemView.findViewById(R.id.imgBackground);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvMaxScore = itemView.findViewById(R.id.btnPlay);
            btPlay = itemView.findViewById(R.id.btnPlay);
            imgFavorito = itemView.findViewById(R.id.ivFavorite);
        }

        public void bindGames(GameName game){
        try{
            Context context = itemView.getContext();
            Resources res =context.getResources();
            int resId = res.getIdentifier(game.getName(), "drawable", context.getPackageName());
            imgBackGround.setImageResource(resId);
        }catch (Exception e){
            imgBackGround.setImageResource(R.drawable.side_nav_bar);
        }

        if (game != null){
            tvGameName.setText(game.getName());
            tvMaxScore.setText(String.valueOf(game.getMaxScore()));
            btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Cambiar de fragment del content_main.xml",Toast.LENGTH_LONG).show();
                }
            });
        }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
