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
    private TrivialGame gameLogic;

    private ImageView img;
    private TextView tvQuestion;
    private TextView time;
    private Button option1;
    private Button option2;
    private Button option3;
    private List<Button> options;

    private String triviaMode;
    private GameName gameName;

    private ProgressBar timeBar;
    private Thread timerThread;

    private IOnGameEndListener gameEndListener;

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
        gameLogic = new TrivialGame(questions);
        gameEndListener = (IOnGameEndListener)  context;
    }

    /**
     * Metodo para cargar la siguiente pregunta. Cargando las respuestas, creando los botones, reseteando el temporizador y asignado la imagen.
     */
    private void loadNextQuestion(){
            Question question = gameLogic.getNextQuestion();
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
                int resId = res.getIdentifier(gameLogic.getCurrentQuestion().getCode().toLowerCase(), "drawable", context.getPackageName());
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

    /**
     * Metodo que gestiona la ui a la hora de seleccionar una opcion.
     * @param answer texto de opcion seleccionada.
     * @param button opcion seleccionada.
     */
    private void checkAnswer(String answer, Button button){
        boolean isCorrect = gameLogic.checkAnswer(answer);
        if (isCorrect && !time.getText().equals("0") && gameLogic.hasQuestions()) {
            button.setBackgroundColor(Color.GREEN);
            Toast.makeText(getContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
            loadNextQuestion();
        } else{
            timerThread.interrupt();
            Button correctButton = findButtonForAnswer(gameLogic.getCorrectAnswer());
            if (correctButton != null) {
                correctButton.setBackgroundColor(Color.GREEN);
            }
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                endGame();
            }, 4000);
        }
    }

    /**
     * Metodo para encontrar boton con la respuesta.
     * @param answer la respuesta a buscar.
     * @return
     */
    private Button findButtonForAnswer(String answer) {
        for (Button option : options) {
            if (option.getText().toString().equals(answer)) {
                return option;
            }
        }
        return null;
    }

    /**
     * Hilo con cuenta atras.
     * @param segundos tiempo de la cuenta atras en segundos.
     */
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

    /**
     * Fin del juego.
     */
    private void endGame() {
        boolean isWin;
        isWin = !gameLogic.hasQuestions();

        gameEndListener.onGameEnd(gameLogic.getScore(), gameName, isWin);
    }
}


