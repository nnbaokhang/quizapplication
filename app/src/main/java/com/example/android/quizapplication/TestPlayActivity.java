package com.example.android.quizapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class TestPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        CardView phy = findViewById(R.id.phy_card);
        // Set a click listener on that View
        phy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent phyIntent = new Intent(PlayActivity.this, PhysicsActivity.class);
                Intent phyIntent = new Intent(TestPlayActivity.this, TestPhysicActivity.class);
                startActivity(phyIntent);
            }
        });

    }
}