package com.hnu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hnu.activity.LoginActivity;
import com.hnu.lsclientapp.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

public class FragmentMy extends BaseFragment{
	@ViewInject(R.id.to_login_btn)
	private Button to_login_btn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_my, null);
		ViewUtils.inject(this,view);
		return view;
	}

	@OnClick({R.id.to_login_btn})
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.to_login_btn:
				startActivity(new Intent(getActivity(),LoginActivity.class));
				break;
			default:break;
		}
	}
}
