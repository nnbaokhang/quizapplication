package com.example.android.quizapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

;


public class SignUpActivity extends AppCompatActivity {
    private EditText registerPassword;
    private EditText registerConfirmPassword;
    private EditText registerUsername;
    private Button   registerButton;
    String url;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public Integer sendPostRequest(String url)  {
        try {

            HttpRequest request =  HttpRequest.post(url);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        registerUsername = (EditText) findViewById(R.id.registerUser);
        registerPassword = (EditText) findViewById(R.id.registerPass1);
        registerConfirmPassword = (EditText) findViewById(R.id.registerPass2);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(!registerUsername.getText().toString().equals("")&& !registerPassword.getText().toString().equals("")&& registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())){
                    url  ="https://quizapp007.herokuapp.com/creatUser?username="+registerUsername.getText().toString()+"&password="+registerPassword.getText().toString();
                    int status = sendPostRequest(url);
                    if(status == 200) {
                        Toast.makeText(getApplicationContext(),
                                "Successful login", Toast.LENGTH_LONG).show();
                        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("name", registerUsername.getText().toString());
                        editor.apply();

                        Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
                        // Start the new activity
                        startActivity(mainActivityIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                    "Please check your username or password", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Please check your username or password", Toast.LENGTH_LONG).show();
                }
            }
        });




    }
}

