package com.example.josimuddin.firebaseloginregisterlict;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisgerActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnSignUp;
    private TextView tvAllreadyRegistered;
   private FirebaseAuth mAuth;
   private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisger);

        //initializing the views....
        etEmail = findViewById(R.id.email_register);
        etPassword = findViewById(R.id.password_register);
        btnSignUp = findViewById(R.id.btn_signup);
        tvAllreadyRegistered = findViewById(R.id.tv_allready_register);

        progressDialog = new ProgressDialog(this);

        //Initialize firebase auth.....
        mAuth = FirebaseAuth.getInstance();

        tvAllreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(RegisgerActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        //attaching listener to the button..
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                // check whether email and password empty or not
                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    //Showign the progress dialog...
                    progressDialog.setMessage("Registring User...Please wait...");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisgerActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent mainIntent = new Intent(RegisgerActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisgerActivity.this, "Failed register...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else {
                    Toast.makeText(RegisgerActivity.this, "Please Fillup All the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
