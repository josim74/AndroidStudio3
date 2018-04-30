package com.example.josimuddin.contextmenuapplict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    String[] names = { "Milon", "Shaila", "Ajmeeri", "Istekharul", "JosimUddin", "Ibrahim"};
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        lv.setAdapter(adapter);
        registerForContextMenu(lv);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // lets set the header for the menu...
        menu.setHeaderTitle("Select Action");
        menu.add(0,v.getId(),0,"Call");
        menu.add(0,v.getId(),0,"Message");
        menu.add(0,v.getId(),0,"Block");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call") {
            Toast.makeText(MainActivity.this, "You Selected "+item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Message") {
            Toast.makeText(MainActivity.this, "You Selected "+item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Block") {
            Toast.makeText(MainActivity.this, "You Selected "+item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
}
