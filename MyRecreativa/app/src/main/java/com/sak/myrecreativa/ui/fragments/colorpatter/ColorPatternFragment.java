package com.sak.myrecreativa.ui.fragments.colorpatter;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.games.colorpatter.ColorPatternGame;

import java.util.List;

public class ColorPatternFragment extends Fragment {
    private ColorPatternGame game;
    private View[] buttons;
    private int currentPatternIndex;
    private final Handler handler = new Handler();
    private int displaySpeed; // Velocidad de reproducción de colores en ms

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.colorpatter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configura la dificultad y velocidad
        int difficulty = getArguments() != null ? getArguments().getInt("difficulty", 1) : 1;
        game = new ColorPatternGame(difficulty);

        switch (difficulty) {
            case 1: // Fácil
                displaySpeed = 1000;
                break;
            case 2: // Medio
                displaySpeed = 700;
                break;
            case 3: // Difícil
                displaySpeed = 500;
                break;
        }

        buttons = new View[] {
                view.findViewById(R.id.button_red),
                view.findViewById(R.id.button_green),
                view.findViewById(R.id.button_blue),
                view.findViewById(R.id.button_yellow)
        };

        for (int i = 0; i < buttons.length; i++) {
            int colorIndex = i;
            buttons[i].setOnClickListener(v -> handleButtonClick(colorIndex));
        }

        startGame();
    }

    private void startGame() {
        showPattern();
    }

    private void showPattern() {
        List<Integer> pattern = game.getPattern();
        currentPatternIndex = 0;

        for (int i = 0; i < pattern.size(); i++) {
            int colorIndex = pattern.get(i);
            handler.postDelayed(() -> highlightButton(colorIndex), i * displaySpeed);
        }
    }

    private void highlightButton(int colorIndex) {
        buttons[colorIndex].setAlpha(0.5f); // Reduce opacidad para destacar
        handler.postDelayed(() -> buttons[colorIndex].setAlpha(1.0f), displaySpeed / 2); // Restablece opacidad
    }

    private void handleButtonClick(int colorIndex) {
        if (colorIndex == game.getPattern().get(currentPatternIndex)) {
            currentPatternIndex++;

            if (currentPatternIndex == game.getPattern().size()) {
                Toast.makeText(getContext(), "¡Correcto! Nuevo patrón", Toast.LENGTH_SHORT).show();
                game.addColorToPattern();
                handler.postDelayed(this::showPattern, 1000L);
            }
        } else {
            Toast.makeText(getContext(), "¡Error! Reiniciando juego.", Toast.LENGTH_SHORT).show();
            game.resetGame();
            handler.postDelayed(this::showPattern, 1000L);
        }
    }
}
