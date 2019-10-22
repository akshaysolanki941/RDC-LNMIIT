package com.example.rdc_lnmiit;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.example.rdc_lnmiit.RVHolder.Holder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
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

public class BookmarkedSchemesActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    RecyclerView bookmark_rv;
    DatabaseReference databaseFAV;
    FirebaseAuth mAuth;
    String UID;
    ArrayList<SchemeDataModel> schemeData_Model_list;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_schemes);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("Bookmarked Schemes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schemeData_Model_list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        UID = user.getUid();

        bookmark_rv = (RecyclerView) findViewById(R.id.recycler_bookmarked_scheme);
        bookmark_rv.setHasFixedSize(true);
        bookmark_rv.setLayoutManager(new LinearLayoutManager(this));

        databaseFAV = FirebaseDatabase.getInstance().getReference("/Profile/" + UID + "/bookmarks");
        databaseFAV.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseFAV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //SchemeDataModel d = ds.getValue(SchemeDataModel.class);
                    schemeData_Model_list.add(ds.getValue(SchemeDataModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookmarkedSchemesActivity.this, "Enable to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseRecyclerAdapter<SchemeDataModel, Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SchemeDataModel, Holder>
                (SchemeDataModel.class, R.layout.scheme_list, Holder.class, databaseFAV) {
            @Override
            protected void populateViewHolder(final Holder holder, SchemeDataModel model, final int position) {

                holder.scheme_name.setText(model.getScheme());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(BookmarkedSchemesActivity.this, SchemesDetailsActivity.class);
                        intent.putExtra("SchemesData", schemeData_Model_list.get(position));

                        startActivity(intent);

                    }
                });
            }
        };

        bookmark_rv.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
