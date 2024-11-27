package com.sak.myrecreativa.models.parsers;

import android.content.Context;

import com.sak.myrecreativa.models.games.trivialGame.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TrivialParser {
    public static List<Question> loadQuestions(Context context, int document)  {
        List<Question> questions = new ArrayList<>();
        try{
            InputStream is = context.getResources().openRawResource(document);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(json);
            JSONArray questionArray = obj.getJSONArray("questions");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject q = questionArray.getJSONObject(i);
                int id = q.getInt("id");
                String movieCode = q.getString("code");
                String question = q.getString("question");
                List<String> options = new ArrayList<>();
                JSONArray optionsArray = q.getJSONArray("options");
                for (int j = 0; j < optionsArray.length(); j++) {
                    options.add(optionsArray.getString(j));
                }
                String correctAnswer = q.getString("correct_answer");
                questions.add(new Question(id, movieCode, question, options, correctAnswer));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return questions;
    }
}
