package com.app.dropphone;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private int billeder = 0;
    private String mCurrentPhotoPath;
    static final int request_image_capture = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    final MainActivity tmp = this;
    int billedecd = 0;

    File[] files;

    //camera funcs

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" +timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void galleryAddPic(String mcpp) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mcpp);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "android.kristiangottschalk.cameratest.FileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }



    //Runs onCreate when app is started up from 100% closed
    @Override
    //test
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
         //KODE DER FÅR GYROSKOPE TIL AT AKTIVERER EFTER ACCELEROMETER
        /*
        if (accelerometerSensor != null)
        {
            finish();
        }

        */

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,files));

        //instantiates gyroscopeListener
        gyroscopeEventListener = new SensorEventListener() {
            //THIS BODY IS RUN ON REPEAT BY gyroscopeEventListener

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[2] > 7) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);

                    if(billedecd == 0)
                    {
                        dispatchTakePictureIntent();
                        billedecd = 30;
                    }
                    else
                    {
                        billedecd--;
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,files));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}//MainActivity Class body end