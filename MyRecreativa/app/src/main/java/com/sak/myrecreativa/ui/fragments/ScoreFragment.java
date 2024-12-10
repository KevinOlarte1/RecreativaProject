package com.sak.myrecreativa.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.sak.myrecreativa.models.GameName;

public class ScoreFragment extends Fragment {
    private GameName name;

    private ImageView banner;
    private ImageView img;
    private TextView tvGameName;
    private ImageView isWinImg;
    private TextView tvScore;
    private Button playAgainButton;
    private Button exitButton;

    private String score;
    private boolean isWin;

    private IOnClickListenner listenner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        banner = view.findViewById(R.id.score_banner);
        tvGameName = view.findViewById(R.id.tvGameNameScore);
        tvScore = view.findViewById(R.id.tvLastGameScore);
        playAgainButton = view.findViewById(R.id.playAgainButton);
        exitButton = view.findViewById(R.id.exitButton);
        isWinImg = view.findViewById(R.id.isWin_img);

        tvGameName.setText(name.getName());
        if (isWin){
            isWinImg.setImageResource(R.drawable.win);
        }else{
            isWinImg.setImageResource(R.drawable.lose);
        }
        tvScore.setText("Score: " + score);
        tvScore.setTextColor(Color.BLUE);
        playAgainButton.setText("Play again");
        exitButton.setText("Exit");

        Context context = view.getContext();
        Resources res =context.getResources();
        int resId = res.getIdentifier(name.getName().toLowerCase(), "drawable", context.getPackageName());
        banner.setImageResource(resId);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onClick(-1, name);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onClick(-2, name);
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
                score = String.valueOf(args.getInt("SCORE"));
            }
            if (args.containsKey("GAME")) {
                name = args.getParcelable("GAME");
            }
            if (args.containsKey("IS_WIN")) {
                isWin = args.getBoolean("IS_WIN");
            }
        }
    }
}
