package com.sak.myrecreativa.ui.fragments.memoryGame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.memoryGame.MemoryGame;


public class    MemoryGameFragment extends Fragment {

    private MemoryGame gameLogic;
    private GridLayout gridLayout;
    private Button[] buttons;
    private TextView timer;
    private int finalTime;
    private int numberOfPairs;
    private boolean isProcessing = false;
    private Thread timerThread;
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
        gridLayout = view.findViewById(R.id.gridLayout_memoryGame);
        timer = view.findViewById(R.id.timer_memory_game);
        finalTime = 0;
        int totalCards = numberOfPairs * 2;
        buttons = new Button[totalCards];

        int columnCount = (int) Math.sqrt(totalCards);
        gridLayout.setColumnCount(columnCount);

        for (int i = 0; i < totalCards; i++) {
            Button button = new Button(getContext());
            button.setId(i);
            button.setOnClickListener(this::onCardClicked);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            button.setLayoutParams(params);

            gridLayout.addView(button);
            buttons[i] = button;
        }
        timer();
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
                break;
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
        if (isProcessing) return;

        int position = view.getId();

        if (gameLogic.isMatched(position)) return;

        Button button = buttons[position];
        int cardValue = gameLogic.getCardAt(position); // Obtén el valor numérico de la carta
        int cardColor = getColorForNumber(cardValue);  // Obtén el color asociado al número
        button.setBackgroundColor(cardColor);         // Cambia el color del botón

        boolean isPair = gameLogic.selectCard(position);

        if (gameLogic.getFirstSelected() != -1 && gameLogic.getSecondSelected() == -1) {
            // Primera carta seleccionada, permitir continuar
            return;
        }

        if (isPair) {
            Toast.makeText(getContext(), "¡Par encontrado!", Toast.LENGTH_SHORT).show();
            gameLogic.resetSelection();
            if (gameLogic.isGameOver()) {
                endGame();
            }
        } else {
            isProcessing = true;
            handler.postDelayed(() -> {
                gameLogic.resetSelection();
                for (Button btn : buttons) {
                    if (!gameLogic.isMatched(btn.getId())) {
                        btn.setBackgroundResource(android.R.drawable.btn_default);
                    }
                }
                isProcessing = false; // Permitir interacción nuevamente
            }, 1000);
        }
    }

    private void timer(){
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 1000000; i++){
                    try {
                        Thread.sleep(1000);
                        int finalI = i;
                        timer.post(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(String.valueOf(finalI));
                                if (timer.getText().toString().equals("1000000"))
                                    endGame();
                            }
                        });

                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        timerThread.start();

    }

    private int getColorForNumber(int number) {
        switch (number) {
            case 1: return Color.RED;
            case 2: return Color.BLUE;
            case 3: return Color.GREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.CYAN;
            case 6: return Color.MAGENTA;
            case 7: return Color.WHITE;
            case 8: return Color.DKGRAY;
            case 9: return 0xFFFFA500; // Orange
            case 10: return 0xFF800080; // Purple
            case 11: return 0xFF008080; // Teal
            case 12: return 0xFFFFC0CB; // Pink
            default: return android.R.drawable.btn_default; // Default color
        }
    }



    private void endGame(){
        finalTime = Integer.valueOf(timer.getText().toString());
        timerThread.interrupt();
        gameEndListener.onGameEnd(gameLogic.calculateScore(mode, finalTime), gameName, gameLogic.isGameOver());
    }
}
