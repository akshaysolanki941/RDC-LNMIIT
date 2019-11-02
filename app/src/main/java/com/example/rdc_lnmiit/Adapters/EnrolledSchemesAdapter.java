package com.example.rdc_lnmiit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rdc_lnmiit.Models.SchemeDataModel;
import com.example.rdc_lnmiit.R;
import com.example.rdc_lnmiit.SchemesDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EnrolledSchemesAdapter extends RecyclerView.Adapter<EnrolledSchemesAdapter.ViewHolder> {

    ArrayList<SchemeDataModel> enrolledSchemes;
    private Context context;

    public EnrolledSchemesAdapter(ArrayList<SchemeDataModel> enrolledSchemes, Context context) {
        this.enrolledSchemes = enrolledSchemes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_enrolled_scheme_list_item, viewGroup, false);
        return new EnrolledSchemesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.schemeName.setText(enrolledSchemes.get(position).getScheme());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SchemesDetailsActivity.class);
                intent.putExtra("SchemesData", enrolledSchemes.get(position));
                context.startActivity(intent);
            }
        });
    }

    public void removeItem(int position) {
        SchemeDataModel model = enrolledSchemes.get(position);
        deleteFromDB(model.getScheme());
        enrolledSchemes.remove(position);
        notifyItemRemoved(position);
    }

    private void deleteFromDB(String schemeName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/enrolledSchemes");
        ref.child(schemeName).removeValue();
    }

    public void restoreItem(SchemeDataModel model){
        restoreInDB(model);
        enrolledSchemes.add(model);
        notifyDataSetChanged();
    }

    private void restoreInDB(SchemeDataModel model){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/enrolledSchemes");
        ref.child(model.getScheme()).setValue(model);
    }

    @Override
    public int getItemCount() {
        return enrolledSchemes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView schemeName;
        public RelativeLayout foreground, background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            schemeName = (TextView) itemView.findViewById(R.id.scheme_name);
            foreground = (RelativeLayout)itemView.findViewById(R.id.foreground);
            background = (RelativeLayout)itemView.findViewById(R.id.background);
        }
    }

}
