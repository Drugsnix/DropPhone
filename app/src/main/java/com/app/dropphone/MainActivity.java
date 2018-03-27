package com.app.dropphone;

import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity
{
    //Fields
    private SensorManager sensorManager;
    //Gyroscope         https://www.youtube.com/watch?v=8Veyw4e1MX0
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    //Accelerometer      https://www.youtube.com/watch?v=YrI2pCZC8cc
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerListener;
    private boolean dropped = false;
    //Sound+ buttons
    private MediaPlayer mediaPlayer, mSound1,mSound2,mSound3,mSound4;
    private Button sound1,sound2,sound3,sound4;
    //Runs onCreate when app is started up from 100% closed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the sensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //instantiate the gyroscope
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //instantiate the accelerometer
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //instantiate media player's
        mediaPlayer = MediaPlayer.create(this,R.raw.wilhelmscream); //default
        mSound1 = MediaPlayer.create(this,R.raw.scream);
        mSound2 = MediaPlayer.create(this,R.raw.malescream);
        mSound3 = MediaPlayer.create(this,R.raw.tarzanscream);
        mSound4 = MediaPlayer.create(this,R.raw.wilhelmscream);
        //instantiates the buttons path
        sound1 = findViewById(R.id.Sound1);
        sound2 = findViewById(R.id.Sound2);
        sound3 = findViewById(R.id.Sound3);
        sound4 = findViewById(R.id.Sound4);


        //Checks if phone got a gyroscope
        if (gyroscopeSensor == null) {
            Toast.makeText(this, "The device has no Gyroscope!", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Checks if phone got an accelerometer
        if (accelerometerSensor == null) {
            Toast.makeText(this, "The device has no accelerometer!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //instantiates gyroscopeListener
        gyroscopeEventListener = new SensorEventListener() {
            //THIS BODY IS RUN ON REPEAT BY gyroscopeEventListener

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[2] > 7) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (sensorEvent.values[2] < -7) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }; //gyroscopeListener body end

        //instantiates accelerometerListener
        accelerometerListener = new SensorEventListener() {
            float xOld, yOld, zOld;
            float xTmp,yTmp,zTmp;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
               //Measures to the tmp fields from the accelerometer
               xTmp = sensorEvent.values[0];
               yTmp = sensorEvent.values[1];
               zTmp = sensorEvent.values[2];
                //Checks if x or y is 19 much higher = a drop
                if(xTmp > xOld + 19||yTmp > yOld + 19)
                {
                    //Calls the onDrop Method that plays the sound
                   onDrop();
                }
                //Sets the new old fields
                xOld = xTmp;
                yOld = yTmp;
                zOld = zTmp;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };//accelerometerListener body end

        sound1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#FF0000"));
                mediaPlayer = mSound1;
                mediaPlayer.start();
            }
        }); //Sound1 body ends

        sound2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer = mSound2;
                mediaPlayer.start();
            }
        });//Sound1 body ends

        sound3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer = mSound3;
                mediaPlayer.start();
            }
        });//Sound1 body ends

        sound4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer = mSound4;
                mediaPlayer.start();
            }
        });//Sound1 body ends

    }//onCreate body end

    //Runs when app is re-opened from pause state
    @Override
    protected void onResume()
    {
        super.onResume();
        //Re-registrers the gyroscopeListener
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //Re-registrers the accelerometerListener
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    //Runs when app is semi closed (aka paused)
    @Override
    protected void onPause()
    {
        super.onPause();
        //unregistrers the gyroscopeListener
        sensorManager.unregisterListener(gyroscopeEventListener);
        //unregistrers the accelerometerListener
        sensorManager.unregisterListener(accelerometerListener);
    }

    public void onDrop()
    {
        mediaPlayer.start();
        dropped = true;
    }
}//MainActivity Class body end