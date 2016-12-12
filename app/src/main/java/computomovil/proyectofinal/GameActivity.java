package computomovil.proyectofinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

import static computomovil.proyectofinal.DefaultValues.*;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {
    private  SensorManager mSensorManager;
    private  Sensor mAccelerometer;

    private TextView positionLabel;
    private GoogleMap map;
    private CircleOptions[] circles = new CircleOptions[]{
            new CircleOptions()
                    .center(new LatLng(21.047866, -89.644431))
                    .radius(2)
            ,
            new CircleOptions()
                    .center(new LatLng(21.047892, -89.644479))
                    .radius(2)
            ,
            new CircleOptions()
                    .center(new LatLng(21.047912, -89.644499))
                    .radius(2)
            ,
            new CircleOptions()
                    .center(new LatLng(21.047842, -89.644419))
                    .radius(2)

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        positionLabel = (TextView) findViewById(R.id.positionLabel);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        diceValue=0;
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.unregisterListener(this);


    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(21.048192, -89.644379), 20);
        map.moveCamera(camUpd);
        updateMap();
    }

    public int getDiceValue() {
        return diceValue;
    }

    public void setDiceValue(int diceValue) {
        this.diceValue = diceValue;
    }

    private void updateMap() {
        map.clear();
        for (CircleOptions circle : circles) {
            map.addCircle(circle);
        }
    }

    int currentPosition = 0;
    private int currentCircle = 0;

     private void setNextPosition(int position) {
        currentPosition = position % circles.length;
        repaintCircles();
        circles[currentPosition].strokeColor(Color.YELLOW);
        updateMap();
    }

    private void repaintCircles() {
        for (CircleOptions circle : circles) {
            circle.fillColor(Color.TRANSPARENT);
            circle.strokeColor(Color.BLUE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.unregisterListener(this);

    }


    /******************************
     * Shake Section
     *
     *
     ************************/
    private int diceValue;



    private int getRandomNumber() {
        Random r = new Random();
        int diceValue = r.nextInt(MAX_DICE - MIN_DICE) + MIN_DICE;
        return Math.round(diceValue);
    }
    private float lastx = 0;
    private float lasty = 0;
    private float lastz = 0;
    private boolean sensorControl=true;
    private int SHAKE_LVL=3;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float dif=0;
       if(sensorControl){
            lastx=x;
            lasty=y;
            lastz=z;
            sensorControl=false;
        }else {
            dif=x+y+z-(lastx+lasty+lastz);
            if(dif>SHAKE_LVL){
                diceValue=getRandomNumber();
                showDiceValue(diceValue);
                mSensorManager.unregisterListener(this);
                sensorControl=true;
                availabre=true;
                lastx=0;
                lasty=0;
                lastz=0;}


        }
    }
    private boolean availabre=true;
    public void diceValue(View view){
        if(availabre){
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            mSensorManager.unregisterListener(this);
        }
        availabre=!availabre;
    }
    public void showDiceValue(final int value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agitaste tu cel!!");
        builder.setMessage("Obtuviste un: "+value)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentPosition+=value;
                        setNextPosition(currentPosition);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
