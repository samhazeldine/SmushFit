package com.shazeldine.smushfit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LookupActivity extends Fragment implements View.OnClickListener {
    private NLGGenerator generator;
    private Lookup lookup;
    private String[] attributes;


    public static LookupActivity newInstance() {
        LookupActivity fragment = new LookupActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generator = new NLGGenerator();
        lookup = new Lookup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_lookup, container, false);
        createSpinner(view);
        Button button = (Button) view.findViewById(R.id.lookupButton);
        button.setOnClickListener(this);
        return view;
    }

    private void createSpinner(ViewGroup view) {
        //Creating insight spinner
        attributes = UserData.getAttributes();
        Log.i("SMUSHFIT_TEST_TAG", "POSITION 1 REACHED");
        String[] attributesConverted = new String[attributes.length];
        for(int i=0; i < attributes.length; i++) {
            String s = generator.attributeConverter(attributes[i])[1];
            attributesConverted[i] = s;
            //TODO Need to add question mark without NULL being given.
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.insightSpinner);
        List<String> spinnerData = Arrays.asList(attributesConverted);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Creating current/max/min/goal spinner
        String[] curMaxMinGoal = {"current", "maximum", "minimum", "average", "goal for"};
        Spinner spinner2 = (Spinner) view.findViewById(R.id.currentSpinner);
        List<String> spinnerData2 = Arrays.asList(curMaxMinGoal);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), DisplayLookupResultActivity.class);
        Spinner spinnerCurMaxMin = (Spinner) getActivity().findViewById(R.id.currentSpinner);
        String type = spinnerCurMaxMin.getSelectedItem().toString();
        Spinner spinnerInsight = (Spinner) getActivity().findViewById(R.id.insightSpinner);
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
            case "goal for":
                value = UserData.getGoalForAttribute(selectedInsight);
                String goalAim = UserData.getGoalAimForAttribute(selectedInsight);
                double currentValue = lookup.findCurrent(selectedInsight);
                generatedStatement = generator.todayGoalGenerator(selectedInsight, value, currentValue, goalAim);
                break;

        }
        intent.putExtra("GENERATED_STATEMENT", generatedStatement);
        Log.i("SMUSHFIT_SPINNER_TEST", "The generated statement is: " + generatedStatement);
        startActivity(intent);
    }
}
