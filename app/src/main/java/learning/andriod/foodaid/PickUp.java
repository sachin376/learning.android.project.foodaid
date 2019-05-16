package learning.andriod.foodaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PickUp extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";
    private Firebase firebase;
    private FirebaseAuth auth;
    private DatabaseReference db;

    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        confirmButton = (Button) findViewById(R.id.confirm);

        Firebase.setAndroidContext(this);
        db = FirebaseDatabase.getInstance().getReference();
        firebase = new Firebase(FIREBASE_URL);
        auth = FirebaseAuth.getInstance();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();

            }
        });
    }

    private void update() {
        String AuthUUID = auth.getUid();
        Intent intent = getIntent();

        if (AuthUUID == null)
            AuthUUID = intent.getStringExtra("AuthUUID");

        Firebase firebaseRef = firebase.child(AuthUUID);
        Firebase flag = firebaseRef.child("flag");
        flag.setValue("0");
        String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

        Firebase time = firebaseRef.child("Time");
        time.setValue(timeStamp);
        Toast.makeText(this, "Thanks for the help !!!", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent a = new Intent(PickUp.this, MainActivity.class);
        startActivity(a);
    }

    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
}