package com.sak.myrecreativa.models.games.colorpatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorPatternGame {

    private final List<Integer> sequence;
    private int userStep;
    private int score;
    private boolean userTurn = false;
    private boolean finish = false;

    public  ColorPatternGame(int dificulty){
        this.sequence = new ArrayList<>();
        startGame(dificulty);
    }

    /**
     * Metodo para inicializar las variables.
     */
    private void startGame(int n) {
        sequence.clear();
        userStep = 0;
        score = 0;
        addNewStep();

        if(n ==2){
            for (int i = 0; i < 5; i++) {
                addNewStep();
            }

        }
        else if (n == 3){
            for (int i = 0; i < 10; i++) {
                addNewStep();
            }
        }
    }

    /**
     * Añadir otro numero random 1-4 para memorizar
     */
    public void addNewStep() {
        Random rnd = new Random();
        sequence.add(rnd.nextInt(4));
    }

    public void updateScore() {
    }

    /**
     * Verificar si el num ingresado esta en esa posicion y si es ese
     * @param colorIndex num a verificar
     */
    public  void  checkInput(int colorIndex){
        if (!userTurn) return;

        if (sequence.get(userStep) == colorIndex){
            userStep++;
            if (userStep == sequence.size()){
                score++;
                userStep = 0;
                userTurn = false;
                addNewStep();
            }
        }
        else
            finish = true;
    }

    /**
     * Saber si el juego a terminado porque el jugador a fallado
     * @return bollenao verificandolo
     */
    public boolean isFinish() {
        return finish;
    }

    public boolean isUserTurn() {
        return userTurn;
    }

    public void setUserTurn(boolean userTurn) {
        this.userTurn = userTurn;
    }

    public int getScore() {
        return score;
    }

    public List<Integer> getSequence() {
        return sequence;
    }
}
