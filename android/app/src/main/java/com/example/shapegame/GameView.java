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
    private int canvasHeight, canvasWidth, count, numPop, timeCount, finalTimeCount;
    private Handler h;
    private int frameRate, colorInt;
    private TextView score, timer;
    private LinkedList<Shape> shapeList;
    private Runnable gameRunner;
    private Thread thread;
    private boolean finish = false;
    private boolean isCircle;
    private String result;


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
                       timeCount ++;
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

    public void setCorrect(String correctColor, String correctShape){
        String correct= correctColor;
        isCircle = correctShape.equals("Circle")? true: false;
        switch(correct){
            case "Red":
                colorInt = Color.RED;
                break;
            case "Orange":
                colorInt = Color.rgb(255, 165, 0);
                break;
            case "Yellow":
                colorInt = Color.YELLOW;
                break;
            case "Green":
                colorInt = Color.GREEN;
                break;
            case "Blue":
                colorInt =  Color.BLUE;
                break;
            case "Purple":
                colorInt = Color.rgb(128, 0, 128);
                break;
            case "White":
                colorInt = Color.WHITE;
                break;
        }
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
                    if(isCircle){
                        if(touchedShape.getColor() == colorInt && touchedShape instanceof Circle){
                            finalTimeCount += timeCount - touchedShape.getTime();
                            String temp = score.getText().toString();
                            if(temp.equals("9")){
                                score.setText("0");
                                result = toTime(finalTimeCount);
                                count = 0;
                                finish = true;
                            }else{
                                int scoreNum = Integer.parseInt(temp) - 1;
                                temp = scoreNum +"";
                                score.setText(temp);
                            }
                        }
                    }
                    else {
                        if(touchedShape.getColor() == colorInt && touchedShape instanceof Square){
                            finalTimeCount += timeCount - touchedShape.getTime();
                            String temp = score.getText().toString();
                            if(temp.equals("9")){
                                score.setText("0");
                                result = toTime(finalTimeCount);
                                count = 0;
                                finish = true;
                            }else{
                                int scoreNum = Integer.parseInt(temp) - 1;
                                temp = scoreNum +"";
                                score.setText(temp);
                            }
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
            String temp = "Time cost: " + result;
            String numPoped = "Number of shape clicked: "+ numPop;
            paint.setColor(Color.WHITE);
            float x = canvasWidth / 4;
            float y = canvasHeight / 2;
            canvas.drawText(temp, x, y, paint);
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
            circle.setTime(timeCount);
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
            square.setTime(timeCount);
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
                        int time = new Random().nextInt(7000 - 3000) + 3000;
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
    public String getTime(){
        return result;
    }
    public String toTime(int inputTime){
        String minute, second;
        if(inputTime> 60){
            int min = inputTime % 60;
            minute = min < 10? "0"+min: min+"";
            inputTime /= 60;
            second = inputTime < 10? "0" + inputTime: inputTime+"";
        }else{
            minute = "00";
            second = inputTime<10? "0" + inputTime: inputTime+"";
        }
        String temp = minute+":"+second;
        return temp;
    }

}
