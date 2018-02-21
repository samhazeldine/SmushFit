package com.shazeldine.smushfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Entry> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createBeans() {
        try {
            userData = new CsvToBeanBuilder(new FileReader("UserData.csv")).withType(Entry.class).build().parse();
        }
        catch (IOException e) {
            Log.e("SMUSHFIT_LOG_TAG", "ERROR (IOException): ", e);
        }
    }

    public void testButton(View view) {
        createBeans();
        Button btn = findViewById(R.id.testButton);
        String str = "Clicked";
        //String str = userData.get(0).getAttribute();
        btn.setText(str);
    }
}
