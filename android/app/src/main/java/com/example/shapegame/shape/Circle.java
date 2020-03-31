/******************************************************************************
 * This class is the class extend by abstract class shape and implement drawShpe
 * function
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public class Circle extends Shape {
    //declare paint
    Paint paint;
    //constructor that store context width, height of screen
    public Circle(Context context, float Width, float Height){
        super(context, Width, Height);
    }
    @Override
    public void drawShape(Canvas canvas) {
        //set the x, y, and size
        float x = super.x;
        float y = super.y;
        float size = super.size;
        //declare new size by set a r
        float newSize = (float) (Math.sqrt(2) * size);
        rectf = new RectF( (x + size /2) - newSize, (y + size /2)- newSize, (x + size /2) + newSize, (y + size /2)+ newSize);
        //rectf = new RectF(x ,y-size ,x+size,y);
        paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(x + size /2,y - size/2, (float) (size / 2.0), paint );
    }
}
