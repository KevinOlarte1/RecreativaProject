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

    public boolean makeMove(int col) {
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][col] == 0) {
                board[i][col] = currentPlayer;
                return true;
            }
        }
        return false; // Columna llena
    }

    public boolean checkWin(int lastRow, int lastCol) {
        return checkDirection(lastRow, lastCol, 0, 1) || // Horizontal
                checkDirection(lastRow, lastCol, 1, 0) || // Vertical
                checkDirection(lastRow, lastCol, 1, 1) || // Diagonal \
                checkDirection(lastRow, lastCol, 1, -1);  // Diagonal /
    }

    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        int count = 1;
        count += countInDirection(row, col, rowDir, colDir);
        count += countInDirection(row, col, -rowDir, -colDir);
        return count >= 4;
    }

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

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = 3 - currentPlayer; // Alterna entre 1 y 2
    }

    public int[][] getBoard() {
        return board;
    }

    public void resetGame() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = 0;
            }
        }
        currentPlayer = 1;
    }

    public void addscore(){
        this.score++;
    }

    public int getScore() {
        return score;
    }
}
