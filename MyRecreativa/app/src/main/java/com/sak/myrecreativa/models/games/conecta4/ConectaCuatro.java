package com.sak.myrecreativa.models.games.conecta4;


public class ConectaCuatro{
    private final int rows = 6;
    private final int columns = 7;
    private final int[][] board;
    private int currentPlayer;

    private int score;

    public ConectaCuatro() {
        board = new int[rows][columns];
        currentPlayer = 1;
    }

    /**
     * Realiza un movimiento en una columna especfica.
     *
     * @param col La columna donde se desea realizar el movimiento.
     * @return true si el movimiento es válido, false si la columna está llena.
     */
    public boolean makeMove(int col) {
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][col] == 0) {
                board[i][col] = currentPlayer;
                return true;
            }
        }
        return false; // Columna llena
    }

    /**
     * Verifica si el jugador actual ha ganado despues de su ultimo movimiento.
     *
     * @param lastRow Fila del último movimiento.
     * @param lastCol Columna del último movimiento.
     * @return true si el jugador actual ha ganado, false en caso contrario.
     */
    public boolean checkWin(int lastRow, int lastCol) {
        return checkDirection(lastRow, lastCol, 0, 1) || // Horizontal
                checkDirection(lastRow, lastCol, 1, 0) || // Vertical
                checkDirection(lastRow, lastCol, 1, 1) || // Diagonal \
                checkDirection(lastRow, lastCol, 1, -1);  // Diagonal /
    }

    /**
     * Verifica si hay una conexion de cuatro fichas en una direccion especfica.
     *
     * @param row    Fila inicial.
     * @param col    Columna inicial.
     * @param rowDir Direccion de la fila.
     * @param colDir Direccio de la columna.
     * @return true si hay una conexión, false en caso contrario.
     */
    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        int count = 1;
        count += countInDirection(row, col, rowDir, colDir);
        count += countInDirection(row, col, -rowDir, -colDir);
        return count >= 4;
    }

    /**
     * Cuenta las fichas consecutivas en una dirección específica.
     *
     * @param row    Fila inicial.
     * @param col    Columna inicial.
     * @param rowDir Dirección de la fila.
     * @param colDir Dirección de la columna.
     * @return Número de fichas consecutivas.
     */
    private int countInDirection(int row, int col, int rowDir, int colDir) {
        int count = 0;
        int player = board[row][col];
        int r = row + rowDir;
        int c = col + colDir;

        while (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == player) {
            count++;
            r += rowDir;
            c += colDir;
        }
        return count;
    }

    /**
     * Obtener el jugador acutal
     * @return
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Intercambia el jugador acutal de una manera peculiar
     */
    public void switchPlayer() {
        currentPlayer = 3 - currentPlayer; // Alterna entre 1 y 2
    }

    /**
     * Obtener el tablero
     * @return
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Metodo para resetear el tablero
     */
    public void resetGame() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = 0;
            }
        }
        currentPlayer = 1;
    }

    /**
     * Metodo para resetear el puntaje a 0
     */
    public void resetScore(){
        this.score = 0;
    }

    /**
     * Aumentar en uno el puntaje
     */
    public void addscore(){
        this.score++;
    }

    public int getScore() {
        return score;
    }
}
