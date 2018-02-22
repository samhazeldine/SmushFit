package com.shazeldine.smushfit;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.opencsv.CSVReader;
import com.shazeldine.smushfit.dummy.DummyContent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements InsightFragment.OnListFragmentInteractionListener {

    private List<Entry> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
        generateInsights();
    }

    public void onListFragmentInteraction(DummyContent.DummyItem item) {
    }

    public void setupData() {
        if (userData == null) {
            List<String[]> userDataString = readCSV();
            userData = convertToBeanArray(userDataString);
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
        }
        catch (IOException e) {
            Log.e("SMUSHFIT_LOG_TAG", "ERROR (IOException): ", e);
            return null;
        }
    }

    // Takes a 2D array of Strings and converts into a list of beans.
    public List<Entry> convertToBeanArray(List<String[]> userDataStrings) {
        List<Entry> tempUserData = new ArrayList<>();
        for (String[] temp : userDataStrings) {
            tempUserData.add(new Entry(temp[0], temp[1], temp[2]));
        }
        return tempUserData;
    }

    public void generateInsights() {

    }

}
