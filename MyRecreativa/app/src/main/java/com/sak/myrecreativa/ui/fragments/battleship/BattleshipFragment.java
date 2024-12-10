package com.sak.myrecreativa.ui.fragments.battleship;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.games.battleship.BattleshipGame;

import java.util.Random;

public class BattleshipFragment extends Fragment {

    private BattleshipGame battleshipGame;
    private TableLayout playerBoardLayout;
    private TableLayout cpuBoardLayout;
    private TextView turnIndicatorTextView;
    private Button fireButton;
    private boolean isPlayerTurn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int boardSize;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        boardSize = 10; // Tamaño predeterminado del tablero
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.battleship_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerBoardLayout = view.findViewById(R.id.playerBoard);
        cpuBoardLayout = view.findViewById(R.id.cpuBoard);
        turnIndicatorTextView = view.findViewById(R.id.tvTurnIndicator);
        fireButton = view.findViewById(R.id.fireButton);

        battleshipGame = new BattleshipGame(boardSize, true);

        createGameBoard(playerBoardLayout, true);
        createGameBoard(cpuBoardLayout, false);

        updateTurnIndicator();

        fireButton.setOnClickListener(v -> handleFireAction());
    }

    private void createGameBoard(TableLayout tableLayout, boolean isPlayerBoard) {
        tableLayout.removeAllViews();

        for (int i = 0; i < boardSize; i++) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            for (int j = 0; j < boardSize; j++) {
                View cell = new View(getContext());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        55,
                        1f
                );
                params.setMargins(2, 2, 2, 2);
                cell.setLayoutParams(params);
                cell.setBackgroundColor(getResources().getColor(R.color.cell_default));

                int finalI = i, finalJ = j;
                cell.setOnClickListener(v -> {
                    if (isPlayerBoard) {
                        if (!isPlayerTurn) {
                            Toast.makeText(getContext(), "No puedes seleccionar este tablero", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (battleshipGame.isCellRevealed(battleshipGame.getOpponentBoard(), finalI, finalJ)) {
                            Toast.makeText(getContext(), "Ya atacaste esta celda", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedRow = finalI;
                        selectedCol = finalJ;
                        Toast.makeText(getContext(), "Seleccionado: (" + (finalI + 1) + ", " + (finalJ + 1) + ")", Toast.LENGTH_SHORT).show();
                    }
                });

                row.addView(cell);
            }
            tableLayout.addView(row);
        }
    }

    private void handleFireAction() {
        if (selectedRow == -1 || selectedCol == -1) {
            Toast.makeText(getContext(), "Selecciona una celda primero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isPlayerTurn) {
            boolean hit = battleshipGame.attackOpponent(selectedRow, selectedCol);
            updateCell(cpuBoardLayout, selectedRow, selectedCol, hit);

            if (battleshipGame.isGameOver()) {
                showWinner();
                return;
            }

            isPlayerTurn = false;
            updateTurnIndicator();

            // Inicia el ataque de la CPU
            handleCpuTurn();
        }
    }

    private void handleCpuTurn() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(boardSize);
            col = random.nextInt(boardSize);
        } while (battleshipGame.isCellRevealed(battleshipGame.getPlayerBoard(), row, col));

        boolean hit = battleshipGame.attackPlayer(row, col);
        updateCell(playerBoardLayout, row, col, hit);

        if (battleshipGame.isGameOver()) {
            showWinner();
            return;
        }

        isPlayerTurn = true;
        updateTurnIndicator();
    }

    private void updateCell(TableLayout tableLayout, int row, int col, boolean hit) {
        TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
        if (tableRow != null) {
            View cell = tableRow.getChildAt(col);
            if (cell != null) {
                if (hit) {
                    cell.setBackgroundColor(getResources().getColor(R.color.cell_hit));
                } else {
                    cell.setBackgroundColor(getResources().getColor(R.color.cell_miss));
                }
                cell.setEnabled(false);
            }
        }
    }

    private void updateTurnIndicator() {
        if (isPlayerTurn) {
            turnIndicatorTextView.setText("Turno del Jugador");
        } else {
            turnIndicatorTextView.setText("Turno del Oponente");
        }
    }

    private void showWinner() {
        String winner = battleshipGame.getWinner();
        Toast.makeText(getContext(), winner + " ha ganado!", Toast.LENGTH_LONG).show();
        fireButton.setEnabled(false);
    }
}
