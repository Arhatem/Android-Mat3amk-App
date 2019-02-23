package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mat3amk.Model.Body;

import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

private Context context;
private List<Body.Result>  data;

    public NearbyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_item,viewGroup,false);

        NearbyAdapter.ViewHolder viewHolder = new NearbyAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Body.Result result = data.get(i);
        String name = result.getPoi().getName();
        String steet = result.getAddress().getStreetName();
        String qesm = result.getAddress().getMunicipalitySubdivision();

        viewHolder.name.setText(name);
        viewHolder.street.setText(steet);
        viewHolder.qesm.setText(qesm);

    }

    @Override
    public int getItemCount() {
        if(data !=null)
            return data.size();

        return 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView street;
        TextView qesm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_tv);
            street = itemView.findViewById(R.id.street_name);
            qesm = itemView.findViewById(R.id.qesm_tv);
        }
    }

    public List<Body.Result> getData() {
        return data;
    }

    public void setData(List<Body.Result> list)
    {
        if(list!=null)
        {
            data = list;
            notifyDataSetChanged();
        }
    }
}
