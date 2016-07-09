package com.hnu.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnu.lsclientapp.R;
import com.hnu.pojo.Goods;
import com.hnu.pojo.Shop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class GoodsDetailsActivity extends Activity {

    private Goods goods;
    private Shop shop;
    @ViewInject(R.id.goods_image)
    private ImageView goods_image;
    @ViewInject(R.id.goods_title)
    private TextView goods_title;
    @ViewInject(R.id.goods_desc)
    private TextView goods_desc;
    @ViewInject(R.id.goods_price)
    private TextView goods_price;
    @ViewInject(R.id.goods_old_price)
    private TextView goods_old_price;

    @ViewInject(R.id.shop_title)
    private TextView shop_title;
    @ViewInject(R.id.shop_phone)
    private TextView shop_phone;
    @ViewInject(R.id.shop_address)
    private TextView shop_address;
    @ViewInject(R.id.shop_call)
    private ImageView shop_call;

    @ViewInject(R.id.tv_more_details_web_view)
    private WebView tv_more_details_web_view;
    @ViewInject(R.id.wv_gn_warm_prompt)
    private WebView wv_gn_warm_prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ViewUtils.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            goods = (Goods) bundle.get("goods");
        }
        if (goods != null) {//渲染数据到界面
            //商品标题图片
            Picasso.with(this).load(goods.getImgUrl())
                    .placeholder(R.drawable.default_pic)//显示默认图片
                    .into(goods_image);//显示到控件中

            //商品标题
            goods_title.setText(goods.getSortTitle());
            goods_desc.setText(goods.getTitle());
            goods_price.setText("￥" + goods.getPrice());
            goods_old_price.setText("￥" + goods.getValue());//原价
            //在原价上画线
            goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            //开始商家信息渲染
            shop = goods.getShop();
            shop_title.setText(shop.getName());
            shop_phone.setText(shop.getTel());
            shop_address.setText(shop.getAddress());
            //开始本单详情 和温馨提示的 WebView控件
            WebSettings webSetting1 = tv_more_details_web_view.getSettings();
            //设置内容的宽度自适应屏幕的宽度
            webSetting1.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            WebSettings webSetting2 = wv_gn_warm_prompt.getSettings();
            webSetting2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            String[] data = htmlSub(goods.getDetail());
            Log.d("GoodsDetailsActivity",data[0]+"\r\n"+data[1]);
            tv_more_details_web_view.loadDataWithBaseURL("", data[1], "text/html", "utf-8", "");
            wv_gn_warm_prompt.loadDataWithBaseURL("", data[0], "text/html", "utf-8", "");

        }
    }

    //获取本单详情和温馨提示等信息
    public String[] htmlSub(String html) {
        char[] str = html.toCharArray();
        int len = str.length;
        System.out.println(len);
        int n = 0;
        String[] data = new String[3];
        int oneindex = 0;
        int secindex = 1;
        int thrindex = 2;
        for (int i = 0; i < len; i++) {
            if (str[i] == '【') {
                n++;
                if (n == 1) {
                    oneindex = i;
                }
                if (n == 2) {
                    secindex = i;
                }
                if (n == 3) {
                    thrindex = i;
                }
            }
        }
        if (oneindex > 0 && secindex > 1 && thrindex > 2) {
            data[0] = html.substring(oneindex, secindex);
            data[1] = html.substring(secindex, thrindex);
            data[2] = html.substring(thrindex, html.length());
        }
        return data;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_call:
                Intent callin = new Intent(Intent.ACTION_CALL);
                callin.setData(Uri.parse("tel:" + shop.getTel()));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(callin);
                break;

            default:
                break;
        }
    }
}
