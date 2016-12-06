package computomovil.proyectofinal;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


public class GameActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener{

    private GoogleMap map;
    private SensorManager sensorManager;
    private Sensor gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super .onPause();
        sensorManager .unregisterListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(21.048192, -89.644379), 20);
        map.moveCamera(camUpd);

        CircleOptions[] circles = new CircleOptions[4];

        circles[0]= new CircleOptions()
                .center(new LatLng(21.047866, -89.644431))
                .radius(4)
                .strokeColor(Color.CYAN)
                .fillColor(Color.CYAN);
        circles[1] = new CircleOptions()
                .center(new LatLng(21.047892, -89.644479))
                .radius(4)
                .strokeColor(Color.CYAN)
                .fillColor(Color.CYAN);

        //Circle circle = map.addCircle(circles[0]);
        //Circle circle2 = map.addCircle(circles[1]);
        map.addCircle(circles[0]);
        map.addCircle(circles[1]);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float []values = event.values ;
        float x = values[ 0 ];
        float y = values[ 1 ];
        float z = values[ 2 ];
        Toast.makeText(this,""+x+""+y+""+z,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
