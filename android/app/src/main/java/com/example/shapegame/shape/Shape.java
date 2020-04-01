/******************************************************************************
 * This class is the abstract class for declare shape
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/

package com.example.shapegame.shape;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.shapegame.ShapeColor;

import java.util.Random;

public abstract class Shape {
    int[] center = new int[2];
    int color;
    public float x, y, size;
    public int time;
    public int liftTime;
    public int totalTime;
    public RectF rectf;

    public Shape(Context context, float width, float height){
        float randSize = new Random().nextInt((64 - 32) + 1) + 32;
        this.size = randSize * context.getResources().getDisplayMetrics().density + 0.5f;
        this.color = ShapeColor.getShapeColor();
        this.x = new Random().nextInt((int) (width - this.size));
        this.y = new Random().nextInt((int) (height - this.size));
        center[0] = (int) (this.x + size/2);
        center[1] = (int) (this.y - size/2);
        rectf = new RectF(x ,y-size ,x+size,y);
    }
    public int getColor(){
        return this.color;
    }
    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getSize(){
        return this.size;
    }

    public void setX(float x){
        this.x = x;
        center[0] = (int) (this.x + size/2);
    }
    public void setY(float y){
        this.y = y;
        center[1] = (int) (this.y - size/2);
    }
    public void setTime(int time){
        this.time = time;
    }
    public int getTime(){
        return this.time;
    }
    public void setLiftTime(int liftTime){
        this.liftTime = liftTime;
    }
    public int getLiftTime(){
        return this.liftTime;
    }
    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }
    public int[] getCenter(){
        return this.center;
    }


    public abstract void drawShape(Canvas canvas);

}
