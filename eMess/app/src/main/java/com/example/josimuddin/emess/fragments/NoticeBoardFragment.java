package com.example.josimuddin.emess.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.josimuddin.emess.ClassNoticeBoard;
import com.example.josimuddin.emess.ClassShowAllMembersMealList;
import com.example.josimuddin.emess.MainActivity;
import com.example.josimuddin.emess.MemberDetailsActivity;
import com.example.josimuddin.emess.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeBoardFragment extends Fragment {


    private View mMainView;
    private CircleImageView circleImageView;
    private EditText etMessage;
    private ImageButton btnSend;
    private RecyclerView rvMessageList;

    private DatabaseReference messageDatabaserReference, messageRetrieveDatabaseReference, userDatabaseReference;
    private FirebaseUser mCurrentUser;
    private Map<String, Object> messageMap;
    private static int pos;

    public NoticeBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_notice_board, container, false);
        circleImageView = mMainView.findViewById(R.id.cimg_profile_pice_signle_notice);
        etMessage = mMainView.findViewById(R.id.et_message_notice);
        btnSend = mMainView.findViewById(R.id.btn_send_notice);

        rvMessageList = mMainView.findViewById(R.id.rv_message_list);
        rvMessageList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        rvMessageList.setLayoutManager(layoutManager);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        userDatabaseReference.keepSynced(true);

        messageDatabaserReference = FirebaseDatabase.getInstance().getReference().child("Mess").child(MainActivity.staticMessId);
        messageDatabaserReference.keepSynced(true);

        messageRetrieveDatabaseReference = messageDatabaserReference.child("message");
        messageRetrieveDatabaseReference.keepSynced(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageMap = new HashMap<>();
                messageMap.put("message", etMessage.getText().toString());
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("sender", mCurrentUser.getUid());
                messageDatabaserReference.child("message").push().setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            etMessage.setText("");
                            rvMessageList.smoothScrollToPosition(pos+1);
                        }
                    }
                });
            }
        });

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ClassNoticeBoard,NoticeBoardFragment.MealViewHolder> mealRecyclerViewAdapter = new FirebaseRecyclerAdapter<ClassNoticeBoard, NoticeBoardFragment.MealViewHolder>(
                ClassNoticeBoard.class,
                R.layout.single_notice_layout,
                NoticeBoardFragment.MealViewHolder.class,
                messageRetrieveDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final NoticeBoardFragment.MealViewHolder viewHolder, ClassNoticeBoard model, final int position) {

                pos = position;
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(model.getTime());
                String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

                viewHolder.setTime(""+date);
                viewHolder.setMessage(model.getMessage());
                final String userId = model.getSender();


               // final String listUserId = getRef(position).getKey();
                userDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String displayName = dataSnapshot.child("name").getValue().toString();
                        String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                        viewHolder.setDisplayName(displayName);
                        viewHolder.setThumbImage(thumbImage, getContext());

                        //rvMessageList.smoothScrollToPosition(position);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.mView.bringToFront();

            }
        };

        rvMessageList.setAdapter(mealRecyclerViewAdapter);
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MealViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMessage(String message) {
            TextView tvMessage = mView.findViewById(R.id.tv_message_signle_notice);
            tvMessage.setText(message);
        }

        public void setTime(String time) {
            TextView tvTime = mView.findViewById(R.id.tv_time_signle_notice);
            tvTime.setText(time);
        }

        public void setDisplayName(String name) {
            TextView tvName = mView.findViewById(R.id.tv_name_signle_notice);
            tvName.setText(name);
        }
        public void setThumbImage(final String thumImage, final Context context){
            final CircleImageView thumbImageView = mView.findViewById(R.id.cimg_profile_pice_signle_notice);
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
