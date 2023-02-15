package com.tks.getorientationwithaccelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private int mPrevRotation = -1;
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

            IntFunction<String> getMsg = (rot) -> {
                if(rot == Surface.ROTATION_0)        return "上向き";
                else if(rot == Surface.ROTATION_180) return "↓向き";
                else if(rot == Surface.ROTATION_270) return "横↑向き";
                else if(rot == Surface.ROTATION_90)  return "横↓向き";
                return "none";
            };

            /* 開始角度>終了角度の場合、反時計回りに回転します */
            Function1<Pair<Integer,Integer>, Pair<Integer,Integer>> getDegrees = (rots) -> {
                if(     rots.first == Surface.ROTATION_0  && rots.second == Surface.ROTATION_90 )  return new Pair<>(360, 270);/*"上向き"->"横↓向き"*/
                else if(rots.first == Surface.ROTATION_0  && rots.second == Surface.ROTATION_180)  return new Pair<>(  0, 180);/*"上向き"->  "↓向き"*/
                else if(rots.first == Surface.ROTATION_0  && rots.second == Surface.ROTATION_270)  return new Pair<>(  0,  90);/*"上向き"->"横↑向き"*/

                else if(rots.first == Surface.ROTATION_90  && rots.second == Surface.ROTATION_0 )  return new Pair<>( 270,360);   /*"横↓向き"->  "上向き"*/
                else if(rots.first == Surface.ROTATION_90  && rots.second == Surface.ROTATION_180) return new Pair<>( 270,180);   /*"横↓向き"->  "↓向き"*/
                else if(rots.first == Surface.ROTATION_90  && rots.second == Surface.ROTATION_270) return new Pair<>( 270, 90);   /*"横↓向き"->"横↑向き"*/
                                                                                                                                        
                else if(rots.first == Surface.ROTATION_180 && rots.second == Surface.ROTATION_0 )  return new Pair<>( 180, 0);   /*"↓向き"->  "上向き"*/
                else if(rots.first == Surface.ROTATION_180 && rots.second == Surface.ROTATION_90)  return new Pair<>( 180,270);   /*"↓向き"->"横↓向き"*/
                else if(rots.first == Surface.ROTATION_180 && rots.second == Surface.ROTATION_270) return new Pair<>( 180, 90);   /*"↓向き"->"横↑向き"*/
                                                                                                                                        
                else if(rots.first == Surface.ROTATION_270 && rots.second == Surface.ROTATION_0 )  return new Pair<>( 90,  0);   /*"横↑向き"->  "上向き"*/
                else if(rots.first == Surface.ROTATION_270 && rots.second == Surface.ROTATION_90)  return new Pair<>( 90,270);   /*"横↑向き"->"横↓向き"*/
                else if(rots.first == Surface.ROTATION_270 && rots.second == Surface.ROTATION_180) return new Pair<>( 90,180);   /*"横↑向き"->  "↓向き"*/

                return new Pair<>(0,0);
            };

            if(mPrevRotation == -1) {
                mPrevRotation = mRotation;
                return;
            }
            if(mPrevRotation != mRotation) {
                Pair<Integer, Integer> dstrot = getDegrees.invoke(new Pair<>(mPrevRotation, mRotation));
                String logstr = String.format(Locale.JAPAN, "%s(%d, %d°) -> %s(%d, %d°)", getMsg.apply(mPrevRotation), mPrevRotation, dstrot.first, getMsg.apply(mRotation), mRotation, dstrot.second);
                Log.d("aaaaa", logstr);
                ImageView imgrabit = findViewById(R.id.img_rabit);
                RotateAnimation rotanim = new RotateAnimation(dstrot.first, dstrot.second, imgrabit.getPivotX(), imgrabit.getPivotY());
                rotanim.setDuration(1000);
                rotanim.setFillAfter(true);
                findViewById(R.id.img_rabit).startAnimation(rotanim);
                mPrevRotation = mRotation;
            }
        }
    };
}