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
        initializeGame(numberOfPairs);
    }

    private void initializeGame(int numberOfPairs) {
        cards = new ArrayList<>();
        matchedCards = new boolean[numberOfPairs * 2];

        for (int i = 1; i <= numberOfPairs; i++) {
            cards.add(i);
            cards.add(i);
        }

        Collections.shuffle(cards);
    }

    public int getCardAt(int position) {
        return cards.get(position);
    }

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

    private boolean compareSelectedCards() {
        if (cards.get(firstSelected).equals(cards.get(secondSelected))) {
            matchedCards[firstSelected] = true;
            matchedCards[secondSelected] = true;
            resetSelection();
            return true;
        } else {
            return false;
        }
    }

    public void resetSelection() {
        firstSelected = -1;
        secondSelected = -1;
    }

    public boolean isMatched(int position) {
        return matchedCards[position];
    }

    public boolean isGameOver() {
        for (boolean matched : matchedCards) {
            if (!matched) return false;
        }
        return true;
    }
}
