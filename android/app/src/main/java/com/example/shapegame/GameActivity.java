/******************************************************************************
 * This activity will allow user play reflection game and add score when score
 * is a high score or go back to main activity if user click back
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class GameActivity extends AppCompatActivity {
    //declare all used text view objects and intent
    private TextView score;
    private GameView gameView;
    private TextView time;
    private Button start;
    private Button clear;
    private Button add;
    Intent intent;
    //declare variables store finaltime, arraylist, and dir
    private String finalTime;
    private File dir;
    private ArrayList<DTO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //initial all button and text
        Intent getIntent = getIntent();
        //get generated color and shape from main activity
        String color = getIntent.getStringExtra("color");
        String shape = getIntent.getStringExtra("shape");
        TextView gameTitle = findViewById(R.id.gameTitle);
        String display = "please touch "+ color +" " + shape;
        gameTitle.setText(display);
        dir = (File) getIntent.getExtras().get("dir");
        IO io = new IO();
        try {
            list = io.IORead(dir);
            //sort only happens when list size greater than 1
            if(list.size() > 2){
                //override compare function
                Collections.sort(list, new Comparator<DTO>() {
                    @Override
                    public int compare(DTO o1, DTO o2) {
                        if(o1.getScore().compareTo(o2.getScore()) != 0){
                            return o1.getScore().compareTo(o2.getScore());
                        }else{
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    }
                });

            }
            //remove all the item if list size greater than 12
            if(list.size()>12){
                for(int i = list.size() - 12; i > 0; i--){
                    list.remove(list.size()-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set all text views
        score = findViewById(R.id.score);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        clear = findViewById(R.id.clear);
        add = findViewById(R.id.Add);
        //set game view
        gameView = findViewById(R.id.gameView);
        //set the generated color, shape , score text view and time text view to custome view
        gameView.setTextViews(score, time);
        gameView.setCorrect(color, shape);
        //add text change listener that maintaining the game end
        score.addTextChangedListener(scoreWatcher);
        //create intent to navigate to add score activity
        intent = new Intent(this, AddActivity.class);
        //back button that go back main activity
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });
        //add button that go to addScore activity
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finalTime = gameView.getTime();
                intent.putExtra("time", finalTime);
                intent.putExtra("dir", dir);
                startActivityForResult(intent, 1, null);
            }
        });
    }
    //function that maintaining the text chage for text view
    private TextWatcher scoreWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //enable the button only game end and score is a high score
            String scoreText = score.getText().toString();

            if(scoreText.equals("0")){
                if(list.size()>11 && isHeighScore()){
                    add.setEnabled(true);
                }else if(list.size() < 12){
                    add.setEnabled(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //function that check ths score is a high score by check the last element and new data
    private boolean isHeighScore(){
        DTO last = list.get(list.size()-1);//get the item of last list
        finalTime = gameView.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdformat = new SimpleDateFormat("mm/dd/yyyy");//set date format
        String lastDate = last.getDate();//get the date for last record
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        String lastScore = last.getScore();
        if(lastScore.compareTo(finalTime)>0) return true;//return true if new score higher
            //if score are same, check the date
        else{
            if(lastScore.equals(finalTime) && lastDate.compareTo(formattedDate) < 0){
                return true;
            }
        }
        //return false if not match
        return false;

    }


    //function that check the result when add score activity finish
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //disable add button
                add.setEnabled(false);
                //display text that let user knwo add score success
                Toast.makeText(getApplicationContext(), "Add Success", Toast.LENGTH_LONG).show();
            }
        }
    }
}
