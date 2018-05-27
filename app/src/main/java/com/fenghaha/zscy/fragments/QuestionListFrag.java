package com.fenghaha.zscy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.activities.AskQuestionActivity;
import com.fenghaha.zscy.adapter.QuestionListRecAdapter;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.JsonParser;
import com.fenghaha.zscy.util.ToastUtil;
import com.fenghaha.zscy.views.MyPopWindow;

/**
 * Created by FengHaHa on2018/5/25 0025 17:13
 */
public class QuestionListFrag extends Fragment {

    public static final String ALL = "全部";
    public static final String STUDY = "学习";
    public static final String LIFE = "生活";
    public static final String EMOTION = "情感";
    public static final String OTHER = "其他";
    private QuestionListRecAdapter recAdapter;
    private RecyclerView recyclerView;
    private View mMainView;
    private String kind;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private View contentView;
    private MyPopWindow popupWindow;
    private RadioGroup radioGroup;

    public QuestionListFrag setKind(String kind) {
        this.kind = kind;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.question_list_fragment, container, false);
        setupViews();
        return mMainView;
    }

    private void refreshQuestion() {
        refreshLayout.setRefreshing(true);
        recAdapter.getQuestions(0, 6, kind, new HttpUtil.HttpCallBack() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                refreshLayout.setRefreshing(false);
                recAdapter.mQuestionList.clear();
                recAdapter.mQuestionList.addAll(JsonParser.getSimpleQuestionList(new String(response.getBytes())));
                recAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String reason) {
                refreshLayout.setRefreshing(false);
                ToastUtil.makeToast("网络连接中断");
            }
        });

    }

    private void setupViews() {
        recyclerView = mMainView.findViewById(R.id.question_list_rec);
        refreshLayout = mMainView.findViewById(R.id.swipe_refresh_layout);
        fab = mMainView.findViewById(R.id.fab_ask_que);
        fab.setOnClickListener(v -> chooseKind());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recAdapter = new QuestionListRecAdapter(getContext());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recAdapter);
        refreshLayout.setOnRefreshListener(this::refreshQuestion);
        contentView = LayoutInflater.from(getContext()).
                inflate(R.layout.que_kind_choose, null, false);
        TextView next = contentView.findViewById(R.id.tv_next);
        radioGroup = contentView.findViewById(R.id.rg_choose_kind);
        Button exit = contentView.findViewById(R.id.bt_exit_choose_kind);
        next.setOnClickListener(v -> next());
        exit.setOnClickListener(v -> popupWindow.dismiss());
    }

    private void chooseKind() {
        popupWindow = new MyPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getActivity().getWindow().setAttributes(lp);
                })
                .setAnimationStyle(R.style.animTranslate)
                .create();
        radioGroup.clearCheck();
        popupWindow.showAtLocation(fab, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);

    }

    private void next() {
        Question question = new Question();
        String kind = null;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_study:
                kind = "学习";
                break;
            case R.id.rb_life:
                kind = "生活";
                break;
            case R.id.rb_emotion:
                kind = "情感";
                break;
            case R.id.rb_other:
                kind = "其他";
                break;
            default:
                ToastUtil.makeToast("请选择一个类型!");
        }
        if (kind != null) {
            question.setKind(kind);
            AskQuestionActivity.actionStart(getContext(), question);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshQuestion();
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}
