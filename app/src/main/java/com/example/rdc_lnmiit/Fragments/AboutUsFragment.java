package com.example.rdc_lnmiit.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rdc_lnmiit.MainActivity;
import com.example.rdc_lnmiit.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class AboutUsFragment extends Fragment {

    Dialog ubaDialog, develDialog;
    CardView card_uba, card_devel;
    TextView uba_link, lnmiit_link;
    RelativeLayout rl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar_title.setText("About Us");

        uba_link = (TextView) view.findViewById(R.id.uba_link);
        uba_link.setMovementMethod(LinkMovementMethod.getInstance());

        lnmiit_link = (TextView) view.findViewById(R.id.lnmiit_link);
        lnmiit_link.setMovementMethod(LinkMovementMethod.getInstance());

       /* ubaDialog = new Dialog(getContext());
        develDialog = new Dialog(getContext());

        card_devel = (CardView) view.findViewById(R.id.card_devel);
        card_devel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogDevel();

            }
        });

        card_uba = (CardView) view.findViewById(R.id.card_uba);
        card_uba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogUba();

            }
        });*/

        return view;
    }

   /* public void showDialogUba(){

        ubaDialog.setContentView(R.layout.dialog_uba);

        uba_link = (TextView) ubaDialog.findViewById(R.id.uba_link);
        uba_link.setMovementMethod(LinkMovementMethod.getInstance());

        lnmiit_link = (TextView) ubaDialog.findViewById(R.id.lnmiit_link);
        lnmiit_link.setMovementMethod(LinkMovementMethod.getInstance());

        ubaDialog.show();
        ubaDialog.setCanceledOnTouchOutside(true);
    }

    public void showDialogDevel(){

        develDialog.setContentView(R.layout.dialog_devel);

        develDialog.show();
        develDialog.setCanceledOnTouchOutside(true);

    }*/
}
