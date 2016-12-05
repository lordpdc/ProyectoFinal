package computomovil.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LaunchActivity extends Activity {
    Intent nexActivity;
    private static final String SESSION="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Splash().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //startActivity(nexActivity);
    }

    private class Splash extends AsyncTask{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Object[] params) {

            SessionManager manager=new SessionManager(getApplicationContext());
            String user=manager.getPreference(SESSION);
            if(user.isEmpty()){
                nexActivity=new Intent(getApplicationContext(),Login.class);
            }
            else{
                nexActivity=new Intent(getApplicationContext(),Initial_Screen.class);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            startActivity(nexActivity);
            finish();
        }
    }

}


