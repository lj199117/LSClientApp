package com.hnu.lsclientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hnu.adapter.GuiderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJ on 2016-04-20.
 */
public class WhatsNewsActivity extends Activity {
    private ViewPager viewPager = null;
    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        initViewPage();
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhatsNewsActivity.this,MainActivity.class));
            }
        });
    }

    public void initViewPage(){
        List<View> mlist = new ArrayList<View>();
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.what_1);
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.what_2);
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.what_3);
        mlist.add(imageView1);
        mlist.add(imageView2);
        mlist.add(imageView3);
        viewPager.setAdapter(new GuiderAdapter(mlist));//ViewPager viewPager
        //只有翻到第三页才有按钮 所以设置事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                if (arg0==2) {
                    button.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.GONE);
                }
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

}
