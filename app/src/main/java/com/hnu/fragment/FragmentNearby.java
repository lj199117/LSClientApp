package com.hnu.fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hnu.activity.GoodsListActivity;
import com.hnu.adapter.NearbyAdapter;
import com.hnu.lsclientapp.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentNearby extends BaseFragment implements AMapLocationListener,LocationListener{
	@ViewInject(R.id.nearby_title)
	private  TextView nearby_title;
	@ViewInject(R.id.location_text)
	private TextView location_text;
	@ViewInject(R.id.nearby_grid_view)
	private GridView nearby_grid_view;
	@ViewInject(R.id.location_group)
	private LinearLayout location_group;
	@ViewInject(R.id.location_progress)
	private View location_progress;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	private Double lat,lon;
	private String desc="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_nearby, null);
		ViewUtils.inject(this,view);
		nearby_grid_view.setAdapter(new NearbyAdapter());
		nearby_grid_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				//假数据
//				lat=39.98318;
//				lon=116.31600;
				if (lat!=null && lon!=null) {
					Intent intent=new Intent(getActivity(),GoodsListActivity.class);
					intent.putExtra("lat", lat);
					intent.putExtra("lng", lon);
					intent.putExtra("desc", desc);
					intent.putExtra("category", ((NearbyAdapter)parent.getAdapter()).getItemValue(position));

					startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_SHORT).show();
				}
			}
		});

		//初始化定位所需变量
		//设置高德地图
		locationClient = new AMapLocationClient(this.getActivity());
		locationOption = new AMapLocationClientOption();
		//initOption();
		// 设置定位模式为高精度模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		// 设置定位监听
		locationClient.setLocationListener(this);
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();

		return view;
	}



	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if (this.getView()!=null) {
			this.getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if(aMapLocation != null){
			this.lat=aMapLocation.getLatitude();
			this.lon=aMapLocation.getLongitude();
			Bundle locBundle = aMapLocation.getExtras();//获取额外信息
			desc = "";
			Log.d("FragmentNearby","locBundle"+locBundle);
			if(locBundle != null){
				String citycode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
				location_text.setText("城市代码" + citycode+","+desc);
				location_progress.setVisibility(View.GONE);
			}
			//关闭定位
			stopLocation();
		}
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		stopLocation();
	}

	private void stopLocation() {
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
