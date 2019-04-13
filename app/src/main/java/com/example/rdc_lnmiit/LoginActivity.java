package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {

    CardView cv_loginEmail, cv_loginPwd;
    Button btn_login;
    TextView register_tv;
    EditText login_email_edittext, login_pwd_edittext;
    FirebaseAuth mAuth;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    SignInButton google_signIn_btn;
    private static final int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        cv_loginEmail = (CardView) findViewById(R.id.cv_loginEmail);
        cv_loginPwd = (CardView) findViewById(R.id.cv_loginPwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        register_tv = (TextView) findViewById(R.id.register_textView);
        login_email_edittext = (EditText) findViewById(R.id.login_email_edittext);
        login_pwd_edittext = (EditText) findViewById(R.id.login_pwd_edittext);
        google_signIn_btn = (SignInButton) findViewById(R.id.google_signIn_btn);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Profile");

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

        google_signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent a = new Intent(LoginActivity.this, CategoriesActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

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
                if(authResult.getUser().isEmailVerified()) {

                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                } else{
                    FirebaseAuth.getInstance().signOut();
                    loading_dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email not verified. Please verify it first.", Toast.LENGTH_LONG).show();
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading_dialog.dismiss();

                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());

                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signIn() {
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());

                loading_dialog.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            String uid = task.getResult().getUser().getUid();
                            String userName = task.getResult().getUser().getDisplayName();
                            String email = task.getResult().getUser().getEmail();

                            UsersModel usersModel = new UsersModel(uid, userName, email);

                            databaseRef.child(uid).setValue(usersModel);

                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            finish();
                        } else {
                            loading_dialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
