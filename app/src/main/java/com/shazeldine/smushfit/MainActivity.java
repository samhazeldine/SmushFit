package com.shazeldine.smushfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserData userData;
    private String[] attributes =
            {"sleep", "steps", "distracting_min", "events", "mood", "productive_min", "sleep_awakenings", "tracks"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
        testInsights();
    }

    public void setupData() {
        if (userData == null) {
            List<String[]> userDataString = readCSV();
            userData = new UserData();
            convertToUserData(userDataString);
        }
        setGoals();
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
            userData.addDataForAttribute(entryString[0], entry);
        }
    }

    private void setGoals () {
        userData.getEntriesOfAttribute("sleep").setGoals("480", "High");
        userData.getEntriesOfAttribute("steps").setGoals("10000", "High");
        userData.getEntriesOfAttribute("distracting_min").setGoals("60", "Low");
        userData.getEntriesOfAttribute("events").setGoals("None", "None");
        userData.getEntriesOfAttribute("mood").setGoals("5", "High");
        userData.getEntriesOfAttribute("productive_min").setGoals("120", "High");
        userData.getEntriesOfAttribute("sleep_awakenings").setGoals("1", "Low");
        userData.getEntriesOfAttribute("tracks").setGoals("None", "None");
    }

    public void testInsights() {
        Lookup lookup = new Lookup();
        NLGGenerator generator = new NLGGenerator();
        generator.testGenerator();
    }

    private void testInsightsTwo () {
        Lookup lookup = new Lookup();
        NLGGenerator generator = new NLGGenerator();
        Log.i("SMUSHFIT_TEST", "TEST_BEGIN");
        for (int i = 0; i < attributes.length; i++) {
            double cor = lookup.findCorrelation(userData, attributes[i], "mood");
            String output = generator.correlationGenerator(attributes[i], "mood", cor);
            Log.i("SMUSHFIT_TEST", output);
            double max = lookup.findMax(userData, attributes[i]);
            output = generator.maxGenerator(attributes[i], max);
            Log.i("SMUSHFIT_TEST", output);
        }
        generator.testGenerator();
        Log.i("SMUSHFIT_TEST","TEST_END");
        double testData = lookup.findTrend(userData, "steps");
        Log.i("SMUSHFIT_TEST", "TREND DATA: " + testData);
    }

    public void goToLookupActivity(View view) {
        Intent intent = new Intent(this, LookupActivity.class);
        startActivity(intent);
    }
}