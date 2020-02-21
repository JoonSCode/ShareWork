package com.sharework.PartTimeFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PtTabPagerAdapter extends FragmentStatePagerAdapter {

    public PtTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PartTimeMenu1Fragment();
            case 1:
                return new PartTimeMenu2Fragment();
            case 3:
                return new PartTimeMenu3Fragment();
            case 4:
                return new PartTimeMenu4Fragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
