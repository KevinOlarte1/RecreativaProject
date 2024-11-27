package com.sak.myrecreativa.ui.fragments.trivialGame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sak.myrecreativa.R;
import com.sak.myrecreativa.models.parsers.TrivialParser;
import com.sak.myrecreativa.models.games.trivialGame.Question;
import com.sak.myrecreativa.models.games.trivialGame.TrivialGame;

import java.util.List;

public class TrivialFragment extends Fragment {
    private TrivialGame game;
    private TextView tvQuestion;
    private Button option1;
    private Button option2;
    private Button option3;
    private String triviaMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trivial_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView countDownBar = view.findViewById(R.id.countdownBar);
        countDownBar.setBackgroundColor(Color.BLUE);

        tvQuestion = view.findViewById(R.id.question);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);

        loadNextQuestion();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Bundle args = getArguments();

        if (args != null && args.containsKey("TRIVIA_MODE")) {
            triviaMode = args.getString("TRIVIA_MODE");
        }

        int jsonResource = -1;
        switch (triviaMode) {
            case "movies":
                jsonResource = R.raw.movie_questions;
                break;
            case "series":
                //jsonResource = R.raw.series_questions;
                break;
            case "anime":
                // jsonResource = R.raw.anime_questions;
                break;
        }

        List<Question> questions = TrivialParser.loadQuestions(context, jsonResource);
        game = new TrivialGame(questions);
    }

    private void loadNextQuestion(){
        if(game.hasQuestions()){
            Question question = game.getNextQuestion();
            tvQuestion.setText(question.getQuestion());
            option1.setText(question.getOptions().get(0));
            option1.setBackgroundColor(Color.DKGRAY);
            option2.setText(question.getOptions().get(1));
            option2.setBackgroundColor(Color.DKGRAY);
            option3.setText(question.getOptions().get(2));
            option3.setBackgroundColor(Color.DKGRAY);

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(question.getOptions().get(0), option1);
                }
            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(question.getOptions().get(1), option2);
                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(question.getOptions().get(2), option3);
                }
            });
        }
    }
    private void checkAnswer(String answer, Button button){
        boolean isCorrect = game.checkAnswer(answer);
        if (isCorrect) {
            button.setBackgroundColor(Color.GREEN);
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
            loadNextQuestion();
        } else {
            button.setBackgroundColor(Color.RED);
            endGame();
        }
    }

    private void endGame() {
        Toast.makeText(getContext(), "Juego terminado. Puntuación: " + game.getScore(), Toast.LENGTH_LONG).show();
    }
}


