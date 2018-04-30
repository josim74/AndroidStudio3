package com.example.josimuddin.emess;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //firebase...
    private DatabaseReference mUserDatabase;
    private  DatabaseReference mConnectionRequestDatabase;
    private  DatabaseReference messDatabaseReference;
    private FirebaseUser mCurrentUser;

    //views...
    private CircleImageView profileImage;
    private TextView tvDisplayName, tvEmail, tvPhoneSelf, tvPhoneHome, tvAddress, tvConnectedMess, tvConnectionRequest;
    private Button btnAccept;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //initailizing views...
        profileImage = findViewById(R.id.profile_image);
        tvDisplayName = findViewById(R.id.tv_display_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhoneSelf = findViewById(R.id.tv_phone_self);
        tvPhoneHome = findViewById(R.id.tv_phone_home);
        tvAddress = findViewById(R.id.tv_address);
        tvConnectedMess = findViewById(R.id.tv_connected_mess);
        btnAccept = findViewById(R.id.btn_accept_or_remove);
        tvConnectionRequest = findViewById(R.id.tv_connection_request);


        //initializing firebase...
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mConnectionRequestDatabase = FirebaseDatabase.getInstance().getReference().child("connection_request");
        mConnectionRequestDatabase.keepSynced(true);

        messDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess");
        messDatabaseReference.keepSynced(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phoneSelf = dataSnapshot.child("phone_self").getValue().toString();
                String phoneHome = dataSnapshot.child("phone_home").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                final String image = dataSnapshot.child("profile_pic").getValue().toString();
                String connectedMessId = dataSnapshot.child("mess").getValue().toString();

                tvDisplayName.setText(name);
                tvEmail.setText(email);
                tvPhoneSelf.setText(phoneSelf);
                tvPhoneHome.setText(phoneHome);
                tvAddress.setText(address);
                if(!image.equals("default")){
                    Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.placeholder).into(profileImage);
                        }
                    });
                }

                //Retrieve mess name................................

                if(!connectedMessId.equals("defaultValue")){

                    messDatabaseReference.child(connectedMessId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String messName = dataSnapshot.child("mess_name").getValue().toString();
                            tvConnectedMess.setText(messName);
                            btnAccept.setVisibility(View.GONE);
                            tvConnectionRequest.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    tvConnectedMess.setText("No Connected Mess");
                }
                //--------------------------------------accept/Cancel/getREmove----------------------------------
                mConnectionRequestDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("request_from")) {
                            final String request_from = dataSnapshot.child("request_from").getValue().toString();

                            if (request_from != null){
                                btnAccept.setText("Accept Request");
                                btnAccept.setVisibility(View.VISIBLE);
                                tvConnectionRequest.setVisibility(View.VISIBLE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                        messDatabaseReference.child(request_from).child("members").child(mCurrentUser.getUid()).child("connection_date").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mConnectionRequestDatabase.child(mCurrentUser.getUid()).child("request_from").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mUserDatabase.child("mess").setValue(request_from).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        Toast.makeText(ProfileActivity.this, "You have Connected to mess successfully", Toast.LENGTH_SHORT).show();

                                                                        //open new intent....
                                                                        Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                                                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(mainIntent);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
