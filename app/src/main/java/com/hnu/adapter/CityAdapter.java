package com.hnu.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hnu.lsclientapp.R;
import com.hnu.pojo.City;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class CityAdapter extends BaseAdapter implements SectionIndexer{
    private List<City> mlist;

    public CityAdapter(List<City> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return (mlist!=null) ? mlist.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (mlist!=null && mlist.size()>position)?mlist.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHandler handler=null;
        if (convertView==null) {
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row, null);
            handler=new ViewHandler();
            ViewUtils.inject(handler, convertView);
            convertView.setTag(handler);
        }else{
            handler=(ViewHandler) convertView.getTag();
        }
        //加载数据
        //数据加载
        City city=mlist.get(position);
        handler.item_city.setText(city.getName());
        //判断是否显示拼音的首字母
        int sec = getSectionForPosition(position);//65 66 67翻到什么位置就出到什么位置
        int pos = getPositionForSection(sec);
        Log.d("CityAdapter","sec:"+sec+",pos:"+pos);
        if (pos == position) {
            handler.item_city_section.setVisibility(View.VISIBLE);
            handler.item_city_section.setText(city.getSortKey());
            Log.i("CityAdapter", city.getSortKey() + "````");
        }else{
            handler.item_city_section.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
    //对于同一部分索引所对应的位置值
    @Override
    public int getPositionForSection(int sectionIndex) {
        int lenght=getCount();
        for (int i = 0; i < lenght; i++) {
            char firstChar=mlist.get(i).getSortKey().charAt(0);
            if (firstChar==sectionIndex) {
                return i;
            }
        }
        return -1;
    }
    // 某个位置的item对应的值
    @Override
    public int getSectionForPosition(int position) {
        return mlist.get(position).getSortKey().toUpperCase().charAt(0);
    }

    //缓存控件
    class ViewHandler{
        @ViewInject(R.id.item_city_section)
        TextView item_city_section;
        @ViewInject(R.id.item_city)
        TextView item_city;
    }
}
