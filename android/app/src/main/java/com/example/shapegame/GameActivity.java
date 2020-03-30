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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class GameActivity extends AppCompatActivity {
//    Random r = new Random();
//    int circleNum = r.nextInt(6) + 6;
    private TextView score;
    private GameView gameView;
    private TextView time;
    private Button start;
    private Button clear;
    private Button add;
    Intent intent;
    private String finalTime;
    private File dir;
    private ArrayList<DTO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //initial all button and text
        Intent getIntent = getIntent();
        String color = getIntent.getStringExtra("color");
        String shape = getIntent.getStringExtra("shape");
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
                            return o2.getScore().compareTo(o1.getScore());
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
        score = findViewById(R.id.score);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        clear = findViewById(R.id.clear);
        add = findViewById(R.id.Add);
        //set game view
        gameView = findViewById(R.id.gameView);
        gameView.setTextViews(score, time);
        gameView.setCorrect(color, shape);
        score.addTextChangedListener(scoreWatcher);
        intent = new Intent(this, AddActivity.class);
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });
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
    private TextWatcher scoreWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //enable the save button only all three fields are not empty
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

    private boolean isHeighScore(){
        DTO last = list.get(list.size()-1);//get the item of last list
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdformat = new SimpleDateFormat("mm/dd/yyyy");//set date format
        String lastDate = last.getDate();//get the date for last record
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        String lastScore = last.getScore();
        if(lastScore.compareTo(finalTime)<0) return true;//return true if new score higher
            //if score are same, check the date
        else{
            if(lastScore.equals(finalTime) && lastDate.compareTo(formattedDate) < 0){
                return true;
            }
        }
        //return false if not match
        return false;

    }

}
