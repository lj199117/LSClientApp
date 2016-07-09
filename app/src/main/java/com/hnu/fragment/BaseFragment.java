package com.hnu.fragment;


import android.view.View;

/**
 * Created by LJ on 2016-04-20.
 */
public class BaseFragment extends android.support.v4.app.Fragment {
    //四个Fragment子类都要有这个方法 所以我父类帮他完成
    // 解决叠层现象
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(this.getView() != null){
            this.getView().setVisibility(menuVisible ? View.VISIBLE:View.GONE);
        }
    }
}
