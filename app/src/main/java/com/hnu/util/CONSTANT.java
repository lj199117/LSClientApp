package com.hnu.util;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class CONSTANT {
    public static final String HOST = "http://192.168.1.105:8080/ls_server";
    //城市类表
    public static final String CITY_LIST = HOST + "/servlet/CityServlet";
    //商品类表
    public static final String GOODS_LIST = HOST + "/servlet/GoodsServlet";
    //附近类表
    //测试  NearbyServlet?lat=39.983456&lon=116.3154950&raidus=1000&caregory=8
    public static final String NearBy_LIST = HOST + "/servlet/NearbyServlet";
    //用户登录注册
    //测试  UserServlet?flag=register&username=xxx&password=xxx
    public static final String USER_REGISTER = HOST + "/servlet/UserServlet";
}
