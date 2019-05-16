package learning.andriod.foodaid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class Startup extends AppCompatActivity {

    private static int time=4000;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        progressBar =(ProgressBar)findViewById(R.id.progress);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent= new Intent(Startup.this, MainActivity.class);
                startActivity(intent);
                finish();
                progressBar.setVisibility(View.INVISIBLE);
            }
        },time);
    }
}
