package com.fenghaha.zscy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.activities.QueDetailActivity;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.bean.User;
import com.fenghaha.zscy.interfaces.MyClickCallBack;
import com.fenghaha.zscy.util.ApiParams;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.ImageLoader.ImageLoader;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.DoubleCache;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.MemoryCache;
import com.fenghaha.zscy.util.JsonParser;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.util.ToastUtil;
import com.fenghaha.zscy.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/25 0025 21:59
 */
public class QuestionListRecAdapter extends RecyclerView.Adapter<QuestionListRecAdapter.ViewHolder> {

    public List<Question> mQuestionList = new ArrayList<>();

    private ImageLoader imageLoader;

    public QuestionListRecAdapter(Context context) {
        imageLoader = new ImageLoader();
        imageLoader.setImageCache(new DoubleCache(context));
    }

    public void getQuestions(int page, int size, String kind, HttpUtil.HttpCallBack callBack) {
        String url = ApiParams.GET_QUESTION_LIST;
        String param = "page=" + page + "&size=" + size + "&kind=" + kind;
        HttpUtil.post(url, param, callBack);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.question_item, parent, false));
        holder.all.setOnClickListener(v -> {
            Question question = mQuestionList.get(holder.getAdapterPosition());
            QueDetailActivity.actionStart(holder.all.getContext(), question);
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mQuestionList.size() > 0) {
            Question question = mQuestionList.get(position);
            SpannableString tag = new SpannableString("#" + question.getTags() + "# ");
            tag.setSpan(new ForegroundColorSpan(Color.parseColor("#7195FA")), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tag.setSpan(new RelativeSizeSpan(0.9f), 0, tag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.title.setText(tag);
            holder.title.append(question.getTitle());
            holder.authorName.setText(question.getQuestioner_nickname());
            holder.reward.setText(String.valueOf(question.getReward()) + "积分");
            holder.lastTime.setText(question.getDisappear_at());
            holder.content.setText(question.getDescription());

            if (!MyTextUtil.isNull(question.getQuestioner_photo_thumbnail_src())) {
                imageLoader.displayImage(question.getQuestioner_photo_thumbnail_src(), holder.avatar);
//                HttpUtil.loadImage(question.getQuestioner_photo_thumbnail_src(),
//                        (bitmap, info) -> holder.avatar.setImageBitmap(bitmap));
            }else holder.avatar.setImageResource(R.drawable.default_avatar);

        }
    }


    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView avatar;
        TextView title;
        TextView authorName;
        TextView content;
        TextView lastTime;
        TextView reward;
        CardView all;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.image_question_avatar);
            title = itemView.findViewById(R.id.question_title);
            authorName = itemView.findViewById(R.id.tv_question_author_name);
            content = itemView.findViewById(R.id.question_content);
            lastTime = itemView.findViewById(R.id.tv_last_time);
            reward = itemView.findViewById(R.id.question_reward);
            all = itemView.findViewById(R.id.card_question_all);
        }
    }
}
