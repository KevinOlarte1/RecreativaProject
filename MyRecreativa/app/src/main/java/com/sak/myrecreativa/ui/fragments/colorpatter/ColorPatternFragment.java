package com.sak.myrecreativa.ui.fragments.colorpatter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.sak.myrecreativa.R;
import com.sak.myrecreativa.interfaces.IOnGameEndListener;
import com.sak.myrecreativa.models.GameName;
import com.sak.myrecreativa.models.games.colorpatter.ColorPatternGame;

import java.util.List;

public class ColorPatternFragment extends Fragment {
    private ColorPatternGame game;

    private View redButton, yellowButton, greenButton, blueButton;
    private TextView scoreText;

    private IOnGameEndListener gameEndListener;
    private GameName gameName;
    private String triviaMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.colorpatter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int difficulty = 1;
        if(triviaMode.equalsIgnoreCase("easy"))
            difficulty = 1;
        else if(triviaMode.equalsIgnoreCase("medium"))
            difficulty = 2;
        else if(triviaMode.equalsIgnoreCase("hard"))
            difficulty = 3;

        game = new ColorPatternGame(difficulty);

        redButton = view.findViewById(R.id.redButton);
        yellowButton = view.findViewById(R.id.yellowButton);
        greenButton = view.findViewById(R.id.greenButton);
        blueButton = view.findViewById(R.id.blueButton);
        scoreText = view.findViewById(R.id.scoreText);

        disableButtons();

        play();
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button", "Button");
                checkInput(0);
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button", "Button");

                checkInput(1);
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button", "Button");

                checkInput(2);
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button", "Button");

                checkInput(3);
            }
        });



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

        gameEndListener = (IOnGameEndListener) context;
    }


    /**
     * Meotodo para inicializar el juego
     */
    private void play()  {
        try{
            Thread.sleep(350);
        }catch (InterruptedException e){

        }
        showSequence();

    }


    /**
     * Metodo para mostrar la sequencia de colores
     */
    private void showSequence() {
        disableButtons();

        Handler handler = new Handler();
        for (int i = 0; i < game.getSequence().size(); i++) {
            int colorIndex = game.getSequence().get(i);
            handler.postDelayed(() -> flashButton(colorIndex), i * 1000);
        }
        handler.postDelayed(() -> {
            game.setUserTurn(true);
            enableButtons();
        }, game.getSequence().size() * 1000);
    }

    /**
     * Metodo para mostrar que botones a sido presionado por la maquina
     * @param colorIndex
     */
    private void flashButton(int colorIndex) {
        View buttonToFlash;
        switch (colorIndex) {
            case 0:
                buttonToFlash = redButton;
                break;
            case 1:
                buttonToFlash = yellowButton;
                break;
            case 2:
                buttonToFlash = greenButton;
                break;
            case 3:
                buttonToFlash = blueButton;
                break;
            default:
                return;
        }

        buttonToFlash.setBackgroundColor(Color.WHITE);
        new Handler().postDelayed(() -> resetButtonColor(buttonToFlash, colorIndex), 500);
    }

    /**
     * Volver al color por defecto despues de cambiarlo a blanco
     * @param button poton al que se le va cambiar
     * @param colorIndex index color
     */
    private void resetButtonColor(View button, int colorIndex) {
        switch (colorIndex) {
            case 0:
                button.setBackgroundColor(Color.RED);
                break;
            case 1:
                button.setBackgroundColor(Color.YELLOW);
                break;
            case 2:
                button.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                button.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    /**
     * Comprobar la sequencia uno a uno
     * @param colorIndex valor de la secuncia en vez de colores
     */
    private void checkInput(int colorIndex) {
        Log.i("Bien", "Bien");
        game.checkInput(colorIndex);

        if (game.isFinish())
            gameOver();
        else if (!game.isUserTurn()){
            updateScore();
            showSequence();
        }

    }

    /**
     * Actualiza la etiqueta con el puntaje acutal
     */
    private void updateScore() {
        scoreText.setText("Actual Score: " + game.getScore());
    }

    /**
     * Metodo para llevarnos a fragment score.
     */
    private void gameOver() {
        gameEndListener.onGameEnd(game.getScore(), gameName, false);
    }

    /**
     * Desactiva el uso de los botones.
     */
    private void disableButtons() {
        Log.i("Adioas", "Adios");
        redButton.setEnabled(false);
        yellowButton.setEnabled(false);
        greenButton.setEnabled(false);
        blueButton.setEnabled(false);
    }

    /**
     * Activa el uso de los botones
     */
    private void enableButtons() {
        Log.i("Hola", "Hola");
        redButton.setEnabled(true);
        yellowButton.setEnabled(true);
        greenButton.setEnabled(true);
        blueButton.setEnabled(true);

    }
}
