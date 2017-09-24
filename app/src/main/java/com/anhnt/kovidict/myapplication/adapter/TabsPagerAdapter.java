package com.anhnt.kovidict.myapplication.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anhnt.kovidict.myapplication.fragment.FavouriteFragment;
import com.anhnt.kovidict.myapplication.fragment.MeaningFragment;
import com.anhnt.kovidict.myapplication.fragment.StartFragment;
import com.anhnt.kovidict.myapplication.fragment.VocabolaryFragment;

import java.util.Locale;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    StartFragment sFragment;
    FavouriteFragment fcFragment;
    MeaningFragment mFragment;
    VocabolaryFragment vcFragment;

    public TabsPagerAdapter(FragmentManager fm, int type_dict) {
        super(fm);
        this.sFragment = new StartFragment();
        this.vcFragment = new VocabolaryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type_dict", type_dict);
        this.vcFragment.setArguments(bundle);
        this.mFragment = new MeaningFragment();
        this.fcFragment = new FavouriteFragment();
        bundle = new Bundle();
        bundle.putInt("type_dict", type_dict);
        this.fcFragment.setArguments(bundle);
    }

    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return this.sFragment;
            case 1:
                return this.vcFragment;
            case 2:
                return this.mFragment;
            case 3:
                return this.fcFragment;
            default:
                return null;
        }
    }

    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        Locale locale = Locale.getDefault();
        switch (position) {
            case 0:
                return "Bắt đầu";
            case 1:
                return "Từ vựng";
            case 2:
                return "Nghĩa";
            case 3:
                return "Yêu thích";
            default:
                return null;
        }
    }
}