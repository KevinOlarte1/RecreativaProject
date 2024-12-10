package com.sak.myrecreativa.ui.fragments.buscaminasGame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.minesweeper.MinesweeperGame;

public class MinesweeperFragment extends Fragment {
    private MinesweeperGame gameLogic;

    private TableLayout tableLayout;
    private ImageButton selectionButton;
    private ImageButton selectBombButton;
    private TextView bombCountTextView;
    private ImageView bombCountImg;
    private TextView timerText;
    private ImageView timerImg;

    private String mode;
    private GameName gameName;

    private Thread timerThread;
    private boolean isMarkMode = false; // Modo inicial: seleccionar casilla
    private int boardSize;
    private int numberOfBombs;

    private IOnGameEndListener gameEndListener;

    private final Handler handler = new Handler();

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

        bombCountTextView = view.findViewById(R.id.tv_bomb_count);
        bombCountImg = view.findViewById(R.id.bomb_count_img);
        bombCountImg.setImageResource(R.drawable.bomb);

        timerText = view.findViewById(R.id.tv_timer);
        timerImg = view.findViewById(R.id.timer_img);
        timerImg.setImageResource(R.drawable.clock);

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

        tableLayout = view.findViewById(R.id.table);
        createGameBoard();
        bombCountTextView.setText(String.valueOf(numberOfBombs));
        timer();
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

        gameLogic = new MinesweeperGame(boardSize, numberOfBombs);
        gameEndListener = (IOnGameEndListener)  context;
    }

    /**
     * Metodo para crear el tablero
     */
    private void createGameBoard() {
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

    /**
     * Metodo para gestionar interfaz a la hora de seleccionar una celda en alguna de los dos modos.
     * @param i posicion del boton.
     * @param j poscion del boton.
     * @param button boton seleccionado.
     */
    private void handleCellClick(int i, int j, ImageButton button) {
        if (gameLogic.isRevealed(i, j)) {
            return; // No permitir interacción adicional
        }

        if (isMarkMode) {
            // Marcar/desmarcar casilla con bandera
            gameLogic.toggleMark(i, j);
            if (gameLogic.isMarked(i, j)) {
                button.setImageResource(R.drawable.flag);
                scaleImage(button);
            } else {
                button.setImageResource(0);
                button.setBackgroundColor(Color.GREEN);
            }

            // Actualizar contador de bombas
            updateBombCount();
        } else {
            // Revelar casilla
            if (gameLogic.isBomb(i, j)) {
                button.setImageResource(R.drawable.bomb);
                scaleImage(button);
                revealAllBombs();

                handler.postDelayed(() -> {
                    endGame();
                }, 4000);
            } else {
                revealArea(i, j);
                if (gameLogic.isWin()) {
                    handler.postDelayed(() -> {
                        endGame();
                    }, 4000);
                }
            }
        }
    }

    /**
     * Metodo para actualizar el contador
     */
    private void updateBombCount() {
        int remainingBombs = gameLogic.getBombCount() - gameLogic.getMarkedCount();
        bombCountTextView.setText(String.valueOf(remainingBombs));
    }

    /**
     * Metodo para revelar las celdas vecinas si no tiene bomba, lo hace de forma recursiva
     * @param x
     * @param y
     */
    private void revealArea(int x, int y) {
        if (!gameLogic.isValidCell(x, y) || gameLogic.isRevealed(x, y) || gameLogic.isMarked(x, y)) {
            return;
        }

        gameLogic.revealCell(x, y);

        TableRow row = (TableRow) tableLayout.getChildAt(x);
        ImageButton button = (ImageButton) row.getChildAt(y);


        int bombs = gameLogic.getAdjacentBombs(x, y);
        if (bombs > 0) {
            button.setImageResource(getNumberDrawable(bombs));
            scaleImage(button);
        } else {
            button.setBackgroundColor(Color.LTGRAY);

            for (int[] neighbor : gameLogic.getNeighbors(x, y)) {
                revealArea(neighbor[0], neighbor[1]);
            }
        }
    }

    /**
     * Metodo para revelar todas las bombas.
     */
    private void revealAllBombs() {
        for (int i = 0; i < gameLogic.getBoardSize(); i++) {
            for (int j = 0; j < gameLogic.getBoardSize(); j++) {
                if (gameLogic.isBomb(i, j)) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    ImageButton button = (ImageButton) row.getChildAt(j);
                    button.setImageResource(R.drawable.bomb);
                    scaleImage(button);
                }
            }
        }
    }

    /**
     * Hilo que cuenta los segundos que pasan de juego.
     */
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
                        timerText.post(new Runnable() {
                            @Override
                            public void run() {
                                timerText.setText(String.valueOf(finalI));
                                if (timerText.getText().toString().equals("1000000"))
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

    /**
     * Metodo para ajustar las imagenes a los botones.
     * @param button boton donde ajustar imagen.
     */
    private void scaleImage(ImageButton button){
        button.setScaleType(ImageView.ScaleType.FIT_XY);
        button.setBackground(null);
        button.setPadding(0, 0, 0, 0);
    }

    /**
     * Metodo para asignar imagen a boton dependiendo del numero de bombas que tenga cerca la celda.
     * @param number numero de bombas cerca.
     * @return la imagen.
     */
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

    /**
     * Fin del juego.
     */
    private void endGame() {
        int finalTime = Integer.valueOf(timerText.getText().toString());
        timerThread.interrupt();
        gameEndListener.onGameEnd(gameLogic.calculateScore(mode, finalTime, gameLogic.isWin()), gameName, gameLogic.isWin());
    }
}
