package computomovil.proyectofinal;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;

public class InitialScreen extends AppCompatActivity {

    public static final String ACCESS_TOKEN="ofmSfNbRITAAAAAAAAAAKzYX5aR7Y_5zJoLa-lh0S6R9t3kNvTzzqVZrvhpSK8Py";
    private DbxClientV2 client;
    private String email="",name="",tipe="",files="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial__screen);
        client= DropboxClient.getClient(ACCESS_TOKEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    public void launchMap(View view) {
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
            Toast.makeText(this,infoAccount,Toast.LENGTH_LONG);
            //((TextView)findViewById(R.id.accountDetail)).setText(infoAccount);

        }
    }
}
