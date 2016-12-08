package computomovil.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText userText,passText;
    Firebase reference;
    Firebase userReference;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userText = (EditText) findViewById(R.id.editUser);
        passText = (EditText) findViewById(R.id.editPass);
        reference = new Firebase("https://gamemovil-77025.firebaseio.com");
    }

    public void validate(View view) {
        user = userText.getText().toString();
        pass = passText.getText().toString();
        if(user.isEmpty()||pass.isEmpty()){
            Toast.makeText(getApplicationContext(),"Rellene su usario y contraseña ",Toast.LENGTH_LONG).show();
            return;
        }
        userReference = reference.child("users").child(user);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String passD = dataSnapshot.getValue(String.class);
                if(pass.equals(passD)){
                    SessionManager.setPreferencer(user);
                    Intent intent = new Intent(getApplicationContext(),InitialScreen.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Conbinancion Incorrecta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
