package com.sak.myrecreativa.ui.fragments.trivialGame;


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
import com.sak.myrecreativa.interfaces.OnGameModeSelectedListener;
import com.sak.myrecreativa.models.GameName;

public class TrivialModeFragment extends Fragment {
    private ConstraintLayout layout;
    private TextView title;
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button play;
    private String selectedMode;
    private OnGameModeSelectedListener listener;
    private GameName gameName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mode_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view.findViewById(R.id.modeLayout);
        title = view.findViewById(R.id.game_title);
        title.setText(gameName.getName());
        mode1 = view.findViewById(R.id.mode1);
        mode1.setText("Movies");
        mode1.setBackgroundColor(Color.BLUE);
        mode2 = view.findViewById(R.id.mode2);
        mode2.setText("Series");
        mode2.setBackgroundColor(Color.BLUE);
        mode3 = view.findViewById(R.id.mode3);
        mode3.setText("Anime");
        mode3.setBackgroundColor(Color.BLUE);
        play = view.findViewById(R.id.play);

        Context context = getContext();
        if(context != null){
            Resources res =context.getResources();
            int resId = res.getIdentifier(gameName.getName().toLowerCase() + "_background", "drawable", context.getPackageName());
            layout.setBackgroundResource(resId);
        }

        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "movies";
                selectMode(selectedMode);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "series";
                selectMode(selectedMode);
            }
        });
        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "anime";
                selectMode(selectedMode);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGameModeSelected(selectedMode, gameName);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args != null && args.containsKey("GAME")) {
            gameName = args.getParcelable("GAME");
        }
        listener = (OnGameModeSelectedListener) context;
    }
    private void selectMode(String mode){
        mode1.setBackgroundColor(Color.BLUE);
        mode2.setBackgroundColor(Color.BLUE);
        mode3.setBackgroundColor(Color.BLUE);
        if (mode.equalsIgnoreCase("movies")){
            mode1.setBackgroundColor(Color.GREEN);
        }
        if (mode.equalsIgnoreCase("series")){
            mode2.setBackgroundColor(Color.GREEN);
        }
        if (mode.equalsIgnoreCase("anime")){
            mode3.setBackgroundColor(Color.GREEN);
        }
    }
}

