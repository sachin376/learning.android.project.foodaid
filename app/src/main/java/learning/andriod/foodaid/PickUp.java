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

    private Button confirm;
    private Firebase mref,ref;
    private FirebaseAuth auth;
    private DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        confirm=(Button)findViewById(R.id.confirm);
        Firebase.setAndroidContext(this);
        db= FirebaseDatabase.getInstance().getReference();
        ref=new Firebase("https://foodaid-1557289172079.firebaseio.com/users");
        auth= FirebaseAuth.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();

            }
        });
    }
    private void update(){

        String s= auth.getUid();
        Intent i=getIntent();
        if(s==null) s=i.getStringExtra("uid");

     //   String s= i.getStringExtra("uid");
        mref=ref.child(s);
        Firebase Fflag=mref.child("flag");
        Fflag.setValue("0");

        String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

        Firebase time=mref.child("Time");
        time.setValue(timeStamp);
        Toast.makeText(this, "Thanks for donating ", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent a = new Intent(PickUp.this, MainActivity.class);

        startActivity(a);
    }
    protected void onStop(){
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }

}
