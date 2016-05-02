package com.example.adapter;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragementAdapter extends FragmentPagerAdapter {

//    OnReloadListener onReloadListener;
    ArrayList<Fragment> items;
    FragmentManager fm;

    public FragementAdapter(FragmentManager fm, ArrayList<Fragment> items) {
	super(fm);
	// TODO Auto-generated constructor stub
	this.items = items;
	this.fm = fm;
    }

    @Override
    public Fragment getItem(int arg0) {
	// TODO Auto-generated method stub
	return items.get(arg0);
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return items.size();
    }

    @Override
    public int getItemPosition(Object object) {
	// TODO Auto-generated method stub
	return super.getItemPosition(object);
    }
}
