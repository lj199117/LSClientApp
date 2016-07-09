package com.hnu.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ToolKits {

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("com.nick", Context.MODE_PRIVATE);
    }

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sharedPreferences=getSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences=getSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        return  getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static String getString(Context context,String key){
        return  getSharedPreferences(context).getString(key, null);
    }
    public static double getDistance(double lat1,double lon1,double lat2,double lon2){
        double radLat1=lat1*Math.PI/180;
        double radLat2=lat2*Math.PI/180;
        double a=radLat1-radLat2;
        double b=lon1*Math.PI/180-lon2*Math.PI/180;
        double s=2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2), 2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2), 2)));
        s=s*6378137.0;
        s=Math.round(s*10000)/10000;
        return s;
    }

}

