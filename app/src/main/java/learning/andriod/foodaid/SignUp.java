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

    private static final String TAG = "sk";
    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";
    private Firebase firebase, firebaseRef;
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
                EditText userIdViewText = (EditText)findViewById(R.id.usertext);
                EditText passwordViewText = (EditText)findViewById(R.id.pwdtext);
                String userId = userIdViewText.getText().toString();
                String password = passwordViewText.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(userId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String authUid= firebaseAuth.getUid();
                            EditText name=(EditText)findViewById(R.id.nametext);
                            EditText address=(EditText)findViewById(R.id.addresstext);
                            EditText phone=(EditText)findViewById(R.id.phonetext);
                            EditText userid=(EditText)findViewById(R.id.usertext);
                            EditText password=(EditText)findViewById(R.id.pwdtext);
                            String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());

                            TextView nameTextView=(TextView)findViewById(R.id.name);
                            TextView addressTextView=(TextView)findViewById(R.id.address);
                            TextView phoneTextView=(TextView)findViewById(R.id.phone);
                            TextView usertTextView=(TextView)findViewById(R.id.user);
                            TextView passwordTextView=(TextView)findViewById(R.id.pwd);

                            firebaseRef = firebase.child(authUid);
                            Firebase nameChild = firebaseRef.child(nameTextView.getText().toString());
                            Firebase phoneChild = firebaseRef.child(phoneTextView.getText().toString());
                            Firebase userIdChild = firebaseRef.child(usertTextView.getText().toString());
                            Firebase passwordChild = firebaseRef.child(passwordTextView.getText().toString());
                            Firebase addressChild = firebaseRef.child(addressTextView.getText().toString());
                            Firebase flagChild = firebaseRef.child("flag");
                            Firebase time= firebaseRef.child("Time");

                            nameChild.setValue(name.getText().toString());
                            phoneChild.setValue(phone.getText().toString());
                            userIdChild.setValue(userid.getText().toString());
                            passwordChild.setValue(password.getText().toString());
                            addressChild.setValue(address.getText().toString());
                            flagChild.setValue("0");
                            time.setValue(timeStamp);

                            Log.e(TAG,"GEO LOCATING");
                            String locationName= address.getText().toString();
                            Geocoder geocoder=new Geocoder(SignUp.this);
                            List<Address> addressList=new ArrayList<>();
                            try {
                                addressList=geocoder.getFromLocationName(locationName,1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(addressList.size()>0){
                                Address addressItem = addressList.get(0);
                                Firebase latitude= firebaseRef.child("Lat");
                                latitude.setValue(addressItem.getLatitude());
                                Firebase longitude= firebaseRef.child("long");
                                longitude.setValue(addressItem.getLongitude());
                                Log.e(TAG,"Geo location found : "+address.toString());
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
