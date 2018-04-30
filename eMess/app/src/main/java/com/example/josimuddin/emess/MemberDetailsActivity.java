package com.example.josimuddin.emess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.josimuddin.emess.fragments.MealFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDetailsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView tvDisplayName, tvEmail,tvAddress, tvPhoneSelf, tvPhoneHome, tvTotalSave, tvTotalMeal, tvTotalPaid, tvRestAmount;
    private ImageButton imgBtnPhoneSelf, imgBtnPhoneHome;
    private ListView listView_meal_list;

    private ArrayList<String> arrayList_meal = new ArrayList<>();


    private DatabaseReference mUserDatabaseReference, messDetabaseReference;
    private String messId;
    private double personalSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        profileImage = findViewById(R.id.iv_member_details);
        tvDisplayName = findViewById(R.id.tv_display_name_member_details);
        tvEmail = findViewById(R.id.tv_email_member_details);
        tvPhoneSelf = findViewById(R.id.tv_phone_self_member_details);
        tvPhoneHome = findViewById(R.id.tv_phone_home_member_details);
        tvAddress = findViewById(R.id.tv_address_member_details);
        tvTotalSave = findViewById(R.id.tv_saved_amount);
        tvTotalMeal = findViewById(R.id.tv_total_meal_single);
        tvTotalPaid = findViewById(R.id.tv_paid_amount);
        tvRestAmount = findViewById(R.id.tv_rest_amount_single);
        imgBtnPhoneSelf = findViewById(R.id.img_btn_call_self);
        imgBtnPhoneHome = findViewById(R.id.img_btn_call_home);
        listView_meal_list = findViewById(R.id.lv_meal_list_member_details);


        final String user_id = getIntent().getStringExtra("UserId");
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        mUserDatabaseReference.keepSynced(true);

        messDetabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess");
        messDetabaseReference.keepSynced(true);

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phoneSelf = dataSnapshot.child("phone_self").getValue().toString();
                String phoneHome = dataSnapshot.child("phone_home").getValue().toString();
                final String image = dataSnapshot.child("profile_pic").getValue().toString();
                String addr = dataSnapshot.child("address").getValue().toString();
                messId = dataSnapshot.child("mess").getValue().toString();

                tvDisplayName.setText(name);
                tvEmail.setText(email);
                tvPhoneSelf.setText(phoneSelf);
                tvPhoneHome.setText(phoneHome);
                tvAddress.setText(addr);
                Picasso.with(MemberDetailsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(profileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(MemberDetailsActivity.this).load(image).placeholder(R.drawable.placeholder).into(profileImage);
                    }
                });


                messDetabaseReference.child(messId).child("save").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        personalSaved = 0.0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            personalSaved = personalSaved+Double.parseDouble(dataSnapshot1.child("value").getValue().toString());
                        }
                        tvTotalSave.setText(""+personalSaved);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                final ArrayAdapter<String> arrayAdapterMealList = new ArrayAdapter<String>(MemberDetailsActivity.this, android.R.layout.simple_list_item_1, arrayList_meal);
                listView_meal_list.setAdapter(arrayAdapterMealList);

                messDetabaseReference.child(messId).child("meal").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double tMeals = 0.0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String value = dataSnapshot1.child("meal_value").getValue().toString();
                            tMeals = tMeals+Double.parseDouble(value);
                            String cld = dataSnapshot1.getKey().toString();
                            arrayList_meal.add(cld+getString(R.string.tab)+value);
                            arrayAdapterMealList.notifyDataSetChanged();
                        }

                        tvTotalMeal.setText(""+tMeals);

                        double totalPaid =tMeals*MealFragment.mealRate;
                        tvTotalPaid.setText(new DecimalFormat("##.##").format(totalPaid));
                        double restAmount = personalSaved - totalPaid;
                        tvRestAmount.setText(new DecimalFormat("##.##").format(restAmount));
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
