package com.sak.myrecreativa.ui.fragments.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.games.sudoku.SudokuGame;

public class SudokuGameFragment extends Fragment {

    private SudokuGame sudokuGame;
    private TableLayout tableLayout;
    private EditText numberInput;
    private Button ingresarButton;
    private Button finishButton;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private String mode;
    private int boardSize;

    /**
     * Método llamado al adjuntar el fragmento al contexto de la actividad.
     * Configura el modo del juego y el tamaño del tablero basado en los argumentos proporcionados.
     *
     * @param context Contexto de la actividad.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();

        if (args != null && args.containsKey("MODE")) {
            mode = args.getString("MODE", "medium");
        } else {
            mode = "medium";
        }

        // Configuración del tamaño del tablero basado en el modo de juego
        switch (mode.toLowerCase()) {
            case "easy":
                boardSize = 4; // Cambiado a 4x4 para asegurar un cuadrado perfecto
                break;
            case "medium":
                boardSize = 9; // 9x9 para el modo medio
                break;
            case "hard":
                boardSize = 16; // 16x16 para el modo difícil
                break;
            default:
                boardSize = 9;
        }

        try {
            sudokuGame = new SudokuGame(boardSize, mode.toLowerCase());
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Error al configurar el tablero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
    }


    /**
     * Crea la vista del fragmento inflando el diseño del tablero de Sudoku.
     * @param inflater Inflador para las vistas.
     * @param container Contenedor del fragmento.
     * @param savedInstanceState Estado previo del fragmento, si existe.
     * @return Vista inflada del diseño del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sudoku_fragment, container, false);
    }

    /**
     * Configura las vistas y los botones después de que la vista ha sido creada.
     * @param view Vista raíz del fragmento.
     * @param savedInstanceState Estado previo del fragmento, si existe.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tableLayout = view.findViewById(R.id.sudokuTable);
        finishButton = view.findViewById(R.id.btn_finalizar);
        ingresarButton = view.findViewById(R.id.btn_ingresar);
        numberInput = view.findViewById(R.id.numberInput);

        if (tableLayout == null || finishButton == null || ingresarButton == null || numberInput == null) {
            throw new IllegalStateException("Algunos elementos del layout no se pudieron inicializar.");
        }

        // Inicializa el juego
        sudokuGame = new SudokuGame(boardSize, mode.toLowerCase());
        createGameBoard();

        // Configura el botón para ingresar el número
        ingresarButton.setOnClickListener(v -> handleNumberInput());

        // Configura el botón para finalizar el juego
        finishButton.setOnClickListener(v -> {
            if (sudokuGame.isSolved()) {
                Toast.makeText(getContext(), "¡Juego completado exitosamente!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "El juego no está completo. ¡Sigue intentándolo!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleNumberInput() {
        if (selectedRow == -1 || selectedCol == -1) {
            Toast.makeText(getContext(), "Selecciona una celda primero", Toast.LENGTH_SHORT).show();
            return;
        }

        String input = numberInput.getText().toString();
        if (input.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "El número ingresado no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sudokuGame.makeMove(selectedRow, selectedCol, number)) {
            updateCell(selectedRow, selectedCol, number);
            numberInput.setText(""); // Limpia el campo de entrada
        } else {
            Toast.makeText(getContext(), "Movimiento no permitido", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crea y muestra el tablero de Sudoku en la interfaz de usuario.
     * Configura las celdas del tablero y las hace interactuables.
     */
    private void createGameBoard() {
        tableLayout.removeAllViews();

        int[][] board = sudokuGame.getBoard();

        for (int i = 0; i < boardSize; i++) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            for (int j = 0; j < boardSize; j++) {
                TextView cell = new TextView(getContext());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                params.setMargins(2, 2, 2, 2);
                cell.setLayoutParams(params);
                cell.setGravity(Gravity.CENTER);
                cell.setTextSize(16); // Ajusta el tamaño del texto para tableros más grandes
                cell.setBackgroundResource(R.drawable.cell_border);

                if (board[i][j] != 0) {
                    cell.setText(String.valueOf(board[i][j]));
                    cell.setClickable(false);
                    cell.setBackgroundColor(Color.LTGRAY);
                } else {
                    cell.setText("");
                    cell.setClickable(true);
                    int finalI = i, finalJ = j;
                    cell.setOnClickListener(v -> {
                        selectedRow = finalI;
                        selectedCol = finalJ;
                        highlightSelectedCell(finalI, finalJ);
                    });
                }

                row.addView(cell);
            }
            tableLayout.addView(row);
        }
    }

    private void highlightSelectedCell(int row, int col) {
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                TextView cell = (TextView) tableRow.getChildAt(j);
                if (i == row && j == col) {
                    cell.setBackgroundColor(Color.YELLOW);
                } else {
                    cell.setBackgroundResource(R.drawable.cell_border);
                }
            }
        }
    }

    /**
     * Actualiza el contenido de una celda en el tablero después de un movimiento válido.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param number Número colocado en la celda.
     */
    private void updateCell(int row, int col, int number) {
        TableRow tableRow = (TableRow) tableLayout.getChildAt(row);
        if (tableRow != null) {
            TextView cell = (TextView) tableRow.getChildAt(col);
            if (cell != null) {
                cell.setText(String.valueOf(number));
                cell.setBackgroundColor(Color.WHITE); // Restaura el fondo
            }
        }
    }
}