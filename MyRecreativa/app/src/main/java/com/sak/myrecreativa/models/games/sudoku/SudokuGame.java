package com.sak.myrecreativa.models.games.sudoku;

import java.util.Random;

public class SudokuGame {
    private final int[][] board; // Tablero con valores iniciales
    private final int[][] solution; // Solución del Sudoku
    private final int boardSize;
    private final int subGridSize;

    public SudokuGame(int boardSize) {
        this.boardSize = boardSize;
        this.subGridSize = (int) Math.sqrt(boardSize);
        this.board = new int[boardSize][boardSize];
        this.solution = new int[boardSize][boardSize];
        generateBoard();
    }

    private void generateBoard() {
        // Genera un tablero completo
        fillDiagonal();
        fillRemaining(0, subGridSize);
        copySolution();
        removeNumbers();
    }

    private void fillDiagonal() {
        for (int i = 0; i < boardSize; i += subGridSize) {
            fillSubGrid(i, i);
        }
    }

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

    private void copySolution() {
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(board[i], 0, solution[i], 0, boardSize);
        }
    }

    private void removeNumbers() {
        Random random = new Random();
        int cellsToRemove = boardSize * boardSize / 2; // Remueve la mitad de los números
        while (cellsToRemove > 0) {
            int row = random.nextInt(boardSize);
            int col = random.nextInt(boardSize);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                cellsToRemove--;
            }
        }
    }

    public boolean isSafe(int row, int col, int num) {
        return !isInRow(row, num) && !isInCol(col, num) && !isInSubGrid(row - row % subGridSize, col - col % subGridSize, num);
    }

    private boolean isInRow(int row, int num) {
        for (int j = 0; j < boardSize; j++) {
            if (board[row][j] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean isInCol(int col, int num) {
        for (int i = 0; i < boardSize; i++) {
            if (board[i][col] == num) {
                return true;
            }
        }
        return false;
    }

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

    public boolean makeMove(int row, int col, int num) {
        if (board[row][col] == 0 && isSafe(row, col, num)) {
            board[row][col] = num;
            return true;
        }
        return false;
    }

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

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolution() {
        return solution;
    }
}
