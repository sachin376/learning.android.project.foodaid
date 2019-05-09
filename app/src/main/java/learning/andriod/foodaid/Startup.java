package learning.andriod.foodaid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Startup extends AppCompatActivity {
    private AdView mAdView;
    private static int time=4000;
    private ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        p=(ProgressBar)findViewById(R.id.progress);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i= new Intent(Startup.this, MainActivity.class);
                startActivity(i);
                finish();
                p.setVisibility(View.INVISIBLE);

            }
        },time);
        MobileAds.initialize(this, String.valueOf(R.id.adView));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
