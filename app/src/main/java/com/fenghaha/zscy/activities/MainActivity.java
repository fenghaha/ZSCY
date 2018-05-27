package com.fenghaha.zscy.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fenghaha.zscy.MyViewPager;
import com.fenghaha.zscy.R;
import com.fenghaha.zscy.adapter.MyFragmentAdapter;
import com.fenghaha.zscy.fragments.AskCquptFrag;
import com.fenghaha.zscy.fragments.FindFrag;
import com.fenghaha.zscy.fragments.MineFrag;
import com.fenghaha.zscy.fragments.TimetableFrag;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rgTabBar;
    private RadioButton rbTimeTable;
    private RadioButton rbAskCqupt;
    private RadioButton rbFind;
    private RadioButton rbMine;
    private MyViewPager viewPager;

    private MyFragmentAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupFragments();
    }

    private void setupFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TimetableFrag());
        fragments.add(new AskCquptFrag());
        fragments.add(new FindFrag());
        fragments.add(new MineFrag());

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments,null);
        viewPager = findViewById(R.id.viewpager_main);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setCanScroll(false);

    }

    private void setupViews() {
        rgTabBar = findViewById(R.id.rg_tab_bar);
        rbTimeTable = findViewById(R.id.rb_timetable);
        rbAskCqupt = findViewById(R.id.rb_ask_cqupt);
        rbFind = findViewById(R.id.rb_find);
        rbMine = findViewById(R.id.rb_mine);
        rbTimeTable.setChecked(true);
        rgTabBar.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_timetable:
                    viewPager.setCurrentItem(PAGE_ONE);
                    break;
                case R.id.rb_ask_cqupt:
                    viewPager.setCurrentItem(PAGE_TWO);
                    break;
                case R.id.rb_find:
                    viewPager.setCurrentItem(PAGE_THREE);
                    break;
                case R.id.rb_mine:
                    viewPager.setCurrentItem(PAGE_FOUR);
                    break;
            }
        });
    }
}
