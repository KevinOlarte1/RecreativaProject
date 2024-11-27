package com.sak.myrecreativa.models.games.trivialGame;

import java.util.List;

public class Question {
    private final int id;
    private final String code;
    private final String question;
    private final List<String> options;
    private final String correct_answer;

    public Question(int id, String code, String question, List<String> options, String correct_answer) {
        this.id = id;
        this.code = code;
        this.question = question;
        this.options = options;
        this.correct_answer = correct_answer;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correct_answer; }
}
