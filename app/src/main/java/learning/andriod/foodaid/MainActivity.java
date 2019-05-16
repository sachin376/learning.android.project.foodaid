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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sk";
    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    private Firebase firebaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private FirebaseAuth.AuthStateListener mAuthstate;
    private EditText userId;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(FIREBASE_URL);
        db = FirebaseDatabase.getInstance().getReference();

        if (isGooglePlayServicesAvailable()) {
            bindViewAttributes();
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthstate = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }
            }
        };
    }

    private void startSignIn() {
        final String email = userId.getText().toString();
        final String password = pwd.getText().toString();
        final String admin = "sachin@gmail.com";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter the Credentials properly.!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!(task.isSuccessful())) {
                        Toast.makeText(MainActivity.this, "Login Failed.! Sign up", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "User:" + email);
                        Log.e(TAG, "admin user:" + admin);
                        Intent intent;
                        if (!email.equals(admin)) {
                            intent = new Intent(MainActivity.this, Resview.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    }
                }

            });
        }
    }

    private void bindViewAttributes() {
        userId = (EditText) findViewById(R.id.login_text);
        pwd = (EditText) findViewById(R.id.password_text);
        Button loginButton = (Button) findViewById(R.id.loginBut);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });
        Button signUpButton = (Button) findViewById(R.id.signupBut);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
    }

    private boolean isGooglePlayServicesAvailable() {
        Log.e(TAG, "isGooglePlayServicesAvailable: Verifying GooglePlay services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.e(TAG, "isGooglePlayServicesAvailable: GooglePlay Services is working");
            //everything is fine and the user can make map requests
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.e(TAG, "isGooglePlayServicesAvailable: An error occurred, possibly we can fix it");
            //An error occurred, possibly we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}