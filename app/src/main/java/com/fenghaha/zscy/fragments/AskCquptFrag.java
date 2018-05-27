package com.fenghaha.zscy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenghaha.zscy.MyViewPager;
import com.fenghaha.zscy.R;
import com.fenghaha.zscy.adapter.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/25 0025 15:32
 */
public class AskCquptFrag extends Fragment {
    MyFragmentAdapter mAdapter;

    private List<Fragment> mFragments = new ArrayList<>();
    private MyViewPager viewPager;
    private TabLayout mTab;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.ask_cqupt_fragment, container, false);
        setUpViews();
        setupFragments();
        return mMainView;
    }

    private void setupFragments() {
        List<Fragment> fragments = new ArrayList<>();
        String tittles[] = {"全部", "学习", "生活", "情感", "其他"};
        for (int i = 0; i < 5; i++) {
            fragments.add(new QuestionListFrag().setKind(tittles[i]));
        }
        mTab = mMainView.findViewById(R.id.tab_layout_ask_cqupt);
        mAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, tittles);
        viewPager = mMainView.findViewById(R.id.viewpager_ask_cqupt);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        mTab.setupWithViewPager(viewPager);
    }

    private void setUpViews() {

    }
}
