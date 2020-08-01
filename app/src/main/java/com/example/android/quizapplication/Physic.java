package com.example.android.quizapplication;

import java.util.ArrayList;

public class Physic {
    //Fields for Contact class
    //private int id;
    private String question;
    private ArrayList<String> as;
    private String sa;
    private Boolean mul;


    public Physic(String question, ArrayList<String> as, String sa, Boolean mul){
       this.question = question;
       this.as = as;
       this.sa = sa;
       this.mul = mul;
    }

    public String getQuestion() {
        return question;
    }
    public ArrayList<String> getAS() {
        return as;
    }
    public String getSA() {return sa;}
    public Boolean getMul() {return mul;}

}