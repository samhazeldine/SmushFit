package com.shazeldine.smushfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LookupActivity extends AppCompatActivity {
    private NLGGenerator generator;
    private Lookup lookup;
    private String[] attributes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        generator = new NLGGenerator();
        lookup = new Lookup();
        createSpinner();
    }

    private void createSpinner() {
        //Creating insight spinner
        attributes = getIntent().getStringArrayExtra("attributes");
        String[] attributesConverted = new String[attributes.length];
        for(int i=0; i < attributes.length; i++) {
            String s = generator.attributeConverter(attributes[i])[1];
            attributesConverted[i] = s;
            //TODO Need to add question mark without NULL being given.
        }
        Spinner spinner = (Spinner) findViewById(R.id.insightSpinner);
        List<String> spinnerData = Arrays.asList(attributesConverted);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Creating current/max/min/goal spinner
        String[] curMaxMinGoal = {"current", "maximum", "minimum", "average", "goal for"};
        Spinner spinner2 = (Spinner) findViewById(R.id.currentSpinner);
        List<String> spinnerData2 = Arrays.asList(curMaxMinGoal);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }

    public void findLookup(View view) {
        //Intent intent = new Intent(this, DisplayLookupResultActivity.class);
        Spinner spinnerCurMaxMin = (Spinner) findViewById(R.id.currentSpinner);
        String type = spinnerCurMaxMin.getSelectedItem().toString();
        Spinner spinnerInsight = (Spinner) findViewById(R.id.insightSpinner);
        int insightPos = spinnerInsight.getSelectedItemPosition();
        String generatedStatement = "";
        String selectedInsight = attributes[insightPos];
        double value;
        switch(type) {
            case "current":
                value = lookup.findCurrent(selectedInsight);
                generatedStatement = generator.currentGenerator(selectedInsight, value);
                break;

            case "maximum":
                value = lookup.findMax(selectedInsight);
                generatedStatement = generator.maxGenerator(selectedInsight, value);
                break;

            case "minimum":
                value = lookup.findMin(selectedInsight);
                generatedStatement = generator.minGenerator(selectedInsight, value);
                break;

            case "average":
                value = lookup.findMean(selectedInsight);
                generatedStatement = generator.averageGenerator(selectedInsight, value);
                break;
            case "goal":
                value = 0.0;
                generatedStatement = generator.todayGoalGenerator(selectedInsight, "0", "0", "High");
                break;

        }
        Log.i("SMUSHFIT_SPINNER_TEST", "The generated statement is: " + generatedStatement);
    }
}
