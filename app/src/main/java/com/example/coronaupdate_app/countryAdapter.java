package com.example.coronaupdate_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class countryAdapter extends ArrayAdapter<countryModel> {
    private List<countryModel> countryModelList;
    private List<countryModel> filterCountryModelList;
    private Context context;

    public countryAdapter(Context context, List<countryModel> countryModelList) {
        super(context, R.layout.design_country_listview, countryModelList);
        this.context = context;
        this.countryModelList = countryModelList;
        this.filterCountryModelList = countryModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.design_country_listview, parent, false);
        }

        TextView textView_countryName = listItemView.findViewById(R.id.textView_countryName);
        ImageView imageView_countryFlag = listItemView.findViewById(R.id.image_flag);

        textView_countryName.setText(filterCountryModelList.get(position).getCountry());
        Glide.with(context).load(filterCountryModelList.get(position).getFlag()).into(imageView_countryFlag);

        return listItemView;
    }

    @Override
    public int getCount() {
        return filterCountryModelList.size();
    }

    @Nullable
    @Override
    public countryModel getItem(int position) {
        return filterCountryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0){
                    filterResults.count = countryModelList.size();
                    filterResults.values = countryModelList;

                }else {
                    List<countryModel> resultModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (countryModel itemModel: countryModelList){
                        if (itemModel.getCountry().toLowerCase().contains(searchStr)){
                            resultModel.add(itemModel);
                        }
                        filterResults.count = resultModel.size();
                        filterResults.values = resultModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filterCountryModelList = (List<countryModel>) results.values;
                AffectedCountryActivity.countryModelsList = (List<countryModel>) results.values;

                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
