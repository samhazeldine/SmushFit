package com.shazeldine.smushfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel Hazeldine on 26/03/2018.
 */

public class AttributeLookup extends Fragment implements View.OnClickListener {
    private NLGGenerator generator;
    private Lookup lookup;
    private String[] attributes;

    public static AttributeLookup newInstanceFour () {
        AttributeLookup fragment = new AttributeLookup();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.attribute_lookup, container, false);
        createSpinner(view);
        Button button = (Button) view.findViewById(R.id.attributeButton);
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
        Spinner spinner = (Spinner) view.findViewById(R.id.attributeSpinner);
        List<String> spinnerData = Arrays.asList(attributesConverted);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), DisplayLookupResultActivity.class);
        Spinner attributeSpinner = (Spinner) getActivity().findViewById(R.id.attributeSpinner);
        int spinnerPos = attributeSpinner.getSelectedItemPosition();
        String attr = attributes[spinnerPos];

        List<CorrelationIdentifier> correlationIdentifiers = lookup.findLikelyCorrelation(attr);
        double max = lookup.findMax(attr);
        double min = lookup.findMin(attr);
        double mean = lookup.findMean(attr);
        double trend = lookup.findTrend(attr);
        double current = lookup.findCurrent(attr);
        double goal = UserData.getGoalForAttribute(attr);
        String highLow = UserData.getGoalAimForAttribute(attr);

        String generatedStatement = generator.generateFullParagraphForAttribute(attr, correlationIdentifiers, max, min, mean, trend, current, goal, highLow);
        intent.putExtra("GENERATED_STATEMENT", generatedStatement);
        Log.i("SMUSHFIT_SPINNER_TEST", "The generated statement is: " + generatedStatement);
        startActivity(intent);
    }
}
