package com.example.rdc_lnmiit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wideinc.library.simpleprefs.SimplePrefs;

public class SettingsActivity extends BaseActivity{

    Toolbar toolbar;
    Switch switch_darkMode;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*new SimplePrefs.Builder()
                .setPrefsName("myapppreference")
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setDefaultUse(false)
                .build();*/

        //sharedPref = context.getSharedPreferences("currentTheme", 0);
        //editor = sharedPref.edit();

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);

        switch_darkMode = (Switch) findViewById(R.id.switch_darkMode);

        if(ThemeUtils.THEME == "Dark") {
            switch_darkMode.setChecked(true);
        }else {
            switch_darkMode.setChecked(false);
        }

        switch_darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //SimplePrefs.putString("theme", "Dark");

                    ThemeUtils.THEME = "Dark";
                }

                else{
                    //SimplePrefs.putString("theme", "Light");

                    ThemeUtils.THEME = "Light";
                }
                restartActivity();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent a = new Intent(SettingsActivity.this, CategoriesActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }
}
