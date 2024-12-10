package com.sak.myrecreativa.ui.fragments.trivialGame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.parsers.TrivialParser;
import com.sak.myrecreativa.models.games.trivialGame.Question;
import com.sak.myrecreativa.models.games.trivialGame.TrivialGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrivialFragment extends Fragment {
    private TrivialGame game;
    private ImageView img;
    private TextView tvQuestion;
    private Button option1;
    private Button option2;
    private Button option3;
    private List<Button> options;
    private String triviaMode;
    private TextView time;
    private ProgressBar timeBar;
    private Thread timerThread;
    private IOnGameEndListener gameEndListener;
    private GameName gameName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trivial_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        options = new ArrayList<>();

        time = view.findViewById(R.id.time);
        timeBar = view.findViewById(R.id.timerBar);
        img = view.findViewById(R.id.questionImg);
        tvQuestion = view.findViewById(R.id.question);
        option1 = view.findViewById(R.id.option1);
        options.add(option1);
        option2 = view.findViewById(R.id.option2);
        options.add(option2);
        option3 = view.findViewById(R.id.option3);
        options.add(option3);

        loadNextQuestion();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Bundle args = getArguments();

        if (args != null && args.containsKey("MODE")) {
            triviaMode = args.getString("MODE");
        }
        if (args != null && args.containsKey("GAME")){
            gameName = args.getParcelable("GAME");
        }

        int jsonResource = -1;
        if (triviaMode != null){
            switch (triviaMode.toLowerCase()) {
                case "movies":
                    jsonResource = R.raw.movie_questions;
                    break;
                case "series":
                    jsonResource = R.raw.series_questions;
                    break;
                case "anime":
                    jsonResource = R.raw.anime_questions;
                    break;
            }
        }
        List<Question> questions = TrivialParser.loadQuestions(context, jsonResource);
        game = new TrivialGame(questions);
        gameEndListener = (IOnGameEndListener)  context;
    }

    private void loadNextQuestion(){
            Question question = game.getNextQuestion();
            tvQuestion.setText(question.getQuestion());

            List<String> shuffledOptions = new ArrayList<>(question.getOptions());
            Collections.shuffle(shuffledOptions);

            option1.setText(shuffledOptions.get(0));
            option1.setBackgroundColor(Color.DKGRAY);
            option2.setText(shuffledOptions.get(1));
            option2.setBackgroundColor(Color.DKGRAY);
            option3.setText(shuffledOptions.get(2));
            option3.setBackgroundColor(Color.DKGRAY);

            timer(15);
            Context context = getContext();
            if(context != null){
                Resources res =context.getResources();
                int resId = res.getIdentifier(game.getCurrentQuestion().getCode().toLowerCase(), "drawable", context.getPackageName());
                img.setBackgroundResource(resId);
            }

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(option1.getText().toString(), option1);
                }
            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(option2.getText().toString(), option2);
                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(option3.getText().toString(), option3);
                }
            });
    }
    private void checkAnswer(String answer, Button button){
        boolean isCorrect = game.checkAnswer(answer);
        if (isCorrect && !time.getText().equals("0") && game.hasQuestions()) {
            button.setBackgroundColor(Color.GREEN);
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
            loadNextQuestion();
        } else{
            timerThread.interrupt();
            Button correctButton = findButtonForAnswer(game.getCorrectAnswer());
            if (correctButton != null) {
                correctButton.setBackgroundColor(Color.GREEN);
            }
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                endGame();
            }, 4000);
        }
    }

    private Button findButtonForAnswer(String answer) {
        for (Button option : options) {
            if (option.getText().toString().equals(answer)) {
                return option;
            }
        }
        return null;
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
        boolean isWin;
        isWin = !game.hasQuestions();

        gameEndListener.onGameEnd(game.getScore(), gameName, isWin);
    }
}


