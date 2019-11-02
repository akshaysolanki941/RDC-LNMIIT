package com.example.rdc_lnmiit.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.Adapters.EnrolledSchemesAdapter;
import com.example.rdc_lnmiit.AddEnrolledSchemeActivity;
import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.example.rdc_lnmiit.R;
import com.example.rdc_lnmiit.RecyclerTouchHelpers.EnrolledSchemesItemTouchHelper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class EnrollmentsFragment extends Fragment implements EnrolledSchemesItemTouchHelper.RecyclerItemTouchHelperListener {

    ArrayList<SchemeDataModel> enrolledSchemeList = new ArrayList<>();
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    RecyclerView rv_enrolledSchemes;
    TextView tv_notEnrolled, tv_swipeRightToDelete;
    FloatingActionButton fab;
    FrameLayout fl;
    EnrolledSchemesAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    Dialog dialog_whichVillage;
    String villageName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrollments, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("My Enrollments");

        fl = (FrameLayout) view.findViewById(R.id.fl);
        rv_enrolledSchemes = (RecyclerView) view.findViewById(R.id.rv_enrolledSchemes);
        rv_enrolledSchemes.setHasFixedSize(true);
        rv_enrolledSchemes.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_notEnrolled = (TextView) view.findViewById(R.id.tv_notEnrolled);
        tv_swipeRightToDelete = (TextView) view.findViewById(R.id.tv_swipeRightToDelete);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        dialog_whichVillage = new Dialog(getContext());
        loading_dialog = new Dialog(getContext());
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getActivity()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFABpressed();
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new EnrolledSchemesItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rv_enrolledSchemes);


        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref1.keepSynced(true);
        ref1.child("village").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    showDialogWhichVillage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Boolean firstLoad = getActivity().getSharedPreferences("PREFERENCE5", MODE_PRIVATE).getBoolean("firstLoad", true);
        if (firstLoad) {
            new TapTargetSequence(getActivity())
                    .targets(
                            TapTarget.forView(tv_swipeRightToDelete, "Swipe right to delete", "You can also restore deleted item")
                                    .cancelable(true)
                                    .tintTarget(true),
                            TapTarget.forView(fab, "Click this to add the scheme in which you are enrolled")
                                    .cancelable(true)
                                    .tintTarget(true)

                    ).start();
            getActivity().getSharedPreferences("PREFERENCE5", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showData();
    }

    void showData() {
        loading_dialog.show();
        tv_notEnrolled.setVisibility(View.GONE);
        enrolledSchemeList.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/enrolledSchemes");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SchemeDataModel model = ds.getValue(SchemeDataModel.class);
                    enrolledSchemeList.add(model);
                }

                if (enrolledSchemeList.isEmpty()) {
                    tv_notEnrolled.setVisibility(View.VISIBLE);
                    tv_swipeRightToDelete.setVisibility(View.GONE);
                } else {
                    tv_notEnrolled.setVisibility(View.GONE);
                    tv_swipeRightToDelete.setVisibility(View.VISIBLE);
                    adapter = new EnrolledSchemesAdapter(enrolledSchemeList, getActivity());
                    rv_enrolledSchemes.setAdapter(adapter);
                }
                loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void onFABpressed() {
        startActivity(new Intent(getActivity(), AddEnrolledSchemeActivity.class));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof EnrolledSchemesAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            final SchemeDataModel model = enrolledSchemeList.get(position);
            adapter.removeItem(viewHolder.getAdapterPosition());
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(fl, model.getScheme() + " deleted!", 6000);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(model);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    void showDialogWhichVillage() {
        dialog_whichVillage.setContentView(R.layout.dialog_which_village);

        final EditText et_village_name = (EditText) dialog_whichVillage.findViewById(R.id.et_village_name);
        Button btn_village_name_submit = (Button) dialog_whichVillage.findViewById(R.id.btn_villageName_submit);

        dialog_whichVillage.setCancelable(false);
        dialog_whichVillage.setCanceledOnTouchOutside(false);
        dialog_whichVillage.show();

        btn_village_name_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                villageName = et_village_name.getText().toString();
                if (villageName.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Village Name", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.keepSynced(true);
                    ref.child("village").setValue(villageName);
                    dialog_whichVillage.dismiss();
                }
            }
        });
    }
}
