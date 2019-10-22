package com.example.rdc_lnmiit.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rdc_lnmiit.R;
import com.example.rdc_lnmiit.SchemesActivity;

import java.util.ArrayList;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.ViewHolder> {

    ArrayList<String> categories;
    private Context context;

    public MyAdpater(ArrayList<String> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdpater.ViewHolder viewHolder, final int i) {
        viewHolder.category.setText(categories.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SchemesActivity.class);
                intent.putExtra("categorySelected", categories.get(i).toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = (TextView) itemView.findViewById(R.id.txtCategory);
        }
    }
}
