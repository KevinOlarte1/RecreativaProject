package com.sak.myrecreativa.models.games.battleship;

import java.util.Random;

public class BattleshipGame {
    private final int[][] playerBoard;
    private final int[][] opponentBoard;
    private final boolean isAgainstCPU;
    private final int boardSize;
    private int playerShipsRemaining;
    private int opponentShipsRemaining;

    // Estados de las celdas
    private static final int EMPTY = 0;
    private static final int SHIP = 1;
    private static final int HIT = -1;
    private static final int MISS = 2;

    public BattleshipGame(int boardSize, boolean isAgainstCPU) {
        this.boardSize = boardSize;
        this.isAgainstCPU = isAgainstCPU;
        this.playerBoard = new int[boardSize][boardSize];
        this.opponentBoard = new int[boardSize][boardSize];
        this.playerShipsRemaining = calculateShips(boardSize);
        this.opponentShipsRemaining = calculateShips(boardSize);

        placeShips(playerBoard, playerShipsRemaining);
        placeShips(opponentBoard, opponentShipsRemaining);
    }

    private int calculateShips(int boardSize) {
        return Math.max(3, boardSize / 2); // Cantidad de barcos mínima: 3
    }

    private void placeShips(int[][] board, int shipCount) {
        Random random = new Random();
        while (shipCount > 0) {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            if (board[x][y] == 0) { // Celda vacía
                board[x][y] = 1; // Colocar barco
                shipCount--;
            }
        }
    }

    public boolean attackOpponent(int x, int y) {
        return attackCell(opponentBoard, x, y);
    }

    public boolean attackPlayer(int x, int y) {
        return attackCell(playerBoard, x, y);
    }

    private boolean attackCell(int[][] board, int x, int y) {
        if (board[x][y] == SHIP) {
            board[x][y] = HIT;
            if (board == playerBoard) {
                playerShipsRemaining--;
            } else {
                opponentShipsRemaining--;
            }
            return true;
        } else if (board[x][y] == EMPTY) {
            board[x][y] = MISS;
        }
        return false;
    }

    public boolean isGameOver() {
        return playerShipsRemaining == 0 || opponentShipsRemaining == 0;
    }

    public String getWinner() {
        if (playerShipsRemaining == 0) {
            return "Opponent";
        } else if (opponentShipsRemaining == 0) {
            return "Player";
        }
        return "No winner yet";
    }

    public int[][] getPlayerBoard() {
        return playerBoard;
    }

    public int[][] getOpponentBoard() {
        return opponentBoard;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isAgainstCPU() {
        return isAgainstCPU;
    }

    public boolean isCellRevealed(int[][] board, int x, int y) {
        return board[x][y] == HIT || board[x][y] == MISS;
    }
}
