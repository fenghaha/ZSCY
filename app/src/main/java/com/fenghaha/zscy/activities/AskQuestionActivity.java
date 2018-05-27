package com.fenghaha.zscy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.adapter.TimeRecAdapter;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.bean.User;
import com.fenghaha.zscy.util.ApiParams;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.util.ToastUtil;
import com.fenghaha.zscy.views.MyPopWindow;
import com.fenghaha.zscy.views.SquareImageView;

public class AskQuestionActivity extends AppCompatActivity {
    private User user = MyApplication.getUser();
    private Question mQuestion;
    private TextView mNext;
    private Button mBack;
    private EditText mTitle;
    private EditText mContent;
    private TextView mTitleNum;
    private TextView mContentNum;
    private SquareImageView[] squareImageViews = new SquareImageView[6];
    private Button mTopic;
    private Button mChooseImage;
    private CheckBox mCheckbox;
    private View mTopicContentView;
    private View mImageContentView;
    private View mQueSettingContentView;
    private View mTimeContentView;
    private View mRewardContentView;

    private MyPopWindow mImageWindow;
    private MyPopWindow mTopicWindow;
    private MyPopWindow mSettingWindow;
    private MyPopWindow mTimeWindow;
    private MyPopWindow mRewardWindow;

    private static final String TAG = "AskQuestionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        mQuestion = (Question) getIntent().getSerializableExtra("question_data");
        mQuestion.setDisappear_at("2018-05-28 01:11:20");
        setupViews();
    }

    private void setupViews() {
        mNext = findViewById(R.id.tv_next);
        mBack = findViewById(R.id.bt_ask_que_back);
        mTitle = findViewById(R.id.et_question_title);
        mContent = findViewById(R.id.et_question_content);
        mTitleNum = findViewById(R.id.tv_title_rest_num);
        mContentNum = findViewById(R.id.tv_content_rest_num);
        mTopic = findViewById(R.id.bt_topic_choose);
        mChooseImage = findViewById(R.id.bt_image_choose);
        mCheckbox = findViewById(R.id.check_is_anonymous);

        mBack.setOnClickListener(v -> finish());
        mNext.setOnClickListener(v -> prePublish());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitleNum.setText(String.valueOf(20 - count));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentNum.setText(String.valueOf(200 - count));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setupChooseImageView();
        setupQueSettingView();
        setupRewardView();
        setupTimeView();
        setupTopicView();

        mNext.setOnClickListener(v -> {
            popSettingWindow();
            hideKeyboard();
        })
        ;
        mTopic.setOnClickListener(v -> {
            popTopicWindow();
            hideKeyboard();
        });

        mChooseImage.setOnClickListener(v -> {
            popImageWindow();
            hideKeyboard();
        });
    }

    private void setupTopicView() {
        mTopicContentView = LayoutInflater.from(this).inflate(R.layout.que_topic_choose, null, false);
        TextView sure = mTopicContentView.findViewById(R.id.tv_sure);
        EditText mInputTopic = mTopicContentView.findViewById(R.id.et_topic);
        RadioGroup mRadioGroup1 = mTopicContentView.findViewById(R.id.rg_topics1);
        RadioGroup mRadioGroup2 = mTopicContentView.findViewById(R.id.rg_topics2);
        RadioButton mRab1 = mTopicContentView.findViewById(R.id.rb_topic01);
        RadioButton mRab2 = mTopicContentView.findViewById(R.id.rb_topic02);
        RadioButton mRab3 = mTopicContentView.findViewById(R.id.rb_topic03);
        RadioButton mRab4 = mTopicContentView.findViewById(R.id.rb_topic04);
        RadioButton mRab5 = mTopicContentView.findViewById(R.id.rb_topic05);
        RadioButton mRab6 = mTopicContentView.findViewById(R.id.rb_topic06);
        RadioButton mRab7 = mTopicContentView.findViewById(R.id.rb_topic07);
        RadioButton mRab8 = mTopicContentView.findViewById(R.id.rb_topic08);
        mRadioGroup1.clearCheck();
        mRadioGroup2.clearCheck();
        mRadioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            mInputTopic.setCompoundDrawables(null, null, null, null);
            if (mRadioGroup2.getCheckedRadioButtonId() != -1) mRadioGroup2.clearCheck();
            switch (checkedId) {
                case R.id.rb_topic01:
                    mInputTopic.setText(mRab1.getText().toString());
                    break;
                case R.id.rb_topic02:
                    mInputTopic.setText(mRab2.getText().toString());
                    break;
                case R.id.rb_topic03:
                    mInputTopic.setText(mRab3.getText().toString());
                    break;
                case R.id.rb_topic04:
                    mInputTopic.setText(mRab4.getText().toString());
                    break;
                case R.id.rb_topic05:
                    mInputTopic.setText(mRab5.getText().toString());
                    break;
                case R.id.rb_topic06:
                    mInputTopic.setText(mRab6.getText().toString());
                    break;
                case R.id.rb_topic07:
                    mInputTopic.setText(mRab7.getText().toString());
                    break;
                case R.id.rb_topic08:
                    mInputTopic.setText(mRab8.getText().toString());
                    break;
            }
        });
        mRadioGroup2.setOnCheckedChangeListener(((group, checkedId) -> {
            mInputTopic.setCompoundDrawables(null, null, null, null);
            if (mRadioGroup1.getCheckedRadioButtonId() != -1) mRadioGroup1.clearCheck();

            switch (checkedId) {
                case R.id.rb_topic01:
                    mInputTopic.setText(mRab1.getText().toString());
                    break;
                case R.id.rb_topic02:
                    mInputTopic.setText(mRab2.getText().toString());
                    break;
                case R.id.rb_topic03:
                    mInputTopic.setText(mRab3.getText().toString());
                    break;
                case R.id.rb_topic04:
                    mInputTopic.setText(mRab4.getText().toString());
                    break;
                case R.id.rb_topic05:
                    mInputTopic.setText(mRab5.getText().toString());
                    break;
                case R.id.rb_topic06:
                    mInputTopic.setText(mRab6.getText().toString());
                    break;
                case R.id.rb_topic07:
                    mInputTopic.setText(mRab7.getText().toString());
                    break;
                case R.id.rb_topic08:
                    mInputTopic.setText(mRab8.getText().toString());
                    break;
            }
        }));

        sure.setOnClickListener(v -> {
            mQuestion.setTags(mInputTopic.getText().toString());
            mTopicWindow.dismiss();
            SpannableString tag = new SpannableString("#" + mQuestion.getTags() + "# ");
            tag.setSpan(new ForegroundColorSpan(Color.parseColor("#7195FA")), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tag.setSpan(new RelativeSizeSpan(1.2f), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            String t = mTitle.getText().toString();
            mTitle.setText(tag);
            mTitle.append(t);
        });

    }

    private void setupChooseImageView() {
        mImageContentView = LayoutInflater.from(this).inflate(R.layout.choose_image, null, false);
        TextView mOpenAlbum = mImageContentView.findViewById(R.id.tv_from_album);
        TextView mTakePhoto = mImageContentView.findViewById(R.id.tv_take_photo);
        mOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupQueSettingView() {
        mQueSettingContentView = LayoutInflater.from(this).inflate(R.layout.que_setting, null, false);
        TextView back = mQueSettingContentView.findViewById(R.id.tv_back);
        TextView send = mQueSettingContentView.findViewById(R.id.tv_send);
        TextView lastTime = mQueSettingContentView.findViewById(R.id.tv_disappear_time);
        TextView reward = mQueSettingContentView.findViewById(R.id.tv_reward);

        back.setOnClickListener(v -> mSettingWindow.dismiss());
        send.setOnClickListener(v -> send());
        lastTime.setOnClickListener(v -> {
            popTimeWindow();
            hideKeyboard();
        });
        reward.setOnClickListener(v -> {
            popRewardWindow();
            hideKeyboard();
        });
    }

    private void setupTimeView() {
        mTimeContentView = LayoutInflater.from(this).inflate(R.layout.que_time_choose, null, false);
        TextView cancel = mTimeContentView.findViewById(R.id.tv_cancel);
        TextView finish = mTimeContentView.findViewById(R.id.tv_finish);
        cancel.setOnClickListener(v -> mTimeWindow.dismiss());
        finish.setOnClickListener(v -> mTimeWindow.dismiss());
        RecyclerView recDay = mTimeContentView.findViewById(R.id.rv_day);
        RecyclerView recHour = mTimeContentView.findViewById(R.id.rv_hour);
        RecyclerView recMin = mTimeContentView.findViewById(R.id.rv_min);
        String days[] = new String[2];
        days[0] = "今天";
        days[1] = "2018年5月27日";
        String hours[] = new String[24];
        String mins[] = new String[60];
        for (int i = 0; i < 24; i++) {
            hours[i] = i + "时";
        }
        for (int i = 0; i < 60; i++) {
            mins[i] = i + "分";
        }
        TimeRecAdapter dayAdapter = new TimeRecAdapter(days);
        TimeRecAdapter hourAdapter = new TimeRecAdapter(hours);
        TimeRecAdapter minAdapter = new TimeRecAdapter(mins);
        LinearLayoutManager manager01 = new LinearLayoutManager(this);
        LinearLayoutManager manager02 = new LinearLayoutManager(this);
        LinearLayoutManager manager03 = new LinearLayoutManager(this);
        recDay.setLayoutManager(manager01);
        recDay.setAdapter(dayAdapter);
        recHour.setLayoutManager(manager02);
        recHour.setAdapter(hourAdapter);
        recMin.setLayoutManager(manager03);
        recMin.setAdapter(minAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setupRewardView() {
        mRewardContentView = LayoutInflater.from(this).inflate(R.layout.que_reward_set, null, false);
        TextView cancel = mRewardContentView.findViewById(R.id.tv_cancel);
        TextView finish = mRewardContentView.findViewById(R.id.tv_finish);
        TextView myScore = mRewardContentView.findViewById(R.id.tv_score_rest);

        TextView reward = mRewardContentView.findViewById(R.id.tv_reward_num);

        RadioGroup radioGroup = mRewardContentView.findViewById(R.id.rg_rewards);
        radioGroup.check(R.id.rb_reward_01);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_reward_01:
                    reward.setText("1积分");
                    break;
                case R.id.rb_reward_02:
                    reward.setText("2积分");
                    break;
                case R.id.rb_reward_03:
                    reward.setText("3积分");
                    break;
                case R.id.rb_reward_05:
                    reward.setText("5积分");
                    break;
                case R.id.rb_reward_10:
                    reward.setText("10积分");
                    break;
                case R.id.rb_reward_15:
                    reward.setText("15积分");
                    break;
            }
        });
        cancel.setOnClickListener(v -> mRewardWindow.dismiss());
        finish.setOnClickListener(v -> {
            if (MyApplication.getUser().getScore() < Integer.parseInt(reward.getText().toString().split("积分")[0])) {
                ToastUtil.makeToast("积分不足");
            } else {
                mQuestion.setReward(Integer.parseInt(reward.getText().toString().split("积分")[0]));
                mRewardWindow.dismiss();
            }
        });
    }

    private void popImageWindow() {
        if (mImageWindow == null) {
            mImageWindow = new MyPopWindow.PopupWindowBuilder(this)
                    .setView(mImageContentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .setOnDismissListener(() -> {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }).setAnimationStyle(R.style.animTranslate)
                    .create();
        }
        mImageWindow.showAtLocation(mTopic, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void popTopicWindow() {
        if (mTopicWindow == null) {
            mTopicWindow = new MyPopWindow.PopupWindowBuilder(this)
                    .setView(mTopicContentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .setOnDismissListener(() -> {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }).setAnimationStyle(R.style.animTranslate)
                    .create();
        }
        mTopicWindow.showAtLocation(mTopic, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void popSettingWindow() {
        if (mSettingWindow == null) {
            mSettingWindow = new MyPopWindow.PopupWindowBuilder(this)
                    .setView(mQueSettingContentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .setOnDismissListener(() -> {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }).setAnimationStyle(R.style.animTranslate)
                    .create();
        }
        mSettingWindow.showAtLocation(mTopic, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void popRewardWindow() {
        if (mRewardWindow == null) {
            mRewardWindow = new MyPopWindow.PopupWindowBuilder(this)
                    .setView(mRewardContentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .setOnDismissListener(() -> {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }).setAnimationStyle(R.style.animTranslate)
                    .create();
        }
        mRewardWindow.showAtLocation(mTopic, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void popTimeWindow() {
        if (mTimeWindow == null) {
            mTimeWindow = new MyPopWindow.PopupWindowBuilder(this)
                    .setView(mTimeContentView)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .size(0, dp2px(210))
                    .setOnDismissListener(() -> {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }).setAnimationStyle(R.style.animTranslate)
                    .create();
        }
        mTimeWindow.showAtLocation(mTopic, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void send() {
        int is_anonymous = mCheckbox.isChecked() ? 1 : 0;
        String title = mTitle.getText().toString();
        String content = mContent.getText().toString();
        if (MyTextUtil.isEmpty(title)) ToastUtil.makeToast("标题不能为空!");
        if (MyTextUtil.isEmpty(content)) ToastUtil.makeToast("问题描述不能为空!");
        if (!MyTextUtil.isEmpty(title) && !MyTextUtil.isEmpty(content)) {
            String url = ApiParams.ASK_A_QUESTION;
            String param = "stuNum=" + user.getStuNum() + "&idNum=" + user.getIdCardNum() +
                    "&title=" + title + "&description=" + content + "&is_anonymous=" +
                    is_anonymous + "&kind=" + mQuestion.getKind() + "&tags=" + mQuestion.getTags()
                    + "&reward=" + mQuestion.getReward() + "&disappear_time=" + mQuestion.getDisappear_at();
            Log.d(TAG, "param=   "+param);
            HttpUtil.post(url, param, new HttpUtil.HttpCallBack() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    Log.d(TAG, "onResponse: "+response.getData());
                    if (response.isOk()) {
                        finish();
                    }
                }

                @Override
                public void onFail(String reason) {
                    ToastUtil.makeToast("发送失败!");
                }
            });
        }

    }


    private void chooseImage() {

    }


    private void prePublish() {

    }

    public static void actionStart(Context context, Question question) {
        Intent intent = new Intent(context, AskQuestionActivity.class);
        intent.putExtra("question_data", question);
        context.startActivity(intent);
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(this.getWindow().
                    getDecorView().getWindowToken(), 0);
        }

    }

}
