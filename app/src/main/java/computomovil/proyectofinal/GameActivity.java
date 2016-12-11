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

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback{

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
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
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

    public void changeMap(View view) {
        repaintCircles();
        circles[currentCircle % circles.length].strokeColor(Color.YELLOW);
        updateMap();
        currentCircle++;
    }

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
    }

    public void setNextTarjet(View view) {
        //setNextPosition(nextPosition);
        positionLabel.setText("Position: " );
    }

    /******************************
     * Shake Section
     *
     *
     ************************/
    private int diceValue;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;


    private int getRandomNumber() {
        Random r = new Random();
        int diceValue = r.nextInt(MAX_DICE - MIN_DICE) + MIN_DICE;
        return Math.round(diceValue);
    }
    public void getDice(View view){
        System.out.println("entro a getDice");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShake() {
                System.out.println("entro a onShake");

                mSensorManager.unregisterListener(mSensorListener);
                diceValue=getRandomNumber();
                showDiceValue(diceValue);

            }
        });
        System.out.println("registro a onShake");
    }

    public void showDiceValue(final int value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agitaste tu cel!!");
        builder.setMessage("Obtuviste un: "+value)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setNextPosition(value);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}
