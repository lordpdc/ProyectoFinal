package computomovil.proyectofinal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import static computomovil.proyectofinal.DefaultValues.*;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView positionLabel;
    private GoogleMap map;
    private ArrayList<CircleOptions> circles = DefaultValues.circles;
    GPSTracker gps;
    MarkerOptions markerOptions;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        positionLabel = (TextView) findViewById(R.id.positionLabel);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        diceValue = 0;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gps = new GPSTracker(this);
        //timer();
        //scheduleSendLocation();
        //gps.showSettingsAlert();
        //initTimer();
    }

    public void setCircles(ArrayList<CircleOptions> circles) {
        this.circles = circles;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        updateMap();
        if(gps.canGetLocation){
            //double lat = gps.getLatitude();
            //double lon = gps.getLongitude();
            //marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            //marker.setTitle("Posicion");

            //MarkerOptions markerOptions = new MarkerOptions();
            //markerOptions.position(new LatLng(lat, lon));
            //markerOptions.title("Posicion");
            //Toast.makeText(getApplicationContext(),lat+""+lon,Toast.LENGTH_SHORT).show();
            //map.addMarker(markerOptions);
        }
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
        currentPosition = position % circles.size()-1;
        repaintCircles();
        circles.get(currentPosition).strokeColor(Color.YELLOW);
        updateMap();
         positionLabel.setText("posicion: "+currentPosition);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Agitalo!!!!");
            builder.setMessage("Agita tu celular para tirar los dados y ver la siguiente ubicacion. ")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            availabre=!availabre;
        }
        /*else{
            mSensorManager.unregisterListener(this);
            availabre=!availabre;
        }*/
    }

    public void showDiceValue(final int value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agitaste tu cel!!");
        builder.setMessage("Obtuviste un: "+value)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentPosition+=value+1;
                        setNextPosition(currentPosition);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /*
    Seccion de GPS
     */

    private final double FIVE_SECONDS = 300;
    public void scheduleSendLocation() {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
            @Override
            public void run() {
                if(gps.canGetLocation){
                    double lat = gps.getLatitude();
                    double lon = gps.getLongitude();
                    marker.remove();
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                    marker.setTitle("Posicion");
                    //markerOptions.position(new LatLng(lat, lon));
                    //map.addMarker(markerOptions);
                    Toast.makeText(getApplicationContext(),lat+""+lon,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Handler handler = new Handler();
    public void timer(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gps.canGetLocation){
                    double lat = gps.getLatitude();
                    double lon = gps.getLongitude();
                    marker.remove();
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                    marker.setTitle("Posicion");
                    Toast.makeText(getApplicationContext(),lat+""+lon,Toast.LENGTH_SHORT).show();
                }
            }
        }, 100);
    }

}
