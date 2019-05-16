package learning.andriod.foodaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Resview extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";

    private Firebase firebase;
    private FirebaseAuth auth;
    private Button donateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resview);
        Firebase.setAndroidContext(this);

        firebase = new Firebase(FIREBASE_URL);
        auth = FirebaseAuth.getInstance();
        donateButton = (Button) findViewById(R.id.donate);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();

            }
        });
    }


    private void update() {

        String authUUID = auth.getUid();
        Intent intent = getIntent();
        if (authUUID == null)
            authUUID = intent.getStringExtra("uid");

        Firebase firebaseRef = firebase.child(authUUID);
        Firebase flag = firebaseRef.child("flag");
        flag.setValue("10");
        String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

        Firebase time = firebaseRef.child("Time");
        time.setValue(timeStamp);
        Toast.makeText(this, "This location has been FLagged", Toast.LENGTH_SHORT).show();
        Intent intentPickup = new Intent(Resview.this, PickUp.class);
        intentPickup.putExtra("uid", authUUID);
        startActivity(intentPickup);

    }

    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
}
