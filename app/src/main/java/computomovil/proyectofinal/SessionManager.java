package computomovil.proyectofinal;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by raoman on 02/12/2016.
 */

public class SessionManager {
    static SharedPreferences settings;
    Context context;
    private static final String SESSION="session_file";

    public SessionManager(Context context ) {
        this.context=context;
        settings = context.getSharedPreferences(SESSION,0);
    }

    public static void setPreferencer(String value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("login",value);
        editor.commit();
    }

    public String getPreference(String key){
        return settings.getString(key,"");
    }


}
