package com.tks.getorientationwithaccelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private int mRotation = Surface.ROTATION_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sensor accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventCallback, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventCallback);
    }

    final private SensorEventCallback mSensorEventCallback = new SensorEventCallback() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            super.onSensorChanged(event);

            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;

            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];

            String strTmp = "加速度センサー\n"
                    + " X: " + sensorX + "\n"
                    + " Y: " + sensorY + "\n"
                    + " Z: " + sensorZ;
            ((TextView)findViewById(R.id.txt_hello)).setText(strTmp);

            if(sensorY>5)       mRotation = Surface.ROTATION_0;
            else if(sensorY<-5) mRotation = Surface.ROTATION_180;
            else if(sensorX> 5) mRotation = Surface.ROTATION_270;
            else if(sensorX<-5) mRotation = Surface.ROTATION_90;

            if(mRotation == Surface.ROTATION_0)
                Log.d("aaaaa", "上向き:" + mRotation);
            else if(mRotation == Surface.ROTATION_180)
                Log.d("aaaaa", "↓向き:" + mRotation);
            else if(mRotation == Surface.ROTATION_270)
                Log.d("aaaaa", "横↑向き:" + mRotation);
            else if(mRotation == Surface.ROTATION_90)
                Log.d("aaaaa", "横↓向き:" + mRotation);
        }
    };
}