package com.hnu.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmatImage {


	public static Bitmap getHttpBitmap(String url){
		URL myFileURL;
		Bitmap bitmap=null;
		try{
			myFileURL = new URL(url);
			//获得连接
			HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
			//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			//连接设置获得数据流
			conn.setDoInput(true);
			//不使用缓存
			conn.setUseCaches(false);
			//这句可有可无，没有影响
			//conn.connect();
			//得到数据流
			InputStream is = conn.getInputStream();
			//解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			//关闭数据流
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		return bitmap;

	}


	/**
	 * 从网络中获取图片，以流的形式返回
	 * @return
	 */
	public static InputStream getImageViewInputStream(String urls) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(urls);
		Log.i("image----->", urls+"--------------");//服务器地址
		if (url != null) {
			//打开连接
			HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
			httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
			httpURLConnection.setDoInput(true);                //打开输入流
			int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
			if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
				inputStream = httpURLConnection.getInputStream();        //获取输入流
			}
		}
		return inputStream;
	}
}
