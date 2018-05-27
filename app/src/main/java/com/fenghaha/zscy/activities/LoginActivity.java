package com.fenghaha.zscy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fenghaha.zscy.R;
import com.fenghaha.zscy.util.ApiParams;
import com.fenghaha.zscy.util.HttpUtil;
import com.fenghaha.zscy.util.JsonParser;
import com.fenghaha.zscy.util.MyApplication;
import com.fenghaha.zscy.util.MyTextUtil;
import com.fenghaha.zscy.util.ToastUtil;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpViews();
    }

    private void setUpViews() {
        Button mBack = findViewById(R.id.bt_login_back);
        Button logIn = findViewById(R.id.bt_login);
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        mBack.setOnClickListener(v -> finish());
        logIn.setOnClickListener(v -> logIn());
    }

    private void logIn() {
        String mUserName = userName.getText().toString();
        String psw = password.getText().toString();
        if (MyTextUtil.isEmpty(mUserName) || MyTextUtil.isEmpty(psw)) {
            ToastUtil.makeToast("请正确输入账号密码!");
        }else {
            String url = ApiParams.LOGIN;
            String param = "stuNum="+mUserName+"&idNum="+psw;
            HttpUtil.post(url, param, new HttpUtil.HttpCallBack() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isOk()){
                        MyApplication.setUser(JsonParser.getUser(response.getData()));
                        finish();
                    }else {
                        ToastUtil.makeToast(response.getInfo());
                    }
                }

                @Override
                public void onFail(String reason) {
                    ToastUtil.makeToast("网络错误,登录失败!");
                }
            });
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
