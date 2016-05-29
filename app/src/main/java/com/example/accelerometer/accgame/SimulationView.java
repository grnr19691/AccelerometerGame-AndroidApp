package com.example.accelerometer.accgame;

/**
 * Created by RajaNageswaraRao on 5/11/2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;


public class SimulationView extends View implements SensorEventListener{
    private  int score=0;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Display mDisplay;
    private static float width;
    private static float height;
    Bitmap field;
    private Bitmap mField;
    private Bitmap mHole;
    private Bitmap mBitmap;
    private static final int BALL_SIZE = 100;
    private static final int BASKET_SIZE = 160;
    OvalShape circle;
    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;
    private Particle mBall = new Particle();

    public SimulationView(Context context) {
        super(context);

        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);

        Bitmap hole = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mHole = Bitmap.createScaledBitmap(hole, BASKET_SIZE, BASKET_SIZE, true);

        Options opts = new Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        field = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);


        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;
        mField=Bitmap.createScaledBitmap(field,w,h,true);
        width=w;
        height=h;
        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
        mBall.mPosY=-mVerticalBound-BALL_SIZE;
        mBall.mPosX=-mHorizontalBound-BALL_SIZE;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
                break;
        }
        mSensorZ = event.values[2];
        mSensorTimeStamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startSimulation() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }


    public void stopSimulation() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mHole, mXOrigin - BASKET_SIZE / 2, (mYOrigin-BASKET_SIZE/2)-(mYOrigin-BASKET_SIZE/2), null);
        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        canvas.drawBitmap(mBitmap, (mXOrigin - BALL_SIZE / 2) + mBall.mPosX, (mYOrigin - BALL_SIZE/2 ) - mBall.mPosY, null);
        float ballCenterX=(mXOrigin) + mBall.mPosX;
        float ballCenterY=(mYOrigin) - mBall.mPosY;
        float dist=(mBall.mPosX*mBall.mPosX+(mBall.mPosY-(mYOrigin-BASKET_SIZE/2))*(mBall.mPosY-(mYOrigin-BASKET_SIZE/2)));

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLACK);
        paint.setTextSize(height/20);
        canvas.drawText("Score : " +score, width/25, height/25, paint);

        if((BALL_SIZE/2+BASKET_SIZE/2)*(BALL_SIZE/2+BASKET_SIZE/2)>dist+dist/4)
        {
            mBall.mPosX=-mHorizontalBound-BASKET_SIZE;
            mBall.mPosY=-mHorizontalBound-BASKET_SIZE;
            mBall.mVelX=0;
            mBall.mVelY=0;
            score++;
        }
        invalidate();
    }
    public static float getScreenWidth()
    {
        return width;
    }
    public static float getScreenHeight()
    {
        return height;
    }
}
