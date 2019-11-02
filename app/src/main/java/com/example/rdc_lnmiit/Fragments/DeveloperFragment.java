package com.example.rdc_lnmiit.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class DeveloperFragment extends Fragment {

    TextView contact_no, gmail_id;
    ImageView linkedin, fb, insta;
    CircleImageView myImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_developer, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("Developer");

        contact_no = (TextView) view.findViewById(R.id.contact_no);
        gmail_id = (TextView) view.findViewById(R.id.gmail_id);
        linkedin = (ImageView) view.findViewById(R.id.linkedin_icon);
        fb = (ImageView) view.findViewById(R.id.fb_icon);
        insta = (ImageView) view.findViewById(R.id.insta_icon);
        myImage = (CircleImageView)view.findViewById(R.id.circleImageView);

        contact_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact_no.getText().toString(), null)));

            }
        });

        gmail_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + gmail_id.getText().toString()));
                    //    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    //  intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.linkedin.com/in/akshaysolanki941/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.facebook.com/akshaysolanki941");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.instagram.com/oyee.velle/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        Boolean firstLoad = getActivity().getSharedPreferences("PREFERENCE4", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            TapTargetView.showFor(getActivity(), TapTarget.forView(view.findViewById(R.id.m), "Thanks for trying out the app :)")
                            .cancelable(true)
                            .tintTarget(true));

            getActivity().getSharedPreferences("PREFERENCE4", MODE_PRIVATE).edit().putBoolean("firstLoad", false).commit();

        }

        return view;
    }
}
