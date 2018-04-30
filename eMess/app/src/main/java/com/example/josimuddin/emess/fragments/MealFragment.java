package com.example.josimuddin.emess.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.josimuddin.emess.ClassShowAllMembersMealList;
import com.example.josimuddin.emess.MainActivity;
import com.example.josimuddin.emess.MemberDetailsActivity;
import com.example.josimuddin.emess.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealFragment extends Fragment {

    private Calendar mCurrentDate;
    private int day,month,year;

    private RecyclerView showMealList;
    private TextView tvDate, tvTotalSave, tvTotalMeal, tvTotalCost, tvMealRate;

    private DatabaseReference mealDatabaseReference, mUserDatabaseReferecne, saveDatabaseReference, costDatabaserReference;
    private String mCurrentUserId;
    private FirebaseAuth mAuth;
    private View mMainView;

    private String date;
    private String messKey;
    public static double totlaMeals, totalSave, totalCost, mealRate;
    public MealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_meal, container, false);
        tvDate = mMainView.findViewById(R.id.tv_date_meal_fragment);
        tvTotalSave = mMainView.findViewById(R.id.tv_total_save_meal_fragment);
        tvTotalMeal = mMainView.findViewById(R.id.tv_total_meal_fragment);
        tvTotalCost = mMainView.findViewById(R.id.tv_total_cost_meal_fragment);
        tvMealRate = mMainView.findViewById(R.id.tv_meal_rate_fragment);

        showMealList = mMainView.findViewById(R.id.show_meal_fragment);
        showMealList.setHasFixedSize(true);
        showMealList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        //mCurrentUserId = mAuth.getCurrentUser().toString();
        mUserDatabaseReferecne = FirebaseDatabase.getInstance().getReference().child("User");
        mUserDatabaseReferecne.keepSynced(true);


        mCurrentDate  = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        tvDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                        date = tvDate.getText().toString();
                        onStart();
                    }
                }, year,month,day);
                datePickerDialog.show();
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

        mealDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId).child("meal");
        mealDatabaseReference.keepSynced(true);
        mealDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totlaMeals = 0.0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        totlaMeals = totlaMeals + Double.parseDouble(dataSnapshot2.child("meal_value").getValue().toString());
                    }
                }
                tvTotalMeal.setText(""+totlaMeals);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        costDatabaserReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId).child("daily_cost");
        costDatabaserReference.keepSynced(true);
        costDatabaserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalCost = 0.0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    totalCost = totalCost + Double.parseDouble(dataSnapshot1.child("cost").getValue().toString());
                }

                tvTotalCost.setText(""+totalCost);

                mealRate = totalCost/totlaMeals;
                tvMealRate.setText(new DecimalFormat("##.##").format(mealRate));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        date = tvDate.getText().toString();

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClassShowAllMembersMealList,MealViewHolder> mealRecyclerViewAdapter = new FirebaseRecyclerAdapter<ClassShowAllMembersMealList, MealViewHolder>(
                ClassShowAllMembersMealList.class,
                R.layout.show_all_meal_single_layout,
                MealViewHolder.class,
                mealDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final MealViewHolder viewHolder, ClassShowAllMembersMealList model, int position) {


                //viewHolder.setMeal(model.getMeal());
                final String listUserId = getRef(position).getKey();
                mUserDatabaseReferecne.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String displayName = dataSnapshot.child("name").getValue().toString();
                        String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                        viewHolder.setDisplayName(displayName);
                        viewHolder.setThumbImage(thumbImage, getContext());


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mealDatabaseReference.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(date)) {
                            String meal = dataSnapshot.child(date).child("meal_value").getValue().toString();
                            viewHolder.setMeal(meal);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            final String userId = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent memberDetailsIntent = new Intent(getContext(), MemberDetailsActivity.class);
                        memberDetailsIntent.putExtra("UserId", userId);
                        startActivity(memberDetailsIntent);

                    }
                });



            }
        };

        showMealList.setAdapter(mealRecyclerViewAdapter);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MealViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String name) {
            TextView displayName = mView.findViewById(R.id.tv_display_name_all_meal);
            displayName.setText(name);
        }
        public void setThumbImage(final String thumImage, final Context context){
            final CircleImageView thumbImageView = mView.findViewById(R.id.iv_thumbnails_all_meal);
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
        public void setMeal(String meal){
            TextView tvMeal = mView.findViewById(R.id.tv_meal_all_meal);
            tvMeal.setText(meal);
        }
    }
}
