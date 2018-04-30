package com.example.josimuddin.firebaseloginregisterlict;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "IUBAT";
    private Button btnLogin, btnRegister;
    private TextInputEditText etEmailLogin, etPasswordLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etEmailLogin = findViewById(R.id.email_login);
        etPasswordLogin = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);


        //initialize auth and Authstatelistener...
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                //check whether user not null
                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChange: signed_in:" + user.getUid());
                }else{
                    // user is signed out
                    Log.d(TAG, "onAuthStateChange: signed_out:");

                }
            }
        };



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisgerActivity.class);
                startActivity(registerIntent);
            }
        });

        //for login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                // check whether email and password fields empty or not..
                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    //Showign the progress dialog...
                    progressDialog.setMessage("Logging you in...Please wait...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "You have logged in.",
                                        Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Fillup All the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // check whether authStateListener...
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }
}
