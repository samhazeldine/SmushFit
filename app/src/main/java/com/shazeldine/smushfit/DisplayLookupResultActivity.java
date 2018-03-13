package com.shazeldine.smushfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayLookupResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lookup_result);
        displayStatement();
    }

    private void displayStatement() {
        Intent intent = getIntent();
        String statement = intent.getStringExtra("GENERATED_STATEMENT");
        TextView textView = findViewById(R.id.statementTextView);
        textView.setText(statement);
    }
}
