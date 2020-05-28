package com.example.coronaupdate_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView totalCases_tv, totalDeaths_tv, recoverCases_tv, activeCases_tv, todayCases, todayDeath,
                criticalCases, totalTests;
    Button selectCounty_btn;
    SimpleArcLoader totalCaseLoader, moreDetailsLoader;
    PieChart pieChart;
    LinearLayout totalCases_linearLayout, moreDetails_linearLayout;

    int positionCountry;
    boolean globalStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For transparent Status bar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        totalCases_tv = findViewById(R.id.totalCases_tv);
        todayCases = findViewById(R.id.todayCases);
        totalDeaths_tv = findViewById(R.id.totalDeaths_tv);
        todayDeath = findViewById(R.id.todayDeath);
        criticalCases = findViewById(R.id.criticalCases);
        totalTests = findViewById(R.id.totalTest);
        recoverCases_tv = findViewById(R.id.recoverCases_tv);
        activeCases_tv = findViewById(R.id.activeCases_tv);
        moreDetailsLoader = findViewById(R.id.loader_moreDetails);
        totalCaseLoader = findViewById(R.id.loader_totalCases);
        pieChart = findViewById(R.id.pieChart);
        totalCases_linearLayout = findViewById(R.id.totalCases_linearLayout);
        moreDetails_linearLayout = findViewById(R.id.moreDetails_linearLayout);
        selectCounty_btn = findViewById(R.id.selectCounty_btn);

        try {
            Intent intent = getIntent();
            globalStatus = intent.getBooleanExtra("status",true);
        }catch (Exception e){
            Toast.makeText(this, "Global Status", Toast.LENGTH_SHORT).show();
        }

        if (globalStatus){
            fetchData();
        }
        if (!globalStatus){
            Intent intent = getIntent();
            positionCountry = intent.getIntExtra("position",0);

            totalCases_tv.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getCases());
            totalDeaths_tv.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getDeaths());
            activeCases_tv.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getActiveCases());
            recoverCases_tv.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getRecover());
            criticalCases.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getCritical());

            todayDeath.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getTodayDeaths());
            todayCases.setText(AffectedCountryActivity.countryModelsList.get(positionCountry).getTodayCases());



            totalCaseLoader.stop();
            moreDetailsLoader.stop();
            totalCaseLoader.setVisibility(View.GONE);
            moreDetailsLoader.setVisibility(View.GONE);
            totalCases_linearLayout.setVisibility(View.VISIBLE);
            moreDetails_linearLayout.setVisibility(View.VISIBLE);
            pieChartUpdate();
        }


        selectCounty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AffectedCountryActivity.class));
            }
        });


    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";

        totalCaseLoader.start();
        moreDetailsLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Set Json Data into textViews
                    totalCases_tv.setText(jsonObject.getString("cases"));
                    totalDeaths_tv.setText(jsonObject.getString("deaths"));
                    recoverCases_tv.setText(jsonObject.getString("recovered"));
                    activeCases_tv.setText(jsonObject.getString("active"));
                    todayCases.setText(jsonObject.getString("todayCases"));
                    todayDeath.setText(jsonObject.getString("todayDeaths"));
                    criticalCases.setText(jsonObject.getString("critical"));
                    totalTests.setText(jsonObject.getString("tests"));

                    pieChartUpdate();

                    totalCaseLoader.stop();
                    moreDetailsLoader.stop();
                    totalCaseLoader.setVisibility(View.GONE);
                    moreDetailsLoader.setVisibility(View.GONE);
                    totalCases_linearLayout.setVisibility(View.VISIBLE);
                    moreDetails_linearLayout.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // To Update Pie chart
    public void pieChartUpdate(){
        long recovered = Long.parseLong(recoverCases_tv.getText().toString());
        long active = Long.parseLong(activeCases_tv.getText().toString());
        long death = Long.parseLong(totalDeaths_tv.getText().toString());
        long critical = Long.parseLong(criticalCases.getText().toString());

        pieChart.addPieSlice(new PieModel("critical",critical, Color.parseColor("#ff704d")));
        pieChart.addPieSlice(new PieModel("recovered",recovered, Color.parseColor("#66ff33")));
        pieChart.addPieSlice(new PieModel("deaths",death, Color.parseColor("#ff0000")));
        pieChart.addPieSlice(new PieModel("active",active, Color.parseColor("#33bbff")));
        pieChart.startAnimation();
    }

    // For completely Transparent StatusBar
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
