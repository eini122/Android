/******************************************************************************
 * This activity will allow user add their score to the top 12 list
 * it will go back game activity once user click save
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/

package com.example.shapegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private Button backBtn, saveBtn;//declare variables for button
    private EditText nameInput, scoreInput, dateInput;//declare variables for editText
    private ImageView dateIcon;//declare variable for ImageView with icon
    private String name, score, date;//declare variable for store name, score, and date
    //declare variables dir for store path, object io and list to store DTO
    private File dir;
    private IO io;
    private ArrayList<DTO> list = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();//get Calendar instance
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //get path from game activity
        dir = (File) getIntent().getExtras().get("dir");
        //generate io object
        io = new IO();
        try {
            list = io.IORead(dir);
            //sort only happens when list size greater than 2
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
            if(list.size()>11){
                for(int i = list.size() - 11; i > 0; i--){
                    list.remove(list.size()-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get all component connected
        nameInput = findViewById(R.id.nameInput);
        scoreInput = findViewById(R.id.scoreInput);
        dateInput = findViewById(R.id.dateInput);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);
        intent = getIntent();
        String finalTime = intent.getStringExtra("time");
        scoreInput.setText(finalTime);
        //user decide not add new score, go back to main page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return with no variable
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        //save button that check the new record, if record is available for list
        //add to list and write to text file
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save click function that check information and set into text file
                try {
                    saveClick();
                    Intent backIntent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        //add listener to ensure all editedtext are not empty
        nameInput.addTextChangedListener(saveWatcher);
        scoreInput.addTextChangedListener(saveWatcher);
        dateInput.addTextChangedListener(saveWatcher);
        dateInput.setText(getCurrentDate());//set date input with current date
    }

    //function that check the record is available to store into list
    private void saveClick() throws ParseException {
        //get the date from edited text
        name = nameInput.getText().toString();
        score = scoreInput.getText().toString();
        date = dateInput.getText().toString();
        DTO result = new DTO();
        //create new object contains new record
        result.setName(name);
        result.setScore(score);
        result.setDate(date);
        list.add(result);
        //write list into txt file
        try{
            io.IOWrite(dir, list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //function that check when text change to enable save button
    private TextWatcher saveWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //enable the save button only all three fields are not empty
            name = nameInput.getText().toString().trim();
            score = scoreInput.getText().toString().trim();
            date = dateInput.getText().toString().trim();
            saveBtn.setEnabled(!name.isEmpty() && !score.isEmpty() && !date.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //get the current date by using calendar
    public String getCurrentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = simpleDateFormat.format(c);

        return formattedDate;
    }

}
