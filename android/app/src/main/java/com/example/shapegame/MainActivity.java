package com.example.shapegame;

import androidx.appcompat.app.AppCompatActivity;
import com.example.shapegame.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button start, showScore;
    private String shape;
    private String color;
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateGoal();
        dir = new File(this.getFilesDir(), "score");
        String intro = "This is a game that test human's reaction time. You need touch only the "+this.color+" " +this.shape+". \nThe game ends after 10 correct shapes have been touched. the rank based on the time, so shorter time you user, better rank you have";
        TextView introText = findViewById(R.id.introduction);
        introText.setText(intro);
        //start game when click
        start = (Button) findViewById(R.id.startBut);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });
        //show score when click
        showScore = findViewById(R.id.scoreBut);
        showScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("dir", dir);
                startActivity(intent);
            }
        });
    }

    public void openGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("dir", dir);
        intent.putExtra("color", color);
        intent.putExtra("shape", shape);
        startActivity(intent);
    }

    public void generateGoal(){
        Random rand = new Random();
        int colorIndex = rand.nextInt(6);
        switch(colorIndex){
            case 0:
                color = "Red";
                break;
            case 1:
                color = "Orange";
                break;
            case 2:
                color = "Yellow";
                break;
            case 3:
                color = "Green";
                break;
            case 4:
                color = "Blue";
                break;
            case 5:
                color = "Purple";
                break;
            case 6:
                color = "White";
                break;
        }
        shape = rand.nextBoolean()? "Circle": "Square";
    }



}
