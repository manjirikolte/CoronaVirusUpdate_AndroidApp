package com.example.coronaupdate_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    TextView globalTextView;
    ListView listView_Countries;
    SimpleArcLoader loader_countries;
    countryAdapter myCountryAdapter;
    countryModel myCountryModel;
    public static List<countryModel> countryModelsList = new ArrayList<>();

    Boolean globalStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_country);

        listView_Countries = findViewById(R.id.listView_country);
        edit_search = findViewById(R.id.edit_search);
        loader_countries = findViewById(R.id.loader_country);

        /*
         * Fetching data function
         */
        fetchData();

        // To search specific Country Corona Update
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myCountryAdapter.getFilter().filter(s);
                myCountryAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        /*
         *  Back to the MainActivity with position of selected country
         */
        listView_Countries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalStatus = false;
                Intent intent = new Intent(AffectedCountryActivity.this, MainActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("status",globalStatus);
                startActivity(intent);
            }
        });

        /*
         * To set global data
         */
        globalTextView = findViewById(R.id.global);
        globalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalStatus = true;
                Intent intent = new Intent(AffectedCountryActivity.this, MainActivity.class);
                intent.putExtra("status",globalStatus);
                startActivity(intent);
            }
        });

    }

    /**
     *  Fetch Data Function declaration
     */
    private void fetchData() {
        loader_countries.start();

        String url = "https://disease.sh/v2/countries";

        StringRequest request = new StringRequest(Request.Method.GET, url.trim(), new Response.Listener<String>() {
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
                        String tests = jsonObject.getString("tests");

                        JSONObject object = jsonObject.getJSONObject("countryInfo");
                        String flagUrl = object.getString("flag");

                        myCountryModel = new countryModel(flagUrl,countryName, cases, todayCases, deaths, todayDeaths, recovered, active, critical, tests);
                        countryModelsList.add(myCountryModel);
                    }

                    myCountryAdapter = new countryAdapter(AffectedCountryActivity.this, countryModelsList);
                    listView_Countries.setAdapter(myCountryAdapter);

                    loader_countries.stop();
                    loader_countries.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AffectedCountryActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorMessageAffected",error.getMessage());
                Toast.makeText(AffectedCountryActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
