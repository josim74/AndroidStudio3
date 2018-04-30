package com.example.josimuddin.emess.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.josimuddin.emess.R;
import com.example.josimuddin.emess.UserDetailsActivity;
import com.example.josimuddin.emess.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewMemberFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mUserDatabase;

    private View mMainView;
    public AddNewMemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_add_new_member, container, false);

        recyclerView = mMainView.findViewById(R.id.lv_users_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mUserDatabase.keepSynced(true);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> removeFirebaseRecyclerAdapterItems;
        final FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class, R.layout.users_single_layout,UsersViewHolder.class, mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                String messKey = model.getMess();
                if (messKey.equals("defaultValue")){

                    viewHolder.setName(model.getName());
                    viewHolder.setEmail(model.getEmail());
                    viewHolder.setthumbImage(model.getthumb_image(), getContext());

                    final String userId = getRef(position).getKey();


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent userDetailsIntent = new Intent(getActivity(), UserDetailsActivity.class);
                            userDetailsIntent.putExtra("userId", userId);
                            startActivity(userDetailsIntent);
                        }
                    });

                }else {
                    viewHolder.setVisibilityToLayout();
                }

            }

        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }
        public void setName(String name){
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
        public void setEmail(String email){
            TextView userEmailView = mView.findViewById(R.id.user_single_email);
            userEmailView.setText(email);
        }
        public void setVisibilityToLayout(){
            LinearLayout linearLayout = mView.findViewById(R.id.users_single_list);
            linearLayout.setVisibility(View.GONE);
        }
        public void setthumbImage(final String thumImage, final Context context){
            final CircleImageView thumbImageView = mView.findViewById(R.id.iv_thumbnails);
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
