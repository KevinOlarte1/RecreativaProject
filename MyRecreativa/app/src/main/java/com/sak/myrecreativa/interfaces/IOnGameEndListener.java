package com.sak.myrecreativa.interfaces;

import com.sak.myrecreativa.models.GameName;

public interface IOnGameEndListener {

    /**
     * Este metodo nos cammbiara al fragment score
     * @param score el puntaje ganado en esa partida
     * @param name el juego a que jugamos
     * @param isWin si hemos perdido o ganado
     */
    void onGameEnd(int score, GameName name,boolean isWin);
}
