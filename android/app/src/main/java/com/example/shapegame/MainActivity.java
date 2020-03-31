/******************************************************************************
 * This is the main activity that introduce the game rule and allow user click
 * button to eight start the game or show the score.
 *
 * In this activity, it also will generate a random color and shape that require
 * user to click in game activity
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/

package com.example.shapegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        //method to generate a random color and shape
        generateGoal();
        //get file path for directory
        dir = new File(this.getFilesDir(), "score");

        //create new string that introduce the game rule with generated color and shape and set it into textview
        String intro = "This is a game that test human's reaction time. You need touch only the "+this.color+" " +this.shape+". \n\nThe game ends after 10 correct shapes have been touched. the rank based on the time that you touched the correct shape plus the correct shape does not been touched, so shorter time you user, better rank you have";
        TextView introText = findViewById(R.id.introduction);
        introText.setText(intro);
        //Button to start the game
        start = (Button) findViewById(R.id.startBut);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });
        //Button move to score activity
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
    //function that put the generated shape and color to intent and pass to game activity
    public void openGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("dir", dir);
        intent.putExtra("color", color);
        intent.putExtra("shape", shape);
        startActivity(intent);
    }
    //function that generate a random color and shape
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
