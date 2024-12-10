package com.sak.myrecreativa.models.games.colorpatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorPatternGame {
    private final List<Integer> pattern; // Secuencia de colores
    private final Random random;
    private int currentStep;
    private final int initialPatternLength; // Longitud inicial del patrón

    public ColorPatternGame(int difficulty) {
        pattern = new ArrayList<>();
        random = new Random();
        currentStep = 0;

        // Configura la longitud inicial del patrón según la dificultad
        switch (difficulty) {
            case 1: // Fácil
                initialPatternLength = 3;
                break;
            case 2: // Medio
                initialPatternLength = 5;
                break;
            case 3: // Difícil
                initialPatternLength = 7;
                break;
            default:
                initialPatternLength = 3;
        }

        // Genera el patrón inicial
        for (int i = 0; i < initialPatternLength; i++) {
            addColorToPattern();
        }
    }

    public void addColorToPattern() {
        pattern.add(random.nextInt(4)); // 4 colores disponibles
        currentStep = 0; // Reinicia el paso actual
    }

    public boolean verifyStep(int color) {
        if (color == pattern.get(currentStep)) {
            currentStep++;
            return currentStep == pattern.size(); // True si completó el patrón
        }
        return false; // Falló el patrón
    }

    public List<Integer> getPattern() {
        return new ArrayList<>(pattern);
    }

    public void resetGame() {
        pattern.clear();
        for (int i = 0; i < initialPatternLength; i++) {
            addColorToPattern();
        }
    }
}
