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


import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private TextView xText, yText, zText;
    private Sensor accelSensor;
    private SensorManager sManager;
    private SensorEventListener gyroscopeEventListener;

//>>>>>>> Stashed changes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//<<<<<<< Updated upstream;

        //makes the Sensormanager
        sManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Sets the accelerometer
        accelSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //sManager.registerListener(this,accelSensor,sensorManager.SENSOR_DELAY_FASTEST).show();


        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);
    }

    //@Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
    xText.setText("X: "+ sensorEvent.values[0]);
    yText.setText("Y: "+ sensorEvent.values[1]);
    zText.setText("Z: "+ sensorEvent.values[2]);
    }

    //@Override
    public void onAccuracyChanged(Sensor sensor, int i) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //tjekker om gyroskopet har en heldning
        if (gyroscopeSensor == null)
        {
            Toast.makeText(this, "The device has no Gyroscope!", Toast.LENGTH_SHORT).show ();
            finish();
        }

        gyroscopeEventListener = new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){

            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i){

            }
        };
//>>>>>>> Stashed changes

    }

    private void TestThing()
    {
        int i = 1+1;
    }

    private void GyroPhoto()
    {

    }


}
