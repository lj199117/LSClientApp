package com.hnu.fragment;

import com.hnu.lsclientapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMore extends BaseFragment {

	  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_more, null);
		return view;
	}
    @Override
    public void setMenuVisibility(boolean menuVisible) {
    	// TODO Auto-generated method stub
    	super.setMenuVisibility(menuVisible);
    	if (this.getView()!=null) {
			this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
    }
}
