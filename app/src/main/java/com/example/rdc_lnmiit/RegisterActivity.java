package com.example.rdc_lnmiit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_edittext = (EditText) findViewById(R.id.name_edittext);
        email_edittext = (EditText) findViewById(R.id.email_edittext);
        pwd_edittext = (EditText) findViewById(R.id.pwd_edittext);
        btn_register = (Button) findViewById(R.id.btn_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Profile");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    public void registerUser() {

        final String email = email_edittext.getText().toString();
        String pwd = pwd_edittext.getText().toString();
        final String userName = name_edittext.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String uid = user.getUid();

                            UsersModel usersModel = new UsersModel(uid, userName, email);

                            databaseRef.child(uid).setValue(usersModel);

                            Toast.makeText(RegisterActivity.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
