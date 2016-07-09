package com.hnu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hnu.lsclientapp.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

/**
 * Created by LJ on 2016-04-28.
 */
public class LoginActivity extends Activity{
    @ViewInject(R.id.vertify_btn)
    private Button vertify_btn;
    @ViewInject(R.id.register)
    private TextView register;
    @ViewInject(R.id.username)
    private EditText username;
    @ViewInject(R.id.password)
    private EditText password;
    @ViewInject(R.id.login_btn)
    private Button login_btn;
    @ViewInject(R.id.fly_view)
    private View fly_view;
    private Animation move_to_left,move_to_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        //加载动画
        move_to_left= AnimationUtils.loadAnimation(this, R.anim.move_to_left);
        move_to_right=AnimationUtils.loadAnimation(this, R.anim.move_to_right);

        username.addTextChangedListener(mTextWatcher);
        password.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (username.getText().toString().trim().length()>0&&password.getText().toString().trim().length()>0) {
                login_btn.setEnabled(true);
            }else{
                login_btn.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };
    @OnClick({R.id.register})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
//                finish();
                break;
        }
    }

    @OnRadioGroupCheckedChange({R.id.rg_login})
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.rg_login)
            switch (checkedId) {
                case R.id.rd1:
                    fly_view.startAnimation(move_to_left);
                    vertify_btn.setVisibility(View.GONE);
                    username.setHint("用户名/邮箱/手机号");
                    password.setHint("密码");
                    break;
                case R.id.rd2:
                    fly_view.startAnimation(move_to_right );
                    vertify_btn.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    password.setHint("验证码");
                    break;
            }
    }
}
