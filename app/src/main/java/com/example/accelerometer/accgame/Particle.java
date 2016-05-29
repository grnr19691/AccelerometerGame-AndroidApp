package com.example.accelerometer.accgame;

/**
 * Created by RajaNageswaraRao on 5/11/2015.
 */
public class Particle {

    /* coefficient of restitution */
    private static final float COR = 0.7f;

    public float mPosX;
    public float mPosY;
    public float mVelX;
    public float mVelY;
    float width=SimulationView.getScreenWidth();
    float height=SimulationView.getScreenHeight();

    public void updatePosition(float sx, float sy, float sz, long timestamp) {
        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
        mVelX += -sx * dt;
        mVelY += -sy * dt;

        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if (mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}