package com.example.coronaupdate_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

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

}
