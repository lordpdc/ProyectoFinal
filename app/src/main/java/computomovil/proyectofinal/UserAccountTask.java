package computomovil.proyectofinal;

import android.os.AsyncTask;
import android.os.Environment;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by dell on 11/12/2016.
 */

public class UserAccountTask extends AsyncTask<Void,Void,FullAccount> {

    private DbxClientV2 dbxClient;
    private TaskDelegate  delegate;
    private Exception error;

    public interface TaskDelegate {
        void onAccountReceived(FullAccount account);
        void onError(Exception error);

    }

    UserAccountTask(DbxClientV2 dbxClient, TaskDelegate delegate){
        this.dbxClient =dbxClient;
        this.delegate = delegate;
    }


    private String nameFile="mapaA.txt";
    private String pathLowerFile="/mapaA.txt";
    private String revFile="551c911a8";
    //private String revFile="151c911a8";
    @Override
    protected FullAccount doInBackground(Void... params) {
        try {
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path+"/BlueBox", nameFile);

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    error = new RuntimeException("Unable to create directory: " + path);
                }
            } else if (!path.isDirectory()) {
                error = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                dbxClient.files().download(pathLowerFile,revFile)
                        .download(outputStream);
            }
            System.out.println(path.toString()+"\n"+path.getAbsolutePath());


            //get the users FullAccount
            return dbxClient.users().getCurrentAccount();
        } catch (Exception e) {
            e.printStackTrace();
            error = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(FullAccount account) {
        super.onPostExecute(account);

        if (account != null && error == null){
            //User Account received successfully
            delegate.onAccountReceived(account);
        }
        else {
            // Something went wrong
            delegate.onError(error);
        }
    }
}