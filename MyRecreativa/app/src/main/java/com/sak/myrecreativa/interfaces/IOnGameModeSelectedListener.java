package com.sak.myrecreativa.interfaces;

import com.sak.myrecreativa.models.GameName;

public interface IOnGameModeSelectedListener {

    /**
     * Se encarga de cargar el fragment del juego cuando ya es seleccionada la dificultad
     * @param mode dificultdad del juego
     * @param name juego al que "jugaremo" que sirve para guardar los datos.
     */
    void onGameModeSelected(String mode, GameName name);
}
