package com.hnu.lsclientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hnu.util.ToolKits;

/**
 * Created by LJ on 2016-04-20.
 */
public class WelcomeActivity extends Activity {
    public static final String IS_FRIST_LOGIN = "is_first";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(ToolKits.getBoolean(WelcomeActivity.this,IS_FRIST_LOGIN,true)){
                    startActivity(new Intent(WelcomeActivity.this,WhatsNewsActivity.class));
                }else
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                ToolKits.putBoolean(WelcomeActivity.this,IS_FRIST_LOGIN,false);
                return true;
            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);

        finish();
    }
}
