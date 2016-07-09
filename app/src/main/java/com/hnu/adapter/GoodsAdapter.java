package com.hnu.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnu.activity.GoodsListActivity;
import com.hnu.lsclientapp.R;
import com.hnu.pojo.Goods;
import com.hnu.util.BitmatImage;
import com.hnu.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

public class GoodsAdapter extends BaseAdapter {

    private List<Goods> mList;
    public GoodsAdapter(List<Goods> list) {
        mList=list;
    }
    public int getCount() {
        return  (mList!=null)?mList.size():0;
    }
    public Goods getItem(int arg0) {
        return  (mList!=null && mList.size()>arg0)?mList.get(arg0):null;
    }
    public long getItemId(int arg0) {
        return arg0;
    }
    public View getView(int position, View v, ViewGroup arg2) {
        ViewHandler handler=null;
        if (v==null) {
            v=LayoutInflater.from(arg2.getContext()).inflate(R.layout.goods_list_row, null);
            handler=new ViewHandler();
            ViewUtils.inject(handler, v);
            v.setTag(handler);
        }else{
            handler=(ViewHandler) v.getTag();
        }
        Goods goods=mList.get(position);
        //图片缓存 框架
        Picasso.with(arg2.getContext()).load(goods.getImgUrl()).placeholder(R.drawable.default_pic).into(handler.photo);

//        Bitmap bitmap = BitmatImage.getHttpBitmap(goods.getImgUrl());
//        Log.i("image----->", bitmap+"------"+goods.getImgUrl()+"--------");
        handler.photo.setBackgroundResource(R.drawable.shop);

//		InputStream inputStream;
//		try {
//			inputStream = BitmatImage.getImageViewInputStream(goods.getImgUrl());
//			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//			handler.photo.setImageBitmap(bitmap);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        handler.title.setText(goods.getSortTitle());

        handler.tv_content.setText(goods.getTitle());

        if (GoodsListActivity.lat!=null&&GoodsListActivity.lon!=null) {
            double distance= ToolKits.getDistance(GoodsListActivity.lat, GoodsListActivity.lon,
                    Double.parseDouble(goods.getShop().getLat()), Double.parseDouble(goods.getShop().getLon()));
            if (distance>1000) {
                handler.count.setText(distance/1000+"千米");
            }else{
                handler.count.setText(distance+"米");
            }
        }

        handler.price.setText(String.valueOf("￥"+goods.getPrice()));
        handler.value.setText(String.valueOf("￥"+goods.getValue()));
        if (goods.isRefund()) {
            handler.appoitment_img.setVisibility(View.VISIBLE);
        }else{
            handler.appoitment_img.setVisibility(View.GONE);
        }
        return v;
    }
    class ViewHandler{
        @ViewInject(R.id.title)
        TextView title;
        @ViewInject(R.id.tv_content)
        TextView tv_content;
        @ViewInject(R.id.price)
        TextView price;
        @ViewInject(R.id.value)
        TextView value;
        @ViewInject(R.id.distance)
        TextView count;
        @ViewInject(R.id.photo)
        ImageView photo;
        @ViewInject(R.id.appoitment_img)
        ImageView appoitment_img;
    }





}

