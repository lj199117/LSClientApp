package com.hnu.activity;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hnu.adapter.CityAdapter;
import com.hnu.lsclientapp.R;
import com.hnu.pojo.City;
import com.hnu.pojo.ResponseObject;
import com.hnu.util.CONSTANT;
import com.hnu.widget.SideBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class CityActivity extends Activity implements AMapLocationListener{

    @ViewInject(R.id.city_list)
    private ListView cityListView;
    @ViewInject(R.id.city_back)
    private TextView cityBack;
    @ViewInject(R.id.city_refresh)
    private ImageView cityRefresh;
    @ViewInject(R.id.side_bar)
    private SideBar side_bar;

    private CityAdapter cityAdapter;

    private TextView myLocation;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ViewUtils.inject(this);

        loadCity();

        //设置监听
        side_bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            public void onTouchingLetterChanged(String s) {
            //根据传进来的参数设置  city_list_view的选择状态
            int index = cityAdapter.getPositionForSection(s.toUpperCase().charAt(0));
            if (index != -1) {
                cityListView.setSelection(index);
            }
            }
        });

        View cityhead = getLayoutInflater().inflate(R.layout.location_city_item, null);
        myLocation=(TextView) cityhead.findViewById(R.id.tv_location_city);//在onLocationChanged()方法中赋值
        cityListView.addHeaderView(cityhead);//在listview的头部再设置一个布局

        //设置高德地图
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        initOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @OnClick({R.id.city_back,R.id.city_refresh,R.id.tv_location_city})
    public void Onclick(View view){
        switch (view.getId()) {
            case R.id.city_back://返回
                finish();//只需从活动栈中删除即可
                break;
            case R.id.city_refresh://更新
                loadCity();
                break;
            case R.id.tv_location_city:

                break;
        }
    }

    private void loadCity() {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONSTANT.CITY_LIST, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new GsonBuilder().create();
                //从服务器获取信息(这里的泛型怎么用值得学习)
                ResponseObject<List<City>> result = gson.fromJson(responseInfo.result,
                        new TypeToken<ResponseObject<List<City>>>() {
                }.getType());
                cityAdapter = new CityAdapter((List<City>) result.getDatas());
                cityListView.setAdapter(cityAdapter);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(CityActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                myLocation.setText(amapLocation.getProvince()+amapLocation.getCity());
                Log.i("------>", amapLocation.getCity());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        String strInterval = "5000";
        if (!TextUtils.isEmpty(strInterval)) {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf(strInterval));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
