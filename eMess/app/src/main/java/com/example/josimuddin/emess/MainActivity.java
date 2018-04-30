package com.example.josimuddin.emess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.josimuddin.emess.fragments.AddNewMemberFragment;
import com.example.josimuddin.emess.fragments.BazarFragment;
import com.example.josimuddin.emess.fragments.BazarInputFragment;
import com.example.josimuddin.emess.fragments.MakeManagerFragment;
import com.example.josimuddin.emess.fragments.MealFragment;
import com.example.josimuddin.emess.fragments.MealInputFragment;
import com.example.josimuddin.emess.fragments.MessCreateFragment;
import com.example.josimuddin.emess.fragments.NoticeBoardFragment;
import com.example.josimuddin.emess.fragments.OthersInputFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //navigation View..........
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mUserDatabase;

    private android.support.v4.app.Fragment fragment;
    private FragmentTransaction ft;

    public static String current_uid;
    public static String staticMessId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initializing firebase...

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            sendToStart();
        }else {

            current_uid = currentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(current_uid);
            mUserDatabase.keepSynced(true);

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    staticMessId = dataSnapshot.child("mess").getValue().toString();
                    if (!staticMessId.equals("defaultValue")) {

                        fragment = new MealFragment();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_content,fragment).commitAllowingStateLoss();
                    }else {
                        fragment = new MessCreateFragment();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_content,fragment).commitAllowingStateLoss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        int id = item.getItemId();
        // Handle navigation view item clicks here.

        if (id == R.id.nav_Profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);

            /*fragment = new ProfileFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content, fragment).commit();*/
        } else if (id == R.id.nav_create_mess) {
            fragment = new MessCreateFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        } else if (id == R.id.nav_meal) {
            fragment = new MealFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        } else if (id == R.id.nav_Bazar) {
            fragment = new BazarFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        } else if (id == R.id.nav_notice_board) {
            fragment = new NoticeBoardFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        } else if (id == R.id.nav_meal_input) {
            Bundle bundle = new Bundle();
            bundle.putString("mess_id", staticMessId);
            fragment = new MealInputFragment();
            fragment.setArguments(bundle);

            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        } else if (id == R.id.nav_bazar_input) {
            fragment = new BazarInputFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        }else if (id == R.id.nav_others_input) {
            fragment = new OthersInputFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        }else if (id == R.id.nav_make_manager) {
            fragment = new MakeManagerFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        }else if (id == R.id.nav_add_new_member) {
            fragment = new AddNewMemberFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content,fragment).commit();

        }else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Firebase Auth..............
    @Override
    public void onStart() {
        super.onStart();
        if (currentUser == null) {
            sendToStart();
        }else {
            //Hide Nav Menu Item............
            hideItem();
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }
    private void hideItem()
    {

        // hide menu item for not connected with mess users...........
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staticMessId = dataSnapshot.child("mess").getValue().toString();

                if(staticMessId.equals("defaultValue")){
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_mess_activity).setVisible(false);
                    nav_Menu.findItem(R.id.nav_manager_activity).setVisible(false);
                    nav_Menu.findItem(R.id.nav_billing_activity).setVisible(false);
                    nav_Menu.findItem(R.id.nav_create_mess).setVisible(true);

                }else{

                    DatabaseReference messManagerReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(staticMessId).child("managers");
                   // messManagerReference.keepSynced(true);
                    if (messManagerReference != null) {
                        messManagerReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String primary_manager = dataSnapshot.child("primary").getValue().toString();
                                // String alternative_manager = dataSnapshot.child("alternative").getValue().toString();

                                if (!primary_manager.equals(current_uid)) {

                                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                                    Menu nav_Menu = navigationView.getMenu();
                                    nav_Menu.findItem(R.id.nav_manager_activity).setVisible(false);
                                    nav_Menu.findItem(R.id.nav_billing_activity).setVisible(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //hide menu item for members whor are not manager.....



    }
}
