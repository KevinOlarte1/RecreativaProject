package com.sak.myrecreativa.models.games.sudoku;

import java.util.Random;
public class SudokuGame {

    private final int[][] board; // Tablero con valores iniciales
    private final int[][] solution; // Solución del Sudoku
    private final int boardSize; // Tamaño del tablero (e.g., 9x9)
    private final int subGridSize; // Tamaño de las subcuadrículas (e.g., 3x3)

    /**
     * Constructor de la clase SudokuGame.
     * Inicializa el tablero, la solución y genera un nuevo Sudoku.
     *
     * @param boardSize Tamaño del tablero de Sudoku (debe ser un cuadrado perfecto, e.g., 9).
     */
    public SudokuGame(int boardSize) {
        this.boardSize = boardSize;
        this.subGridSize = (int) Math.sqrt(boardSize);
        this.board = new int[boardSize][boardSize];
        this.solution = new int[boardSize][boardSize];
        generateBoard();
    }

    /**
     * Genera un tablero de Sudoku completo con solución.
     * Llena el tablero, copia la solución y luego elimina números para crear el puzzle.
     */
    private void generateBoard() {
        fillDiagonal();
        fillRemaining(0, subGridSize);
        copySolution();
        removeNumbers();
    }

    /**
     * Llena las subcuadrículas diagonales con números válidos.
     */
    private void fillDiagonal() {
        for (int i = 0; i < boardSize; i += subGridSize) {
            fillSubGrid(i, i);
        }
    }

    /**
     * Llena una subcuadrícula con números válidos.
     *
     * @param row Fila de inicio de la subcuadrícula.
     * @param col Columna de inicio de la subcuadrícula.
     */
    private void fillSubGrid(int row, int col) {
        Random random = new Random();
        for (int i = 0; i < subGridSize; i++) {
            for (int j = 0; j < subGridSize; j++) {
                int num;
                do {
                    num = random.nextInt(boardSize) + 1;
                } while (!isSafe(row + i, col + j, num));
                board[row + i][col + j] = num;
            }
        }
    }

    /**
     * Llena las celdas restantes del tablero de Sudoku recursivamente.
     *
     * @param row Fila inicial.
     * @param col Columna inicial.
     * @return true si se completó exitosamente, false si no.
     */
    private boolean fillRemaining(int row, int col) {
        if (col >= boardSize && row < boardSize - 1) {
            row++;
            col = 0;
        }
        if (row >= boardSize && col >= boardSize) {
            return true;
        }

        if (row < subGridSize) {
            if (col < subGridSize) {
                col = subGridSize;
            }
        } else if (row < boardSize - subGridSize) {
            if (col == (row / subGridSize) * subGridSize) {
                col += subGridSize;
            }
        } else {
            if (col == boardSize - subGridSize) {
                row++;
                col = 0;
                if (row >= boardSize) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= boardSize; num++) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                if (fillRemaining(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    /**
     * Copia la solución generada al arreglo de solución.
     */
    private void copySolution() {
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(board[i], 0, solution[i], 0, boardSize);
        }
    }

    /**
     * Elimina números del tablero para crear el puzzle.
     */
    private void removeNumbers() {
        Random random = new Random();
        int cellsToRemove = boardSize * boardSize / 2;
        while (cellsToRemove > 0) {
            int row = random.nextInt(boardSize);
            int col = random.nextInt(boardSize);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                cellsToRemove--;
            }
        }
    }

    /**
     * Verifica si un número puede ser colocado en una celda.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param num Número a verificar.
     * @return true si es seguro colocar el número, false en caso contrario.
     */
    public boolean isSafe(int row, int col, int num) {
        return !isInRow(row, num) && !isInCol(col, num) && !isInSubGrid(row - row % subGridSize, col - col % subGridSize, num);
    }

    /**
     * Verifica si un número está en una fila.
     *
     * @param row Fila a verificar.
     * @param num Número a buscar.
     * @return true si el número está en la fila, false en caso contrario.
     */
    private boolean isInRow(int row, int num) {
        for (int j = 0; j < boardSize; j++) {
            if (board[row][j] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si un número está en una columna.
     *
     * @param col Columna a verificar.
     * @param num Número a buscar.
     * @return true si el número está en la columna, false en caso contrario.
     */
    private boolean isInCol(int col, int num) {
        for (int i = 0; i < boardSize; i++) {
            if (board[i][col] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si un número está en una subcuadrícula.
     *
     * @param startRow Fila de inicio de la subcuadrícula.
     * @param startCol Columna de inicio de la subcuadrícula.
     * @param num Número a buscar.
     * @return true si el número está en la subcuadrícula, false en caso contrario.
     */
    private boolean isInSubGrid(int startRow, int startCol, int num) {
        for (int i = 0; i < subGridSize; i++) {
            for (int j = 0; j < subGridSize; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Realiza un movimiento en el tablero.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @param num Número a colocar.
     * @return true si el movimiento es válido, false en caso contrario.
     */
    public boolean makeMove(int row, int col, int num) {
        if (board[row][col] == 0 && isSafe(row, col, num)) {
            board[row][col] = num;
            return true;
        }
        return false;
    }

    /**
     * Verifica si el Sudoku ha sido resuelto correctamente.
     *
     * @return true si está resuelto, false en caso contrario.
     */
    public boolean isSolved() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0 || board[i][j] != solution[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Obtiene el tablero actual del Sudoku.
     *
     * @return Matriz bidimensional que representa el tablero.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Obtiene la solución del Sudoku.
     *
     * @return Matriz bidimensional que representa la solución.
     */
    public int[][] getSolution() {
        return solution;
    }
}