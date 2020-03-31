/******************************************************************************
 * This activity will display the score order by time, It will using io that read
 * from the txt file and sort it. Then using score adapter to store the data
 * line by line
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
import android.widget.ListView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ScoreActivity extends AppCompatActivity {
    //declare variables
    private Button backBtn;
    private ListView mListView;
    private ScoreListAdapter adapter;
    private ArrayList<DTO> list;
    private File dir;
    private IO io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        //get the path from main activity
        dir = (File) getIntent().getExtras().get("dir");
        io = new IO();
        try {
            list = io.IORead(dir);
            //sort only happens when list size greater than 1
            if(list.size() > 1){
                //override compare function
                Collections.sort(list, new Comparator<DTO>() {
                    @Override
                    public int compare(DTO o1, DTO o2) {
                        if(o1.getScore().compareTo(o2.getScore()) != 0){
                            int res = o2.getScore().compareTo(o1.getScore());
                            int res2 = o1.getScore().compareTo(o2.getScore());
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

        //create adapter that contains the input list
        adapter = new ScoreListAdapter(this, R.layout.adapter_view_layout, list);
        mListView = findViewById(R.id.listView);//connect list view
        mListView.setAdapter(adapter);//set list view with adapter
        //set backButton
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
