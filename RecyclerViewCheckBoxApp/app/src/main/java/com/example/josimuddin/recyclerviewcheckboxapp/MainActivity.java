package com.example.josimuddin.recyclerviewcheckboxapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    StringBuffer sb = null;
    MyAdapter adapter;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new MyAdapter(this, getRecycles());


        btn = findViewById(R.id.btn_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*sb = new StringBuffer();
                for (Recycles r : adapter.checkedPlayers) {
                    sb.append(r.getName());
                    sb.append("\n");
                }

                if (adapter.checkedPlayers.size() > 0) {
                    Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Please Check Players", Toast.LENGTH_SHORT).show();
                }*/

                Intent otherIntent = new Intent(MainActivity.this, AnotherActivity.class);
                startActivity(otherIntent);
            }
        });


        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);
    }

    private ArrayList<Recycles> getRecycles() {
        ArrayList<Recycles> players=new ArrayList<>();
        Recycles p=new Recycles("Johon","Stricker",R.drawable.icon1);
        players.add(p);

        p=new Recycles("Roy Kenny","MidFielder",R.drawable.icon2);
        players.add(p);


        p=new Recycles("JJ Abram","MidFielder",R.drawable.icon3);
        players.add(p);


        p=new Recycles("John Mou","Defenter",R.drawable.icon4);
        players.add(p);

        p=new Recycles("Jerry boy","MidFielder",R.drawable.icon5);
        players.add(p);

        p=new Recycles("J.S Ajirra","Defender",R.drawable.icon6);
        players.add(p);


        return players;
    }
}
