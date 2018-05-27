package com.fenghaha.zscy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.bean.Comment;
import com.fenghaha.zscy.util.ApiParams;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.ImageLoader.ImageLoader;
import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.DoubleCache;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/27 0027 18:58
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    public List<Comment> mCommentList = new ArrayList<>();
    private ImageLoader imageLoader;

    public void getCommentList(int answerId, HttpUtil.HttpCallBack callBack) {
        String param = "stuNum=" + MyApplication.getUser().getStuNum()
                + "&idNum=" + MyApplication.getUser().getIdCardNum() + "&answer_id=" + answerId;
    HttpUtil.post(ApiParams.GET_REMARK_LIST,param,callBack);
    }

    public CommentListAdapter(Context context) {
        imageLoader = new ImageLoader();
        imageLoader.setImageCache(new DoubleCache(context));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCommentList.size() > 0) {
            Comment comment = mCommentList.get(position);
            if (!MyTextUtil.isEmpty(comment.getPhoto_thumbnail_src()))
                imageLoader.displayImage(comment.getPhoto_thumbnail_src(), holder.avatar);
            else holder.avatar.setImageResource(R.drawable.default_avatar);
            holder.name.setText(comment.getNickname());
            holder.time.setText(comment.getCreated_at());
            holder.content.setText(comment.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView avatar;
        TextView name;
        TextView content;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.image_comment_avatar);
            name = itemView.findViewById(R.id.tv_comment_author_name);
            content = itemView.findViewById(R.id.tv_comment_content);
            time = itemView.findViewById(R.id.comment_time);
        }
    }
}
