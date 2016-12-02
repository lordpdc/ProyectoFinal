package computomovil.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {
    Intent nexActivity;
    private static final String SESSION="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager manager=new SessionManager(this);
        String user=manager.getPreference(SESSION);
        if(user.isEmpty()){
            nexActivity=new Intent(this,Login.class);
        }
        else{
            nexActivity=new Intent(this,Initial_Screen.class);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(nexActivity);
    }
}
