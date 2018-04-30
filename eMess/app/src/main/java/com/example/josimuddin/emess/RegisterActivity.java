package com.example.josimuddin.emess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class RegisterActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private TextInputLayout etFullName, etPhoneSelf, etPhoneHome, etaddress, etEmail, etPassword;
    private Button btnRegister, btnSelectProfilePic;

    private ProgressDialog progressBarDialog;

    //Croped image uri.........
    private Uri resultUri;
    //Download uri of profile pic......
    private String download_url;
    HashMap<String, String> userMap;

    //Fire base....
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private StorageReference mImageStorage;
    private FirebaseUser mCurrentUser;

    //bitmap thumbnail...
    private Bitmap thumbBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.et_full_name_register);
        etPhoneSelf = findViewById(R.id.et_phone_self_register);
        etPhoneHome = findViewById(R.id.et_phone_home_register);
        etEmail = findViewById(R.id.et_email_register);
        etPassword = findViewById(R.id.et_pasword_register);
        etaddress = findViewById(R.id.et_address_register);
        btnSelectProfilePic = findViewById(R.id.btn_select_profile_pic);
        btnRegister = findViewById(R.id.btn_register);

        progressBarDialog = new ProgressDialog(this, R.style.progressDialogStyle);

        //initializing Firebase authentication............
        firebaseAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Image picking and Cropping...
        btnSelectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK );
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etFullName.getEditText().getText().toString();
                String phoneSelf = etPhoneSelf.getEditText().getText().toString();
                String phoneHome = etPhoneHome.getEditText().getText().toString();
                String address = etaddress.getEditText().getText().toString();
                String email = etEmail.getEditText().getText().toString();
                String password = etPassword.getEditText().getText().toString();
                String pickProfilePicture = btnSelectProfilePic.getText().toString();
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneSelf) && !TextUtils.isEmpty(phoneHome) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(address) && !pickProfilePicture.equals("pick a profile picture")) {

                        progressBarDialog.setTitle("Registring User");
                        progressBarDialog.setMessage("Please wait while we create your account");
                        progressBarDialog.setCanceledOnTouchOutside(false);
                        progressBarDialog.show();

                        userRegister(name, phoneSelf, phoneHome, address, email, password);
                    }else {
                        Toast.makeText(RegisterActivity.this, "Please fill up " +
                                "all the fields and pick profile picture", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

    //Overriding Image Cropping method......
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1).setMinCropWindowSize(500, 500).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                btnSelectProfilePic.setText(resultUri.toString());

                File thumbFilePath = new File(resultUri.getPath());
                try {
                    thumbBitmap = new Compressor(this).setMaxWidth(100).setMaxHeight(100).setQuality(75).compressToBitmap(thumbFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Log.d("BEFORE", download_url+"Berof message");
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void userRegister(final String name, final String phoneSelf, final String phoneHome, final String address, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            final String uid = currentUser.getUid();
                            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                            firebaseDatabase.keepSynced(true);

                            userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("phone_self", phoneSelf);
                            userMap.put("phone_home", phoneHome);
                            userMap.put("address",address );
                            userMap.put("mess","defaultValue");

                            // putting the profile pic in storage.... and getting the download uri of profile pic...to put in database....
                            if(resultUri != null){
                                ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
                                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
                                final byte[] thumbByte = byteArrayInputStream.toByteArray();

                                StorageReference filePath = mImageStorage.child("profile_images").child(uid+".jpg");
                                final StorageReference thumbFilePath = mImageStorage.child("profile_images").child("thumbs").child(uid+".jpg");

                                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            download_url = task.getResult().getDownloadUrl().toString();
                                            UploadTask uploadTask = thumbFilePath.putBytes(thumbByte);
                                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {


                                                    if (thumbTask.isSuccessful()) {
                                                        String thumbDownloadUrl = thumbTask.getResult().getDownloadUrl().toString();

                                                        firebaseDatabase.child("profile_pic").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (!task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Error in saving profile image url to database", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        firebaseDatabase.child("thumb_image").setValue(thumbDownloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (!task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Error in saving thumb url to database", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }else {
                                                        Toast.makeText(RegisterActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_SHORT).show();
                                                        progressBarDialog.dismiss();
                                                    }

                                                }
                                            });
                                        }else {
                                            Toast.makeText(RegisterActivity.this, "Error is occured", Toast.LENGTH_SHORT).show();
                                            progressBarDialog.dismiss();
                                        }
                                    }
                                });
                            }else {

                                userMap.put("profile_pic","default");
                                userMap.put("thumb_image","default");
                            }


                            firebaseDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //ProgressBarDialog dismis...
                                    progressBarDialog.dismiss();
                                    //open new intent....
                                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            });
                        } else {
                            progressBarDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Cannot Sign in. Please check the form and try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
