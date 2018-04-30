package com.example.josimuddin.emess;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView imageViewUser;
    TextView tvDisplayName, tvEmail, tvConnectState;
    private Button btnAdd;

    private DatabaseReference mUserDatabase;
    private ProgressDialog progressDialog;
    private DatabaseReference mAddRequestDatabase, mGetMessIdDatabase;
    private FirebaseUser mCurrentUser;
    private String mCurrentState;
    private String messID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        final String user_id = getIntent().getStringExtra("userId");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        mUserDatabase.keepSynced(true);

        mAddRequestDatabase = FirebaseDatabase.getInstance().getReference().child("connection_request");
        mAddRequestDatabase.keepSynced(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mGetMessIdDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(mCurrentUser.getUid());
        mGetMessIdDatabase.keepSynced(true);


        imageViewUser = findViewById(R.id.iv_user_profile);
        tvDisplayName = findViewById(R.id.tv_user_name);
        tvEmail = findViewById(R.id.tv_user_email);
        tvConnectState = findViewById(R.id.tv_user_connected_state);
        btnAdd = findViewById(R.id.btn_add);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading User Data");
        progressDialog.setMessage("Pleas wait while we load the user data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mCurrentState = "not_connected";
// set View values........................................................
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("profile_pic").getValue().toString();
                String messName = dataSnapshot.child("mess").getValue().toString();

                tvDisplayName.setText(name);
                tvEmail.setText(email);
                Picasso.with(UserDetailsActivity.this).load(image).placeholder(R.drawable.placeholder).into(imageViewUser);

                if(!messName.equals("defaultValue")){
                    btnAdd.setVisibility(View.GONE);
                    tvConnectState.setText("This person is already connected to a mess.");
                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


// retrieve mess ID from Manager........................................
        mGetMessIdDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messID = dataSnapshot.child("mess").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setEnabled(false);
                // --------------------not connected state-----------------------
                if (mCurrentState.equals("not_connected")) {
                    mAddRequestDatabase.child(user_id).child("request_from").setValue(messID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                            btnAdd.setEnabled(true);
                                            mCurrentState = "request_sent";
                                            btnAdd.setText("Cancel");
                            }else {
                                Toast.makeText(UserDetailsActivity.this, "Faild to send Connect request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                //-----------------------cancel Connect request-----------------------
                if (mCurrentState.equals("request_sent")) {
                    mAddRequestDatabase.child(user_id).child("request_from").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(UserDetailsActivity.this, "Request Canceled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
