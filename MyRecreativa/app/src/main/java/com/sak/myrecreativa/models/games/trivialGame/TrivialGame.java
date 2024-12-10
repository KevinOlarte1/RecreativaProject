package com.sak.myrecreativa.models.games.trivialGame;


import android.widget.ProgressBar;

import java.util.List;

import java.util.ArrayList;
import java.util.Random;

public class TrivialGame {
    private final List<Question> unansweredQuestions;
    private Question currentQuestion;
    private int score;

    public TrivialGame(List<Question> questions) {
        this.unansweredQuestions = new ArrayList<>(questions);
        this.score = 0;
    }

    /**
     * Metodo para obtener siguiente pregunta si existe.
     * @return pregunta.
     */
    public Question getNextQuestion() {
        if (unansweredQuestions.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(unansweredQuestions.size());
        currentQuestion = unansweredQuestions.get(index);
        return currentQuestion;
    }

    /**
     * Metodo para comprobar si la respuesta es correcta o no.
     * @param selectedAnswer respuesta del usuario.
     * @return true si es correcto y false si no.
     */
    public boolean checkAnswer(String selectedAnswer) {
        if (currentQuestion == null) {
            return false;
        }

        boolean isCorrect = currentQuestion.getCorrectAnswer().equals(selectedAnswer);

        updateGame(isCorrect);
        return isCorrect;
    }

    /**
     * Metodo para actualizar puntuacion y añadir pregunta contestada a la lista de preguntas contestadas.
     * @param isCorrect dependiendo de si la respuesta es correcta o no sumara y añadira la pregunta en la lista o no.
     */
    private void updateGame(boolean isCorrect){
        if (isCorrect) {
            score++;
        }
        unansweredQuestions.remove(currentQuestion);
        //currentQuestion = null;
    }

    /**
     * Metodo para comprobar si existe una siguiente pregunta en la lista.
     * @return true si es que si y false si no.
     */
    public boolean hasQuestions() {
        if (unansweredQuestions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public int getScore() {
        return score;
    }
    public Question getCurrentQuestion() {
        return currentQuestion;
    }
    public String getCorrectAnswer(){
        return currentQuestion.getCorrectAnswer();
    }
}

