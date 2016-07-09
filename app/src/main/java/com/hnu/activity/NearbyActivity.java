package com.hnu.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hnu.lsclientapp.R;
import com.hnu.pojo.Goods;
import com.hnu.pojo.ResponseObject;
import com.hnu.pojo.Shop;
import com.hnu.util.CONSTANT;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Administrator on 2016/4/23 0023.
 */
public class NearbyActivity extends Activity implements LocationSource,
        AMapLocationListener,
        AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter,
        AMap.OnInfoWindowClickListener {
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @ViewInject(R.id.backImg)
    private ImageView backImg;
    @ViewInject(R.id.refreshImg)
    private ImageView refreshImg;

    private double lat=40.075483,lon=116.367612,radius=1000;
    private boolean isLoad = false;//加载一次之后不再重复加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_map);
        ViewUtils.inject(this);//不能忘

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        loadData(lat,lon,radius);
    }
    @OnClick({R.id.backImg,R.id.refreshImg})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.backImg:
                finish();
                //Toast.makeText(this,"backImg",Toast.LENGTH_SHORT).show();
                break;
            case R.id.refreshImg:
                loadData(lat,lon,radius);
                break;
        }
    }

    private void loadData(final double lat, final double lon,double radius) {
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("lat",String.valueOf(lat));
        requestParams.addQueryStringParameter("lon",String.valueOf(lon));
        requestParams.addQueryStringParameter("radius", String.valueOf(radius));
        Log.d("------>", "lat:" + lat + "lon:" + lon + "radius:" + radius);
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONSTANT.NearBy_LIST, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new GsonBuilder().create();
                ResponseObject<List<Goods>> responseObject = gson.fromJson(responseInfo.result,
                        new TypeToken<ResponseObject<List<Goods>>>() {
                        }.getType());
                List<Goods> data = responseObject.getDatas();
                //在地图上标记商家
                addMarker(data);
                //将镜头自动设置到某个缩放级别
                aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(
                        new LatLng(lat,lon),14,0,0
                    )
                ));
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(NearbyActivity.this,"数据加载失败...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //将数据适配到地图上
    private void addMarker(List<Goods> data) {
        MarkerOptions markerOptions;
        for(Goods goods:data){
            Shop shop = goods.getShop();
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Double.parseDouble(shop.getLat()),
                    Double.parseDouble(shop.getLon())))
                    .title(shop.getName())
                    .snippet("￥" + goods.getPrice());
            //不同类型商品设置不同类型图标
            if(goods.getCategoryId().equals("3")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_food));
            }else if(goods.getCategoryId().equals("5")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_ktv));
            }else if(goods.getCategoryId().equals("8")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_hotel));
            }else if(goods.getCategoryId().equals("6")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_life));
            }else if(goods.getCategoryId().equals("4")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_movie));
            }else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_fun));
            }
            //设置商家位置不可被拖动
            markerOptions.draggable(false);
            aMap.addMarker(markerOptions).setObject(goods);
        }
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnInfoWindowClickListener(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0 && !isLoad) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                lon = amapLocation.getLongitude();
                lat = amapLocation.getLatitude();
                //Log.e("------>", lon+" "+lat);
                loadData(lat,lon,radius);
                isLoad = true;//加载过了，回调onLocationChanged时不再调用了
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(10000);//时间间隔10s
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.getObject();//得到商品信息
        //然后根据这个信息可以切换到商品详情列表

    }
}
