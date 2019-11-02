package com.example.rdc_lnmiit.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rdc_lnmiit.Adapters.MyAdpater;
import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFragment extends Fragment {

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
    RelativeLayout rl;
    AnimationDrawable animationDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("Select Category");

        firebaseAuth = FirebaseAuth.getInstance();
        adminEmailAK = "akshaysolanki941@gmail.com";
        adminEmailRG = "gupta.rohan@live.com";

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Toast.makeText(this, "ONLINE", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Online", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "OFFLINE", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Offline", Toast.LENGTH_SHORT).show();
        }

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
        categories.add("Ministry of water resources, river development and Ganga rejuvenation");
        categories.add("Ministry of labour and employment");
        categories.add("Ministry of electronics and IT");
        categories.add("Ministry of road transport and waterways");
        categories.add("Miscellaneous schemes");

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdpater(categories, getContext());
        rv.setAdapter(adapter);

        return view;
    }
}
