package com.fenghaha.zscy.bean;

import java.io.Serializable;

/**
 * Created by FengHaHa on2018/5/25 0025 20:06
 */
public class Question implements Serializable{

    private String questioner_gender;

    private int id;

    private int is_anonymous;

    private boolean is_self;

    private String created_at;

    private int ans_num;

    private String title;

    private String description;

    private int reward;

    private String disappear_at;

    private String tags;

    private String kind;

    private String photo_urls[];

    private String questioner_nickname;

    private String questioner_photo_thumbnail_src;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public boolean isIs_self() {
        return is_self;
    }

    public void setIs_self(boolean is_self) {
        this.is_self = is_self;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public int getAns_num() {
        return ans_num;
    }

    public void setAns_num(int ans_num) {
        this.ans_num = ans_num;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getDisappear_at() {
        return disappear_at;
    }

    public void setDisappear_at(String disappear_at) {
        this.disappear_at = disappear_at;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String[] getPhoto_urls() {
        return photo_urls;
    }

    public void setPhoto_urls(String[] photo_urls) {
        this.photo_urls = photo_urls;
    }

    public String getQuestioner_nickname() {
        return questioner_nickname;
    }

    public void setQuestioner_nickname(String questioner_nickname) {
        this.questioner_nickname = questioner_nickname;
    }

    public String getQuestioner_photo_thumbnail_src() {
        return questioner_photo_thumbnail_src;
    }

    public void setQuestioner_photo_thumbnail_src(String questioner_photo_thumbnail_src) {
        this.questioner_photo_thumbnail_src = questioner_photo_thumbnail_src;
    }

    public String getQuestioner_gender() {
        return questioner_gender;
    }

    public void setQuestioner_gender(String questioner_gender) {
        this.questioner_gender = questioner_gender;
    }
}
