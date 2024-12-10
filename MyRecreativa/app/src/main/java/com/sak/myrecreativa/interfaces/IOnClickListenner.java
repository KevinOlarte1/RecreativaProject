package com.sak.myrecreativa.interfaces;

import android.view.View;

import com.sak.myrecreativa.models.GameName;

public interface IOnClickListenner {

    /**
     * Cuando le hacemos click a unos de los juegos en el recycleView
     * @param position recibe su posicion
     * @param gameName y el juego indicado.
     */
    void onClick (int position,GameName gameName);
}
