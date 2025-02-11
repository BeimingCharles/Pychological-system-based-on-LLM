package com.example.test.adapter;

import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {


    private List<Integer> mPics=null;

    @Override
    public int getCount() {
        if(mPics!=null){
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public void setData(List<Integer> colors) {
        this.mPics=colors;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition=position%mPics.size();
        ImageView imageView=new ImageView(container.getContext());
        //imageView.setBackgroundColor(mColors.get(position));
        imageView.setImageResource(mPics.get(realPosition));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public int getDataRealSize(){
        if(mPics!=null){
            return mPics.size();
        }
        return 0;
    }
}
