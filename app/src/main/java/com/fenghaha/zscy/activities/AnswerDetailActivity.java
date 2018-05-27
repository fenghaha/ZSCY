package com.fenghaha.zscy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.adapter.CommentListAdapter;
import com.fenghaha.zscy.bean.Answer;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.bean.User;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.ImageLoader.ImageLoader;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.DoubleCache;
import com.fenghaha.zscy.util.JsonParser;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.views.RoundImageView;

public class AnswerDetailActivity extends AppCompatActivity {
    private Answer mAnswer;
    private Question mQuestion;
    private ImageLoader mLoader;
    private User mUser = MyApplication.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);
        mAnswer = (Answer) getIntent().getSerializableExtra("answer_data");
        mQuestion = (Question) getIntent().getSerializableExtra("question_data");
        mLoader = new ImageLoader();
        mLoader.setImageCache(new DoubleCache(this));
        setupViews();
    }

    private void setupViews() {
        Button mBack = findViewById(R.id.bt_back);
        Button mMenu = findViewById(R.id.bt_menu);
        TextView mTitle = findViewById(R.id.tv_title);
        RoundImageView mAvatar = findViewById(R.id.image_answer_avatar);
        Button mAdopt = findViewById(R.id.bt_adopt);
        LinearLayout mAdopted = findViewById(R.id.ln_adopted);
        TextView mContent = findViewById(R.id.tv_content);
        TextView mTime = findViewById(R.id.tv_answer_time);
        TextView mCommentNum = findViewById(R.id.tv_comment_num);
        TextView mComment = findViewById(R.id.tv_comment);
        LinearLayout mCommentClick = findViewById(R.id.ln_comment);
        ImageView mPraise = findViewById(R.id.iv_praise);
        ImageView mGender = findViewById(R.id.iv_gender);
        mBack.setOnClickListener(v -> finish());
        mMenu.setOnClickListener(v -> popupMenuWindow());
        mTitle.setText(mQuestion.getTitle());
//        if (!MyTextUtil.isNull(mAnswer.getPhoto_thumbnail_src())) {
//            mLoader.displayImage(mAnswer.getPhoto_thumbnail_src(), mAvatar);
//        }
        //else mAvatar.setImageResource(R.drawable.default_avatar);
        mAdopt.setOnClickListener(v -> adoptAnswer());
        if (mQuestion.getQuestioner_nickname().equals(mUser.getNickName())) {
            if (mAnswer.getIs_adopted() == 0) {
                mAdopt.setVisibility(View.VISIBLE);
                mAdopted.setVisibility(View.GONE);
            } else {
                mAdopt.setVisibility(View.GONE);
                mAdopted.setVisibility(View.VISIBLE);
            }
        } else {
            mAdopt.setVisibility(View.GONE);
            mAdopted.setVisibility(View.GONE);
        }
        mContent.setText(mAnswer.getContent());
        mTime.setText(mAnswer.getCreated_at());
        mCommentNum.setText(mAnswer.getComment_num() + "评论");
        mComment.setText("评论" + "(" + mAnswer.getComment_num() + ")");
        mCommentClick.setOnClickListener(v -> popupCommentWindow());
        if (mQuestion.getKind().equals("情感")) mGender.setVisibility(View.VISIBLE);
        else mGender.setVisibility(View.GONE);
        if (mAnswer.getIs_praised() == 0) mPraise.setImageResource(R.drawable.ic_praise);
        else  mPraise.setImageResource(R.drawable.ic_praise_pressed);
        mPraise.setOnClickListener(v -> {
            if (mAnswer.getIs_praised() == 0)praiseAnswer();
            else  cancelPraiseAnswer();
        });


        RecyclerView mCommentsRec = findViewById(R.id.rec_comment_list);
        CommentListAdapter adapter = new CommentListAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mCommentsRec.setLayoutManager(manager);
        mCommentsRec.setAdapter(adapter);
        adapter.getCommentList(mAnswer.getId(), new HttpUtil.HttpCallBack() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                if (response.isOk()){
                    adapter.mCommentList.clear();
                    adapter.mCommentList.addAll(JsonParser.getCommentList(new String(response.getBytes())));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String reason) {

            }
        });
    }

    private void cancelPraiseAnswer() {
    }

    private void praiseAnswer() {
    }

    private void popupCommentWindow() {
    }

    private void adoptAnswer() {
    }

    public static void actionStart(Context context, Question question,Answer answer) {
        Intent intent = new Intent(context, AnswerDetailActivity.class);
        intent.putExtra("question_data", question);
        intent.putExtra("answer_data",answer);
        context.startActivity(intent);
    }
    private void popupMenuWindow() {
    }
}
