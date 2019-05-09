package learning.andriod.foodaid;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private AdView mAdView;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Firebase mref,ref,d;
    private FirebaseAuth mAuth;
    private EditText userid;
    private EditText pwd;
    private DatabaseReference db;
    private FirebaseAuth.AuthStateListener mAuthstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        mref=new Firebase("https://foodaid-1557289172079.firebaseio.com/users");
        db= FirebaseDatabase.getInstance().getReference();

        if (isServicesOK()) {
            init();
        }
        mAuth = FirebaseAuth.getInstance();
        userid = (EditText) findViewById(R.id.login_text);
        pwd = (EditText) findViewById(R.id.password_text);
        mAuthstate = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }
            }
        };
 //       MobileAds.initialize(this, String.valueOf(R.id.adView));
//        mAdView = findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }


 /*   protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthstate);
    }*/

    private void startSignIn(){
        final String email=userid.getText().toString();
        final String pass=pwd.getText().toString();
        final String admin="krishchandran@ymail.com";
        final String ps="aaaaaaaaa";
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Enter the Credentials properly.!", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!(task.isSuccessful())) {





                        Toast.makeText(MainActivity.this, "Login Failed.! Sign up", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("User",":"+email);
                        Log.e("Usersss",":"+admin);
                        Intent intent;
                        if(!email.equals(admin)){

                            intent = new Intent(MainActivity.this, Resview.class);
                            startActivity(intent);

                        }else {
                            intent = new Intent(MainActivity.this, MapsActivity.class);

                            startActivity(intent);


                        }
                    }

                }

            });
        }
    }
    protected void surfacechanged(){

    }
    private void init(){




        Button btnMap = (Button) findViewById(R.id.loginBut);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();

            }
        });

        Button signup=(Button)findViewById(R.id.signupBut);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });


    }

    public boolean isServicesOK(){
        Log.e(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.e(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.e(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}

