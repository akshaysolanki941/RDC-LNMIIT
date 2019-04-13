package com.example.rdc_lnmiit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class BookmarkedSchemesActivity extends BaseActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_schemes);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarked Schemes");
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        Intent a = new Intent(BookmarkedSchemesActivity.this, CategoriesActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        break;

                    case R.id.menu_aboutUs:
                        BottomSheetDialogAboutUs bottomSheetDialogAboutUs = new BottomSheetDialogAboutUs();
                        bottomSheetDialogAboutUs.show(getSupportFragmentManager(), "AboutUsBottomSheet");
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(BookmarkedSchemesActivity.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;

                    case R.id.menu_profile:
                        startActivity(new Intent(BookmarkedSchemesActivity.this, ProfileActivity.class));
                        finish();
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(BookmarkedSchemesActivity.this, ProfileActivity.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BookmarkedSchemesActivity.this, ProfileActivity.class));
        finish();
    }
}
