package com.sak.myrecreativa.models.games.battleship;

import java.util.Random;

public class BattleshipGame {
    private final int[][] playerBoard; // Tablero del jugador
    private final int[][] opponentBoard; // Tablero del oponente (CPU o jugador 2)
    private final boolean isAgainstCPU; // Indica si el juego es contra la CPU
    private final int boardSize; // Tamaño del tablero (NxN)
    private int playerShipsRemaining; // Barcos restantes del jugador
    private int opponentShipsRemaining; // Barcos restantes del oponente

    // Estados de las celdas
    private static final int EMPTY = 0; // Celda vacía
    private static final int SHIP = 1; // Celda con un barco
    private static final int HIT = -1; // Celda con un barco golpeado
    private static final int MISS = 2; // Celda atacada pero sin barco

    /**
     * Constructor para inicializar el juego de Battleship.
     *
     * @param boardSize    Tamaño del tablero (NxN).
     * @param isAgainstCPU Indica si el juego es contra la CPU (true) o contra otro jugador (false).
     */
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

    /**
     * Calcula la cantidad de barcos basada en el tamaño del tablero.
     * La cantidad mínima de barcos es 3.
     *
     * @param boardSize Tamaño del tablero.
     * @return Cantidad de barcos.
     */
    private int calculateShips(int boardSize) {
        return Math.max(3, boardSize / 2);
    }

    /**
     * Coloca barcos aleatoriamente en el tablero.
     *
     * @param board     Tablero en el que se colocarán los barcos.
     * @param shipCount Cantidad de barcos a colocar.
     */
    private void placeShips(int[][] board, int shipCount) {
        Random random = new Random();
        while (shipCount > 0) {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            if (board[x][y] == EMPTY) { // Verifica que la celda esté vacía
                board[x][y] = SHIP; // Coloca un barco
                shipCount--;
            }
        }
    }

    /**
     * Realiza un ataque al tablero del oponente.
     *
     * @param x Coordenada X de la celda a atacar.
     * @param y Coordenada Y de la celda a atacar.
     * @return true si el ataque golpea un barco, false si falla.
     */
    public boolean attackOpponent(int x, int y) {
        return attackCell(opponentBoard, x, y);
    }

    /**
     * Realiza un ataque al tablero del jugador (por parte del oponente o CPU).
     *
     * @param x Coordenada X de la celda a atacar.
     * @param y Coordenada Y de la celda a atacar.
     * @return true si el ataque golpea un barco, false si falla.
     */
    public boolean attackPlayer(int x, int y) {
        return attackCell(playerBoard, x, y);
    }

    /**
     * Realiza un ataque a una celda específica de un tablero.
     *
     * @param board Tablero en el que se realiza el ataque.
     * @param x     Coordenada X de la celda.
     * @param y     Coordenada Y de la celda.
     * @return true si el ataque golpea un barco, false si falla.
     */
    private boolean attackCell(int[][] board, int x, int y) {
        if (board[x][y] == SHIP) {
            board[x][y] = HIT; // Marca la celda como golpeada
            if (board == playerBoard) {
                playerShipsRemaining--;
            } else {
                opponentShipsRemaining--;
            }
            return true;
        } else if (board[x][y] == EMPTY) {
            board[x][y] = MISS; // Marca la celda como fallida
        }
        return false;
    }

    /**
     * Verifica si el juego ha terminado.
     *
     * @return true si todos los barcos de un jugador han sido destruidos, false en caso contrario.
     */
    public boolean isGameOver() {
        return playerShipsRemaining == 0 || opponentShipsRemaining == 0;
    }

    /**
     * Obtiene el ganador del juego.
     *
     * @return "Player" si el jugador ha ganado, "Opponent" si el oponente ha ganado, "No winner yet" si el juego continúa.
     */
    public String getWinner() {
        if (playerShipsRemaining == 0) {
            return "Opponent";
        } else if (opponentShipsRemaining == 0) {
            return "Player";
        }
        return "No winner yet";
    }

    /**
     * Obtiene el tablero del jugador.
     *
     * @return Matriz que representa el tablero del jugador.
     */
    public int[][] getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Obtiene el tablero del oponente.
     *
     * @return Matriz que representa el tablero del oponente.
     */
    public int[][] getOpponentBoard() {
        return opponentBoard;
    }

    /**
     * Obtiene el tamaño del tablero.
     *
     * @return Tamaño del tablero (NxN).
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Indica si el juego es contra la CPU.
     *
     * @return true si el juego es contra la CPU, false si es contra otro jugador.
     */
    public boolean isAgainstCPU() {
        return isAgainstCPU;
    }

    /**
     * Verifica si una celda ha sido atacada previamente.
     *
     * @param board Tablero en el que se verifica la celda.
     * @param x     Coordenada X de la celda.
     * @param y     Coordenada Y de la celda.
     * @return true si la celda ya ha sido atacada, false en caso contrario.
     */
    public boolean isCellRevealed(int[][] board, int x, int y) {
        return board[x][y] == HIT || board[x][y] == MISS;
    }
}
