package com.example.josimuddin.emess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView tvLogoName;
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvRegisterHere;
    private Animation alpha, fromBottom, fromTop;

    private ProgressDialog progressDialog;
    //firebase....
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        //view component initialization..........
        imgLogo = findViewById(R.id.iv_logo_start);
        tvLogoName = findViewById(R.id.tv_logodesc_start);
        etEmail = findViewById(R.id.et_login_email_start);
        etPassword = findViewById(R.id.et_login_pasword_start);
        btnLogin = findViewById(R.id.btn_login_start);
        tvForgotPassword = findViewById(R.id.tv_forgot_password_start);
        tvRegisterHere = findViewById(R.id.tv_register_here_start);

        //progress dialog...
        progressDialog = new ProgressDialog(this, R.style.progressDialogStyle);


        //fire base....
        firebaseAuth = FirebaseAuth.getInstance();
        //initializing animation...........
         alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(3000);
         fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
         fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        imgLogo.setAnimation(fromTop);
        tvLogoName.setAnimation(alpha);
        etEmail.setAnimation(alpha);
        etPassword.setAnimation(alpha);

        btnLogin.setAnimation(fromBottom);
        tvForgotPassword.setAnimation(fromBottom);
        tvRegisterHere.setAnimation(fromBottom);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getEditText().getText().toString();
                String password = etPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    progressDialog.setTitle("Logging In");
                    progressDialog.setMessage("Please wait while we check your credentials.");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    loginUser(email, password);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartActivity.this, "Implement Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void loginUser(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else {
                    progressDialog.hide();
                    Toast.makeText(StartActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
