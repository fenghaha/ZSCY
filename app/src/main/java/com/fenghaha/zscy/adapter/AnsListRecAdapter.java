package com.fenghaha.zscy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.bean.Answer;
import com.fenghaha.zscy.bean.User;
import com.fenghaha.zscy.interfaces.MyClickCallBack;
import com.fenghaha.zscy.util.ApiParams;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.ImageLoader.ImageLoader;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.DoubleCache;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.util.ToastUtil;
import com.fenghaha.zscy.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/27 0027 14:46
 */
public class AnsListRecAdapter extends RecyclerView.Adapter<AnsListRecAdapter.ViewHolder> {
    private User mUser = MyApplication.getUser();
    private String kind;
    private int questionId;
    public List<Answer> mAnswerList = new ArrayList<>();
    private ImageLoader imageLoader;
    private MyClickCallBack mCLickListener;

    private static final String TAG = "AnsListRecAdapter";

    public AnsListRecAdapter(Context context, int questionId, String kind) {
        this.kind = kind;
        this.questionId = questionId;
        imageLoader = new ImageLoader();
        imageLoader.setImageCache(new DoubleCache(context));
    }

    public void getAnswers(int page, int size, HttpUtil.HttpCallBack callBack) {
        String url = ApiParams.GET_QUESTION_DETAIL;
        String param = "stuNum=" + mUser.getStuNum() + "&idNum=" + mUser.getIdCardNum() + "&question_id=" + questionId;
        HttpUtil.post(url, param, callBack);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.answer_item, parent, false));
        holder.praise.setOnClickListener(v -> {
            Answer answer = mAnswerList.get(holder.getAdapterPosition());
            if (answer.getIs_praised() == 0) {
                praiseAnswer(holder, answer);
            } else {
                cancelPraiseAnswer(holder, answer);
            }
        });
        return holder;
    }

    private void cancelPraiseAnswer(ViewHolder holder, Answer answer) {

        HttpUtil.post(ApiParams.CANCEL_ANSWER_PRAISE, "stuNum=" + mUser.getStuNum() + "&idNum=" +
                mUser.getIdCardNum() + "&answer_id=" + answer.getId(), new HttpUtil.HttpCallBack() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                holder.praise.setImageResource(R.drawable.ic_praise);
                holder.praiseNum.setText(String.valueOf(answer.getPraise_num() - 1));
                if (response.isOk()) {
                    answer.setPraise_num(answer.getPraise_num() - 1);
                    answer.setIs_praised(0);
                } else {
                    ToastUtil.makeToast(response.getInfo());
                }
            }

            @Override
            public void onFail(String reason) {
                ToastUtil.makeToast("网络错误,点赞失败!");
            }
        });
    }

    private void praiseAnswer(ViewHolder holder, Answer answer) {
        String param = "stuNum=" + mUser.getStuNum() + "&idNum=" +
                mUser.getIdCardNum() + "&answer_id=" + answer.getId();
        Log.d(TAG, "praiseAnswer:   " + param);
        HttpUtil.post(ApiParams.ANSWER_PRAISE, param, new HttpUtil.HttpCallBack() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                holder.praise.setImageResource(R.drawable.ic_praise_pressed);
                holder.praiseNum.setText(String .valueOf(answer.getPraise_num() + 1));

                if (response.isOk()) {
                    answer.setIs_praised(1);
                    answer.setPraise_num(answer.getPraise_num() + 1);
                } else {
                    Log.d(TAG, "onResponse: " + new String(response.getBytes()));
                    ToastUtil.makeToast(response.getInfo());
                }
            }

            @Override
            public void onFail(String reason) {
                ToastUtil.makeToast("网络错误,取消点赞失败!");
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mAnswerList.size() > 0) {
            Answer answer = mAnswerList.get(position);
            if (!MyTextUtil.isEmpty(answer.getPhoto_thumbnail_src())) {
                imageLoader.displayImage(answer.getPhoto_thumbnail_src(), holder.avatar);
            } else holder.avatar.setImageResource(R.drawable.default_avatar);
            if (kind.equals("情感")) holder.gender.setVisibility(View.VISIBLE);
            else holder.gender.setVisibility(View.GONE);
            String nickname = MyApplication.getUser().getNickName();
            if (!MyTextUtil.isNull(nickname)) {
                if (nickname.equals(answer.getNickname())) {
                    if (answer.getIs_adopted() == 0) {
                        holder.adopt.setVisibility(View.VISIBLE);
                        holder.adopted.setVisibility(View.GONE);
                    } else {
                        holder.adopt.setVisibility(View.GONE);
                        holder.adopted.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.adopt.setVisibility(View.GONE);
                    holder.adopted.setVisibility(View.GONE);
                }
            }

            Log.d(TAG, "position = " + position + "isp = " + answer.getIs_praised());
            if (answer.getIs_praised() == 0) {
                holder.praise.setImageResource(R.drawable.ic_praise);
            } else {
                holder.praise.setImageResource(R.drawable.ic_praise_pressed);
            }
            holder.name.setText(answer.getNickname());
            holder.content.setText(answer.getContent());
            holder.time.setText(answer.getCreated_at());
            holder.commentNum.setText(String.valueOf(answer.getComment_num()));
            holder.praiseNum.setText(String.valueOf(answer.getPraise_num()));
        }

    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView avatar;
        TextView name;
        Button adopt;
        LinearLayout adopted;
        TextView content;
        TextView time;
        TextView commentNum;
        TextView praiseNum;
        LinearLayout comment;
        ImageView praise;
        ImageView gender;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_answer_author_name);
            avatar = itemView.findViewById(R.id.image_answer_avatar);
            adopt = itemView.findViewById(R.id.bt_adopt);
            adopted = itemView.findViewById(R.id.ln_adopted);
            content = itemView.findViewById(R.id.tv_answer_content);
            time = itemView.findViewById(R.id.tv_answer_create_time);
            commentNum = itemView.findViewById(R.id.tv_comment_num);
            praiseNum = itemView.findViewById(R.id.tv_praise_num);
            comment = itemView.findViewById(R.id.ln_comment);
            praise = itemView.findViewById(R.id.iv_praise);
            gender = itemView.findViewById(R.id.iv_gender);
        }
    }

}
