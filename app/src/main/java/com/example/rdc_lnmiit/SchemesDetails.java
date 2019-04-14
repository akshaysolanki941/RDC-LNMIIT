package com.example.rdc_lnmiit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchemesDetails extends BaseActivity {

    TextView year, centralorstate, bene, motive, mile;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    String currentTheme;
    CardView cv_mile;
    com.github.ivbaranov.mfb.MaterialFavoriteButton materialFavoriteButton;
    DatabaseReference databasefav;
    FirebaseAuth auth;
    String userUID;
    SharedPreferences sharedPreferences;
    TextView bookmarked_tv;
    RelativeLayout bookmark_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes_details);

        bookmark_view = (RelativeLayout) findViewById(R.id.bookmark_view);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            bookmark_view.setVisibility(View.VISIBLE);
            userUID = user.getUid();
            databasefav = FirebaseDatabase.getInstance().getReference("/Profile/" + userUID + "/bookmarks");
            databasefav.keepSynced(true);
        } else {
            bookmark_view.setVisibility(View.GONE);
        }


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Intent a = new Intent(SchemesDetails.this, CategoriesActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        break;

                    case R.id.menu_aboutUs:
                        BottomSheetDialogAboutUs bottomSheetDialogAboutUs = new BottomSheetDialogAboutUs();
                        bottomSheetDialogAboutUs.show(getSupportFragmentManager(), "AboutUsBottomSheet");
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(SchemesDetails.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;

                    case R.id.menu_profile:
                        startActivity(new Intent(SchemesDetails.this, ProfileActivity.class));
                        break;
                }

                return false;
            }
        });

        Intent i = getIntent();

        final Data data = i.getParcelableExtra("SchemesData");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(data.getScheme());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cv_mile = (CardView) findViewById(R.id.cv_mile);
        year = (TextView) findViewById(R.id.year);
        centralorstate = (TextView) findViewById(R.id.centralorstate);
        bene = (TextView) findViewById(R.id.bene);
        motive = (TextView) findViewById(R.id.motive);
        mile = (TextView) findViewById(R.id.mile);
        ImageView logoImageView = findViewById(R.id.detail_logo_image_view);
        materialFavoriteButton = findViewById(R.id.mfb);
        bookmarked_tv = (TextView) findViewById(R.id.bk_tv);

        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean isFAV = sharedPreferences.getBoolean("isFav", false);
        if (isFAV) {
            materialFavoriteButton.setFavorite(true);
            bookmarked_tv.setText("Bookmarked");
        } else {
            materialFavoriteButton.setFavorite(false);
            bookmarked_tv.setText("Bookmark this Scheme");
        }

        if(databasefav != null) {
            databasefav.orderByChild("scheme").equalTo(data.getScheme()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        materialFavoriteButton.setFavorite(true);
                        bookmarked_tv.setText("Bookmarked");
                    } else {
                        materialFavoriteButton.setFavorite(false);
                        bookmarked_tv.setText("Bookmark this Scheme");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        year.setText(data.getYear());
        centralorstate.setText(data.getRg_value());
        bene.setText(data.getBene());
        motive.setText(data.getMotive());

        if (!TextUtils.isEmpty(data.getMile())) {
            mile.setText(data.getMile());
        } else {
            cv_mile.getLayoutParams().height = 0;
            cv_mile.setVisibility(View.GONE);
        }
        Glide.with(this).load(data.getPicURL()).placeholder(R.drawable.placeholder).into(logoImageView);

        materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite) {
                    String FAV_schemeName = data.getScheme();
                    String FAV_year = year.getText().toString();
                    String FAV_centralorstate = centralorstate.getText().toString();
                    String FAV_bene = bene.getText().toString();
                    String FAV_motive = motive.getText().toString();
                    String FAV_mile = mile.getText().toString();
                    String FAV_picURL = data.getPicURL();
                    String FAV_inOperation = data.getRg_inOperation();

                    Data d = new Data(FAV_schemeName, FAV_year, FAV_motive, FAV_bene, FAV_mile, FAV_centralorstate, FAV_picURL, FAV_inOperation);

                    databasefav.child(FAV_schemeName).setValue(d);
                    bookmarked_tv.setText("Bookmarked");
                    Toast.makeText(SchemesDetails.this, "Bookmarked!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putBoolean("isFav", true);
                    editor.apply();
                }

                else{
                    databasefav.child(data.getScheme()).removeValue();
                    bookmarked_tv.setText("Bookmark this Scheme");
                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putBoolean("isFav", false);
                    editor.apply();
                }
            }
        });
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // recreate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
