package learning.andriod.foodaid;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUp extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";

    private Firebase firebase;
    private Firebase firebaseRef;
    private FirebaseAuth firebaseAuth;

    private Button signButton, logButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        firebase =new Firebase(FIREBASE_URL);
        firebaseAuth = FirebaseAuth.getInstance();

        signButton =(Button)findViewById(R.id.sign);
        logButton =(Button)findViewById(R.id.logins);
        init();
    }

    void init(){
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userid=(EditText)findViewById(R.id.usertext);
                EditText pwd=(EditText)findViewById(R.id.pwdtext);

                String userId = userid.getText().toString();
                String password = pwd.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(userId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid= firebaseAuth.getUid();
                            EditText name=(EditText)findViewById(R.id.nametext);
                            EditText address=(EditText)findViewById(R.id.addresstext);
                            EditText phone=(EditText)findViewById(R.id.phonetext);
                            EditText userid=(EditText)findViewById(R.id.usertext);
                            EditText pwd=(EditText)findViewById(R.id.pwdtext);
                            String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
                            TextView phonet=(TextView)findViewById(R.id.phone);

                            TextView useridt=(TextView)findViewById(R.id.user);
                            TextView pwdt=(TextView)findViewById(R.id.pwd);

                            TextView na=(TextView)findViewById(R.id.name);

                            TextView add=(TextView)findViewById(R.id.address);
                            firebaseRef = firebase.child(uid);

                            Firebase Fname= firebaseRef.child(na.getText().toString());
                            Fname.setValue(name.getText().toString());

                            Firebase Fphone= firebaseRef.child(phonet.getText().toString());
                            Fphone.setValue(phone.getText().toString());
                            Firebase Fuserid= firebaseRef.child(useridt.getText().toString());
                            Fuserid.setValue(userid.getText().toString());
                            Firebase Fpwd= firebaseRef.child(pwdt.getText().toString());
                            Fpwd.setValue(pwd.getText().toString());
                            Firebase Faddress= firebaseRef.child(add.getText().toString());
                            Faddress.setValue(address.getText().toString());

                            Firebase Fflag= firebaseRef.child("flag");
                            Fflag.setValue("0");

                            Firebase time= firebaseRef.child("Time");
                            time.setValue(timeStamp);

                            String user_id= userid.getText().toString();
                            String pass= pwd.getText().toString();
                            //The second parameter below is the default string returned if the value is not there.

                            Log.e("GEolocate","GEOLOCATING");
                            String searchstring= address.getText().toString();
                            Geocoder geocoder=new Geocoder(SignUp.this);
                            List<Address> list=new ArrayList<>();
                            try {
                                list=geocoder.getFromLocationName(searchstring,1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(list.size()>0){
                                Address addr=list.get(0);
                                Firebase lat= firebaseRef.child("Lat");
                                lat.setValue(addr.getLatitude());
                                Firebase longi= firebaseRef.child("long");
                                longi.setValue(addr.getLongitude());
                                Log.e("GELOC","gfound location: "+address.toString());

                            }

                            Toast.makeText(SignUp.this, "User Register Successful", Toast.LENGTH_SHORT).show();

                        }else{


                            Toast.makeText(SignUp.this, "Failure", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });
    }
}
