package computomovil.proyectofinal;

import com.google.android.gms.maps.model.CircleOptions;

import java.util.ArrayList;

/**
 * Created by raoman on 07/12/2016.
 */

public  class DefaultValues {
    public static final float SHAKE_THRESHOLD = 3.1f;
    public static final float ROTATION_THRESHOLD = 3.0f;
    public static final int SHAKE_WAIT_TIME_MS = 250;
    public static final int ROTATION_WAIT_TIME_MS = 100;
    public static final int MIN_DICE=1;
    public static final int MAX_DICE=6;
    public static final int UPGRADE =1;
    public static final int DOWNGRADE =0;
    public static final double MIN_MOV =3.5;
    public static final boolean AVAILABLE=true;
    public static final boolean DISABLE=true;
    public static final int CUSTOM_TOAST_TIME=5000;//N segundos *1000
    public static final String ACCESS_TOKEN="ofmSfNbRITAAAAAAAAAAKzYX5aR7Y_5zJoLa-lh0S6R9t3kNvTzzqVZrvhpSK8Py";
    public static ArrayList<CircleOptions> circles;

}
