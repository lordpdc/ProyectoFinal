package computomovil.proyectofinal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


public class GameActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(21.048192, -89.644379), 20);
        map.moveCamera(camUpd);

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(21.047866, -89.644431))
                .radius(4)
                .strokeColor(Color.CYAN)
                .fillColor(Color.CYAN);
        Circle circle = map.addCircle(circleOptions);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
