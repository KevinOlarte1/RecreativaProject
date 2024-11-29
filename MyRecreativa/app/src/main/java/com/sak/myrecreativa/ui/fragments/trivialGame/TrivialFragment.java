package com.sak.myrecreativa.ui.fragments.trivialGame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private TextView time;
    private ProgressBar timeBar;
    private Thread timerThread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trivial_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        time = view.findViewById(R.id.time);
        timeBar = view.findViewById(R.id.timerBar);

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

        if (args != null && args.containsKey("TRIVIAL_MODE")) {
            triviaMode = args.getString("TRIVIAL_MODE");
        }

        int jsonResource = -1;
        if (triviaMode != null){
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

            timer(15);

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
        if (isCorrect && !time.getText().equals("0")) {
            button.setBackgroundColor(Color.GREEN);
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
            loadNextQuestion();
        } else{
            endGame();
        }
    }
    public void timer(int segundos){
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }

        timeBar.setProgress(0);
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = segundos; i >= 0; i--){
                    try {
                        Thread.sleep(1000);
                        timeBar.incrementProgressBy(100 / segundos);
                        int finalI = i;
                        time.post(new Runnable() {
                            @Override
                            public void run() {
                                time.setText(String.valueOf(finalI));
                                if (time.getText().equals("0"))
                                    endGame();
                            }
                        });

                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        timerThread.start();
    }

    private void endGame() {
        Toast.makeText(getContext(), "Juego terminado. Puntuación: " + game.getScore(), Toast.LENGTH_LONG).show();
        timerThread.interrupt();
    }
}


