/******************************************************************************
 * This class is the class extend by abstract class shape and implement drawshpe
 * function
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame.shape;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Square extends Shape {
    //declare paint
    Paint paint;
    //constructor that store context, screen width, and height
    public Square(Context context, float Width, float Height){
        super(context, Width, Height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    //function that draw a shape
    public void drawShape(Canvas canvas) {
        rectf = new RectF(x ,y-size ,x+size,y);
        paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectf, paint);
    }
}
