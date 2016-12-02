package computomovil.proyectofinal;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by raoman on 02/12/2016.
 */

public class SessionManager {
    SharedPreferences settings;
    Context context;
    private static final String SESSION="session_file";

    public SessionManager(Context context ) {
        this.context=context;
        settings = context.getSharedPreferences(SESSION,0);
    }

    public void setPreferencer(String key, String value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getPreference(String key){
        return settings.getString(key,"none");
    }


}
