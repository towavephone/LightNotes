package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.lightnotes.R;
import com.example.lightnotes.ShowNoteActivity;
import com.example.tools.ImageUtils;
import com.example.tools.TimeUtils;
import com.example.views.XListView;

public class NoteAllLists extends BaseFragment implements
	XListView.IXListViewListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreateView(inflater, container, savedInstanceState);
	loadData("tb_notes", null);
	addListener();
	return view;
    }

    @Override
    public void updateLocalData(
	    ArrayList<HashMap<String, Object>> infoMapArr_Original) {
	// TODO Auto-generated method stub
	super.updateLocalData(infoMapArr_Original);
	for (HashMap<String, Object> map : infoMapArr_Original) {
	    HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    String time = map.get("CreateTime").toString();
	    int id = Integer.valueOf(map.get("_id") + "");
	    byte[] content = (byte[]) map.get("NoteContent");
	    String title = (String) map.get("NoteTitle");
	    String note_content;
	    switch (Integer.valueOf(map.get("NoteClassification") + "")) {
	    case 0:
		if (title.equals("")) {
		    title = time + " 文字";
		}
		note_content = TimeUtils.getTimestampString(TimeUtils
			.StrToDate(time)) + "  " + new String(content);
		hashMap.put("note_title", title);
		hashMap.put("note_content", note_content);
		hashMap.put("note_icon", ImageUtils.zoomDrawable(getResources()
			.getDrawable(R.drawable.notelist_thumbnail_note),
			size[0], size[1]));
		break;
	    case 1:
		if (title.equals("")) {
		    title = time + " 图片";
		}
		note_content = TimeUtils.getTimestampString(TimeUtils
			.StrToDate(time));
		hashMap.put("note_title", title);
		hashMap.put("note_content", note_content);
		hashMap.put("note_icon", ImageUtils.zoomDrawable(
			ImageUtils.byteToDrawable(content), size[0], size[1]));
		break;
	    default:
		break;
	    }
	    hashMap.put("CreateTime", time);
	    hashMap.put("_id", id);
	    infoMapArr.add(hashMap);
	}
	TimeUtils.compareTimeSort(infoMapArr);
	adapter.notifyDataSetChanged();
	textView.setText("全部笔记("+infoMapArr.size()+")");
    }

    public void addListener() {
	// TODO Auto-generated method stub
	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, ShowNoteActivity.class);
		intent.putExtra("id", infoMapArr.get(arg2 - 1).get("_id") + "");
		intent.putExtra("All", "All");
		startActivityForResult(intent, 1);
	    }
	});
    }

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub

    }
}
