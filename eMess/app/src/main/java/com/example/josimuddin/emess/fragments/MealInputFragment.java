package com.example.josimuddin.emess.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josimuddin.emess.ClassInputMealList;
import com.example.josimuddin.emess.InputMoneyActivity;
import com.example.josimuddin.emess.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealInputFragment extends Fragment {

    private Calendar mCurrentDate;
    private int day,month,year;
    DatePickerDialog datePickerDialog;

    private RecyclerView mealInputList;
    private DatabaseReference messDatabaseReference, userDatabaseReference;
    private FirebaseUser mCurrentUser;
    private Button btnSubmit;
    private TextView tvDatePicker;

    private View mMainView;

    private Map hashMapMealData;
    //private String listUserId;


    public MealInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String messKey = getArguments().getString("mess_id");

        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_meal_input, container, false);


        btnSubmit = mMainView.findViewById(R.id.btn_submit);
        tvDatePicker = mMainView.findViewById(R.id.tv_date_meal_input);


        mealInputList = mMainView.findViewById(R.id.rv_meal_input_list);
        mealInputList.setHasFixedSize(true);
        mealInputList.setLayoutManager(new LinearLayoutManager(getContext()));

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        userDatabaseReference.keepSynced(true);

        messDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(messKey).child("members");
        messDatabaseReference.keepSynced(true);

        hashMapMealData = new HashMap();
        mCurrentDate  = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        tvDatePicker.setText(year+"-"+monthOfYear+"-"+dayOfMonth);

                        hashMapMealData.clear();
                        onStart();
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });






        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(messKey).child("meal");
                messDatabaseReference.keepSynced(true);
                if (!tvDatePicker.getText().toString().equals("Select Date")) {
                    messDatabaseReference.updateChildren(hashMapMealData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getActivity(), "Data is Saved", Toast.LENGTH_SHORT).show();
                            }else {
                                String error = databaseError.getMessage();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "Select Date First", Toast.LENGTH_LONG).show();
                }
            }
        });

        return mMainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClassInputMealList, MembersViewHolders> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClassInputMealList, MembersViewHolders>(
                ClassInputMealList.class, R.layout.members_single_layout, MembersViewHolders.class, messDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final MembersViewHolders viewHolder, ClassInputMealList model, final int position) {

                final String listUserId = getRef(position).getKey();

                userDatabaseReference.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String displayName = dataSnapshot.child("name").getValue().toString();
                        String phoneSelf = dataSnapshot.child("phone_self").getValue().toString();
                        String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder.setDisplayName(displayName);
                        viewHolder.setPhoneSelf(phoneSelf);
                        viewHolder.setthumbImage(thumbImage, getContext());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double mValue = Double.parseDouble(viewHolder.tvMealNumbers.getText().toString());
                        mValue = mValue+0.5;
                        viewHolder.tvMealNumbers.setText(""+mValue);
                    }
                });

                viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double mValue = Double.parseDouble(viewHolder.tvMealNumbers.getText().toString());
                        if (mValue < 0.5) {
                            mValue = 0;
                            viewHolder.tvMealNumbers.setText(""+mValue);
                        }else {
                            mValue = mValue-0.5;
                            viewHolder.tvMealNumbers.setText(""+mValue);
                        }
                    }
                });

                viewHolder.tvMealNumbers.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        //getMealNumer = s.toString();
                        //hashMapMealData.put(listUserId, s.toString());

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // getMealNumer = s.toString();
                        hashMapMealData.put(listUserId+"/"+tvDatePicker.getText().toString()+"/"+"meal_value", s.toString());

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }


                });

                hashMapMealData.put(listUserId+"/"+tvDatePicker.getText().toString()+"/"+"meal_value", viewHolder.tvMealNumbers.getText().toString());

//........open input money activity............

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inputMoneyIntent = new Intent(getActivity(), InputMoneyActivity.class);
                        inputMoneyIntent.putExtra("UserId", listUserId);
                        startActivity(inputMoneyIntent);

                    }
                });


                mealInputList.smoothScrollToPosition(position+1);

            }

            public void onBindViewHolder(MembersViewHolders viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);


            }
        };
        mealInputList.setAdapter(firebaseRecyclerAdapter);

    }



    public static class MembersViewHolders extends RecyclerView.ViewHolder{
        View mView;
        Button btnPlus;
        Button btnMinus;
        TextView tvMealNumbers;
        public MembersViewHolders(View itemView) {
            super(itemView);
            mView = itemView;
            btnPlus = mView.findViewById(R.id.btn_plus_members);
            btnMinus = mView.findViewById(R.id.btn_minus_members);
            tvMealNumbers = mView.findViewById(R.id.tv_meal_numbers);
        }
        public void setDisplayName(String name) {
            TextView displayName = mView.findViewById(R.id.tv_display_name_members);
            displayName.setText(name);
        }
        public void setPhoneSelf(String phone) {
            TextView phoneSelf = mView.findViewById(R.id.tv_phone_members);
            phoneSelf.setText(phone);
        }

        public void setthumbImage(final String thumImage, final Context context){
            final CircleImageView thumbImageView = mView.findViewById(R.id.iv_thumbnails_members);
            Picasso.with(context).load(thumImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(thumbImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(thumImage).placeholder(R.drawable.placeholder).into(thumbImageView);
                }
            });
        }



    }
}
