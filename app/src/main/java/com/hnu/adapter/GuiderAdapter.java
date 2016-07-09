package com.hnu.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by LJ on 2016-04-20.
 */
public class GuiderAdapter extends PagerAdapter {
    List<View> mlist;
    public GuiderAdapter(List<View> mlist){
        this.mlist = mlist;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    //移除不可见的view destroyItem
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mlist.get(position));//原代码没这句，导致回看崩溃
        ((ViewPager)container).recomputeViewAttributes(mlist.get(position));
    }
    //添加可见的view
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mlist.get(position));
        return mlist.get(position);
    }
}
