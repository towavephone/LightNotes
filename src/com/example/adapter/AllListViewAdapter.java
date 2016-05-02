package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightnotes.R;

final class ViewHolder {
    public ImageView note_icon;
    public TextView note_title;
    public TextView note_content;
}

public class AllListViewAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> infoMapArr;
    private Context context;
    private NameFilter filter;
    TextView textView;
    private ArrayList<Map<String, Object>> infoMapArr_original;
    public AllListViewAdapter(Context context,
	    ArrayList<HashMap<String, Object>> infoMapArr) {
	this.context = context;
	mInflater = LayoutInflater.from(context);
	this.infoMapArr = infoMapArr;
    }
    public AllListViewAdapter(Context context,
	    ArrayList<HashMap<String, Object>> infoMapArr,TextView textView) {
	this.context = context;
	this.textView=textView;
	mInflater = LayoutInflater.from(context);
	this.infoMapArr = infoMapArr;
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

	ViewHolder holder = null;
	if (convertView == null) {
	    holder = new ViewHolder();
	    convertView = mInflater.inflate(R.layout.listview_item, null);
	    holder.note_icon = (ImageView) convertView
		    .findViewById(R.id.note_icon);
	    holder.note_title = (TextView) convertView
		    .findViewById(R.id.note_title);
	    holder.note_content = (TextView) convertView
		    .findViewById(R.id.note_content);
	    convertView.setTag(holder);

	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	//
	holder.note_icon.setImageDrawable((Drawable) infoMapArr.get(position)
		.get("note_icon"));
	//
	holder.note_title.setText(infoMapArr.get(position).get("note_title")
		+ "");
	//
	holder.note_content.setText(infoMapArr.get(position)
		.get("note_content") + "");
	return convertView;
    }

    @Override
    public Filter getFilter() {
	// TODO Auto-generated method stub
	if (filter == null) {
	    filter = new NameFilter();
	}
	return filter;
    }

    public ArrayList<HashMap<String, Object>> getInfoMapArr() {
	return infoMapArr;
    }

    public void setInfoMapArr(ArrayList<HashMap<String, Object>> infoMapArr) {
	this.infoMapArr = infoMapArr;
    }

    public class NameFilter extends Filter {

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
	    // TODO Auto-generated method stub
	    constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    if (infoMapArr_original == null) {
		synchronized (this) {
		    infoMapArr_original = new ArrayList<Map<String, Object>>(
			    infoMapArr);
		}
	    }
	    if (constraint != null && constraint.toString().length() > 0) {
		ArrayList<Map<String, Object>> infoMapArr_search = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : infoMapArr_original) {
		    if (map.get("note_title").toString().toLowerCase()
			    .contains(constraint)) {
			infoMapArr_search.add(map);
		    }
		}
		result.count = infoMapArr_search.size();
		result.values = infoMapArr_search;
	    } else {
		synchronized (this) {
		    result.values = infoMapArr_original;
		    result.count = infoMapArr_original.size();
		}
	    }
	    return result;
	}
	
	@Override
	protected void publishResults(CharSequence constraint,
		FilterResults results) {
	    infoMapArr = (ArrayList<HashMap<String, Object>>) results.values;
	    if (results.count > 0) {
		notifyDataSetChanged();
		textView.setText("搜索结果("+results.count+")");
	    } else {
		notifyDataSetInvalidated();
		textView.setText("搜索结果");
	    }
	}

    }

}