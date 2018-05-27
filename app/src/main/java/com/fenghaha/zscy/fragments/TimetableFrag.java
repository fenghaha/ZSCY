package com.fenghaha.zscy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.activities.LoginActivity;
import com.fenghaha.zscy.util.MyApplication;

/**
 * Created by FengHaHa on2018/5/25 0025 15:28
 */
public class TimetableFrag extends Fragment {
    private LinearLayout linearLayout;
    private TextView hasLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_fragment, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        TextView login = view.findViewById(R.id.tv_login);
        linearLayout = view.findViewById(R.id.ln_not_login);
        hasLogin = view.findViewById(R.id.tv_had_login);
        login.setOnClickListener(v -> login());
        refresh();
    }

    private void refresh() {
        if (!MyApplication.isIsLogin()) {
            linearLayout.setVisibility(View.VISIBLE);
            hasLogin.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            hasLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void login() {
        LoginActivity.actionStart(getContext());
    }
}
