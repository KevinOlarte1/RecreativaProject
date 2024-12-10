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

    /**
     * Metodo para generar el tablero.
     */
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

    /**
     * Incrementa el conteo de bombas cercanas en las celdas adyacentes a la posición (i, j) si no contienen una bomba.
     * @param i
     * @param j
     */
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

    /**
     * Verifica si las coordenadas (i, j) están dentro de los límites del tablero.
     * @param i
     * @param j
     * @return true si estan dentro y false si no.
     */
    public boolean isValidCell(int i, int j) {
        return i >= 0 && i < boardSize && j >= 0 && j < boardSize;
    }

    /**
     * Comprobar si es bomba en las coordenadas (i,j)
     * @param i
     * @param j
     * @return true si es bomba y false si no.
     */
    public boolean isBomb(int i, int j) {
        return board[i][j] == -1;
    }

    /**
     * Devuelve el número de bombas cercanas a la celda.
     * @param i
     * @param j
     * @return el numero de bombas.
     */
    public int getAdjacentBombs(int i, int j) {
        return board[i][j];
    }

    /**
     * Comprobar si la posicion a sido revelada o no.
     * @param i
     * @param j
     * @return devuleve true si es que si y false si no.
     */
    public boolean isRevealed(int i, int j) {
        return revealed[i][j];
    }

    /**
     * Comprobar si la posicion a sido marcada como bomba.
     * @param i
     * @param j
     * @return devuelve true si es que si y false si no.
     */
    public boolean isMarked(int i, int j) {
        return marked[i][j];
    }

    /**
     * Metodo que perimte al jugador marcar una celda.
     * @param i
     * @param j
     */
    public void toggleMark(int i, int j) {
        if (!revealed[i][j]) {
            marked[i][j] = !marked[i][j];
        }
    }

    /**
     * Revela la celda (i, j) si es válida y no ha sido revelada previamente.
     * @param i
     * @param j
     */
    public void revealCell(int i, int j) {
        if (!isValidCell(i, j) || revealed[i][j]) return;
        revealed[i][j] = true;
    }

    /**
     * Comprueba si el jugador ha ganado el juego. Esto ocurre si todas las celdas que no contienen bombas están reveladas
     * @return true si a ganado y false si no.
     */
    public boolean isWin() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!revealed[i][j] && board[i][j] != -1) return false;
            }
        }
        return true;
    }

    /**
     * Metodo para obtener las celdas vecinas de una celda
     * @param i
     * @param j
     * @return lista con las coordenadas de las celdas vecinas.
     */
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

    /**
     * Comprobar las posciones marcadas como posible bomba.
     * @return el numero de marcados.
     */
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

    /**
     * Metodo para calcular puntuacion obtenida en la partida.
     * @param mode modo de juego de la partida.
     * @param secondsTaken tiempo que a durado la partida.
     * @param isWin si a ganado o a perdido(a revelado por completo el tablero o le a dado a una bomba).
     * @return la puntuacion (0 si a perdido).
     */
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
        return Math.max(finalScore, 0); // Asegurarse de que no sea negativo
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getBombCount() {
        return bombCount;
    }

}
