package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.Adapters.AddEnrolledSchemesAdapter;
import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.example.rdc_lnmiit.RecyclerTouchHelpers.AddEnrolledSchemesItemTouchHelper;
import com.example.rdc_lnmiit.Utils.TinyDB;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddEnrolledSchemeActivity extends AppCompatActivity implements AddEnrolledSchemesItemTouchHelper.RecyclerItemTouchHelperListener {

    Toolbar toolbar;
    TextView toolbar_title;
    AnimationDrawable animationDrawable;
    ArrayList<SchemeDataModel> allSchemesList = new ArrayList<>();
    ArrayList<SchemeDataModel> enrolledSchemes = new ArrayList<>();
    TinyDB tinyDB;
    MaterialSearchBar searchBar;
    RecyclerView rv_allSchemesList;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    ItemTouchHelper itemTouchHelper;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enrolled_scheme);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("Add Enrolled Schemes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);

        loading_dialog = new Dialog(this);
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);

        rv_allSchemesList = (RecyclerView) findViewById(R.id.rv_allSchemesList);
        rv_allSchemesList.setHasFixedSize(true);
        rv_allSchemesList.setLayoutManager(new LinearLayoutManager(this));

        rl = (RelativeLayout) findViewById(R.id.rl);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        tinyDB = new TinyDB(this);
        searchBar.setLastSuggestions(tinyDB.getListString("suggestions"));

        fetchAllSchemes();

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                search(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_BACK:
                        fetchAllSchemes();
                }
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new AddEnrolledSchemesItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rv_allSchemesList);

        Boolean firstLoad = this.getSharedPreferences("PREFERENCE6", MODE_PRIVATE).getBoolean("firstLoad", true);
        if (firstLoad) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(searchBar, "Search your desired scheme", "Search 'all' to get the list of all available schemes")
                                    .cancelable(true)
                                    .tintTarget(true),
                            TapTarget.forView(findViewById(R.id.tv_swipeRightToAdd), "Swipe right to add the scheme in your enrolled list", "Please be honest in adding the schemes. Add only those schemes in which you are really enrolled")
                                    .cancelable(true)
                                    .tintTarget(true)

                    ).start();
            this.getSharedPreferences("PREFERENCE6", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();
        }
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof AddEnrolledSchemesAdapter.ViewHolder) {
            if (isAlreadyAdded(viewHolder)) {
                Snackbar snackbar = Snackbar.make(rl, "Already Added!!", Snackbar.LENGTH_LONG);
                snackbar.show();
                itemTouchHelper.attachToRecyclerView(null);
                itemTouchHelper.attachToRecyclerView(rv_allSchemesList);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you are already enrolled in this scheme?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                loading_dialog.show();
                                final SchemeDataModel model = allSchemesList.get(position);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/enrolledSchemes");
                                ref.child(model.getScheme()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ((AddEnrolledSchemesAdapter.ViewHolder) viewHolder).tv_enrolledStatus.setVisibility(View.VISIBLE);
                                        Toast.makeText(AddEnrolledSchemeActivity.this, model.getScheme() + " added!", Toast.LENGTH_SHORT).show();
                                        loading_dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddEnrolledSchemeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        loading_dialog.dismiss();
                                    }
                                });

                                itemTouchHelper.attachToRecyclerView(null);
                                itemTouchHelper.attachToRecyclerView(rv_allSchemesList);
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemTouchHelper.attachToRecyclerView(null);
                                itemTouchHelper.attachToRecyclerView(rv_allSchemesList);
                            }
                        });
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }

    void fetchAllSchemes() {
        loading_dialog.show();
        allSchemesList.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        SchemeDataModel model = ds1.getValue(SchemeDataModel.class);
                        allSchemesList.add(model);
                    }
                }
                rv_allSchemesList.setAdapter(new AddEnrolledSchemesAdapter(allSchemesList, AddEnrolledSchemeActivity.this));
                loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading_dialog.dismiss();
            }
        });
    }

    boolean isAlreadyAdded(RecyclerView.ViewHolder viewHolder) {
        if (((AddEnrolledSchemesAdapter.ViewHolder) viewHolder).tv_enrolledStatus.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    void search(CharSequence text) {
        String query = text.toString().toLowerCase();
        if (query.equals("all")) {
            fetchAllSchemes();
        } else {
            ArrayList<SchemeDataModel> searchSchemeList = new ArrayList<>();
            for (SchemeDataModel model : allSchemesList) {
                if (model.getScheme().toLowerCase().contains(query)) {
                    searchSchemeList.add(model);
                }
            }
            allSchemesList.clear();
            allSchemesList = searchSchemeList;
            rv_allSchemesList.setAdapter(new AddEnrolledSchemesAdapter(allSchemesList, AddEnrolledSchemeActivity.this));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // showData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save last queries to disk
        tinyDB.putListString("suggestions", (ArrayList<String>) searchBar.getLastSuggestions());
    }
}
