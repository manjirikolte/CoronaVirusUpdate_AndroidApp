package com.example.coronaupdate_app;

public class countryModel {
    private String flag, country, cases, todayCases, deaths, todayDeaths, recover,
            activeCases, critical, test;

    public countryModel(String flag, String country, String cases, String todayCases, String deaths, String todayDeaths, String recover, String activeCases, String critical, String test) {
        this.flag = flag;
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recover = recover;
        this.activeCases = activeCases;
        this.critical = critical;
        this.test = test;
    }

    public String getFlag() {
        return flag;
    }

    public String getCountry() {
        return country;
    }

    public String getCases() {
        return cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public String getRecover() {
        return recover;
    }

    public String getActiveCases() {
        return activeCases;
    }

    public String getCritical() {
        return critical;
    }

    public String getTest() {
        return test;
    }
}
