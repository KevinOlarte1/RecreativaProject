package com.sak.myrecreativa.ui.fragments.conecta4;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sak.myrecreativa.R;

import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.interfaces.conecta4.GameControllerConectaCuatro;
import com.sak.myrecreativa.models.GameName;

public class ConectaCuatroFragment extends Fragment {

    private GameControllerConectaCuatro controller;
    private Button[][] buttons;
    private TextView gameStatus;
    private Button finishButton;

    private  Button resetButton;
    private final int rows = 6;
    private final int columns = 7;

    private IOnGameEndListener gameEndListener;
    private GameName gameName;
    private String mode;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conecta_cuatro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gameStatus = view.findViewById(R.id.gameStatus);
        finishButton = view.findViewById(R.id.finishButton);
        resetButton = view.findViewById(R.id.resetButton);
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);

        buttons = new Button[rows][columns];
        controller = new GameControllerConectaCuatro(this);

        // Crear botones dinámicamente
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Button button = new Button(requireContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 120;
                params.height = 120;
                params.setMargins(4, 4, 4, 4);
                button.setLayoutParams(params);
                button.setBackgroundColor(Color.DKGRAY);
                int finalJ = j;
                button.setOnClickListener(v -> controller.makeMove(finalJ));
                gridLayout.addView(button);
                buttons[i][j] = button;
            }
        }

        finishButton.setOnClickListener(v -> controller.endGame());
        resetButton.setOnClickListener(v -> controller.restartGame());
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
        gameEndListener = (IOnGameEndListener) context;

    }

    /**
     * Pintar el tablero
     * @param row file afectada
     * @param col columna afectada
     * @param player 1 rojo 2 azul
     */
    public void updateBoard(int row, int col, int player) {
        if (player == 1) {
            buttons[row][col].setBackgroundColor(Color.RED);
        } else if (player == 2) {
            buttons[row][col].setBackgroundColor(Color.BLUE);
        }
    }

    /**
     * Metod para formatear los botones al color por default
     */
    public void resetMap() {
        for (Button[] buttonRow : buttons) {
            for (Button button : buttonRow) {
                button.setBackgroundColor(Color.DKGRAY);
                button.setEnabled(true);
            }
        }
    }

    /**
     * Actualizar un TextLabel con el ganador de la partida
     * @param player ganador de la partida
     */
    public void showWinner(int player) {
        gameStatus.setText("Jugador " + player + " gana!");
        finishButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
    }

    /**
     * Actualizar un TextLabel con el turno acitual
     * @param player
     */
    public void showTurn(int player) {
        gameStatus.setText("Turno del Jugador " + player);
    }

    /**
     * Metodo para resetear el UI al defecto inicial
     */
    public void resetUI() {
        finishButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        gameStatus.setText("Turno del Jugador 1");
    }

    /**
     * Metodo para acabar la partida
     * @param playerWin jugador que ha ganado 1 o 2
     * @param score
     */
    public void endGame(int playerWin, int score){
        Log.i("Puntaje: ", String.valueOf(score)); //TODO: Bien hasta aqui
        boolean win = playerWin == 1? true : false;
        gameEndListener.onGameEnd(score, gameName, win);
    }
}
