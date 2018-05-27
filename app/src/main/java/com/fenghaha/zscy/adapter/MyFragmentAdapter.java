package com.fenghaha.zscy.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/25 0025 15:35
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String mTitle[];

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String mTitle[]) {
        super(fm);
        this.fragments = fragments;
        this.mTitle = mTitle;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitle != null)
            return mTitle[position];
        else return null;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
