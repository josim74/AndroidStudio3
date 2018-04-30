package com.example.josimuddin.sqlitedatabaseprojectlict;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyDatabase myDatabase = new MyDatabase(this);
    EditText etName, etLocation, etSalary;
    Button btnInsert;
    ListView listView;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        myDatabase.open();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentValues = new ContentValues();
                contentValues.put("name", etName.getText().toString());
                contentValues.put("location", etLocation.getText().toString());
                contentValues.put("salary", Integer.parseInt(etSalary.getText().toString()));

                //insert the values to the database..
                myDatabase.empInsert(contentValues);

                Toast.makeText(MainActivity.this, "Value inserted", Toast.LENGTH_SHORT).show();

                etName.setText(null);
                etLocation.setText(null);
                etSalary.setText(null);



                //query again for view entry
                cursor.requery();


            }
        });
        //Retrive the data using getEmp
        cursor = myDatabase.getEmp();

        String[] fromDb = {"name","location","salary"};
        int[] toList = {R.id.tv_name_row, R.id.tv_location_row, R.id.tv_salary_row};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, fromDb, toList);

        listView.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.notifyDataSetChanged();
        cursor.requery();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etSalary = findViewById(R.id.etSalary);

        listView = findViewById(R.id.listData);
        btnInsert = findViewById(R.id.btnInsert);
    }
}
