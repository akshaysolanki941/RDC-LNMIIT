package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity {

    EditText name_edittext, email_edittext, pwd_edittext;
    Button btn_register;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseRef;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    CardView c1, c2, c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_edittext = (EditText) findViewById(R.id.name_edittext);
        email_edittext = (EditText) findViewById(R.id.email_edittext);
        pwd_edittext = (EditText) findViewById(R.id.pwd_edittext);
        btn_register = (Button) findViewById(R.id.btn_register);
        c1 = (CardView) findViewById(R.id.c1);
        c2 = (CardView) findViewById(R.id.c2);
        c3 = (CardView) findViewById(R.id.c3);
        loading_dialog = new Dialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Profile");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(name_edittext.getText()) && !TextUtils.isEmpty(email_edittext.getText()) && !TextUtils.isEmpty(pwd_edittext.getText())) {
                    registerUser();
                } else {
                    Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);

                    if (TextUtils.isEmpty(name_edittext.getText())) {
                        c1.startAnimation(shake);
                    }

                    if (TextUtils.isEmpty(email_edittext.getText())) {
                        c2.startAnimation(shake);
                    }

                    if (TextUtils.isEmpty(pwd_edittext.getText())) {
                        c3.startAnimation(shake);
                    }
                }

            }
        });
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    public void registerUser() {

        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        final String email = email_edittext.getText().toString();
        String pwd = pwd_edittext.getText().toString();
        final String userName = name_edittext.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = firebaseAuth.getCurrentUser();

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String uid = user.getUid();

                                        UsersModel usersModel = new UsersModel(uid, userName, email);

                                        databaseRef.child(uid).setValue(usersModel);
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        Toast.makeText(RegisterActivity.this, "Verification Email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Verification Email NOT sent.", Toast.LENGTH_LONG).show();
                                        overridePendingTransition(0, 0);
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                    }
                                }
                            });


                        } else {
                            loading_dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registeration failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading_dialog.dismiss();

                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());

                Toast.makeText(RegisterActivity.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent a = new Intent(RegisterActivity.this, CategoriesActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

    }
}
