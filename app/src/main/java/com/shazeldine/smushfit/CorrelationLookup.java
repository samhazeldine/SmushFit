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
 * Created by Samuel Hazeldine on 14/03/2018.
 */

public class CorrelationLookup extends Fragment implements View.OnClickListener {
    private NLGGenerator generator;
    private Lookup lookup;
    private String[] attributes;

    public static CorrelationLookup newInstanceTwo () {
        CorrelationLookup fragmentTwo = new CorrelationLookup();
        return fragmentTwo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generator = new NLGGenerator();
        lookup = new Lookup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.correlation_lookup, container, false);
        createSpinner(view);
        Button button = (Button) view.findViewById(R.id.correlationButton);
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
        Spinner spinner = (Spinner) view.findViewById(R.id.correlationSpinnerOne);
        List<String> spinnerDataOne = Arrays.asList(attributesConverted);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerDataOne);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List<String> spinnerDataTwo = Arrays.asList(attributesConverted);
        Spinner spinner2 = (Spinner) view.findViewById(R.id.correlationSpinnerTwo);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerDataTwo);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), DisplayLookupResultActivity.class);
        Spinner correlationSpinnerOne = (Spinner) getActivity().findViewById(R.id.correlationSpinnerOne);
        int spinnerOnePos = correlationSpinnerOne.getSelectedItemPosition();
        String attr1 = attributes[spinnerOnePos];
        Spinner correlationSpinnerTwo = (Spinner) getActivity().findViewById(R.id.correlationSpinnerTwo);
        int spinnerTwoPos = correlationSpinnerTwo.getSelectedItemPosition();
        String attr2 = attributes[spinnerTwoPos];
        double correlationP = lookup.findCorrelation(attr1, attr2);
        String generatedStatement = generator.correlationGenerator(attr1, attr2, correlationP);
        intent.putExtra("GENERATED_STATEMENT", generatedStatement);
        Log.i("SMUSHFIT_SPINNER_TEST", "The generated statement is: " + generatedStatement);
        startActivity(intent);
    }
}
