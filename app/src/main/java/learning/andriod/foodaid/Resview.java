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

    private Button donate;
    private Firebase mref,ref;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resview);
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://foodaid-1557289172079.firebaseio.com/users");
        auth= FirebaseAuth.getInstance();
        donate=(Button)findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
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
        mref=ref.child(s);
        Firebase Fflag=mref.child("flag");
        Fflag.setValue("10");
        String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

        Firebase time=mref.child("Time");
        time.setValue(timeStamp);
        Toast.makeText(this, "FLagged", Toast.LENGTH_SHORT).show();
        Intent a = new Intent(Resview.this, PickUp.class);
        a.putExtra("uid",s);
        startActivity(a);

    }
    protected void onStop(){
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
}
