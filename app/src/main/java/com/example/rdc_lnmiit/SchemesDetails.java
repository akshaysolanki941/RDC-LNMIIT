package com.example.rdc_lnmiit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class SchemesDetails extends BaseActivity {

    TextView year, centralorstate, bene, motive, mile;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  sharedPref = new SettingsActivity().sharedPref;
        String currentTheme = sharedPref.getString("current_theme", "light");
        if(currentTheme == "light")
            setTheme(R.style.AppTheme_Light);

        else
            setTheme(R.style.AppTheme_Dark);*/

        setContentView(R.layout.activity_schemes_details);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        Intent a = new Intent(SchemesDetails.this, CategoriesActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        break;

                    case R.id.menu_aboutUs:
                        /*Intent b = new Intent(SchemesDetails.this, AboutUsActivity.class);
                        startActivity(b);*/
                        BottomSheetDialogAboutUs bottomSheetDialogAboutUs = new BottomSheetDialogAboutUs();
                        bottomSheetDialogAboutUs.show(getSupportFragmentManager(), "AboutUsBottomSheet");
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(SchemesDetails.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;
                }

                return false;
            }
        });

        Intent i = getIntent();

        Data data = i.getParcelableExtra("SchemesData");

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data.getScheme());
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);

        year = (TextView) findViewById(R.id.year);
        centralorstate = (TextView) findViewById(R.id.centralorstate);
        bene = (TextView) findViewById(R.id.bene);
        motive = (TextView) findViewById(R.id.motive);
        mile = (TextView) findViewById(R.id.mile);



        year.setText(data.getYear());
        centralorstate.setText(data.getRg_value());
        bene.setText(data.getBene());
        motive.setText(data.getMotive());
        mile.setText(data.getMile());
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
       // recreate();
    }
}
