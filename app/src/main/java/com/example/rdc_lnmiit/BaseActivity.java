package com.example.rdc_lnmiit;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wideinc.library.simpleprefs.SimplePrefs;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        onCreation(savedInstanceState);
    }

    /**
     * All the activity should override this.
     * @param savedInstanceState
     */
    protected abstract void onCreation(@Nullable Bundle savedInstanceState);

    /**
     * have your base layout here, inflate/add on top of your base layout
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * have your base layout here, inflate/add on top of your base layout
     * @param view
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    /**
     * Method 1:
     * This has to be called when theme is changed in the activity.This internally recreated the activity
     */
    public void reCreate() {
        Bundle savedInstanceState = new Bundle();
        //this is important to save all your open states/fragment states
        onSaveInstanceState(savedInstanceState);
        //this is needed to release the resources
        super.onDestroy();

        //call on create where new theme is applied
        onCreate(savedInstanceState);//you can pass bundle arguments to skip your code/flows on this scenario
    }

    /**
     * Method 2:
     * This has to be called when theme is changed in the activity.This internally restarts the activity
     * This gives you the flicker
     */
    public void restartActivity() {
        Intent i = getIntent();
        this.overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        //restart the activity without animation
        this.overridePendingTransition(0, 0);
        this.startActivity(i);
    }
}

