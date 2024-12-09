package com.sak.myrecreativa.ui.fragments.buscaminasGame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.minesweeper.MinesweeperGame;

public class MinesweeperFragment extends Fragment {
    private MinesweeperGame minesweeperGame;
    private TableLayout tableLayout;
    private ImageButton selectionButton;
    private ImageButton selectBombButton;
    private boolean isMarkMode = false; // Modo inicial: seleccionar casilla
    private String mode;
    private GameName gameName;
    private int boardSize;
    private int numberOfBombs;
    private IOnGameEndListener gameEndListener;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.minesweeper_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectionButton = view.findViewById(R.id.selectButton);
        selectionButton.setImageResource(R.drawable.shovel);
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

        minesweeperGame = new MinesweeperGame(boardSize, numberOfBombs);
        tableLayout = view.findViewById(R.id.table);
        createGameBoard(tableLayout, boardSize);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();

        if (args != null && args.containsKey("MODE")) {
            mode = args.getString("MODE");
        }
        if (args != null && args.containsKey("GAME")){
            gameName = args.getParcelable("GAME");
        }
        if (mode != null){
            switch (mode.toLowerCase()){
                case "easy":
                    boardSize = 6;
                    numberOfBombs = 6;
                    break;
                case "medium":
                    boardSize = 8;
                    numberOfBombs = 8;
                    break;
                case "hard":
                    boardSize = 10;
                    numberOfBombs = 10;
                    break;
            }
        }
        gameEndListener = (IOnGameEndListener)  context;
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
        if (minesweeperGame.isRevealed(i, j)) {
            return; // No permitir interacción adicional
        }

        if (isMarkMode) {
            // Marcar/desmarcar casilla con bandera
            minesweeperGame.toggleMark(i, j);
            if (minesweeperGame.isMarked(i, j)) {
                button.setImageResource(R.drawable.flag);
                scaleImage(button);
            } else {
                button.setImageResource(0);
                button.setBackgroundColor(Color.GREEN);
            }
        } else {
            // Revelar casilla
            if (minesweeperGame.isBomb(i, j)) {
                button.setImageResource(R.drawable.bomb);
                scaleImage(button);
                revealAllBombs();

                handler.postDelayed(() -> {
                    endGame();
                }, 4000);
            } else {
                revealArea(i, j);
                if (minesweeperGame.isWin()) {
                    handler.postDelayed(() -> {
                        endGame();
                    }, 4000);
                }
            }
        }
    }

    private void revealArea(int x, int y) {
        if (!minesweeperGame.isValidCell(x, y) || minesweeperGame.isRevealed(x, y) || minesweeperGame.isMarked(x, y)) {
            return;
        }

        minesweeperGame.revealCell(x, y);

        TableRow row = (TableRow) tableLayout.getChildAt(x);
        ImageButton button = (ImageButton) row.getChildAt(y);


        int bombs = minesweeperGame.getAdjacentBombs(x, y);
        if (bombs > 0) {
            button.setImageResource(getNumberDrawable(bombs));
            scaleImage(button);
        } else {
            button.setBackgroundColor(Color.LTGRAY);

            // Llama recursivamente para revelar las celdas vecinas
            for (int[] neighbor : minesweeperGame.getNeighbors(x, y)) {
                revealArea(neighbor[0], neighbor[1]);
            }
        }
    }

    private void revealAllBombs() {
        for (int i = 0; i < minesweeperGame.getBoardSize(); i++) {
            for (int j = 0; j < minesweeperGame.getBoardSize(); j++) {
                if (minesweeperGame.isBomb(i, j)) {
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
    private void endGame() {
        gameEndListener.onGameEnd(0, gameName, minesweeperGame.isWin());
    }
}
