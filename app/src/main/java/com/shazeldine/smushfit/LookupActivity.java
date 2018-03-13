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
        String[] attributes = getIntent().getStringArrayExtra("attributes");
        for(int i=0; i < attributes.length; i++) {
            String s = generator.attributeConverter(attributes[i])[1];
            attributes[i] = s;
            //TODO Need to add question mark without NULL being given.
        }
        Spinner spinner = (Spinner) findViewById(R.id.insightSpinner);
        List<String> spinnerData = Arrays.asList(attributes);
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
        String insight = spinnerInsight.getSelectedItem().toString();
        String generatedStatement;
        switch(type) {
            case "current":

        }

    }
}
