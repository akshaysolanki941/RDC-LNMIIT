package com.example.rdc_lnmiit.Fragments;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.Models.BarchartModel;
import com.example.rdc_lnmiit.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class GraphFragment extends Fragment {

    BarChart barChart;
    ArrayList<BarchartModel> barchartModelList = new ArrayList<>();
    ArrayList<String> schemeNameList = new ArrayList<>();
    String schemeName;
    int i;
    Spinner spinner;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("Graph");

        loading_dialog = new Dialog(getContext());
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);
        Glide.with(getActivity()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);
        loading_dialog.show();

        spinner = (Spinner) view.findViewById(R.id.spinner);
        barChart = (BarChart) view.findViewById(R.id.barChart);
        getSchemeNames();
        populateBarchartModelList();

        return view;
    }

    void getSchemeNames() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Data");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        schemeName = ds1.child("scheme").getValue(String.class);
                        schemeNameList.add(schemeName);
                    }
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, schemeNameList);
                spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void populateBarchartModelList() {
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Profile");
        ref1.keepSynced(true);
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final String schemename : schemeNameList) {
                    i = 0;
                    for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
                        for (DataSnapshot ds3 : ds2.child("enrolledSchemes").getChildren()) {
                            if (ds3.child("scheme").getValue(String.class).equals(schemename)) {
                                i++;
                            }
                        }
                    }
                    BarchartModel model = new BarchartModel(schemename, i);
                    barchartModelList.add(model);
                }
                loadIntoBarchart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadIntoBarchart() {
        List<BarEntry> entries = new ArrayList<>();
        int j = 0;
        for (BarchartModel model : barchartModelList) {
            entries.add(new BarEntry(j, model.getNoOfPeopleEnrolled()));
            j++;
        }

        loading_dialog.dismiss();

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu_bold.ttf");
        BarDataSet dataSet = new BarDataSet(entries, "Number of people enrolled");
        dataSet.setValueTextSize(10f);
        dataSet.setValueTypeface(font);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.35f);
        barChart.setData(data);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(true);
        final YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setEnabled(true);

        XAxis topXAxis = barChart.getXAxis();
        topXAxis.setEnabled(false);
        final XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setLabelRotationAngle(90);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(schemeNameList));

        barChart.setFitBars(true);
        barChart.animateY(2000, Easing.EaseOutBack);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setVisibleXRangeMaximum(10);
        barChart.zoomAndCenterAnimated(4.6f, 1f, 0, barchartModelList.get(0).getNoOfPeopleEnrolled(), leftYAxis.getAxisDependency(), 3000);
        barChart.invalidate();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                barChart.centerViewToAnimated(position, barchartModelList.get(position).getNoOfPeopleEnrolled(), leftYAxis.getAxisDependency(), 1000);
                //barChart.zoom(1.4f, 1f, position, barchartModelList.get(position).getNoOfPeopleEnrolled(), leftYAxis.getAxisDependency());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
