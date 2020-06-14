package com.example.yeditepesocapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
    private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist
    private Bundle fragmentBundle;


    public ViewPagerAdapter(FragmentManager manager, Bundle bundle) {
        super(manager);
        fragmentBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        fragment = mFragmentList.get(position);
        fragment.setArguments(this.fragmentBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    //adding fragments and title method
    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

