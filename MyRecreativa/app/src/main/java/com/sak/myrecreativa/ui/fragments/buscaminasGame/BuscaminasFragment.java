package com.sak.myrecreativa.ui.fragments.buscaminasGame;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.games.buscaminas.Game;

public class BuscaminasFragment extends Fragment {
    private Game game;
    private TableLayout tableLayout;
    private ImageButton selectionButton;
    private ImageButton selectBombButton;
    private boolean isMarkMode = false; // Modo inicial: seleccionar casilla

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buscaminas_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectionButton = view.findViewById(R.id.selectButton);
        selectionButton.setImageResource(R.drawable.flag);
        selectBombButton = view.findViewById(R.id.selectBombButton);
        selectBombButton.setImageResource(R.drawable.flag);
        scaleImage(selectBombButton);
        scaleImage(selectionButton);


        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMarkMode = false;
            }
        });
        selectBombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMarkMode = true;
            }
        });

        game = new Game(10, 10);
        tableLayout = view.findViewById(R.id.table);
        createGameBoard(tableLayout, 10);
    }

    private void createGameBoard(TableLayout tableLayout, int boardSize) {
        tableLayout.post(() -> {
            int layoutWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375, getResources().getDisplayMetrics());
            int layoutHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

            int buttonWidth = layoutWidthPx / boardSize;
            int buttonHeight = layoutHeightPx / boardSize;

            int buttonSize = Math.min(buttonWidth, buttonHeight) -4;

            for (int i = 0; i < boardSize; i++) {
                TableRow row = new TableRow(getContext());
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                for (int j = 0; j < boardSize; j++) {
                    ImageButton button = new ImageButton(getContext());
                    TableRow.LayoutParams params = new TableRow.LayoutParams(buttonSize, buttonSize);
                    button.setLayoutParams(params);
                    button.setId(i * boardSize + j);
                    params.setMargins(2, 2, 2, 2);
                    button.setBackgroundColor(Color.GREEN);


                    int finalI = i, finalJ = j;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleCellClick(finalI, finalJ, button);
                        }
                    });

                    button.setScaleType(ImageButton.ScaleType.FIT_START);
                    button.setAdjustViewBounds(true);

                    row.addView(button);
                }
                tableLayout.addView(row);
            }
        });
    }

    private void handleCellClick(int i, int j, ImageButton button) {
        if (isMarkMode) {
            // Marcar/desmarcar casilla
            game.toggleMark(i, j);
            if (game.isMarked(i, j)) {
                button.setImageResource(R.drawable.flag);
                scaleImage(button);
            } else {
                button.setImageResource(0);
            }
        } else {
            // Revelar casilla
            if (game.isBomb(i, j)) {
                button.setImageResource(R.drawable.bomb);
                scaleImage(button);
                Toast.makeText(getContext(), "¡Game Over! Tocaste una bomba.", Toast.LENGTH_SHORT).show();
                revealAllBombs();
            } else {
                revealArea(i, j);
                if (game.isWin()) {
                    Toast.makeText(getContext(), "¡Felicidades! Ganaste el juego.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void revealArea(int x, int y) {
        if (!game.isValidCell(x, y) || game.isRevealed(x, y) || game.isMarked(x, y)) {
            return;
        }

        game.revealCell(x, y);

        TableRow row = (TableRow) tableLayout.getChildAt(x);
        ImageButton button = (ImageButton) row.getChildAt(y);


        int bombs = game.getAdjacentBombs(x, y);
        if (bombs > 0) {
            button.setImageResource(getNumberDrawable(bombs));
            scaleImage(button);
        } else {
            button.setBackgroundColor(Color.LTGRAY);

            // Llama recursivamente para revelar las celdas vecinas
            for (int[] neighbor : game.getNeighbors(x, y)) {
                revealArea(neighbor[0], neighbor[1]);
            }
        }
    }

    private void revealAllBombs() {
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isBomb(i, j)) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    ImageButton button = (ImageButton) row.getChildAt(j);
                    button.setImageResource(R.drawable.bomb);
                    scaleImage(button);
                }
            }
        }
    }

    private void scaleImage(ImageButton button){
        button.setScaleType(ImageView.ScaleType.FIT_XY);
        button.setBackground(null);
        button.setPadding(0, 0, 0, 0);
    }

    private int getNumberDrawable(int number) {
        switch (number) {
            case 1: return R.drawable.ic_number_1;
            case 2: return R.drawable.ic_number_2;
            case 3: return R.drawable.ic_number_3;
            case 4: return R.drawable.ic_number_4;
            case 5: return R.drawable.ic_number_5;
            case 6: return R.drawable.ic_number_6;
            case 7: return R.drawable.ic_number_7;
            case 8: return R.drawable.ic_number_8;
            default: return 0;
        }
    }
}
