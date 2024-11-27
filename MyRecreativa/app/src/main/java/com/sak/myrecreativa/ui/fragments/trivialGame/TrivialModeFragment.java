package com.sak.myrecreativa.ui.fragments.trivialGame;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.OnGameModeSelectedListener;
import com.sak.myrecreativa.models.GameName;

public class TrivialModeFragment extends Fragment {
    private TextView title;
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button play;
    private String selectedMode;
    private OnGameModeSelectedListener listener;
    private GameName name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mode_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.game_title);
        mode1 = view.findViewById(R.id.mode1);
        mode2 = view.findViewById(R.id.mode2);
        mode3 = view.findViewById(R.id.mode3);
        play = view.findViewById(R.id.play);

        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "movies";
                Toast.makeText(getContext(), "Movies", Toast.LENGTH_LONG).show();
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "series";
            }
        });
        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = "anime";
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGameModeSelected(selectedMode, name);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        name = new GameName("trivial");
        listener = (OnGameModeSelectedListener) context;
    }
}

