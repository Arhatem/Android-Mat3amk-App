package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mat3amk.Model.Body;

import java.util.ArrayList;
import java.util.List;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> implements Filterable {

private Context context;
private List<Body.Result>  data;
    private List<Body.Result> resListFull;
    private Filter resFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Body.Result> filteredList = new ArrayList<>();

            if(TextUtils.isEmpty(constraint))
            {
                filteredList.addAll(resListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Body.Result restaurant : resListFull)
                {
                    if(restaurant.getPoi().getName().toLowerCase().contains(filterPattern))
                        filteredList.add(restaurant);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public NearbyAdapter(Context context,List<Body.Result> data) {
        this.data = data;
        resListFull = new ArrayList<>(this.data);
        this.context = context;

    }
    @Override
    public Filter getFilter() {
        return resFilter;
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
            resListFull = new ArrayList<>(data);
            notifyDataSetChanged();
        }
    }
}
