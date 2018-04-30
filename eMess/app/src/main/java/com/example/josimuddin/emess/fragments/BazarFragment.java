package com.example.josimuddin.emess.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.josimuddin.emess.ClassBazarList;
import com.example.josimuddin.emess.ClassShowAllMembersMealList;
import com.example.josimuddin.emess.MainActivity;
import com.example.josimuddin.emess.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BazarFragment extends Fragment {

    private RecyclerView showBazarList;

    private DatabaseReference bazarDatabaseReference, mUserDatabaseReferecne;
    private String mCurrentUserId;
    private FirebaseAuth mAuth;
    private View mMainView;

    private String messKey;
    private String listBazarId;


    public BazarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_bazar, container, false);

        showBazarList = mMainView.findViewById(R.id.recV_show_bazar_list);
        showBazarList.setHasFixedSize(true);
        showBazarList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().toString();
        mUserDatabaseReferecne = FirebaseDatabase.getInstance().getReference().child("User");
        mUserDatabaseReferecne.keepSynced(true);


        bazarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId).child("daily_cost");
        bazarDatabaseReference.keepSynced(true);

        return mMainView;
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClassBazarList, BazarViewHolder> bazarRecyclerAdapter = new FirebaseRecyclerAdapter<ClassBazarList, BazarViewHolder>(
                ClassBazarList.class,
                R.layout.show_single_bazar_list,
                BazarViewHolder.class,
                bazarDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(BazarViewHolder viewHolder, ClassBazarList model, int position) {
                listBazarId = getRef(position).getKey();

                viewHolder.setDisplayName(model.getName());
                viewHolder.setCost(model.getCost());
                viewHolder.setDate(listBazarId);

            }
        };

        showBazarList.setAdapter(bazarRecyclerAdapter);
    }


    public static class BazarViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public BazarViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String name) {
            TextView displayName = mView.findViewById(R.id.tv_shopper_name);
            displayName.setText(name);
        }
        public void setCost(String cost){
            TextView tvCost = mView.findViewById(R.id.tv_daily_cost);
            tvCost.setText(cost);
        }
        public void setDate(String date){
            TextView tvDate = mView.findViewById(R.id.tv_shopping_date);
            tvDate.setText(date);
        }
    }

}
