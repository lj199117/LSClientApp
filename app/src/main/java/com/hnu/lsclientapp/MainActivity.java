package com.hnu.lsclientapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.hnu.fragment.FragmentIndex;
import com.hnu.fragment.FragmentMore;
import com.hnu.fragment.FragmentMy;
import com.hnu.fragment.FragmentNearby;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

public class MainActivity extends FragmentActivity {
    @ViewInject(R.id.bottom_bar)
    private RadioGroup bottom_bar;
    @ViewInject(R.id.layout_content)
    private FrameLayout layout_content;

    private boolean isInit=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        bottom_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            int index=0;
            switch (checkedId) {
                case R.id.radio0:
                    index=0;
                    break;
                case R.id.radio1:
                    index=1;
                    break;
                case R.id.radio2:
                    index=2;
                    break;
                case R.id.radio3:
                    index=3;
                    break;
            }
            //通过适配器动态设置Fragment中的内容
            Fragment fragment=(Fragment) fragmentPagerAdapter.instantiateItem(layout_content, index);
            fragmentPagerAdapter.setPrimaryItem(layout_content, 0, fragment);
            fragmentPagerAdapter.finishUpdate(layout_content);
            }
        });
    }
    FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment=null;
            switch (arg0) {
                case 0:
                    fragment=new FragmentIndex();
                    break;
                case 1:
                    fragment=new FragmentNearby();
                    break;
                case 2:
                    fragment=new FragmentMy();
                    break;
                case 3:
                    fragment=new FragmentMore();
                    break;
                default:
                    fragment=new FragmentIndex();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    //这个操作由于改变xml默认设置,所以会导致RadioGroup的OncheckChanged事件
    protected void onStart() {
        super.onStart();
        bottom_bar.check(R.id.radio0);
    }
    /*//变成一个独立的方法->通过注解
    @OnRadioGroupCheckedChange({R.id.bottom_bar})
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }*/
}
