package com.sak.myrecreativa.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnClickListenner;

public class ScoreFragment extends Fragment {
    private String score;
    private String name;
    private ImageView img;
    private TextView tvGameName;
    private TextView tvGameStatus;
    private TextView tvScore;
    private Button playAgainButton;
    private Button exitButton;
    private ConstraintLayout layout;
    private boolean isWin;

    private IOnClickListenner listenner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.scoreLayout);
        tvGameName = view.findViewById(R.id.tvGameNameScore);
        tvGameStatus = view.findViewById(R.id.tvGameStatus);
        tvScore = view.findViewById(R.id.tvLastGameScore);
        playAgainButton = view.findViewById(R.id.playAgainButton);
        exitButton = view.findViewById(R.id.exitButton);

        tvGameName.setText(name);
        if (isWin){
            tvGameStatus.setText("YOU WIN");
        }else{
            tvGameStatus.setText("GAME OVER");
        }
        tvScore.setText("Score: " + score);
        playAgainButton.setText("Play again");
        exitButton.setText("Exit");

        Context context = view.getContext();
        Resources res =context.getResources();
        int resId = res.getIdentifier(name + "_background", "drawable", context.getPackageName());
        layout.setBackgroundResource(resId);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onClick(-1);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onClick(-2);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenner = (IOnClickListenner) context;

        Bundle args = getArguments();
        if(args != null){
            if (args.containsKey("SCORE")) {
                score = args.getString("SCORE");
            }
            if (args.containsKey("GAME_NAME")){
                name = args.getString("GAME_NAME");
            }
            if (args.containsKey("IS_WIN")){
                isWin = args.getBoolean("IS_WIN");
            }
        }
    }
}
