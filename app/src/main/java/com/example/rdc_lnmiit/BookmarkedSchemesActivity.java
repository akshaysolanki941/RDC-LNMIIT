package com.example.rdc_lnmiit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookmarkedSchemesActivity extends BaseActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    RecyclerView bookmark_rv;
    DatabaseReference databaseFAV;
    FirebaseAuth mAuth;
    String UID;
    ArrayList<Data> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_schemes);

        data_list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        UID = user.getUid();

        bookmark_rv = (RecyclerView) findViewById(R.id.recycler_bookmarked_scheme);
        bookmark_rv.setHasFixedSize(true);
        bookmark_rv.setLayoutManager(new LinearLayoutManager(this));

        databaseFAV = FirebaseDatabase.getInstance().getReference("/Profile/" + UID + "/bookmarks");
        databaseFAV.keepSynced(true);

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
    protected void onStart() {
        super.onStart();

        databaseFAV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //Data d = ds.getValue(Data.class);
                    data_list.add(ds.getValue(Data.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookmarkedSchemesActivity.this, "Enable to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseRecyclerAdapter<Data, Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Data, Holder>
                (Data.class, R.layout.scheme_list, Holder.class, databaseFAV) {
            @Override
            protected void populateViewHolder(final Holder holder, Data model, final int position) {

                holder.scheme_name.setText(model.getScheme());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(BookmarkedSchemesActivity.this, SchemesDetails.class);
                        intent.putExtra("SchemesData", data_list.get(position));

                        startActivity(intent);

                    }
                });
            }
        };

        bookmark_rv.setAdapter(firebaseRecyclerAdapter);
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
