package com.example.rdc_lnmiit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class CategoriesActivity extends BaseActivity{

    RecyclerView rv;
    DatabaseReference mDatabase;
    ArrayList<String> categories;
    MyAdpater adapter;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Category");

        connected();

        aSwitch = (Switch)findViewById(R.id.switch_darkMode);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu_aboutUs:
                        Intent b = new Intent(CategoriesActivity.this, AboutUsActivity.class);
                        startActivity(b);
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(CategoriesActivity.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;
                }

                return false;
            }
        });

        categories = new ArrayList<String>();
        categories.add("Ministry of housing and urban poverty alleviation");
        categories.add("Ministry of finance");
        categories.add("Ministry of human resource and development");
        categories.add("Ministry of rural development");
        categories.add("Ministry of urban development");
        categories.add("Ministry of social justice and empowerment");
        categories.add("Ministry of women and child development");
        categories.add("Ministry of health and family welfare");
        categories.add("Ministry of agriculture and farmers welfare");
        categories.add("Ministry of power");
        categories.add("Ministry of commerce");
        categories.add("Ministry of skill development and entrepreneurship");
        categories.add("Ministry of water resources, river development and ganga rejuvenation");
        categories.add("Ministry of labour and employment");
        categories.add("Ministry of electronics and IT");
        categories.add("Ministry of road transport and waterways");
        categories.add("Miscellaneous schemes");

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdpater(categories, this);
        rv.setAdapter(adapter);

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

       //restartActivity();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_data_menu:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void connected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
           // Toast.makeText(this, "ONLINE", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.coo), "Online", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        } else {
            //Toast.makeText(this, "OFFLINE", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.coo), "Offline", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }
}
