package com.example.shapegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.shapegame.shape.Circle;
import com.example.shapegame.shape.Shape;
import com.example.shapegame.shape.Square;

import java.util.LinkedList;
import java.util.Random;


public class GameView extends View {
    private Paint paint;
    private int canvasHeight, canvasWidth, count, numPop;
    private Handler h;
    private int frameRate;
    private TextView score, timer;
    private LinkedList<Shape> shapeList;
    private Runnable gameRunner;
    private Thread thread;
    private boolean finish = false;



    public GameView(Context context) {
        super(context);
        init(context);

    }

    public GameView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        thread = new Thread(){
            @Override
            public void run() {
               while(!isInterrupted()){

                   try {
                       Thread.sleep(1000);
                       if(score.getText().toString().equals("0")){
                           break;
                       }
                       ((Activity) context).runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               String[] time = timer.getText().toString().split(":");
                               int second = Integer.parseInt(time[1]);
                               int minute = Integer.parseInt(time[0]);
                               if(second == 59){
                                   second = 0;
                                   minute = minute + 1;
                               }else{
                                   second = second + 1;
                               }
                               String minTemp = minute<10? "0"+minute: minute+"";
                               String temp = second<10? "0"+second: second+"";
                               timer.setText(minTemp+":"+temp);
                           }
                       });
                   }
                   catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }
            }
        };
        thread.start();
        startTimer();
    }

    public void init(Context context){
        h = new Handler();
        int frameRate = 1;
        shapeList = new LinkedList<>();

    }

    public void setTextViews(TextView score, TextView timer){
        this.score = score;
        this.timer = timer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(count > 1){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                Shape touchedShape = null;
                for(Shape shape: shapeList){
                    float x = shape.getX();
                    float y = shape.getY();
                    float size = shape.getSize();
                    float eventX = event.getX();
                    float eventY = event.getY();
                    if (x < event.getX() && event.getX() < x + size && y > event.getY() && event.getY() > y - size)
                    {
                        touchedShape = shape;
                        break;
                    }
                }

                if(touchedShape != null){
                    numPop += 1;
                    int color = Color.RED;
                    boolean circle = (touchedShape instanceof Circle);
                    if(touchedShape.getColor() == Color.RED && touchedShape instanceof Circle){
                        String temp = score.getText().toString();
                        if(temp.equals("9")){
                            score.setText("0");
                            count = 0;
                            finish = true;
                        }else{
                            int scoreNum = Integer.parseInt(temp) - 1;
                            temp = scoreNum +"";
                            score.setText(temp);
                        }
                    }
                    shapeList.remove(touchedShape);
                    addShape();
                }
            }

        }
        return super.onTouchEvent(event);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Random rand = new Random();
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        //if no shape on the view, initial all the shapes
        if(count == 0 && finish){
            shapeList.clear();
            Paint paint = new Paint();
            paint.setTextSize(60);
            String result = "Time cost: " + timer.getText().toString();
            String numPoped = "Number of shape clicked: "+ numPop;
            paint.setColor(Color.WHITE);
            float x = canvasWidth / 4;
            float y = canvasHeight / 2;
            canvas.drawText(result, x, y, paint);
            canvas.drawText(numPoped, x, y+60, paint);
        }

        if(count == 0 && !finish){
            count = rand.nextInt(12-6)+6;
            for(int i = 0; i < count; i++){
                addShape();
            }
        }
        int difference = count - shapeList.size();
        for(int i = 0; i < difference; i++){
            addShape();
        }
        //ensure not over bound and draw the shape
        for(Shape shape: shapeList){
            shape.drawShape(canvas);
        }

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        },10);

    }

    public boolean overLap(Shape newShape){
        for(Shape shape: shapeList){
            if(shape.rectf.intersect(newShape.rectf)){
                return true;
            }
        }
        return false;
    }



    public void fixBound(Shape shape){
            float x = shape.getX();
            float y = shape.getY();
            float size = (float) (shape.getSize() * 1.2);
            if(y - size< 0){
                shape.setY(y + size);
            }
            else if(y + size> canvasHeight){
                shape.setY(y - size);
            }
            if(x - size < 0){
                shape.setX(x + size);
            }else if(x + size > canvasWidth){
                shape.setX(x - size);
            }

    }
    public void addShape(){
        Random rand = new Random();
        if(rand.nextBoolean()){
            Circle circle = new Circle(getContext(), canvasWidth, canvasHeight);
            fixBound(circle);
            if(shapeList.size()>0){
                while(overLap(circle)){
                    circle = new Circle(getContext(), canvasWidth, canvasHeight);
                    fixBound(circle);
                }
            }
            long time = (long)circle.time;
            shapeList.add(circle);


        }else{
            Square square = new Square(getContext(), canvasWidth, canvasHeight);
            fixBound(square);
            if(shapeList.size()>0) {
                while (overLap(square)) {
                    square = new Square(getContext(), canvasWidth, canvasHeight);
                    fixBound(square);
                }
            }
            shapeList.add(square);

        }
    }
    public void startTimer(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    while(!finish){
                        int time = new Random().nextInt(7000 - 5000) + 5000;
                        Thread.sleep(time);
                        if(shapeList.size()>0 && !finish) {
                            shapeList.clear();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }


}
