package computomovil.proyectofinal;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by dell on 11/12/2016.
 */

public class DropboxClient {
    public static DbxClientV2 getClient(String ACCESS_TOKEN) {
        DbxRequestConfig config = new DbxRequestConfig("Box", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        return client;
    }
}
