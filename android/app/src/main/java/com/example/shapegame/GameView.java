/******************************************************************************
 * This is the custom view that using on draw method to draw random shapes and
 * allow user click it to play game. it will display 6-12 random number of shapes
 * with different color and refresh every 3-7 seconds.
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    //declare all global variables and objects that other function will use
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

    //constructor
    public GameView(Context context) {
        super(context);
        init(context);

    }
    //constructor that run thread to display the time that user played
    public GameView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        //thread that update time text view every second
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
        //timer that using for check each shape life time
        startTimer();
    }

    public void init(Context context){
        //create handler and generate shape arraylist
        h = new Handler();
        frameRate = 10;
        shapeList = new LinkedList<>();
    }
    //function that set the score and timer text view
    public void setTextViews(TextView score, TextView timer){
        this.score = score;
        this.timer = timer;
    }
    //function that set the correct color and shape
    public void setCorrect(String correctColor, String correctShape){
        String correct= correctColor;
        isCircle = correctShape.equals("Circle")? true: false;
        //using switch that set the color from string to int
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
    //function that check only when user touch screen to maintaining the shape
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //check the touchevent only when there is at least one shape
        if(count > 1){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                //create a shape object that store the touched shape
                Shape touchedShape = null;
                //check the touched position has a shape or not
                //if there is a shape, store it into touchedShape
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
                //if user touched position is a shape, ensure it is the required shape and color
                if(touchedShape != null){
                    if(isCircle){
                        //if it is correct shape, update remaining text view to let user know how many shape left
                        if(touchedShape.getColor() == colorInt && touchedShape instanceof Circle){
                            finalTimeCount += timeCount - touchedShape.getTime();
                            String temp = score.getText().toString();
                            //if it is the last shape, update finaltime and end the game
                            if(temp.equals("1")){
                                result = toTime(finalTimeCount);
                                score.setText("0");
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
                        //if it is correct shape, update remaining text view to let user know how many shape left
                        if(touchedShape.getColor() == colorInt && touchedShape instanceof Square){
                            finalTimeCount += timeCount - touchedShape.getTime();
                            String temp = score.getText().toString();
                            //if it is the last shape, update finaltime and end the game
                            if(temp.equals("1")){
                                result = toTime(finalTimeCount);
                                score.setText("0");
                                count = 0;
                                finish = true;
                            }else{
                                int scoreNum = Integer.parseInt(temp) - 1;
                                temp = scoreNum +"";
                                score.setText(temp);
                            }
                        }
                    }
                    //remove the touched shape form ArrayList and add new shape
                    shapeList.remove(touchedShape);
                    addShape();
                }
            }

        }
        return super.onTouchEvent(event);
    }


    //function that override ondraw method that draw shape and text
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Random rand = new Random();
        //get screen width and height
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        //start when game finished, draw the result to custom view
        if(count == 0 && finish){
            shapeList.clear();
            Paint paint = new Paint();
            paint.setTextSize(60);
            String temp = "Time cost: " + result;
            String numPoped = "Correct shape not clicked: "+ numPop;
            paint.setColor(Color.WHITE);
            float x = canvasWidth / 4;
            float y = canvasHeight / 2;
            canvas.drawText(temp, x, y, paint);
            canvas.drawText(numPoped, x, y+60, paint);
        }
        //if no shape on the view, initial all the shapes
        if(count == 0 && !finish){
            count = rand.nextInt(12-6)+6;
            for(int i = 0; i < count; i++){
                addShape();
            }
        }
        //redraw the shapes after refresh page
        int difference = count - shapeList.size();
        for(int i = 0; i < difference; i++){
            addShape();
        }
        //ensure not over bound and draw the shape
        for(Shape shape: shapeList){
            shape.drawShape(canvas);
        }
        //refresh the page and draw the shapes
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        },10);

    }
    //function that ensure the shapes not overlap
    public boolean overLap(Shape newShape){
        for(Shape shape: shapeList){
            if(shape.rectf.intersect(newShape.rectf)){
                return true;
            }
        }
        return false;
    }


    //if the new shape is over bound, fix the bound
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
    //function that add new shapes to custom view
    public void addShape(){
        Random rand = new Random();
        //create a circle shape
        if(rand.nextBoolean()){
            Circle circle = new Circle(getContext(), canvasWidth, canvasHeight);
            fixBound(circle);
            if(shapeList.size()>0){
                while(overLap(circle)){
                    circle = new Circle(getContext(), canvasWidth, canvasHeight);
                    fixBound(circle);
                }
            }
            //ste create time for this object
            circle.setTime(timeCount);
            shapeList.add(circle);
        }else{
            //create a square shape
            Square square = new Square(getContext(), canvasWidth, canvasHeight);
            fixBound(square);
            if(shapeList.size()>0) {
                while (overLap(square)) {
                    square = new Square(getContext(), canvasWidth, canvasHeight);
                    fixBound(square);
                }
            }
            //set create time for the shape
            square.setTime(timeCount);
            shapeList.add(square);

        }
    }
    //function that keep refresh the page after every 3 - 7 seconds
    public void startTimer(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(!finish){
                        //generate a random time
                        int time = new Random().nextInt(7000 - 3000) + 3000;
                        //wait for this time long
                        Thread.sleep(time);
                        //if game not finish and shape size greater that 0, check how many correct shape left and add the time to total time
                        if(shapeList.size()>0 && !finish) {
                            for(Shape shape: shapeList){
                                if(isCircle){
                                    if(shape.getColor() == colorInt && shape instanceof Circle){
                                        finalTimeCount += timeCount - shape.getTime();
                                        numPop += 1;
                                    }
                                }
                                else {
                                    if(shape.getColor() == colorInt && shape instanceof Square){
                                        finalTimeCount += timeCount - shape.getTime();
                                        numPop += 1;
                                    }
                                }
                            }
                            //clear the shape list
                            shapeList.clear();
                            count = new Random().nextInt(12-6)+6;
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }
    //function return final time
    public String getTime(){
        return result;
    }
    //function that convert time int to string that display and store in add activity
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
