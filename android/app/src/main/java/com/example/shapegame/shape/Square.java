package com.example.shapegame.shape;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Square extends Shape {
    Paint paint;
    public Square(Context context, float Width, float Height){
        super(context, Width, Height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void drawShape(Canvas canvas) {
        float x = super.x;
        float y = super.y;
        float size = super.size;
        rectf = new RectF(x ,y-size ,x+size,y);
        paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectf, paint);
    }
}
