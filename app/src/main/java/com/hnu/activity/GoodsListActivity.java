package com.hnu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class GoodsListActivity extends Activity implements AMapLocationListener{
    @ViewInject(R.id.location_text)
    private TextView location_text;
    @ViewInject(R.id.location_progress)
    private View location_progress;
    @ViewInject(R.id.location_group)
    private LinearLayout location_group;
    @ViewInject(R.id.goods_list_view)
    private PullToRefreshListView goods_list_view;

    private int page=0;
    private int size=20;
    private int count=0;
    private List<Goods> mList;
    GoodsAdapter goodsAdapter = null;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    public static Double lat,lon;
    private String desc="";
    private String category="";
    private String radius="1000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ViewUtils.inject(this);
        //初始化并启动定位
        initLocation();

        //配置--
        //同时支持  上拉更多 下拉刷新
        goods_list_view.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
        //设置 加载是否可以滚动刷新
        goods_list_view.setScrollingWhileRefreshingEnabled(true);
        //上拉更多 下拉刷新 监听
        goods_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //数据加载的操作
                loadDatas(refreshView.getScrollY() < 0);
            }
        });
        goods_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsListActivity.this,GoodsDetailsActivity.class);
                intent.putExtra("goods",goodsAdapter.getItem(position));
                startActivity(intent);
            }
        });

        //首次自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                goods_list_view.setRefreshing();//框架自己的方法
                return false;
            }
        }).sendEmptyMessageDelayed(0, 2000);


    }

    /**
     * 先从上一级的activity传来的Intent中获取必要的参数信息直接定位
     * 如果没取到必要信息，我再采用重新定位
     */
    private void initLocation() {
        Bundle bundle=getIntent().getExtras();//从Intent中获取额外参数
        Log.d("GoodsListActivity", "GoodsListActivity" + bundle.toString());
        if (bundle!=null) {
            lat=bundle.getDouble("lat");
            lon=bundle.getDouble("lng");
            desc=bundle.getString("desc");
            category=bundle.getString("category");
            Log.d("GoodsListActivity", bundle.toString());
        }
        if (lat==null ||lon==null) {
            //初始化定位所需变量
            //设置高德地图
            locationClient = new AMapLocationClient(GoodsListActivity.this);
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
        }else{
            location_text.setText(desc);
            location_progress.setVisibility(View.GONE);
        }
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
        params.addQueryStringParameter("lat",String.valueOf(lat));
        params.addQueryStringParameter("lon",String.valueOf(lon));
        params.addQueryStringParameter("caregory",category);
        params.addQueryStringParameter("radius",radius);
        new HttpUtils().send(HttpRequest.HttpMethod.GET, CONSTANT.NearBy_LIST, params, new RequestCallBack<String>() {
            @SuppressWarnings("unchecked")
            public void onSuccess(ResponseInfo<String> arg0) {
                goods_list_view.onRefreshComplete();//恢复
                ResponseObject<List<Goods>> object = new GsonBuilder().create().fromJson(arg0.result, new TypeToken<ResponseObject<List<Goods>>>() {
                }.getType());
                //分页
                page = object.getPage();
                size = object.getSize();
                count = object.getCount();
                if (direction) {
                    //true ---->  从上 向下拉 刷新
                    mList = (List<Goods>) object.getDatas();
                    goodsAdapter = new GoodsAdapter(mList);
                    goods_list_view.setAdapter(goodsAdapter);
                } else {
                    //false ----->从下 向上拉  加载更多
                    mList.addAll((List<Goods>) object.getDatas());
                    // goodsAdapter.notifyDataSetChanged();
                }
                if (count == page) {
                    //最后一页
                    goods_list_view.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH.PULL_FROM_START);
                }
            }

            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(GoodsListActivity.this, arg1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation != null){
            this.lat=aMapLocation.getLatitude();
            this.lon=aMapLocation.getLongitude();
            Bundle locBundle = aMapLocation.getExtras();//获取额外信息
            String desc = "";
            Log.d("GoodsListActivity",locBundle.toString());
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
    protected void onDestroy() {
        super.onDestroy();
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
