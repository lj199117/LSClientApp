package com.hnu.adapter;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hnu.lsclientapp.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class NearbyAdapter extends BaseAdapter{
    private String values[]={"3","2894","8","2892","2856","2924","4","6",""};

    //文字
    private String labels[]=new String[]{"美食","电影","酒店","KTV","火锅","美容美发","休闲娱乐","生活服务","全部"};
    //颜色
    private int colors[]=new int[]{R.color.item_0,R.color.item_1,R.color.item_2,R.color.item_3,R.color.item_4,R.color.item_5,R.color.item_6,R.color.item_7,R.color.item_8};
    //图片
    private int icons[]=new int[]{
            R.drawable.food_select,
            R.drawable.movie_select,
            R.drawable.hotel_select,
            R.drawable.ktv_select,
            R.drawable.hot_pot_select,
            R.drawable.hair_select,
            R.drawable.fun_select,
            R.drawable.life_select,
            R.drawable.all
    };

    @Override
    public int getCount() {
        return labels.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public String getItemValue(int position) {
        return values[position];
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_grid_view_item,null);
            viewHolder = new ViewHolder();
            //viewHolder.textView = (TextView)convertView.findViewById(R.id.textView);
            ViewUtils.inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //渲染数据
        viewHolder.textView.setText(labels[position]);
        viewHolder.textView.setTextColor(parent.getContext()
                            .getResources().getColor(colors[position]));//这...
        Drawable drawable=parent.getContext().getResources().getDrawable(icons[position]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        viewHolder.textView.setCompoundDrawables(null,  null, null,drawable);//绘制的地方
        return convertView;
    }

    class ViewHolder{
        @ViewInject(R.id.textView)
        TextView textView;
    }
}
