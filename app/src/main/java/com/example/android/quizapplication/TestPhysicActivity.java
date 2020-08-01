package com.example.android.quizapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Objects;

public class TestPhysicActivity extends AppCompatActivity {

    String doBetter = "You can do better!" + System.getProperty("line.separator") + System.getProperty("line.separator") + "Try again?";
    String poor = "Brush up your knowledge, maybe?";
    String congrats = "Well done!" + System.getProperty("line.separator") + System.getProperty("line.separator") + "You are awesome!";

    LinearLayout linearLayout;
    RadioGroup[] radioGroup;
    EditText[] editText;
    CardView[] cardView;
    TextView[] textView;
    Integer final_score  = 0;
    Boolean didSubmitButton = false;
    ArrayList<Physic> physicArrayList = new ArrayList<>();
    String response;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;



    int[] color = new int[]{0xff2ecc71,0xfff5426c,0xff424ef5};
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void convertToJsonArray(String key, String response){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(response);

            JSONArray subjectList = (JSONArray) Objects.requireNonNull(json.get(key));
            for(int i = 0 ; i < subjectList.size(); i++){
                JSONArray test = (JSONArray) subjectList.get(i);
                System.out.println(test.get(1));
                JSONObject jsonObject = (JSONObject) test.get(1);
                System.out.println(jsonObject);
                physicArrayList.add(new Physic((String) jsonObject.get("question"),(ArrayList<String>) jsonObject.get("as"),(String) jsonObject.get("sa"),(Boolean) jsonObject.get("mul")));
            }
            //subjectAdapter.notifyDataSetChanged();
        } catch(ParseException e){
            e.printStackTrace();
        }
    }
    public String sendRequest()  {
        try {
            HttpRequest request =  HttpRequest.get("https://quizapp007.herokuapp.com/physic");
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This strict mode is for http to work, need to investigate this further.


        response = sendRequest();
        convertToJsonArray("subject",response);
        cardView   =  new CardView[physicArrayList.size()];
        radioGroup = new RadioGroup[physicArrayList.size()];
        editText   = new EditText[physicArrayList.size()];
        textView   = new TextView[physicArrayList.size()];



        setContentView(R.layout.test_physic);

        linearLayout = findViewById(R.id.physic_linearlayout);

        LinearLayout.LayoutParams editTextP =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextP.setMargins(50,50,50,50);
        LinearLayout.LayoutParams textViewP =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewP.setMargins(20, 20, 20, 20);
        LinearLayout.LayoutParams radioGroupP =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        radioGroupP.setMargins(100,100,100,100);

        LinearLayout[] linearLayoutCardView = new LinearLayout[physicArrayList.size()];
        for(int i = 0 ; i < physicArrayList.size(); i++){
            System.out.println(physicArrayList.get(i).getQuestion());
            radioGroup[i]= new RadioGroup(this);
            textView[i]  = new TextView(this);
            cardView[i] = new CardView(this);
            linearLayoutCardView[i] = new LinearLayout(this);
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1000, 500);


            cardView[i].setCardBackgroundColor(color[i%3]);
            cardView[i].addView(linearLayoutCardView[i]);
            linearLayout.addView(cardView[i]);
            //cardView[i].layout(4,20,4,20);
            cardView[i].setCardElevation(10);


            textView[i].setLayoutParams(textViewP);
            linearLayoutCardView[i].addView(textView[i]);
            linearLayoutCardView[i].setOrientation(LinearLayout.VERTICAL);
            cardView[i].setRadius(50);
            int n = i + 1;


            //linearLayout.addView(textView[i]);
            //If this is multiple questions
            if(physicArrayList.get(i).getMul()) {
                textView[i].setText(n +": " +physicArrayList.get(i).getQuestion());
                //radioGroup[i].setLayoutParams(radioGroupP);
                linearLayoutCardView[i].addView(radioGroup[i]);
                RadioButton[] radioButton = new RadioButton[physicArrayList.get(i).getAS().size()];
                ArrayList<String> as = physicArrayList.get(i).getAS();
                for (int j = 0; j < as.size(); j++) {
                    radioButton[j] = new RadioButton(this);
                    radioButton[j].setText(as.get(j));
                    radioGroup[i].addView(radioButton[j]);
                }
            }
            //If this is not a multiple question
            else{
                textView[i].setText(n +"  Written questions:\n " +physicArrayList.get(i).getQuestion());
                editText[i]= new EditText(this);

                //editText[i].setLayoutParams(editTextP);
                linearLayoutCardView[i].addView(editText[i]);
            }
        }



        ScrollView view = findViewById(R.id.scroll_view);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab.startAnimation(rotate_backward);
                    fab1.startAnimation(fab_close);
                    fab2.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab1.setClickable(false);
                    fab2.setClickable(false);
                    fab3.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab.startAnimation(rotate_forward);
                    fab1.startAnimation(fab_open);
                    fab2.startAnimation(fab_open);
                    fab3.startAnimation(fab_open);
                    fab1.setClickable(true);
                    fab2.setClickable(true);
                    fab3.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        if(didSubmitButton){
                                            Toast.makeText(TestPhysicActivity.this,
                                                    "You need to clear your answer before submit again", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        didSubmitButton = true;
                                        int[] selectedId = new int[physicArrayList.size()];
                                        RadioButton[] radio = new RadioButton[physicArrayList.size()];

                                        for (int i = 0; i < physicArrayList.size(); i++) {
                                            selectedId[i] = radioGroup[i].getCheckedRadioButtonId();
                                            System.out.println(selectedId[i]);
                                            if (selectedId[i] != -1 && physicArrayList.get(i).getMul()) {
                                                radio[i] = (RadioButton) findViewById(selectedId[i]);
                                                if (radio[i].getText().toString().toLowerCase().equals(physicArrayList.get(i).getSA().toLowerCase())) {
                                                    final_score++;
                                                }
                                            }
                                            //If edit text
                                            else if (!physicArrayList.get(i).getMul()) {
                                                // EditText editText = new EditText(this);
                                                if (editText[i].getText().toString().toLowerCase().equals(physicArrayList.get(i).getSA().toLowerCase())) {
                                                    final_score++;
                                                }

                                            }
                                        }

                                        //Gets the instance of the LayoutInflater, uses the context of this activity
                                        LayoutInflater inflater = (LayoutInflater) TestPhysicActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
                                        View layout = inflater.inflate(R.layout.popup, null);
                                        if (final_score <= 3) {
                                            ((TextView) layout.findViewById(R.id.popup)).setText(poor);
                                        } else if (final_score >= 4 && final_score <= 9) {
                                            ((TextView) layout.findViewById(R.id.popup)).setText(doBetter);
                                        } else {
                                            ((TextView) layout.findViewById(R.id.popup)).setText(congrats);
                                        }



                                        //Get the devices screen density to calculate correct pixel sizes
                                        float density = TestPhysicActivity.this.getResources().getDisplayMetrics().density;
                                        // create a focusable PopupWindow with the given layout and correct size
                                        final PopupWindow pw = new PopupWindow(layout, (int) density * 350, (int) density * 400, true);
                                        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        pw.setTouchInterceptor(new View.OnTouchListener() {
                                            public boolean onTouch(View v, MotionEvent event) {
                                                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                                    pw.dismiss();
                                                    return true;
                                                }
                                                return false;
                                            }
                                        });
                                        pw.setOutsideTouchable(true);
                                        // display the pop-up in the center
                                        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                                        Context context = getApplicationContext();
                                        CharSequence text = "Your Score: " + final_score;
                                        int duration = Toast.LENGTH_LONG;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                                    }

                                }
        );

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didSubmitButton = false;
                final_score = 0;
                for(int i = 0 ; i < radioGroup.length;i++){
                    if(!(radioGroup[i] == null)) {
                        radioGroup[i].clearCheck();
                    }
                }
                for(int i = 0 ; i < editText.length;i++){
                    if(!(editText[i] == null)) {
                        editText[i].setText("");
                    }
                }
                Toast.makeText(TestPhysicActivity.this,
                        "Let do it again, shall we?", Toast.LENGTH_SHORT).show();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey! Love Quizzes? My highscore is " + final_score + " on Physics\nCan you beat me?\nDownload the app from https://twitter.com/Swapnilsrkr";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }
}