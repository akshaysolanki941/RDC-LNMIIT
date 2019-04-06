package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.CardView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomSheetDialogAboutUs extends BottomSheetDialogFragment {

    Dialog ubaDialog, develDialog;
    CardView card_uba, card_devel;
    TextView uba_link, lnmiit_link;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_aboutus, container, false);

        ubaDialog = new Dialog(getContext());
        develDialog = new Dialog(getContext());

        card_devel = (CardView) v.findViewById(R.id.card_devel);
        card_devel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogDevel();

            }
        });

        card_uba = (CardView) v.findViewById(R.id.card_uba);
        card_uba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogUba();

            }
        });

        return v;
    }

    public void showDialogUba(){

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

    }
}
