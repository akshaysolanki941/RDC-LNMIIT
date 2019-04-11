package com.example.rdc_lnmiit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class MainActivity extends BaseActivity {

    EditText gov_scheme_editText;
    EditText year_editText;
    EditText motive_editText;
    EditText bene_editText;
    EditText mile_editText;
    Button btn_submit, btn_photo_select;
    ImageView scheme_imageView, loading_gif_imageView;
    RadioGroup radioGroup, rg_inOperation;
    Spinner category_spinner;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    String currentTheme;
    Context context;
    Uri selectedPhotoUri;
    ProgressBar add_progressBar;
    TextView adding;
    Dialog loading_dialog;

    DatabaseReference databaseReference1; /*databaseReference2*/
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Data");
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title_font);

        FirebaseApp.initializeApp(this);

        loading_dialog = new Dialog(this);

        databaseReference1 = FirebaseDatabase.getInstance().getReference("Data");

        gov_scheme_editText = (EditText) findViewById(R.id.gov_scheme_editText);
        year_editText = (EditText) findViewById(R.id.year_editText);
        motive_editText = (EditText) findViewById(R.id.motive_editText);
        bene_editText = (EditText) findViewById(R.id.bene_editText);
        mile_editText = (EditText) findViewById(R.id.mile_editText);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        btn_photo_select = (Button) findViewById(R.id.btn_photo_select);
        scheme_imageView = (ImageView) findViewById(R.id.scheme_imageView);
        rg_inOperation = (RadioGroup) findViewById(R.id.rg_inOperation);
        add_progressBar = (ProgressBar) findViewById(R.id.add_progressBar);
        adding = (TextView) findViewById(R.id.adding);

        add_progressBar.setVisibility(View.GONE);
        adding.setVisibility(View.GONE);

        btn_photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image_intent = new Intent(Intent.ACTION_PICK);
                image_intent.setType("image/*");
                startActivityForResult(image_intent, 1);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    if (radioGroup.getCheckedRadioButtonId() != -1 && rg_inOperation.getCheckedRadioButtonId() != -1 && !TextUtils.isEmpty(gov_scheme_editText.getText()) && !TextUtils.isEmpty(year_editText.getText()) && !TextUtils.isEmpty(motive_editText.getText()) && !TextUtils.isEmpty(bene_editText.getText())) {

                        if(selectedPhotoUri != null) {
                            loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loading_dialog.setContentView(R.layout.loading_dialog);
                            loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

                            Glide.with(getApplicationContext()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
                            loading_dialog.setCanceledOnTouchOutside(false);
                            loading_dialog.setCancelable(false);
                            loading_dialog.show();
                            String filename = UUID.randomUUID().toString();

                            storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);

                            storageReference.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            addData(uri.toString());
                                        }
                                    });

                                }
                            });
                        } else{
                            Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                            btn_photo_select.startAnimation(shake);
                        }

                    } else {
                        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            radioGroup.startAnimation(shake);
                        }

                        if (rg_inOperation.getCheckedRadioButtonId() == -1) {
                            rg_inOperation.startAnimation(shake);
                        }

                        if(TextUtils.isEmpty(gov_scheme_editText.getText())){
                            Snackbar.make(findViewById(R.id.relative), "Scheme name field is empty", Snackbar.LENGTH_SHORT).show();
                        }

                        else if(TextUtils.isEmpty(year_editText.getText())){
                            Snackbar.make(findViewById(R.id.relative), "Year Initiated field is empty", Snackbar.LENGTH_SHORT).show();
                        }

                        else if(TextUtils.isEmpty(motive_editText.getText())){
                            Snackbar.make(findViewById(R.id.relative), "Motives field is empty", Snackbar.LENGTH_SHORT).show();
                        }

                        else if(TextUtils.isEmpty(bene_editText.getText())){
                            Snackbar.make(findViewById(R.id.relative), "Beneficiaries field is empty", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Snackbar.make(findViewById(R.id.relative), "No Internet", Snackbar.LENGTH_SHORT).show();

                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            scheme_imageView.setImageBitmap(bitmap);

            btn_photo_select.setAlpha(0);

        }

    }

    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState) {

    }


    private void addData(String picURL) {



        String category = category_spinner.getSelectedItem().toString();
        String scheme = gov_scheme_editText.getText().toString();
        String year = year_editText.getText().toString();
        String motive = motive_editText.getText().toString();
        String bene = bene_editText.getText().toString();
        String mile = mile_editText.getText().toString();
        String rg_value = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        String rg_inOperation_value = ((RadioButton) findViewById(rg_inOperation.getCheckedRadioButtonId())).getText().toString();

        // String id = databaseReference.push().getKey();


        CategoryModel categoryModel = new CategoryModel(category);

        // databaseReference2 = FirebaseDatabase.getInstance().getReference(category);


        Data data = new Data(/*id, */scheme, year, motive, bene, mile, rg_value, picURL, rg_inOperation_value);

        //databaseReference1.child(category).push().setValue(categoryModel);
        databaseReference1.child(category).child(scheme).setValue(data);

        gov_scheme_editText.getText().clear();
        year_editText.getText().clear();
        motive_editText.getText().clear();
        bene_editText.getText().clear();
        mile_editText.getText().clear();
        radioGroup.clearCheck();
        rg_inOperation.clearCheck();
        btn_photo_select.setAlpha(1);
        scheme_imageView.setImageDrawable(null);
        loading_dialog.dismiss();

        Snackbar.make(findViewById(R.id.relative), "Data Added", Snackbar.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2, menu);

        MenuItem item = menu.getItem(0);
        SpannableString s = new SpannableString("Show Data");
        s.setSpan(new ForegroundColorSpan(getAttributeColor(getApplicationContext(), R.attr.text)), 0, s.length(), 0);
        item.setTitle(s);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.show_data_menu:
                Intent intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // recreate();
    }

    public static int getAttributeColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            // Log.w(TAG, "Not found color resource by id: " + colorRes);
        }
        return color;
    }
}
