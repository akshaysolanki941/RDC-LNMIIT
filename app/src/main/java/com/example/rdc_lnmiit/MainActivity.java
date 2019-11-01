package com.example.rdc_lnmiit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rdc_lnmiit.Adapters.MyAdpater;
import com.example.rdc_lnmiit.Fragments.AboutUsFragment;
import com.example.rdc_lnmiit.Fragments.CategoryFragment;
import com.example.rdc_lnmiit.Fragments.DeveloperFragment;
import com.example.rdc_lnmiit.Fragments.EnrollmentsFragment;
import com.example.rdc_lnmiit.Fragments.NewsFragment;
import com.example.rdc_lnmiit.Fragments.ProfileFragment;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    DatabaseReference mDatabase;
    ArrayList<String> categories;
    MyAdpater adapter;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    Switch aSwitch;
    FirebaseAuth firebaseAuth;
    String adminEmailAK, adminEmailRG;
    boolean doubleBackToExitPressedOnce;
    public TextView toolbar_title;
    Drawer result;
    AccountHeader headerResult;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        adminEmailAK = "akshaysolanki941@gmail.com";

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("Select Category");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.gradient1)
                .addProfiles(
                        new ProfileDrawerItem().withName("UBA Cell - LNMIIT").withEmail("उन्नत भारत अभियान").withIcon(getResources().getDrawable(R.drawable.ic_launcher_web))
                )
                .build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Categories").withIcon(R.drawable.ic_home_black_24dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Profile").withIcon(R.drawable.ic_person_outline_black_24dp);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("India News").withIcon(R.drawable.ic_format_align_left_black_24dp);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("My Enrollments").withIcon(R.drawable.ic_note_add_black_24dp);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("About us").withIcon(R.drawable.ic_assistant_black_24dp);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Developer").withIcon(R.drawable.ic_code_black_24dp);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        new DividerDrawerItem(),
                        item5,
                        item6
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        switch (position) {
                            case 1:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                        .replace(R.id.fragment_container, new CategoryFragment(), "categoryfragment")
                                        .commit();
                                result.closeDrawer();

                                break;

                            case 2:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, new ProfileFragment(), "profilefragment")
                                        .commit();
                                result.closeDrawer();
                                break;

                            case 3:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, new NewsFragment(), "newsfragment")
                                        .commit();
                                result.closeDrawer();
                                break;

                            case 4:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, new EnrollmentsFragment(), "enrollmentfragment")
                                        .commit();
                                result.closeDrawer();
                                break;

                            case 6:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, new AboutUsFragment(), "aboutusfragment")
                                        .commit();
                                result.closeDrawer();
                                break;

                            case 7:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.fragment_container, new DeveloperFragment(), "developerfragment")
                                        .commit();
                                result.closeDrawer();
                                break;
                        }

                        return false;
                    }
                })
                .withDisplayBelowStatusBar(true)
                .build();



        Boolean firstLoad = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            TapTargetView.showFor(this, TapTarget.forToolbarNavigationIcon(toolbar, "Take a look!!")
                            .cancelable(false)
                            .tintTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            result.openDrawer();
                        }
                    });

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();

        }

        /*new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.never), "Gonna"),
                        TapTarget.forView(findViewById(R.id.give), "You", "Up")
                                .dimColor(android.R.color.never)
                                .outerCircleColor(R.color.gonna)
                                .targetCircleColor(R.color.let)
                                .textColor(android.R.color.you),
                        TapTarget.forBounds(rickTarget, "Down", ":^)")
                                .cancelable(false)
                                .icon(rick))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }


                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });*/

    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1, menu);

        MenuItem item = menu.getItem(0);

        if (firebaseAuth.getCurrentUser() != null && (firebaseAuth.getCurrentUser().getEmail().equals(adminEmailAK))) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }


        SpannableString s = new SpannableString("Add Data");
        //s.setSpan(new ForegroundColorSpan(getAttributeColor(getApplicationContext(), R.attr.text)), 0, s.length(), 0);
        item.setTitle(s);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_data_menu:
                Intent intent = new Intent(this, AddDataActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static int getAttributeColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            // Log.w(TAG, "Not found color resource by id: " + colorRes);
        }
        return color;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

}
