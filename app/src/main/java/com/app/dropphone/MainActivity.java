package com.app.dropphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.util.EventLog;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;


    private TextView xText, yText, zText;
    private Sensor accelSensor;
    private SensorManager sManager;
    private SensorEventListener accelListener;
    private SensorEventListener gyroscopeEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//<<<<<<< Updated upstream;

        //makes the Sensormanager
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Sets the accelerometer
        //https://www.youtube.com/watch?v=YrI2pCZC8cc
        accelSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);

        //accelListener får body som den tjekker konstant
        accelListener = new SensorEventListener() {
            float xtop, ytop, ztop;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float xtmp = sensorEvent.values[0];
                float ytmp = sensorEvent.values[1];
                float ztmp = sensorEvent.values[2];
                if (xtop < xtmp) {
                    xtop = xtmp;
                    xText.setText("X: " + xtop); //max so far= 19.483f
                }
                if (ytop < ytmp) {
                    ytop = ytmp;
                    yText.setText("Y: " + ytop); //max so far= 19.618f
                }
                if (ztop < ztmp) {
                    ztop = ztmp;
                    zText.setText("Z: " + ztop); //max so far= 19.245f
                }
            }

            //@Override
            //https://www.youtube.com/watch?v=8Veyw4e1MX0
            public void onAccuracyChanged(Sensor sensor, int i) {


                gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                //tjekker om gyroskopet har en heldning
                if (gyroscopeSensor == null) {
                    Toast.makeText(this, "The device has no Gyroscope!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                gyroscopeEventListener = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {
                    }
                };
            }


            //@Override
            protected void onResume() {
                //når mobilen resumer appen skal listeneren sættes igen
                super.onResume();
                sManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
            }

            //@Override
            protected void onPause() {
                //når mobilen pauser appen skal listeneren slukkes
                super.onPause();
                sManager.unregisterListener(accelListener);
            }

            private void TestThing() {
                int i = 1 + 1;
            }

            private void GyroPhoto() {

            }


        }

