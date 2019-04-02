package com.example.rdc_lnmiit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wideinc.library.simpleprefs.SimplePrefs;

public class ThemeUtils {
    //TODO have a getter/setter
    //static global variable which stores the current theme.
    //static SharedPreferences sharedPref;
    public static String THEME = "Light";

    /**
     *
     * @param activity
     */
    public static void applyTheme(Activity activity){
        if(ThemeUtils.THEME == "Dark"){
            activity.getApplicationContext().setTheme(R.style.AppTheme_Dark);
            activity.setTheme(R.style.AppTheme_Dark);
        }else {
            activity.getApplicationContext().setTheme(R.style.AppTheme_Light);
            activity.setTheme(R.style.AppTheme_Light);
        }
    }
}
