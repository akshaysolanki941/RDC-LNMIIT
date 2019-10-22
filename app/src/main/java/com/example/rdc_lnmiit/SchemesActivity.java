package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.example.rdc_lnmiit.RVHolder.Holder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SchemesActivity extends AppCompatActivity {

    RecyclerView recycler_scheme;
    DatabaseReference databaseRef, databaseRefDialog;
    ProgressBar progressBar;
    TextView toolbar_title;
    String categorySelected;
    ArrayList<SchemeDataModel> schemeData_Model_list;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    String YES;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("Select Schemes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        YES = "YES";

        loading_dialog = new Dialog(this);

        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        schemeData_Model_list = new ArrayList<>();

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
    protected void onStart() {
        super.onStart();

        databaseRefDialog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //SchemeDataModel d = ds.getValue(SchemeDataModel.class);
                    schemeData_Model_list.add(ds.getValue(SchemeDataModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SchemesActivity.this, "Enable to fetch data", Toast.LENGTH_SHORT).show();
            }
        });



       FirebaseRecyclerAdapter<SchemeDataModel, Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SchemeDataModel, Holder>
                (SchemeDataModel.class, R.layout.scheme_list, Holder.class, databaseRef) {
            @Override
            protected void populateViewHolder(final Holder holder, SchemeDataModel model, final int position) {

                if(model != null) {
                    if (model.getRg_inOperation().equals(YES))
                        holder.scheme_name.setText(model.getScheme());


                    else {
                        holder.itemView.getLayoutParams().height = 0;
                        holder.itemView.setVisibility(View.GONE);
                    }
                }

                loading_dialog.dismiss();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SchemesActivity.this, SchemesDetailsActivity.class);
                        intent.putExtra("SchemesData", schemeData_Model_list.get(position));

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
