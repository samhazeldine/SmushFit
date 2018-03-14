package com.shazeldine.smushfit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Samuel Hazeldine on 13/03/2018.
 */

public class LookupSliderAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public LookupSliderAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return LookupActivity.newInstance();
            case 1:
                return LookupActivity.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Simple";
            case 1:
                return "Correlation";
            default:
                return "LOL";
        }
    }
}
