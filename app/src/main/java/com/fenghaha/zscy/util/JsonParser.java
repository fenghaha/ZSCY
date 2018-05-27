package com.fenghaha.zscy.util;

import android.util.Log;

import com.fenghaha.zscy.bean.Answer;
import com.fenghaha.zscy.bean.Question;
import com.fenghaha.zscy.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FengHaHa on2018/5/25 0025 12:13
 */
public class JsonParser {
    private static final String TAG = "JsonParser";
    public static boolean has(String data, String name) {
        try {
            JSONObject js = new JSONObject(data);
            //检查存在才返回
            return js.has(name);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return false;
    }
    public static User getUser(String data){
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(data);
            user.setStuNum(jsonObject.getInt("stuNum"));
            user.setUserName(jsonObject.getString("name"));
            user.setCollege(jsonObject.getString("college"));
            user.setKlass(jsonObject.getString("class"));
            user.setClassNum(jsonObject.getInt("classNum"));
            user.setGender(jsonObject.getString("gender"));
            user.setMajor(jsonObject.getString("major"));
            user.setGrade(jsonObject.getInt("grade"));
            user.setIdCardNum(jsonObject.getString("idNum"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<Answer> getAnswerList(String data) {
        List<Answer> mAnswerList = new ArrayList<>();
        Log.d(TAG, "data =  "+data);
        try {
            JSONArray answerArray = null;
            Object json = new JSONTokener(data).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) json;
                answerArray = jsonObject.getJSONArray("answers");
            } else if (json instanceof JSONArray) {
                answerArray = (JSONArray) json;
            }
            for (int i = 0; i < answerArray.length(); i++) {
                JSONObject one = answerArray.getJSONObject(i);
                Answer answer = new Answer();
                answer.setId(one.getInt("id"));
                answer.setNickname(one.getString("nickname"));
                answer.setPhoto_thumbnail_src(one.getString("photo_thumbnail_src"));
                answer.setGender(one.getString("gender"));
                answer.setContent(one.getString("content"));
                answer.setCreated_at(one.getString("created_at"));
                answer.setPraise_num(one.getInt("praise_num"));
                answer.setComment_num(one.getInt("comment_num"));
                answer.setIs_adopted(one.getInt("is_adopted"));
                if (one.has("is_praised")) answer.setIs_praised(one.getInt("is_praised"));
                else answer.setIs_praised(0);
//                JSONArray photo_urls = one.getJSONArray("photo_url");
//                String urls[] = new String[photo_urls.length()];
//                for (int j = 0; j < photo_urls.length(); j++) {
//                    urls[j] = photo_urls.getString(j);
//                }
//                answer.setPhoto_url(urls);
                mAnswerList.add(answer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAnswerList;
    }

    public static List<Question> getSimpleQuestionList(String data) {
        List<Question> questionList = new ArrayList<>();
        try {
            JSONObject dataAll = new JSONObject(data);
            JSONArray questionArray = dataAll.getJSONArray("data");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject one = questionArray.getJSONObject(i);
                Question question = new Question();
                question.setTitle(one.getString("title"));
                question.setDescription(one.getString("description"));
                question.setKind(one.getString("kind"));
                question.setTags(one.getString("tags"));
                question.setReward(one.getInt("reward"));
                question.setAns_num(one.getInt("answer_num"));
                question.setDisappear_at(one.getString("disappear_at"));
                question.setCreated_at(one.getString("created_at"));
                question.setIs_anonymous(one.getInt("is_anonymous"));
                question.setId(one.getInt("id"));
                question.setQuestioner_photo_thumbnail_src(one.getString("photo_thumbnail_src"));
                question.setQuestioner_nickname(one.getString("nickname"));
                question.setQuestioner_gender(one.getString("gender"));
                questionList.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public static String getElement(String data, String name) {
        try {
            JSONObject js = new JSONObject(data);
            //检查存在才返回
            if (js.has(name)) {
                return js.getString(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return null;
    }
}
