package com.example.josimuddin.emess.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.josimuddin.emess.MainActivity;
import com.example.josimuddin.emess.R;
import com.example.josimuddin.emess.RegisterActivity;
import com.example.josimuddin.emess.UserDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessCreateFragment extends Fragment {

    private View mMainView;
    private Button btnCreateMess;
    private EditText etMessName;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference messDatabaseReference, connectionRequestDatabaseReference;
    private Fragment fragment;
    private FragmentTransaction ft;

    String current_uid;

    public MessCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_mess_create, container, false);
        etMessName = mMainView.findViewById(R.id.et_mess_name_fragment);
        btnCreateMess = mMainView.findViewById(R.id.btn_create_mess_fragment);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        current_uid = firebaseUser.getUid();
        messDatabaseReference = FirebaseDatabase.getInstance().getReference();
        messDatabaseReference.keepSynced(true);

        connectionRequestDatabaseReference = FirebaseDatabase.getInstance().getReference().child("connection_request");
        connectionRequestDatabaseReference.keepSynced(true);

        btnCreateMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etMessName.getText().toString();
                if(name != null){
                    createMess(name, v);
                }else {
                    Toast.makeText(getContext(), "Please give a name for your mess", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return mMainView;
    }
    private void createMess(final String messName, final View view) {

        messDatabaseReference.child("Mess").child(current_uid).child("mess_name").setValue(messName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    messDatabaseReference.child("Mess").child(current_uid).child("managers").child("primary").setValue(current_uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                messDatabaseReference.child("Mess").child(current_uid).child("members").child(current_uid).child("connection_date").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(current_uid).child("mess");
                                                            mUserDatabase.keepSynced(true);

                                            mUserDatabase.setValue(current_uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //........remove connection Request..............
                                                    connectionRequestDatabaseReference.child(current_uid).child("request_from").removeValue();

                                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                                    view.getContext().startActivity(intent);

                                                    /*Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);*/

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Something is Wrong to create mess", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
