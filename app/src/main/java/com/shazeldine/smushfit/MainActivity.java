package com.shazeldine.smushfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
    }

    public void setupData() {
        List<String[]> userDataString = readCSV();
        convertToUserData(userDataString);
        setGoals();
        testInsights();
        //testInsightsTwo();
    }

    // Reads each line in the CSV and converts it into a 2D array of Strings.
    public List<String[]> readCSV() {
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("userdata.csv"));
            BufferedReader reader = new BufferedReader(is);
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> userDataStrings = csvReader.readAll();
            return userDataStrings;
        } catch (IOException e) {
            Log.e("SMUSHFIT_ERROR_TAG", "ERROR (IOException): ", e);
            return null;
        }
    }

    // Converts the elements into a UserData object
    public void convertToUserData(List<String[]> userDataString) {
        for (String[] entryString : userDataString) {
            Entry entry = new Entry(entryString[1], entryString[2]);
            UserData.addDataForAttribute(entryString[0], entry);
        }
    }

    private void setGoals () {
        UserData.getEntriesOfAttribute("sleep").setGoals(480, "High");
        UserData.getEntriesOfAttribute("steps").setGoals(10000, "High");
        UserData.getEntriesOfAttribute("distracting_min").setGoals(30, "Low");
        UserData.getEntriesOfAttribute("events").setGoals(-1, "None");
        UserData.getEntriesOfAttribute("mood").setGoals(5, "High");
        UserData.getEntriesOfAttribute("productive_min").setGoals(120, "High");
        UserData.getEntriesOfAttribute("sleep_awakenings").setGoals(2, "Low");
        UserData.getEntriesOfAttribute("tracks").setGoals(-1, "None");
        UserData.getEntriesOfAttribute("test1").setGoals(2, "High");
        UserData.getEntriesOfAttribute("test2").setGoals(2, "High");
        UserData.getEntriesOfAttribute("test3").setGoals(2, "Low");
        UserData.getEntriesOfAttribute("test4").setGoals(2, "High");

    }

    public void testInsights() {
        Lookup lookup = new Lookup();
        NLGGenerator generator = new NLGGenerator();
        String s = generator.trendGenerator("steps", lookup.findTrend("steps"));
        Log.i("SMUSHFIT_TEST", s);
    }

    private void testInsightsTwo () {
        Lookup lookup = new Lookup();
        NLGGenerator generator = new NLGGenerator();
        Log.i("SMUSHFIT_TEST", "TEST_BEGIN");
        for (int i = 0; i < UserData.getAttributes().length; i++) {
            for (int j = 0; j < UserData.getAttributes().length; j++) {
                double cor = lookup.findCorrelation(UserData.getAttributes()[j] , UserData.getAttributes()[i]);
                String output = generator.correlationGenerator(UserData.getAttributes()[j], UserData.getAttributes()[i], cor);
                Log.i("SMUSHFIT_TEST", output);
            }

        }
        generator.testGenerator();
        String s = generator.todayGoalGenerator("steps", "10000", "12000", "High");
        Log.i("SMUSHFIT_TEST", s);
        String s1 = generator.todayGoalGenerator("sleep_awakenings", "4", "5", "Low");
        Log.i("SMUSHFIT_TEST", s1);
        String s2 = generator.todayGoalGenerator("steps", "10000", "4522", "High");
        Log.i("SMUSHFIT_TEST", s2);
        String s3 = generator.todayGoalGenerator("sleep_awakenings", "4", "1", "Low");
        Log.i("SMUSHFIT_TEST", s3);
        Log.i("SMUSHFIT_TEST","TEST_END");
    }

    public void goToLookupActivity(View view) {
        Intent intent = new Intent(this, LookupSlider.class);
        startActivity(intent);
    }
}