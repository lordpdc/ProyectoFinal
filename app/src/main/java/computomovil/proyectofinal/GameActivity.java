package computomovil.proyectofinal;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.List;
import java.util.Random;

import static computomovil.proyectofinal.DefaultValues.*;


public class GameActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener{

    private boolean isDiceAvailable;
    private TextView positionLabel;
    private float prevX = 0;
    private float prevY = 0;
    private float prevZ = 0;
    private float curX = 0;
    private float curY = 0;
    private float curZ = 0;
    private GoogleMap map;
    private SensorManager sensorManager;
    private Sensor acceler;
    private long currentTime=0;
    private boolean lastUpdate=AVAILABLE;
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
        positionLabel=(TextView)findViewById(R.id.positionLabel);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        acceler = sensorManager .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
isDiceAvailable=AVAILABLE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void onPause() {
        super .onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
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
    private void updateMap(){
        map.clear();
        for(CircleOptions circle:circles){
            map.addCircle(circle);
        }
    }
    int currentPosition=0;
    private int currentCircle=0;
    public void changeMap(View view){
        repaintCircles();
        circles[currentCircle%circles.length].strokeColor(Color.YELLOW);
        updateMap();
        currentCircle++;
    }
    private void setNextPosition(int position){
        currentPosition=position%circles.length;
        repaintCircles();
        circles[currentPosition].strokeColor(Color.YELLOW);
        updateMap();
    }
    private void repaintCircles(){
        for(CircleOptions circle:circles){
            circle.fillColor(Color.TRANSPARENT);
            circle.strokeColor(Color.BLUE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setNextTarjet(View view){
        int diceValue=getRandomNumber();
        shotNotification("obtuviste: "+diceValue,CUSTOM_TOAST_TIME);
        int nextPosition=currentPosition+diceValue;
        setNextPosition(nextPosition);
        positionLabel.setText("Position: "+nextPosition);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(isDiceAvailable!=AVAILABLE){
            curX = sensorEvent.values[0];
            curY = sensorEvent.values[1];
            curZ = sensorEvent.values[2];
            if(lastUpdate==AVAILABLE) {
                prevX = Math.abs(curX);
                prevY = Math.abs(curY);
                prevZ = Math.abs(curZ);
                lastUpdate=DISABLE;
            }

        else if(lastUpdate==DISABLE){
                int difX = Math.round(prevX - curX);
                int difY = Math.round(prevY - curY);
                int difZ = Math.round(prevZ - curZ);
                if (difX > MIN_MOV || difY > MIN_MOV || difZ > MIN_MOV) {
                    getRandomNumber();
                    lastUpdate=AVAILABLE;
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private int getRandomNumber(){
        Random r = new Random();
        int diceValue = r.nextInt(MAX_DICE - MIN_DICE ) + MIN_DICE;
        return Math.round(diceValue);


    }
    private void shotNotification(String msg,int durationTime){
        Toast notification = new Toast(getApplicationContext());
        notification.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

    }

}
