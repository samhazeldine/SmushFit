package com.shazeldine.smushfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Entry> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void testButton(View view) {
        List<String[]> userDataString = readCSV();
        userData = convertToBeanArray(userDataString);
        String str = userData.get(0).getAttribute();
        Button btn = findViewById(R.id.testButton);
        btn.setText(str);
    }
}
