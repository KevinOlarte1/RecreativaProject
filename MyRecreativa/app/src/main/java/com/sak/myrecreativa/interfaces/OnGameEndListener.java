package com.sak.myrecreativa.interfaces;

import com.sak.myrecreativa.models.GameName;

public interface OnGameEndListener {
    void onGameEnd(int score, GameName name,boolean isWin);
}
