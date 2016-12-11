package computomovil.proyectofinal;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by raoman on 11/12/2016.
 */

public class ShakeEventListener implements SensorEventListener {

    private static final int MIN_FORCE = 10; /** Minimum movement force to consider. */
    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 3;
    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;
    /** Maximum allowed time for shake gesture. */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;
    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;
    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;
    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    private float lastX = 0;/** The last x position. */
    private float lastY = 0;/** The last y position. */
    private float lastZ = 0;/** The last z position. */

    private OnShakeListener mShakeListener;

    /** OnShakeListener that is called when shake is detected. */

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        customShake(se);
        simpleShake(se);
    }
    private boolean shakeAvailble=true;
    private void simpleShake(SensorEvent se){
        // get sensor data
        System.out.println("entro a detectar cambios en sensor");

        float x = Math.abs(se.values[SensorManager.DATA_X]);
        float y = Math.abs(se.values[SensorManager.DATA_Y]);
        float z = Math.abs(se.values[SensorManager.DATA_Z]);
        // calculate movement
        float totalMovement = x + y + z - lastX - lastY - lastZ;
        if (totalMovement > MIN_FORCE) {
        }
    }
    private void customShake(SensorEvent se){

        // get sensor data
        System.out.println("entro a detectar cambios en sensor");

        float x = se.values[SensorManager.DATA_X];
        float y = se.values[SensorManager.DATA_Y];
        float z = se.values[SensorManager.DATA_Z];
        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);
        if (totalMovement > MIN_FORCE) {
            // get time
            long now = System.currentTimeMillis();
            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {
                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;
                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;
                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {
                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
                        System.out.println("entro cerca del onshake");

                        mShakeListener.onShake();
                        resetShakeParameters();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }

    }


    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}