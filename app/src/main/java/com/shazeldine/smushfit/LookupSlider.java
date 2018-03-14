package com.shazeldine.smushfit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LookupSlider extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookup_page_slider);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        adapterViewPager = new LookupSliderAdapter(getSupportFragmentManager());
        pager.setAdapter(adapterViewPager);
    }

}
