package com.example.josimuddin.datapickerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

     TextView crntDate;
     DatePicker datePicker;
     Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




/*
        crntDate = findViewById(R.id.tv_current_date);
        datePicker = findViewById(R.id.date_picker);
        btn = findViewById(R.id.btn_change_date);


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        crntDate.setText(date);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crntDate.setText(getCurrentDate());
            }
        });*/

    }

    /*private CharSequence getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append("Current date is :");
        builder.append((datePicker.getMonth()+1)+ "/");// considering month as 0
        builder.append(datePicker.getDayOfMonth()+"/");
        builder.append(datePicker.getYear());

        return builder.toString();
    }*/
}
