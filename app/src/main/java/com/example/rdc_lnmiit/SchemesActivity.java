package com.example.rdc_lnmiit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SchemesActivity extends BaseActivity {

    RecyclerView recycler_scheme;
    DatabaseReference databaseRef, databaseRefDialog;
    ProgressBar progressBar;
    TextView loading;
    String categorySelected;
    ArrayList<Data> data_list;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* sharedPref = new SettingsActivity().sharedPref;
        String currentTheme = sharedPref.getString("current_theme", "light");
        if(currentTheme == "light")
            setTheme(R.style.AppTheme_Light);

        else
            setTheme(R.style.AppTheme_Dark);*/

        setContentView(R.layout.activity_schemes);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Scheme");
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        Intent a = new Intent(SchemesActivity.this, CategoriesActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();
                        break;

                    case R.id.menu_aboutUs:
                        /*Intent b = new Intent(SchemesActivity.this, AboutUsActivity.class);
                        startActivity(b);*/
                        BottomSheetDialogAboutUs bottomSheetDialogAboutUs = new BottomSheetDialogAboutUs();
                        bottomSheetDialogAboutUs.show(getSupportFragmentManager(), "AboutUsBottomSheet");
                        break;

                    case R.id.menu_settings:
                        Intent c = new Intent(SchemesActivity.this, SettingsActivity.class);
                        startActivity(c);
                        finish();
                        break;
                }

                return false;
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        loading = (TextView) findViewById(R.id.loading);

        data_list = new ArrayList<>();

        categorySelected = getIntent().getStringExtra("categorySelected");

        recycler_scheme = (RecyclerView) findViewById(R.id.recycler_scheme);
        recycler_scheme.setHasFixedSize(true);
        recycler_scheme.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("/Data/" + categorySelected);
        databaseRef.keepSynced(true);

        databaseRefDialog = FirebaseDatabase.getInstance().getReference("/Data/" + categorySelected/* + "/" + schemeName*/);
        databaseRefDialog.keepSynced(true);

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseRefDialog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Data d = ds.getValue(Data.class);
                    data_list.add(d);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SchemesActivity.this, "Enable to fetch data", Toast.LENGTH_SHORT).show();
            }
        });



       FirebaseRecyclerAdapter<Data, Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Data, Holder>
                (Data.class, R.layout.scheme_list, Holder.class, databaseRef) {
            @Override
            protected void populateViewHolder(final Holder holder, Data model, final int position) {

                holder.scheme_name.setText(model.getScheme());

                progressBar.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //showDialog(data_list.get(position));

                        Intent intent = new Intent(SchemesActivity.this, SchemesDetails.class);
                        intent.putExtra("SchemesData", data_list.get(position));

                        startActivity(intent);

                    }
                });
            }
        };

       recycler_scheme.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  recreate();

    }

    /*public void showDialog(Data d){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new SchemeDialog();

        ((SchemeDialog) dialogFragment).dialog_year.setText(d.getYear());
        ((SchemeDialog) dialogFragment).dialog_centralorstate.setText(d.getRg_value());
        ((SchemeDialog) dialogFragment).dialog_bene.setText(d.getBene());
        ((SchemeDialog) dialogFragment).dialog_motive.setText(d.getMotive());
        ((SchemeDialog) dialogFragment).dialog_mile.setText(d.getMile());

        dialogFragment.show(ft, "dialog");

    }*/

}
