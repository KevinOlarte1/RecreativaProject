package com.sak.myrecreativa.ui.fragments.memoryGame;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.memoryGame.MemoryGame;


public class MemoryGameFragment extends Fragment {

    private MemoryGame gameLogic;
    private Button[] buttons;
    private int numberOfPairs;
    private String mode;
    private Handler handler = new Handler();
    private IOnGameEndListener gameEndListener;
    private GameName gameName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.memory_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayout gridLayout = view.findViewById(R.id.gridLayout_memoryGame);
        int totalCards = numberOfPairs * 2;
        buttons = new Button[totalCards];
        gridLayout.setColumnCount((int) Math.sqrt(totalCards));

        for (int i = 0; i < totalCards; i++) {
            Button button = new Button(getContext());
            button.setId(i);
            button.setOnClickListener(this::onCardClicked);
            buttons[i] = button;
            gridLayout.addView(button);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Bundle args = getArguments();
        if (args != null && args.containsKey("MODE")) {
            mode = args.getString("MODE");
        }
        if (args != null && args.containsKey("GAME")) {
            gameName = args.getParcelable("GAME");
        }

        switch (mode.toLowerCase()) {
            case "easy":
                numberOfPairs = 4;
            case "medium":
                numberOfPairs = 8;
                break;
            case "hard":
                numberOfPairs = 12;
                break;
        }

        gameLogic = new MemoryGame(numberOfPairs);

        gameEndListener = (IOnGameEndListener)  context;

    }

    private void onCardClicked(View view) {
        int position = view.getId();

        if (gameLogic.isMatched(position)) return;

        Button button = buttons[position];
        int cardValue = gameLogic.getCardAt(position);
        button.setText(String.valueOf(cardValue));

        if (gameLogic.selectCard(position)) {
            // Si las cartas coinciden
            Toast.makeText(getContext(), "¡Par encontrado!", Toast.LENGTH_SHORT).show();
            if (gameLogic.isGameOver()) {
                endGame();
            }
        } else {
            // Si no coinciden, espera un momento y oculta las cartas
            handler.postDelayed(() -> {
                gameLogic.resetSelection();
                for (Button btn : buttons) {
                    if (!gameLogic.isMatched(btn.getId())) {
                        btn.setText("");
                    }
                }
            }, 1000);
        }
    }
    private void endGame(){
        gameEndListener.onGameEnd(0, gameName, gameLogic.isGameOver());
    }
}
