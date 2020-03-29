package com.example.shapegame;

import android.graphics.Color;

import java.util.Random;

public class ShapeColor {
    private static final int[] shapeColor = {Color.RED, Color.rgb(255, 165, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(128, 0, 128), Color.WHITE};
    /*
     * Generate a random color
     * */
    public static int getShapeColor(){
        Random rand = new Random();
        int randColorIndex = rand.nextInt(shapeColor.length);
        return shapeColor[randColorIndex];
    }

    public ShapeColor(){};

}
