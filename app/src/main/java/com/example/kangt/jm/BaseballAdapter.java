package com.example.kangt.jm;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BaseballAdapter extends PagerAdapter {
    private int[] images = {R.drawable.basketballevent3, R.drawable.basketballevent1, R.drawable.basketballevent2};
    private LayoutInflater inflater;
    private Context context;

    public BaseballAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount(){
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj){
        return view == ((LinearLayout)obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_eventbaseball1, container, false);
        ImageView img = (ImageView)v.findViewById(R.id.img_baseball);
        img.setImageResource(images[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj){
        container.invalidate();
    }
}
