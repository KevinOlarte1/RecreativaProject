package com.sak.myrecreativa.interfaces.conecta4;

import com.sak.myrecreativa.models.games.conecta4.ConectaCuatro;
import com.sak.myrecreativa.ui.fragments.conecta4.ConectaCuatroFragment;

public class GameControllerConectaCuatro {


    private final ConectaCuatro model;
    private final ConectaCuatroFragment view;

    public GameControllerConectaCuatro(ConectaCuatroFragment view) {
        this.view = view;
        this.model = new ConectaCuatro();
    }


    public void makeMove(int col) {
        int lastRow = -1;

        if (!model.makeMove(col)) {
            return; // Si la columna está llena, no hacemos nada
        }

        // Buscar la fila donde se colocó la ficha
        int[][] board = model.getBoard();
        for (int i = 0; i < board.length; i++) {
            if (board[i][col] != 0) {
                lastRow = i;
                break;
            }
        }

        // Actualizar la posición específica en la vista
        view.updateBoard(lastRow, col, model.getCurrentPlayer());

        // Comprobar si hay un ganador
        if (model.checkWin(lastRow, col)) {
            view.showWinner(model.getCurrentPlayer());
            if (model.getCurrentPlayer() == 1)
                model.addscore();
            else
                model.resetScore();
        } else {
            model.switchPlayer();
            view.showTurn(model.getCurrentPlayer());
        }
    }


    public void endGame() {
        int score  = model.getScore();
        model.resetScore();
        view.endGame(model.getCurrentPlayer(), score);

    }

    public void restartGame() {
        model.resetGame(); // Reinicia el modelo (tablero vacío)
        view.resetUI(); // Restablece la interfaz de usuario (turno inicial, botones reiniciados)

        view.resetMap();
    }

}
