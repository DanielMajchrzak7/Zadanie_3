package com.example.zadanie_3;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static public SensorManager mSensorManager;
    public Sensor mSensor;
    public long lastUpdate = -1;
    private ObjectAnimator startAnimator;
    private ObjectAnimator Animator;
    private AnimatorSet move;
    float start;
    float left;
    float right;
    ImageView ball;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView answer = findViewById(R.id.odpowiedz);
        answer.setVisibility(View.INVISIBLE);
        ball = findViewById(R.id.imageView);

        if(MainActivity.mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        start = ball.getX();
        left = start - 30;
        right = start + 30;

        startAnimator = ObjectAnimator.ofFloat(ball,"translationX",start,left); //wykonanie ruchu od srodka na lewo
        startAnimator.setInterpolator(new LinearInterpolator());
        startAnimator.setDuration(50);

        Animator = ObjectAnimator.ofFloat(ball,"translationX",left,right);
        Animator.setInterpolator(new LinearInterpolator());
        Animator.setDuration(80);
        Animator.setRepeatCount(ValueAnimator.INFINITE);
        Animator.setRepeatCount(ValueAnimator.REVERSE);

        move = new AnimatorSet();
        move.play(startAnimator).before(Animator);

    }


    public void onSensorChanged(SensorEvent event){

    TextView answer = findViewById(R.id.odpowiedz);
    ImageView ball = findViewById(R.id.imageView);
    long timeus;

    if(lastUpdate == -1) // na samym początku, gdy apk nie była włączona
    {
        lastUpdate = event.timestamp;
        timeus = 0;
    }

    else{
        timeus = (event.timestamp - lastUpdate)/1000L;
        lastUpdate = event.timestamp;
    }

    if(event.values[0]!=0) // jesl cos zczytal, wynik w cm
    {
    move.cancel();
    ball.setTranslationX(start);
    ball.setImageResource(R.drawable.hw3ball_empty);
    answer.setText(setResult(timeus));
    answer.setVisibility(View.VISIBLE);

    }

    else{ // to się wykona po wlaczeniu aplikacji kiedy nie mam nic zczytane z czujnika
        answer.setVisibility(View.INVISIBLE);
        ball.setImageResource(R.drawable.hw3ball_front);
        move.start();
         // stworzyc metode robiaca animacje
    }
}

    protected void onResume(){
        super.onResume();
        if(mSensor != null)
            mSensorManager.registerListener(this, mSensor, 100000);
    }

    protected void onPause(){
        super.onPause();
        if(mSensor != null)
            mSensorManager.unregisterListener(this, mSensor);
    }

    private String setResult(long time){
    String[] tabAns = getResources().getStringArray(R.array.odpowiedzi);
    String ans;
    int size = tabAns.length;

        if ((double) time <=3){
            Random rand = new Random();
            int n = rand.nextInt(size/2);
            ans = tabAns[n];
        }

        else {
            Random rand = new Random();
            int n = (size/2) + rand.nextInt(size/2);
            ans = tabAns[n];
        }
        return ans;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

