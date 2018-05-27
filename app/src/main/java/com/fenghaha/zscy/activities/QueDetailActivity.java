package com.fenghaha.zscy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.adapter.AnsListRecAdapter;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.ImageLoader.ImageLoader;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.DoubleCache;
import com.fenghaha.zscy.util.JsonParser;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.util.ToastUtil;
import com.fenghaha.zscy.views.RoundImageView;

import org.w3c.dom.Text;

public class QueDetailActivity extends AppCompatActivity {
    private Question question;
    private ImageLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que_detail);
        mLoader = new ImageLoader();
        mLoader.setImageCache(new DoubleCache(this));
        question = (Question) getIntent().getSerializableExtra("question_data");
        setupViews();
    }

    private void setupViews() {
        Button mBack = findViewById(R.id.bt_back);
        Button mMenu = findViewById(R.id.bt_menu);
        TextView mTitle = findViewById(R.id.tv_title);
        TextView mContent = findViewById(R.id.tv_content);
        RoundImageView mAvatar = findViewById(R.id.image_question_avatar);
        TextView mDissTime = findViewById(R.id.tv_last_time);
        TextView mQuestionerName = findViewById(R.id.tv_question_author_name);
        TextView mScore = findViewById(R.id.tv_question_reward);
        TextView mAnswerNum = findViewById(R.id.tv_answer_num);
        TextView mSort = findViewById(R.id.tv_sort);
        mBack.setOnClickListener(v -> finish());
        mMenu.setOnClickListener(v -> popupMenuWindow());
        mTitle.setText(question.getTitle());
        SpannableString tag = new SpannableString("#" + question.getTags() + "# ");
        tag.setSpan(new ForegroundColorSpan(Color.parseColor("#7195FA")), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tag.setSpan(new RelativeSizeSpan(1.1f), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mContent.setText(tag);
        mContent.append(question.getDescription());
        if (!MyTextUtil.isEmpty(question.getQuestioner_photo_thumbnail_src())) {
            mLoader.displayImage(question.getQuestioner_photo_thumbnail_src(), mAvatar);
        }
        mDissTime.setText(question.getDisappear_at());
        mQuestionerName.setText(question.getQuestioner_nickname());
        mScore.setText(String.valueOf(question.getReward()) + "积分");
        mAnswerNum.setText(String.valueOf(question.getAns_num())+"个回答");
        mSort.setOnClickListener(v -> popupSortWindow());
        RecyclerView recyclerView = findViewById(R.id.rec_answer_list);
        AnsListRecAdapter ansListRecAdapter = new AnsListRecAdapter(this, question.getId(), question.getKind());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setAdapter(ansListRecAdapter);
        recyclerView.setLayoutManager(manager);
        ansListRecAdapter.getAnswers(0, 6, new HttpUtil.HttpCallBack() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                ansListRecAdapter.mAnswerList.clear();
                ansListRecAdapter.mAnswerList.addAll(JsonParser.getAnswerList(response.getData()));
                ansListRecAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String reason) {
                ToastUtil.makeToast("网络连接错误!");
            }
        });
    }

    private void popupSortWindow() {
    }

    private void popupMenuWindow() {
    }

    public static void actionStart(Context context, Question question) {
        Intent intent = new Intent(context, QueDetailActivity.class);
        intent.putExtra("question_data", question);
        context.startActivity(intent);
    }
}
