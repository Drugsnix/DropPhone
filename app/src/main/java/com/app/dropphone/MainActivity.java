package com.app.dropphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.util.EventLog;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements  SensorEventListener{

    private TextView xText, yText, zText;
    private Sensor accelSensor;
    private SensorManager sManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //makes the Sensormanager
        sManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Sets the accelerometer
        accelSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_FASTEST);


        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
    xText.setText("X: "+ sensorEvent.values[0]);
    yText.setText("Y: "+ sensorEvent.values[1]);
    zText.setText("Z: "+ sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void TestThing()
    {
        int i = 1+1;
    }
}
