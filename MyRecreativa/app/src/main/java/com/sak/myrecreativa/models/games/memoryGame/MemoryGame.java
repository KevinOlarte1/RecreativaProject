package com.sak.myrecreativa.models.games.memoryGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGame {

    private List<Integer> cards;
    private boolean[] matchedCards;
    private int firstSelected = -1;
    private int secondSelected = -1;

    public MemoryGame(int numberOfPairs) {
        cards = new ArrayList<>();
        matchedCards = new boolean[numberOfPairs * 2];

        for (int i = 1; i <= numberOfPairs; i++) {
            cards.add(i);
            cards.add(i);
        }

        Collections.shuffle(cards);
    }

    /**
     * Metodo para añadir estado al boton.
     * @param position posicion del boton.
     * @return false si es la primera seleccion y si es la segunda llama para comprobar si las dos seleccionas coinciden o no.
     */
    public boolean selectCard(int position) {
        if (firstSelected == -1) {
            firstSelected = position;
            return false;
        } else if (secondSelected == -1 && position != firstSelected) {
            secondSelected = position;
            return compareSelectedCards();
        }
        return false;
    }

    /**
     * Metodo para comprobar si las cartas seleccionadas coinciden o no.
     * @return si coinciden devuelve true y si no devuelve false.
     */
    private boolean compareSelectedCards() {
        if (cards.get(firstSelected).equals(cards.get(secondSelected))) {
            matchedCards[firstSelected] = true;
            matchedCards[secondSelected] = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo para resetear los valores de seleccion, quitarle la poscion del boton y volver a -1.
     */
    public void resetSelection() {
        firstSelected = -1;
        secondSelected = -1;
    }

    public boolean isMatched(int position) {
        return matchedCards[position];
    }

    /**
     * Metodo para comprobar si ya se han encontrado todas la coincidencias o no.
     * @return true si es final de juego y false si aun falta parejas por encontrar.
     */
    public boolean isGameOver() {
        for (boolean matched : matchedCards) {
            if (!matched) return false;
        }
        return true;
    }

    /**
     * Metodo para calcular la puntuacion conseguida en la partida.
     * @param mode dependiendo del modo de juego.
     * @param secondsTaken y del timepo que se a tardado en encontrar todas las parejas.
     * @return la puntuacion.
     */
    public int calculateScore(String mode, int secondsTaken) {
        int basePoints = 0;
        int timePenalty = 0;

        // Determinar los puntos base y penalización de tiempo según el modo
        switch (mode.toLowerCase()) {
            case "easy":
                basePoints = 100;
                timePenalty = 3;
                break;
            case "medium":
                basePoints = 200;
                timePenalty = 2;
                break;
            case "hard":
                basePoints = 300;
                timePenalty = 1;
                break;
        }

        // Calcular puntaje final
        int finalScore = basePoints - ((secondsTaken - 2) * timePenalty);
        finalScore = Math.max(finalScore, 0); // Asegurarse de que no sea negativo

        // Normalizar puntaje sobre 5
        return Math.round((finalScore / (float) basePoints) * 5);
    }

    public int getCardAt(int position) {
        return cards.get(position);
    }

    public int getFirstSelected() {
        return firstSelected;
    }

    public int getSecondSelected() {
        return secondSelected;
    }

}
