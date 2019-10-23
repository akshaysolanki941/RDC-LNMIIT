package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.Models.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText name_edittext, email_edittext, pwd_edittext;
    Button btn_register;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseRef;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    CardView c1, c2, c3;
    RelativeLayout relative_register;
    AnimationDrawable animationDrawable;
    TextView login_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        relative_register = (RelativeLayout)findViewById(R.id.relative_register);
        animationDrawable = (AnimationDrawable) relative_register.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);

        name_edittext = (EditText) findViewById(R.id.name_edittext);
        email_edittext = (EditText) findViewById(R.id.email_edittext);
        pwd_edittext = (EditText) findViewById(R.id.pwd_edittext);
        btn_register = (Button) findViewById(R.id.btn_register);
        login_textView = (TextView) findViewById(R.id.login_textView);
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

        login_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
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

                                        final String uid = user.getUid();

                                        databaseRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.exists()) {

                                                    UsersModel usersModel = new UsersModel(uid, userName, email);

                                                    databaseRef.child(uid).setValue(usersModel);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


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
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
