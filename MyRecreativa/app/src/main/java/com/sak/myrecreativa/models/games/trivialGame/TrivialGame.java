package com.sak.myrecreativa.models.games.trivialGame;


import android.widget.ProgressBar;

import java.util.List;

import java.util.ArrayList;
import java.util.Random;

public class TrivialGame {
    private final List<Question> questions;
    private final List<Question> unansweredQuestions;
    private Question currentQuestion;
    private int score;

    public TrivialGame(List<Question> questions) {
        this.questions = questions;
        this.unansweredQuestions = new ArrayList<>(questions);
        this.score = 0;
    }

    public Question getNextQuestion() {
        if (unansweredQuestions.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(unansweredQuestions.size());
        currentQuestion = unansweredQuestions.get(index);
        return currentQuestion;
    }

    public boolean checkAnswer(String selectedAnswer) {
        if (currentQuestion == null) {
            return false;
        }

        boolean isCorrect = currentQuestion.getCorrectAnswer().equals(selectedAnswer);

        updateGame(isCorrect);
        return isCorrect;
    }
    private void updateGame(boolean isCorrect){
        if (isCorrect) {
            score++;
        }
        unansweredQuestions.remove(currentQuestion);
        //currentQuestion = null;
    }

    public int getScore() {
        return score;
    }

    public boolean hasQuestions() {
        if (unansweredQuestions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }
    public String getCorrectAnswer(){
        return currentQuestion.getCorrectAnswer();
    }
}

