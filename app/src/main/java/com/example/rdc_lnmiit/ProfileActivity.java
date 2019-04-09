package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends BaseActivity {

    Toolbar toolbar;
    TextView tv_userName, tv_email;
    CardView cv_bookmarked_schemes;
    FirebaseAuth auth;
    DatabaseReference ref;
    String uid;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        Intent a = new Intent(ProfileActivity.this, CategoriesActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        break;

                    case R.id.menu_aboutUs:
                        BottomSheetDialogAboutUs bottomSheetDialogAboutUs = new BottomSheetDialogAboutUs();
                        bottomSheetDialogAboutUs.show(getSupportFragmentManager(), "AboutUsBottomSheet");
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(ProfileActivity.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;
                }

                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            uid = user.getUid();
        } else{
            Toast.makeText(this, "Please Register First", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ProfileActivity.this, RegisterActivity.class));
            finish();
        }

        loading_dialog = new Dialog(this);
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        ref = FirebaseDatabase.getInstance().getReference("/Profile/" + uid);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);

        tv_userName = (TextView) findViewById(R.id.tv_userName);
        tv_email = (TextView) findViewById(R.id.tv_email);
        cv_bookmarked_schemes = (CardView) findViewById(R.id.cv_bookmarked_schemes);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    UsersModel u = ds.getValue(UsersModel.class);

                    tv_userName.setText(u.getUserName());
                    tv_email.setText(u.getEmail());

                    loading_dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Enable to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        cv_bookmarked_schemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 09-04-2019 intent to bookmarked schemes list activity
            }
        });

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }
}
