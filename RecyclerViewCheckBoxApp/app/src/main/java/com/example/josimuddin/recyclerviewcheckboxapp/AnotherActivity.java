package com.example.josimuddin.recyclerviewcheckboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        textView = findViewById(R.id.other_text);
        textView.setText("Hello"+getString(R.string.tab)+"World");
    }
}
