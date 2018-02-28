package com.shazeldine.smushfit;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.opencsv.CSVReader;
import com.shazeldine.smushfit.dummy.DummyContent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends FragmentActivity implements InsightFragment.OnListFragmentInteractionListener {

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
        calculateInsights();
    }

    public void onListFragmentInteraction(DummyContent.DummyItem item) {
    }

    public void setupData() {
        if (userData == null) {
            List<String[]> userDataString = readCSV();
            userData = new UserData();
            convertToUserData(userDataString);
        }
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
            Log.e("SMUSHFIT_LOG_TAG", "ERROR (IOException): ", e);
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

    public void calculateInsights() {
        Lookup lookup = new Lookup();
        double correlationPValue = lookup.findCorrelation(userData, "test1", "test2");
        Log.i("SMUSHFIT_TEST_TAG", "Correlation value is: " + correlationPValue);
    }
}