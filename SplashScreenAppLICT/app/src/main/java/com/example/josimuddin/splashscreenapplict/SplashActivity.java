package com.example.josimuddin.splashscreenapplict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  //  Toast.makeText(SplashActivity.this, "Going to next activity", Toast.LENGTH_SHORT).show();
                }
            }
        };
        t.start();
    }
}
