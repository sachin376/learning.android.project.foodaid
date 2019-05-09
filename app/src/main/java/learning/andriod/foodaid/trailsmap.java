package learning.andriod.foodaid;

import android.app.Application;

import com.firebase.client.Firebase;

public class trailsmap extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
