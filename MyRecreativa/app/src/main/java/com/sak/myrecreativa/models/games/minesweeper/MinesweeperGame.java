package com.sak.myrecreativa.models.games.minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperGame {
    private final int[][] board;
    private final boolean[][] revealed;
    private final boolean[][] marked;
    private final int boardSize;
    private final int bombCount;

    public MinesweeperGame(int boardSize, int bombCount) {
        this.boardSize = boardSize;
        this.bombCount = bombCount;
        this.board = new int[boardSize][boardSize];
        this.revealed = new boolean[boardSize][boardSize];
        this.marked = new boolean[boardSize][boardSize];
        generateBoard();
    }

    private void generateBoard() {
        Random random = new Random();
        int bombsPlaced = 0;

        while (bombsPlaced < bombCount) {
            int i = random.nextInt(boardSize);
            int j = random.nextInt(boardSize);

            if (board[i][j] != -1) {
                board[i][j] = -1;
                bombsPlaced++;
                updateAdjacentCells(i, j);
            }
        }
    }

    private void updateAdjacentCells(int i, int j) {
        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                int ni = i + y, nj = j + z;
                if (isValidCell(ni, nj) && board[ni][nj] != -1) {
                    board[ni][nj]++;
                }
            }
        }
    }

    public boolean isValidCell(int i, int j) {
        return i >= 0 && i < boardSize && j >= 0 && j < boardSize;
    }

    public boolean isBomb(int i, int j) {
        return board[i][j] == -1;
    }

    public int getAdjacentBombs(int i, int j) {
        return board[i][j];
    }

    public boolean isRevealed(int i, int j) {
        return revealed[i][j];
    }

    public boolean isMarked(int i, int j) {
        return marked[i][j];
    }

    public void toggleMark(int i, int j) {
        if (!revealed[i][j]) {
            marked[i][j] = !marked[i][j];
        }
    }

    public void revealCell(int i, int j) {
        if (!isValidCell(i, j) || revealed[i][j]) return;
        revealed[i][j] = true;
    }

    public boolean isWin() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!revealed[i][j] && board[i][j] != -1) return false;
            }
        }
        return true;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getBombCount() {
        return bombCount;
    }

    public List<int[]> getNeighbors(int i, int j) {
        List<int[]> neighbors = new ArrayList<>();
        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                int ni = i + y, nj = j + z;
                if ((y != 0 || z != 0) && isValidCell(ni, nj)) {
                    neighbors.add(new int[]{ni, nj});
                }
            }
        }
        return neighbors;
    }

    public int getMarkedCount() {
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (marked[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    public int calculateScore(String mode, int secondsTaken, boolean isWin) {
        if (!isWin){
            return 0;
        }
        int basePoints = 0;
        int timePenalty = 0;

        // Determinar los puntos base y penalización de tiempo según el modo
        switch (mode.toLowerCase()) {
            case "easy":
                basePoints = 100;
                timePenalty = 1;
                break;
            case "medium":
                basePoints = 200;
                timePenalty = 2;
                break;
            case "hard":
                basePoints = 300;
                timePenalty = 3;
                break;
        }

        // Calcular puntaje final
        int finalScore = basePoints - ((secondsTaken - 2) * timePenalty);
        finalScore = Math.max(finalScore, 0); // Asegurarse de que no sea negativo

        // Normalizar puntaje sobre 5
        int normalizedScore = Math.round((finalScore / (float) basePoints) * 5);

        return normalizedScore;
    }

}
