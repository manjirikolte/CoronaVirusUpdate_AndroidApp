package com.example.coronaupdate_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountryActivity extends AppCompatActivity {

    EditText edit_search;
    ListView listView_Countries;
    SimpleArcLoader loader_countries;
    countryAdapter myCountryAdapter;
    countryModel myCountryModel;
    public static List<countryModel> countryModelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_country);

        listView_Countries = findViewById(R.id.listView_country);
        edit_search = findViewById(R.id.edit_search);
        loader_countries = findViewById(R.id.loader_country);

        fetchData();
    }
    private void fetchData() {

        String url = "https://disease.sh/v2/countries";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String countryName = jsonObject.getString("country");
                        String cases = jsonObject.getString("cases");
                        String deaths = jsonObject.getString("deaths");
                        String todayCases = jsonObject.getString("todayCases");
                        String todayDeaths = jsonObject.getString("todayDeaths");
                        String recovered = jsonObject.getString("recovered");
                        String active = jsonObject.getString("active");
                        String critical = jsonObject.getString("critical");

                        JSONObject object = jsonObject.getJSONObject("countryInfo");
                        String flagUrl = object.getString("flag");

                        myCountryModel = new countryModel(flagUrl,countryName, cases, todayCases, deaths, todayDeaths, recovered, active, critical);
                        countryModelsList.add(myCountryModel);
                    }


                    myCountryAdapter = new countryAdapter(AffectedCountryActivity.this, countryModelsList);
                    listView_Countries.setAdapter(myCountryAdapter);

                    loader_countries.stop();
                    loader_countries.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AffectedCountryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AffectedCountryActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
