package com.hnu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hnu.lsclientapp.R;
import com.hnu.pojo.ResponseObject;
import com.hnu.pojo.User;
import com.hnu.util.CONSTANT;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by LJ on 2016-04-28.
 */
public class RegisterActivity extends Activity  {
    @ViewInject(R.id.reg_user)
    private EditText reg_user;
    @ViewInject(R.id.reg_pwd)
    private EditText reg_pwd;
    @ViewInject(R.id.reg_pwd2)
    private EditText reg_pwd2;
    @ViewInject(R.id.reg_btn)
    private Button reg_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
    }
    @OnClick({R.id.reg_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.reg_btn:
                userRegister();
                break;
        }
    }

    private void userRegister() {
        //明明线程可以关闭啊
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    finish();
//                }catch (Exception e){}
//            }
//        }).start();

        //注册
        if (TextUtils.isEmpty(reg_user.getText().toString())) {
            reg_user.setError(Html.fromHtml("<font color=red>用户名不能为空</font>"));
            return ;
        }
        if (TextUtils.isEmpty(reg_pwd.getText().toString())) {
            reg_pwd.setError(Html.fromHtml("<font color=red>密码不能为空</font>"));
            return ;
        }
        if (!reg_pwd.getText().toString().equals(reg_pwd2.getText().toString())) {
            reg_pwd2.setError(Html.fromHtml("<font color=red>两次密码不正确</font>"));
            return ;
        }
        //&username=test&password=123

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("flag","register");
        params.addQueryStringParameter("username",this.reg_user.getText().toString().trim());
        params.addQueryStringParameter("password", this.reg_pwd.getText().toString().trim());
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONSTANT.USER_REGISTER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ResponseObject<User> result=new GsonBuilder().create().fromJson(responseInfo.result,
                        new TypeToken<ResponseObject<User>>(){}.getType());
                if (responseInfo.statusCode==1) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //无法在这finish() 活动 why?
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
