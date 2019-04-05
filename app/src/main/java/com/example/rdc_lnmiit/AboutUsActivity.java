package com.example.rdc_lnmiit;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {

    Toolbar toolbar;
    SharedPreferences sharedPref;
    String currentTheme;
    Dialog ubaDialog, develDialog;
    CardView card_uba, card_devel;
    TextView uba_link, lnmiit_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Us");

        ubaDialog = new Dialog(this);
        develDialog = new Dialog(this);

        card_devel = (CardView) findViewById(R.id.card_devel);
        card_devel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogDevel();

            }
        });

        card_uba = (CardView) findViewById(R.id.card_uba);
        card_uba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogUba();

            }
        });
    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
       // recreate();
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


