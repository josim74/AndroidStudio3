package com.example.josimuddin.bluetoothapplict;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView res;
    Button btnOn, btnOff, btnDiscover;

    private static int REQUEST_ENABLE_BT = 0;
    private static int REQUEST_DESCOVERABLE_BT = 0;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOn = findViewById(R.id.button_on);
        btnOff = findViewById(R.id.button_off);
        res = findViewById(R.id.textView);
        btnDiscover = findViewById(R.id.btn_discover);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            res.append("Device not supported");
        }


        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                    Toast.makeText(MainActivity.this, "Turning on the device", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    bluetoothAdapter.disable();
            }
        });



        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bluetoothAdapter.isDiscovering()) {
                    Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

                    startActivityForResult(discoverIntent, REQUEST_DESCOVERABLE_BT);

                    Toast.makeText(MainActivity.this, "Turning on the device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
