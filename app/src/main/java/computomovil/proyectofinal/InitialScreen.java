package computomovil.proyectofinal;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static computomovil.proyectofinal.DefaultValues.ACCESS_TOKEN;
import static computomovil.proyectofinal.DefaultValues.circles;

public class InitialScreen extends AppCompatActivity {

    //public static final String ACCESS_TOKEN="ofmSfNbRITAAAAAAAAAAKzYX5aR7Y_5zJoLa-lh0S6R9t3kNvTzzqVZrvhpSK8Py";
    private DbxClientV2 client;
    private String email="",name="",tipe="",files="";
    private ArrayList<CircleOptions> circlesA = new ArrayList<>();
    ArrayList<String> valores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial__screen);
        client= DropboxClient.getClient(ACCESS_TOKEN);
        //generateCircles(21.047866, -89.644431);
        //generateCircles(21.047892, -89.644479);
        //generateCircles(21.047912, -89.644499);
        //generateCircles(21.047842, -89.644419);
        GPSTracker gps = new GPSTracker(this);
        if(!gps.isGPSEnabled){
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    public void launchMap(View view) {
        DefaultValues.circles = circlesA;
        readTxt();
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.initial_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SessionManager.cleanPreference();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void readTxt(){
        File sdcard = Environment.getExternalStorageDirectory();

        File file = new File(sdcard+"/BlueBox","mapaA.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split(",");
                generateCircles(Double.valueOf(coordinates[0]),Double.valueOf(coordinates[1]));
            }
            br.close();
        }
        catch (IOException e) {
        }
    }

    private void getAccountDetail() {
        if (ACCESS_TOKEN == null)return;

        new UserAccountTask(DropboxClient.getClient(ACCESS_TOKEN), new UserAccountTask.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                email=account.getEmail();
                name= account.getName().getDisplayName();
                tipe= account.getAccountType().name();
                ListFolderResult result = null;

            }
            @Override
            public void onError(Exception error) {
                name="Error receiving account details.";
            }

        }

        ).execute();
    }

    public void downloadMaps(View view){
        String infoAccount="";
        if(client!=null){
            getAccountDetail();
            infoAccount= email+"\n"+name+"\n"+tipe+"\n"+files;
            Toast.makeText(this,"Mapa Descargado",Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,infoAccount,Toast.LENGTH_LONG);
            //((TextView)findViewById(R.id.accountDetail)).setText(infoAccount);

        }
    }

    public void generateCircles(double lat, double lon){
        CircleOptions circle = new CircleOptions();
        circle.center(new LatLng(lat, lon));
        circle.radius(2);
        circlesA.add(circle);
    }
}
