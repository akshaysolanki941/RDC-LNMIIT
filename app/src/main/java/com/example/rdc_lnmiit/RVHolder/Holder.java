package com.example.rdc_lnmiit.RVHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.rdc_lnmiit.R;

public class Holder extends RecyclerView.ViewHolder {

    public TextView scheme_name;

    public Holder(@NonNull final View itemView) {
        super(itemView);

        scheme_name = (TextView) itemView.findViewById(R.id.scheme_name);

    }

    public void setScheme_name(TextView scheme_name) {
        this.scheme_name = scheme_name;
    }


}
