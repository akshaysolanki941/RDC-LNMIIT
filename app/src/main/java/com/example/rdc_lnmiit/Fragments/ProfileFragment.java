package com.example.rdc_lnmiit.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.BookmarkedSchemesActivity;
import com.example.rdc_lnmiit.LoginActivity;
import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.Models.UsersModel;
import com.example.rdc_lnmiit.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    Toolbar toolbar;
    TextView tv_userName, tv_email, tv_noOfEnrolledSchemes;
    CardView cv_bookmarked_schemes;
    FirebaseAuth auth;
    DatabaseReference ref;
    String uid;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    BottomNavigationView bottomNavigationView;
    String adminEmailAK, adminEmailRG;
    Button btn_signOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("My Profile");

        adminEmailAK = "akshaysolanki941@gmail.com";

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        /*if (user != null) {
            uid = user.getUid();
        } else {
            Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }*/

        btn_signOut = (Button) view.findViewById(R.id.btn_signOut);
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        loading_dialog = new Dialog(getContext());
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getActivity()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        if (auth.getCurrentUser() != null && (auth.getCurrentUser().getEmail().equals(adminEmailAK))) {
            mainActivity.toolbar_title.setText("My Profile (ADMIN)");
        }

        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        cv_bookmarked_schemes = (CardView) view.findViewById(R.id.cv_bookmarked_schemes);
        tv_noOfEnrolledSchemes = (TextView) view.findViewById(R.id.tv_noOfenrolledSchemes);
        tv_noOfEnrolledSchemes.setVisibility(View.GONE);

        Boolean firstLoad = getActivity().getSharedPreferences("PREFERENCE2", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            TapTargetView.showFor(getActivity(), TapTarget.forView(view.findViewById(R.id.tv2), "Click to view your bookmarked schemes")
                            .cancelable(true)
                            .tintTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                        }
                    });

            getActivity().getSharedPreferences("PREFERENCE2", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseUser user = auth.getCurrentUser();
        final String UID = user.getUid();

        ref = FirebaseDatabase.getInstance().getReference("Profile/" + UID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UsersModel model = dataSnapshot.getValue(UsersModel.class);
                tv_userName.setText(model.getUserName());
                tv_email.setText(model.getEmail());

                loading_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Enable to fetch data. " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        cv_bookmarked_schemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BookmarkedSchemesActivity.class));
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Profile/" + UID + "/enrolledSchemes");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = dataSnapshot.getChildrenCount();
                tv_noOfEnrolledSchemes.setText("According to the data you provided, you are currently enrolled in " + number + " scheme(s)");
                tv_noOfEnrolledSchemes.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
