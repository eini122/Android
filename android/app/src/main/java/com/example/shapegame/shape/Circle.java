package com.example.shapegame.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public class Circle extends Shape {
    Paint paint;
    public Circle(Context context, float Width, float Height){
        super(context, Width, Height);
    }
    @Override
    public void drawShape(Canvas canvas) {
        float x = super.x;
        float y = super.y;
        float size = super.size;
        float newSize = (float) (Math.sqrt(2) * size);
        super.rectf = new RectF( x - newSize, y - newSize, x + newSize, y + newSize);
        paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(x + size /2,y - size/2, (float) (size / 2.0), paint );
    }
}
