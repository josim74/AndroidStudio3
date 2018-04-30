package com.example.josimuddin.emess;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class InputMoneyActivity extends AppCompatActivity {

    private Calendar mCurrentDate;
    private int day,month,year;

    private CircleImageView circleImageView;
    private TextView displayName, email, phoneSelf,totalSave,selectDate;
    private EditText etInsertAmount;
    private Button btnSubmit, btn_make_manager;
    private ImageButton btnCall;

    private DatabaseReference messDatabaseReference, userDatabaseReference;
    private FirebaseUser currentUSer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_money);

        final String user_id = getIntent().getStringExtra("UserId");

        circleImageView = findViewById(R.id.civ_profile_pic);
        displayName = findViewById(R.id.tv_display_name_input_money);
        email = findViewById(R.id.tv_email_input_money);
        phoneSelf = findViewById(R.id.tv_phone_input_money);
        etInsertAmount = findViewById(R.id.et_amount_input_money);
        selectDate = findViewById(R.id.tv_date_input_money);
        btnSubmit = findViewById(R.id.btn_submit_input_money);
        btnCall = findViewById(R.id.iv_phone_call_input_money);
        totalSave = findViewById(R.id.tv_total_save_input_money);
        btn_make_manager = findViewById(R.id.btn_make_manager);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        userDatabaseReference.keepSynced(true);

        currentUSer = FirebaseAuth.getInstance().getCurrentUser();

        messDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess");
        messDatabaseReference.keepSynced(true);

        mCurrentDate  = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        btn_make_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Change Manager")
                        .setMessage("This person will be the manager instead of you. Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                                messDatabaseReference.child(MainActivity.staticMessId).child("managers").child("primary").setValue(user_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            messDatabaseReference.child(MainActivity.staticMessId).child("managers").child("temporary").setValue(currentUSer.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(InputMoneyActivity.this, "New Manager has created and you are also a temporary manager", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }})
                        .setNegativeButton("Cancel", null).show();

            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InputMoneyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        selectDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });


        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String mail = dataSnapshot.child("email").getValue().toString();
                String phone = dataSnapshot.child("phone_self").getValue().toString();
                final String image = dataSnapshot.child("profile_pic").getValue().toString();
                final String messId = dataSnapshot.child("mess").getValue().toString();

                messDatabaseReference.child(messId).child("save").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double sum = 0.0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            sum = sum+Double.parseDouble(dataSnapshot1.child("value").getValue().toString());
                        }
                        totalSave.setText(""+sum);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                displayName.setText(name);
                email.setText(mail);
                phoneSelf.setText(phone);
                Picasso.with(InputMoneyActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(circleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(InputMoneyActivity.this).load(image).placeholder(R.drawable.placeholder).into(circleImageView);
                    }
                });


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = selectDate.getText().toString();
                        String amount = etInsertAmount.getText().toString();
                            if (!date.equals("Select Date") && amount != null) {
                                messDatabaseReference.child(messId).child("save").child(user_id).child(date).child("value").setValue(amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(InputMoneyActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                            etInsertAmount.setText("");
                                        }else {
                                            Toast.makeText(InputMoneyActivity.this, "Not Saved! Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(InputMoneyActivity.this, "Please Select Date and Provide Amount", Toast.LENGTH_LONG).show();
                            }
                        }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
