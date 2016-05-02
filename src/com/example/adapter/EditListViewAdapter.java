package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.lightnotes.R;

import android.R.bool;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import android.widget.RadioButton;
import android.widget.TextView;

final class EditViewHolder {
    public CheckBox is_checked;
    public TextView name;
    public RadioButton is_selected;
}

public class EditListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> infoMapArr;
    private Context context;
    private boolean isVisibleCheckBox;
    private boolean isVisibleRadioButton;

    public EditListViewAdapter(Context context,
	    ArrayList<HashMap<String, Object>> infoMapArr) {
	this.context = context;
	mInflater = LayoutInflater.from(context);
	this.infoMapArr = infoMapArr;
    }

    public boolean isVisibleCheckBox() {
	return isVisibleCheckBox;
    }

    public void setVisibleCheckBox(boolean isVisibleCheckBox) {
	this.isVisibleCheckBox = isVisibleCheckBox;
    }

    public boolean isVisibleRadioButton() {
	return isVisibleRadioButton;
    }

    public void setVisibleRadioButton(boolean isVisibleRadioButton) {
	this.isVisibleRadioButton = isVisibleRadioButton;
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return infoMapArr.size();
    }

    @Override
    public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return infoMapArr.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	EditViewHolder holder = null;
	if (convertView == null) {
	    holder = new EditViewHolder();
	    convertView = mInflater.inflate(R.layout.listview_item2, null);
	    holder.name = (TextView) convertView.findViewById(R.id.name);
	    holder.is_checked = (CheckBox) convertView
		    .findViewById(R.id.is_checked);
	    holder.is_selected = (RadioButton) convertView
		    .findViewById(R.id.is_selected);
	    convertView.setTag(holder);

	} else {
	    holder = (EditViewHolder) convertView.getTag();
	}
	//
	holder.name.setText(infoMapArr.get(position).get("name") + "");
	//
	holder.is_checked.setVisibility(isVisibleCheckBox ? View.VISIBLE
		: View.GONE);
	if (isVisibleCheckBox) {
	    holder.is_checked.setChecked((Boolean) infoMapArr.get(position).get(
			"is_checked"));
	}
	//
	holder.is_selected.setVisibility(isVisibleRadioButton ? View.VISIBLE
		: View.GONE);
	if (isVisibleRadioButton) {
	    holder.is_selected.setChecked((Boolean) infoMapArr.get(position).get(
			"is_selected"));
	}
	return convertView;
    }

}