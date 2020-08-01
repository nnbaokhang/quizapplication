package com.example.android.quizapplication;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Objects;

public class ScoreActivity  extends AppCompatActivity {

    String url;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String name;
    JSONObject computerJson;
    JSONObject physicJson ;
    int computerScore;
    int physicScore;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void convertToJsonArray(String key, String response){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(response);
            JSONObject subJson = (JSONObject) json.get("score");
            computerJson = (JSONObject) subJson.get("computerscience");
            physicJson   = (JSONObject) subJson.get("physic");

        } catch(ParseException e){
            e.printStackTrace();
        }
    }
    public String sendRequest(String url)  {
        try {
            HttpRequest request =  HttpRequest.get(url);
            Log.d("status",String.valueOf(request.code()));

            if (request.ok()) {
                return String.valueOf(request.body());
            }
        } catch (HttpRequest.HttpRequestException e){
            Log.d("http", "Wrong");
            e.printStackTrace();
            return "";
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        name = pref.getString("name",null);

        String response;
        url = "https://quizapp007.herokuapp.com/creatUser/score?username=" + name;
        response = sendRequest(url);
        convertToJsonArray("score",response);
        TextView computerText = (TextView) findViewById(R.id.score_computer_grade);
        TextView physicText   = (TextView) findViewById(R.id.score_physic_grade);

        if(Integer.parseInt(computerJson.get("question").toString()) == 0){
             computerScore = 100;
        }
        if(Integer.parseInt(physicJson.get("question").toString() )== 0){
            physicScore = 100;
        }
        if(Integer.parseInt(computerJson.get("question").toString()) != 0 && Integer.parseInt(physicJson.get("question").toString() )!= 0) {
            computerScore = Integer.parseInt(computerJson.get("grade").toString()) * 100/ Integer.parseInt(computerJson.get("question").toString());
            physicScore = Integer.parseInt(physicJson.get("grade").toString()) * 100 / Integer.parseInt(physicJson.get("question").toString());
        }

        computerText.setText(String.valueOf(computerScore) + " %");
        physicText.setText(String.valueOf(physicScore) + " %");
    }
}
