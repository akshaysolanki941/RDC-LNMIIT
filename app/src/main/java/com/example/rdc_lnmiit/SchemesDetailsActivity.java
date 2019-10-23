package com.example.rdc_lnmiit;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
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

public class SchemesDetailsActivity extends AppCompatActivity {

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
    TextView bookmarked_tv, schemeName;
    RelativeLayout bookmark_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes_details);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userUID = user.getUid();

       /* if (user != null) {
            bookmark_view.setVisibility(View.VISIBLE);
            userUID = user.getUid();
            databasefav = FirebaseDatabase.getInstance().getReference("/Profile/" + userUID + "/bookmarks");
            databasefav.keepSynced(true);
        } else {
            bookmark_view.setVisibility(View.GONE);
        }*/

        Intent i = getIntent();
        final SchemeDataModel schemeDataModel = i.getParcelableExtra("SchemesData");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Scheme Details");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookmark_view = (RelativeLayout) findViewById(R.id.bookmark_view);
        schemeName = (TextView) findViewById(R.id.schemeName);
        cv_mile = (CardView) findViewById(R.id.cv_mile);
        year = (TextView) findViewById(R.id.year);
        centralorstate = (TextView) findViewById(R.id.centralorstate);
        bene = (TextView) findViewById(R.id.bene);
        motive = (TextView) findViewById(R.id.motive);
        mile = (TextView) findViewById(R.id.mile);
        ImageView logoImageView = findViewById(R.id.detail_logo_image_view);
        materialFavoriteButton = findViewById(R.id.mfb);
        bookmarked_tv = (TextView) findViewById(R.id.bk_tv);

        schemeName.setText(schemeDataModel.getScheme());

        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean isFAV = sharedPreferences.getBoolean("isFav", false);
        if (isFAV) {
            materialFavoriteButton.setFavorite(true);
            bookmarked_tv.setText("Bookmarked");
        } else {
            materialFavoriteButton.setFavorite(false);
            bookmarked_tv.setText("Bookmark this Scheme");
        }

        databasefav = FirebaseDatabase.getInstance().getReference("Profile/" + userUID + "/bookmarks");

        if (databasefav != null) {
            databasefav.orderByChild("scheme").equalTo(schemeDataModel.getScheme()).addValueEventListener(new ValueEventListener() {
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

        year.setText(schemeDataModel.getYear());
        centralorstate.setText(schemeDataModel.getRg_value());
        bene.setText(schemeDataModel.getBene());
        motive.setText(schemeDataModel.getMotive());

        if (!TextUtils.isEmpty(schemeDataModel.getMile())) {
            mile.setText(schemeDataModel.getMile());
        } else {
            cv_mile.getLayoutParams().height = 0;
            cv_mile.setVisibility(View.GONE);
        }
        Glide.with(SchemesDetailsActivity.this).load(schemeDataModel.getPicURL()).placeholder(R.drawable.placeholder).into(logoImageView);

        materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite) {
                    String FAV_schemeName = schemeDataModel.getScheme();
                    String FAV_year = year.getText().toString();
                    String FAV_centralorstate = centralorstate.getText().toString();
                    String FAV_bene = bene.getText().toString();
                    String FAV_motive = motive.getText().toString();
                    String FAV_mile = mile.getText().toString();
                    String FAV_picURL = schemeDataModel.getPicURL();
                    String FAV_inOperation = schemeDataModel.getRg_inOperation();

                    SchemeDataModel d = new SchemeDataModel(FAV_schemeName, FAV_year, FAV_motive, FAV_bene, FAV_mile, FAV_centralorstate, FAV_picURL, FAV_inOperation);

                    databasefav.child(FAV_schemeName).setValue(d);
                    bookmarked_tv.setText("Bookmarked");
                    Toast.makeText(SchemesDetailsActivity.this, "Bookmarked!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putBoolean("isFav", true);
                    editor.apply();
                } else {
                    databasefav.child(schemeDataModel.getScheme()).removeValue();
                    bookmarked_tv.setText("Bookmark this Scheme");
                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putBoolean("isFav", false);
                    editor.apply();
                }
            }
        });

        Boolean firstLoad = getSharedPreferences("PREFERENCE3", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            TapTargetView.showFor(this, TapTarget.forView(materialFavoriteButton, "Tap to bookmark this scheme ;)")
                            .cancelable(false)
                            .tintTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                        }
                    });

            getSharedPreferences("PREFERENCE3", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();

        }
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
