package com.example.josimuddin.popupmenuapplict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnShowPopUpMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowPopUpMenu = findViewById(R.id.btn_show_pop_up_menu);

        btnShowPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnShowPopUpMenu);

                //inflate popup menu
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                //setting onclick listener on menu Item....
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(MainActivity.this, "You Clicked On "+item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                //show the popup menu
                popupMenu.show();
            }
        });
    }


}
