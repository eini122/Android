/******************************************************************************
 * This class the data to object class that store user name, score and data
 * created
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame;


import java.io.Serializable;

//Object class that contains player name, score, and date
public class DTO implements Serializable {
    private String name;
    private String score;
    private String date;
    //empty constructor
    public DTO(){}
    //constructor with date
    public DTO(String name, String score, String date){
        this.name = name;
        this.score = score;
        this.date = date;
    }
    /* setter and getters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
