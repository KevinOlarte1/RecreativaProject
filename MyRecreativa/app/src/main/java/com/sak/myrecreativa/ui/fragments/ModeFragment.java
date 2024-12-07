package com.sak.myrecreativa.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameModeSelectedListener;
import com.sak.myrecreativa.models.GameName;

public class ModeFragment extends Fragment {
    private ConstraintLayout layout;
    private TextView title;
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button play;
    private String selectedMode;
    private IOnGameModeSelectedListener listener;
    private GameName gameName;
    private String[] modes;

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
        mode1.setText(modes[0]);
        mode1.setBackgroundColor(Color.BLUE);
        mode2 = view.findViewById(R.id.mode2);
        mode2.setText(modes[1]);
        mode2.setBackgroundColor(Color.BLUE);
        mode3 = view.findViewById(R.id.mode3);
        mode3.setText(modes[2]);
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
                selectedMode = modes[0];
                selectMode(selectedMode);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = modes[1];
                selectMode(selectedMode);
            }
        });
        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMode = modes[2];
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
        if (args != null && args.containsKey("MODES")) {
            modes = args.getStringArray("MODES");
        }
        listener = (IOnGameModeSelectedListener) context;
    }
    private void selectMode(String mode){
        mode1.setBackgroundColor(Color.BLUE);
        mode2.setBackgroundColor(Color.BLUE);
        mode3.setBackgroundColor(Color.BLUE);
        if (mode.equalsIgnoreCase(modes[0])){
            mode1.setBackgroundColor(Color.GREEN);
        }
        if (mode.equalsIgnoreCase(modes[1])){
            mode2.setBackgroundColor(Color.GREEN);
        }
        if (mode.equalsIgnoreCase(modes[2])){
            mode3.setBackgroundColor(Color.GREEN);
        }
    }
}
