package com.example.android.quizapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText username;
    private EditText password;
    private Button signinButton;
    private Button signupButton;
    String url;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String name;



    public Integer sendGetRequest(String url)  {
        try {
            HttpRequest request =  HttpRequest.get(url);
            Log.d("status",String.valueOf(request.code()));
            if (String.valueOf(request.code()).equals("200")) {
                System.out.println("success" + 200);
                return 200;
            }
        } catch (HttpRequest.HttpRequestException e){
            Log.d("http", "Wrong");
            e.printStackTrace();
            return 401;
        }
        return 401;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This strict mode is for http to work, need to investigate this further.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_signin);
        // startActivity(playIntent);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signinButton = findViewById(R.id.signinbutton);
        signupButton = findViewById(R.id.signupbutton);

         pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         editor = pref.edit();
         name = pref.getString("name",null);
         if(name != null){

             setContentView(R.layout.activity_main);



             // Showing selected spinner item
             Toast.makeText(getApplicationContext(), "Welcome back " + name,Toast.LENGTH_LONG).show();

             // Spinner element
             Spinner spinner = (Spinner) findViewById(R.id.spinner);
             // Spinner click listener

             // Spinner Drop down elements
             List<String> categories = new ArrayList<String>();
             categories.add(name);
             categories.add("Score");
             categories.add("Log out");
             // Creating adapter for spinner
             ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);

             // Drop down layout style - list view with radio button
             dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             // attaching data adapter to spinner
             spinner.setAdapter(dataAdapter);
             spinner.setOnItemSelectedListener(MainActivity.this);



             CardView play = findViewById(R.id.cardview1);
             // Set a click listener on that View
             play.setOnClickListener(new View.OnClickListener() {
                 // The code in this method will be executed when the numbers category is clicked on.
                 @Override
                 public void onClick(View view) {
                     Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                     // Start the new activity
                     startActivity(playIntent);
                 }
             });


         }
        else {
             signinButton.setOnClickListener(new View.OnClickListener() {

                 @Override
                 public void onClick(View view) {
                     url = "https://quizapp007.herokuapp.com/creatUser?username="+username.getText().toString()+"&password="+password.getText().toString();
                     int status = sendGetRequest(url);
                     if (status == 200) {
                         //This is the main app so let people open their application.
                         editor.putString("name", username.getText().toString());
                         editor.apply();
                         setContentView(R.layout.activity_main);

                         Toast.makeText(getApplicationContext(), "Welcome  " + username.getText().toString() ,Toast.LENGTH_LONG).show();



                         CardView play = findViewById(R.id.cardview1);
                         // Set a click listener on that View
                         play.setOnClickListener(new View.OnClickListener() {
                             // The code in this method will be executed when the numbers category is clicked on.
                             @Override
                             public void onClick(View view) {
                                 Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                                 // Start the new activity
                                 startActivity(playIntent);
                             }
                         });

                     

                     } else {
                         Toast.makeText(getApplicationContext(),
                                 "Wrong username or password", Toast.LENGTH_LONG).show();
                     }
                 }
             });

             signupButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                     // Start the new activity
                     startActivity(signUpIntent);
                 }
                }
                );
         }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if(item.equals("History")){
            //Intent HistoryIntent = new Intent(MainActivity.this, MainActivity.this);
            //startActivity(HistoryIntent);
        }
        else if(item.equals("Log out")){
                Intent LogOutIntent = new Intent(this,MainActivity.class);

                pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                editor = pref.edit();
                editor.putStringSet("name",null);
                editor.apply();

                LogOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LogOutIntent);
        } else if (item.equals("Score")) {
            Intent ScoreIntent = new Intent(this,ScoreActivity.class);
            startActivity(ScoreIntent);

        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}