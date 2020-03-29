package com.example.shapegame;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //initial all button and text
        score = findViewById(R.id.score);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        clear = findViewById(R.id.clear);
        add = findViewById(R.id.Add);
        gameView = findViewById(R.id.gameView);
        gameView.setTextViews(score, time);
        score.addTextChangedListener(scoreWatcher);
        intent = new Intent(this, AddActivity.class);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
                String timeText = time.getText().toString();
                intent.putExtra("time", timeText);
                add.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
