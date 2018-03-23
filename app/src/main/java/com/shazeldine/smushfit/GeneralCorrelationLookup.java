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
 * Created by Samuel Hazeldine on 23/03/2018.
 */

public class GeneralCorrelationLookup extends Fragment implements View.OnClickListener {
    private NLGGenerator generator;
    private Lookup lookup;
    private String[] attributes;

    public static GeneralCorrelationLookup generalCorrelationLookupInstance() {
        GeneralCorrelationLookup instance = new GeneralCorrelationLookup();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generator = new NLGGenerator();
        lookup = new Lookup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.general_correlation_lookup, container, false);
        createSpinner(view);
        Button button = (Button) view.findViewById(R.id.generalCorrelationButton);
        button.setOnClickListener(this);
        return view;
    }

    private void createSpinner(ViewGroup view) {
        //Creating insight spinner
        attributes = UserData.getAttributes();
        String[] attributesConverted = new String[attributes.length];
        for(int i=0; i < attributes.length; i++) {
            String s = generator.attributeConverter(attributes[i])[1];
            attributesConverted[i] = s;
            //TODO Need to add question mark without NULL being given.
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.generalCorrelationSpinner);
        List<String> spinnerDataOne = Arrays.asList(attributesConverted);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerDataOne);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), DisplayLookupResultActivity.class);
        Spinner generalCorrelationSpinner = (Spinner) getActivity().findViewById(R.id.generalCorrelationSpinner);
        int spinnerOnePos = generalCorrelationSpinner.getSelectedItemPosition();
        String attr1 = attributes[spinnerOnePos];
        //Get values
        //Get strings
        String generatedStatement = "";
        intent.putExtra("GENERATED_STATEMENT", generatedStatement);
        Log.i("SMUSHFIT_SPINNER_TEST", "The generated statement is: " + generatedStatement);
        startActivity(intent);
    }

}
