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

    private Button sign,log;
    private Firebase ref;
    private Firebase mref;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://foodaid-1557289172079.firebaseio.com/users"); // firebase link
        sign=(Button)findViewById(R.id.sign);
        mAuth = FirebaseAuth.getInstance();

        log=(Button)findViewById(R.id.logins);
        init();
    }

    void init(){
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userid=(EditText)findViewById(R.id.usertext);
                EditText pwd=(EditText)findViewById(R.id.pwdtext);
                String user_id=userid.getText().toString();
                String pass=pwd.getText().toString();
                mAuth.createUserWithEmailAndPassword(user_id,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=mAuth.getUid();
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
                            mref=ref.child(uid);

                            Firebase Fname=mref.child(na.getText().toString());
                            Fname.setValue(name.getText().toString());

                            Firebase Fphone=mref.child(phonet.getText().toString());
                            Fphone.setValue(phone.getText().toString());
                            Firebase Fuserid=mref.child(useridt.getText().toString());
                            Fuserid.setValue(userid.getText().toString());
                            Firebase Fpwd=mref.child(pwdt.getText().toString());
                            Fpwd.setValue(pwd.getText().toString());
                            Firebase Faddress=mref.child(add.getText().toString());
                            Faddress.setValue(address.getText().toString());

                            Firebase Fflag=mref.child("flag");
                            Fflag.setValue("0");

                            Firebase time=mref.child("Time");
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
                                Firebase lat=mref.child("Lat");
                                lat.setValue(addr.getLatitude());
                                Firebase longi=mref.child("long");
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

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });



    }

    /*public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }*/
}
