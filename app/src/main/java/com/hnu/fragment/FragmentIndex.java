package com.hnu.fragment;

import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hnu.activity.CityActivity;
import com.hnu.activity.GoodsDetailsActivity;
import com.hnu.activity.NearbyActivity;
import com.hnu.adapter.GoodsAdapter;
import com.hnu.lsclientapp.R;
import com.hnu.pojo.Goods;
import com.hnu.pojo.ResponseObject;
import com.hnu.util.CONSTANT;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.ActionBar.OnMenuVisibilityListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.media.JetPlayer.OnJetEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentIndex extends Fragment{
	@ViewInject(R.id.home_city)
	private TextView home_city;
	@ViewInject(R.id.home_icon_map)
	private ImageView home_icon_map;
	@ViewInject(R.id.home_icon_search)
	private ImageView home_icon_search;

	@ViewInject(R.id.goods_list_view)
	private PullToRefreshListView goods_list_view;

	private int page=0;
	private int size=20;
	private int count=0;

	private List<Goods> mList;

	private GoodsAdapter goodsAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_index, null);
		ViewUtils.inject(this, view);
		//配置--
		//同时支持  上拉更多 下拉刷新
		goods_list_view.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		//设置 加载是否可以滚动刷新
		goods_list_view.setScrollingWhileRefreshingEnabled(true);
		//上拉更多 下拉刷新 监听
		goods_list_view.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//数据加载的操作
				loadDatas(refreshView.getScrollY()<0);
			}
		});
		goods_list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(),GoodsDetailsActivity.class);
				intent.putExtra("goods",goodsAdapter.getItem(position));
				startActivity(intent);
			}
		});
		//点击事件
//		goods_list_view.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//									int position, long id) {
//				Intent intent=new Intent(getActivity(),GoodsDetailsActivity.class);
//				intent.putExtra("goods", goodsAdapter.getItem(position));
//				startActivity(intent);
//			}
//		});

		//首次自动加载数据
		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
			goods_list_view.setRefreshing();//框架自己的方法
			return false;
			}
		}).sendEmptyMessageDelayed(0, 2000);
		return view;
	}
	//加载数据
	//true ---->  从上 向下拉 刷新
	//false ----->从下 向上拉  加载更多
	private void loadDatas(final boolean direction){
		RequestParams params=new RequestParams();
		//判断分页
		if (direction) {
			page=1;
		}else{
			page++;
		}

		//设置请求参数
		params.addQueryStringParameter("page", String.valueOf(page));
		params.addQueryStringParameter("size", String.valueOf(size));
		Log.i("----->", CONSTANT.GOODS_LIST+"-----------"+params);
		new HttpUtils().send(HttpMethod.GET, CONSTANT.GOODS_LIST, params, new RequestCallBack<String>() {
			@SuppressWarnings("unchecked")
			public void onSuccess(ResponseInfo<String> arg0) {
				goods_list_view.onRefreshComplete();//恢复
				ResponseObject<List<Goods>> object=new GsonBuilder().create().fromJson(arg0.result, new TypeToken<ResponseObject<List<Goods>>>(){}.getType());
				//分页
				page =object.getPage();
				size=object.getSize();
				count=object.getCount();
				Log.i("----->", object.getDatas()+"-----------");
				if (direction) {
					//true ---->  从上 向下拉 刷新
					mList=(List<Goods>) object.getDatas();
					goodsAdapter=new GoodsAdapter(mList);
					goods_list_view.setAdapter(goodsAdapter);
				}else{
					//false ----->从下 向上拉  加载更多
					mList.addAll((List<Goods>)object.getDatas());
					goodsAdapter.notifyDataSetChanged();
				}
				if (count==page) {
					//最后一页
					goods_list_view.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH.PULL_FROM_START);
				}
			}

			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}


	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView()!=null) {
			this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
	}


	@OnClick({R.id.home_city,R.id.home_icon_map,R.id.home_icon_search})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.home_city:
				startActivity(new Intent(getActivity(),CityActivity.class));
				break;
			case R.id.home_icon_map:
				startActivity(new Intent(getActivity(),NearbyActivity.class));
				break;
			case R.id.home_icon_search:
				break;
		}
	}
}
