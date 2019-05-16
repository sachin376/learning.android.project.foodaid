package learning.andriod.foodaid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "sk";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String FIREBASE_URL = "https://foodaid-1557289172079.firebaseio.com/users";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private ArrayList<LatLng> position;
    private ArrayList<Double> latitude, longitude;

    private FirebaseAuth firebaseAuthentication;
    private Firebase firebaseRef;
    private Boolean locationPermissionsGranted = false;
    private GoogleMap googleMap;
    public FusedLocationProviderClient fusedLocationProviderClient; // New client to retrieve the location.

    private Marker marker;
    private Button logout;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map got Ready", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onMapReady: Map got ready");
        this.googleMap = googleMap;

        if (locationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.googleMap.setMyLocationEnabled(true);
            init();
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Firebase.setAndroidContext(this);
        position = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseRef = new Firebase(FIREBASE_URL);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });
        getLocationPermission();

        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getKey().toString());
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user : users) {

                    if (user.getKey().equals("Lat")) {
                        double latitudeFromDb = (double) user.getValue();
                        for (DataSnapshot UserDocument : users) {
                            Log.e(TAG, "key :" + user.getKey() + " value :" + user.getValue());
                            // todo change from 10 to 1 or 0
                            if (UserDocument.getKey().equals("flag") && UserDocument.getValue().equals("10")) {
                                latitude.add(latitudeFromDb);
                                for (DataSnapshot UserItem : users) {
                                    if (UserItem.getKey().equals("long")) {
                                        longitude.add((Double) UserItem.getValue());
                                    }
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < latitude.size(); i++) {
                    Log.e(TAG, "Latitude : " + latitude.get(i) + "&& Longitude :" + longitude.get(i));
                    position.add(i, new LatLng(latitude.get(i), longitude.get(i)));
                    moveCamera(position.get(i), 15f, "FOOD IS AVAILABLE HERE");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.toString());
                int m = 0, n = 0;
                Iterable<DataSnapshot> dt = dataSnapshot.getChildren();
                for (DataSnapshot d : dt) {

                    if (d.getKey().equals("Lat")) {
                        Log.e("LAAATTT", "YESSS");
                        double ef = (double) d.getValue();

                        for (DataSnapshot f : dt) {
                            Log.e("VALUE FOR KEY", ":" + d.getKey() + ":" + d.getValue());
                            if (f.getKey().equals("flag") && f.getValue().equals("10")) {
                                Log.e("YESSS", "YESYEYSYES");
                                latitude.add(ef);

                                for (DataSnapshot e : dt) {
                                    if (e.getKey().equals("long")) {
                                        Log.e("LOONGGG", "YESSS");
                                        longitude.add((Double) e.getValue());
                                        m = 1;
                                    }
                                }

                            } else if (f.getKey().equals("flag") && f.getValue().equals("0")) {
                                Log.e("YESSS", "YESYEYSYES");
                                latitude.remove(ef);

                                for (DataSnapshot e : dt) {
                                    if (e.getKey().equals("long")) {
                                        Log.e("LOONGGG", "noo");
                                        longitude.remove((Double) e.getValue());
                                        m = 0;
                                    }
                                }
                            }
                        }
                    }
                }

                if (m == 1) {
                    for (int i = 0; i < latitude.size(); i++) {
                        Log.e("LATI LONG", "LAT: " + latitude.get(i) + "&& LONG :" + longitude.get(i));
                        position.add(i, new LatLng(latitude.get(i), longitude.get(i)));
                        moveCamera(position.get(i), 15f, "FOOD IS AVAILABLE HERE");
                    }
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(700);

                    Toast.makeText(MapsActivity.this, "WE are getting this changed", Toast.LENGTH_SHORT).show();
                } else if (m == 0) {
                    for (int i = 0; i < latitude.size(); i++) {
                        Log.e("LATI LONG", "LAT: " + latitude.get(i) + "&& LONG :" + longitude.get(i));
                        position.add(i, new LatLng(latitude.get(i), longitude.get(i)));
                        moveCamera(position.get(i), 30f, "FOOD IS AVAILABLE HERE");
                    }
                    Toast.makeText(MapsActivity.this, "WE are not getting this changed", Toast.LENGTH_SHORT).show();

                }
                Intent i = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(i);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getDeviceLocation() {
        Log.e("DEVICE LOCATIOn", "WE ARE ENTERING THIS METHOD");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (locationPermissionsGranted) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.e("FOUND", "CURRENT LOCATION");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f, "My location");


                        } else {
                            Log.e("NOT FOUND", "CURRENT LOCATION");
                        }
                    }

                });
            }

        } catch (SecurityException e) {
            Log.e("SECURITY", "EXCEPTION");
        }


    }

    private void init() {
        Log.e("map", "Initializing");
        //geolocate();
    }

   /* private void geolocate(){
        Intent i = getIntent();
        //The second parameter below is the default string returned if the value is not there.
        String txtData = i.getExtras().getString("txtData","");
        Log.e("GEolocate","GEOLOCATING");
        String searchstring= txtData;
        Geocoder geocoder=new Geocoder(MapsActivity.this);
        List<Address> list=new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(searchstring,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size()>0){
            Address address=list.get(0);

            Log.e("GELOC","gfound location: "+address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15f,address.getLocality());
        }
    }*/


    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.e("POSTIIONO", "MOVING TO LATLONG AND ZOOMING");
        googleMap.setOnMarkerClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);


        marker = googleMap.addMarker(options);


    }

    private void initMap() {
        Log.e(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.e(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: called.");
        locationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.e(TAG, "onRequestPermissionsResult: permission granted");
                    locationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker maarker) {
        if (maarker.equals(marker)) {
            String l = marker.getId();

            firebaseRef.orderByChild("Time");
            Log.e("MARKER ID:", "12");


            Toast.makeText(this, "Yeas", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}





