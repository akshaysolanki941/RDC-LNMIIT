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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddEnrolledSchemesAdapter extends RecyclerView.Adapter<AddEnrolledSchemesAdapter.ViewHolder> {

    ArrayList<SchemeDataModel> Schemes;
    private Context context;

    public AddEnrolledSchemesAdapter() {

    }

    public AddEnrolledSchemesAdapter(ArrayList<SchemeDataModel> Schemes, Context context) {
        this.Schemes = Schemes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_add_enrolled_scheme_list_item, viewGroup, false);
        return new AddEnrolledSchemesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        setEnrolledStatus(holder, position);
        holder.schemeName.setText(Schemes.get(position).getScheme());
        holder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SchemesDetailsActivity.class);
                intent.putExtra("SchemesData", Schemes.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Schemes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView schemeName, tv_view_details;
        public TextView tv_enrolledStatus;
        public RelativeLayout foreground, background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            schemeName = (TextView) itemView.findViewById(R.id.scheme_name);
            tv_view_details = (TextView) itemView.findViewById(R.id.tv_view_details);
            tv_enrolledStatus = (TextView) itemView.findViewById(R.id.tv_enrolledStatus);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            foreground = (RelativeLayout) itemView.findViewById(R.id.foreground);
        }
    }

    public void setEnrolledStatus(final ViewHolder holder, final int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/enrolledSchemes");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SchemeDataModel> enrolledSchemes = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    enrolledSchemes.add(ds.getValue(SchemeDataModel.class));
                }
                if (!enrolledSchemes.isEmpty()) {
                    for (SchemeDataModel model : enrolledSchemes) {
                        if (model.getScheme().equals(Schemes.get(position).getScheme())) {
                            holder.tv_enrolledStatus.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            holder.tv_enrolledStatus.setVisibility(View.GONE);
                        }
                    }
                } else {
                    holder.tv_enrolledStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
