package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    CardView cv_loginEmail, cv_loginPwd;
    Button btn_login;
    TextView register_tv;
    EditText login_email_edittext, login_pwd_edittext;
    FirebaseAuth mAuth;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cv_loginEmail = (CardView) findViewById(R.id.cv_loginEmail);
        cv_loginPwd = (CardView) findViewById(R.id.cv_loginPwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        register_tv = (TextView) findViewById(R.id.register_textView);
        login_email_edittext = (EditText) findViewById(R.id.login_email_edittext);
        login_pwd_edittext = (EditText) findViewById(R.id.login_pwd_edittext);

        mAuth = FirebaseAuth.getInstance();

        loading_dialog = new Dialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(login_email_edittext.getText()) && !TextUtils.isEmpty(login_pwd_edittext.getText())) {
                    loginUser();
                } else {
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);

                    if (TextUtils.isEmpty(login_email_edittext.getText())) {
                        cv_loginEmail.startAnimation(shake);
                    }

                    if (TextUtils.isEmpty(login_pwd_edittext.getText())) {
                        cv_loginPwd.startAnimation(shake);
                    }
                }

            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    public void loginUser(){

        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        String email = login_email_edittext.getText().toString();
        String pwd = login_pwd_edittext.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading_dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
