package com.example.josimuddin.emess.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josimuddin.emess.MainActivity;
import com.example.josimuddin.emess.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BazarInputFragment extends Fragment {

    private Calendar mCurrentDate;
    private int day,month,year;


    private TextView tvDate,tvTotalSave,tvTotalCost,tvRestAmount;
    private Button btnSave;
    private EditText etCostAmount,etShopperName;
    private ListView listView;


    private DatabaseReference messDatabaseReference, userDatabaseReference, costDatabaseReference, saveDatabaseReference;
    private FirebaseUser mCurrentUser;


    private String messKey;
    private double totalCost;
    private double totalSave;
    private double restAmount;

    private View mMainView;

    public BazarInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_bazar_input, container, false);

        tvDate = mMainView.findViewById(R.id.tv_date_cost_daily_cost_input);
        tvTotalSave = mMainView.findViewById(R.id.tv_total_save);
        tvTotalCost = mMainView.findViewById(R.id.tv_totoal_cost);
        tvRestAmount = mMainView.findViewById(R.id.tv_rest_amount);
        etCostAmount = mMainView.findViewById(R.id.et_daily_cost_amount);
        etShopperName = mMainView.findViewById(R.id.et_shopper_name);
        btnSave = mMainView.findViewById(R.id.btn_save_daily_cost);

        messDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess");
        messDatabaseReference.keepSynced(true);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        userDatabaseReference.keepSynced(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = mCurrentUser.getUid().toString();

        mCurrentDate  = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        tvDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        userDatabaseReference.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messKey = dataSnapshot.child("mess").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId).child("save");
        saveDatabaseReference.keepSynced(true);

        saveDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalSave = 0.0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        totalSave = totalSave + Double.parseDouble(dataSnapshot2.child("value").getValue().toString());
                    }
                }
                tvTotalSave.setText(""+totalSave);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        costDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId).child("daily_cost");
        costDatabaseReference.keepSynced(true);

        costDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalCost = 0.0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    totalCost = totalCost + Double.parseDouble(dataSnapshot1.child("cost").getValue().toString());
                }

                tvTotalCost.setText(""+totalCost);

                restAmount = totalSave - totalCost;
                tvRestAmount.setText(""+restAmount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etShopperName.getText().toString();
                String date = tvDate.getText().toString();
                String cost = etCostAmount.getText().toString();

                HashMap<String, String> bazarInputMap = new HashMap<>();
                bazarInputMap.put("name", name);
                bazarInputMap.put("cost", cost);

                if (messKey != null) {

                    messDatabaseReference.child(messKey).child("daily_cost").child(date).setValue(bazarInputMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                restAmount = totalSave - totalCost;
                                tvRestAmount.setText(""+restAmount);
                                etShopperName.setText("");
                                etCostAmount.setText("");
                            }
                        }
                    });

                }
            }
        });


        return mMainView;
    }

}
