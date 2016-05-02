package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.lightnotes.CreateTagActivity;
import com.example.lightnotes.CreateTypeActivity;
import com.example.lightnotes.MainActivity;
import com.example.lightnotes.R;
import com.example.lightnotes.SearchActivity;
import com.example.lightnotes.ShowNoteActivity;
import com.example.tools.Constants;
import com.example.tools.DialogDemo;
import com.example.tools.ImageUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;
import com.example.views.XListView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class NoteTagLists extends BaseFragment implements
	XListView.IXListViewListener {
    private Handler deleteHandler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case Constants.DELETE_DATA_SUCCESS:
		//adapter.notifyDataSetChanged();
		T.showShort(context, "删除成功");
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		intent.putExtra("currentpager", 2);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		startActivity(intent);
		//textView.setText("全部标签(" + infoMapArr.size() + ")");
		break;
	    case Constants.DELETE_DATA_FAIL:
		T.showShort(context, "删除异常");
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreateView(inflater, container, savedInstanceState);
	loadData("tb_notetags", null);
	addListener();
	return view;
    }

    private void addListener() {
	// TODO Auto-generated method stub
	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, SearchActivity.class);
		ArrayList<HashMap<String, Object>> arrayMaps = operationData
			.viewDatas(
				"tb_note_tags",
				"NoteTagID=?",
				new String[] { infoMapArr.get(arg2 - 1).get(
					"id")
					+ "" });
		ArrayList<String> ids = new ArrayList<String>();
		if (arrayMaps != null) {
		    for (HashMap<String, Object> hashMap : arrayMaps) {
			ids.add(hashMap.get("NoteID") + "");
		    }
		}
		intent.putExtra("ids", ids);
		startActivity(intent);
	    }
	});
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
	    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		    final int arg2, long arg3) {
		// TODO Auto-generated method stub
		String[] items = new String[] { "重命名", "删除" };
		final String note_title = infoMapArr.get(arg2 - 1).get(
			"note_title")
			+ "";
		DialogDemo.builder(context, "我的标签:" + note_title, items,
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:
				    Intent intent = new Intent(context,
					    CreateTagActivity.class);
				    intent.putExtra("tagID",
					    infoMapArr.get(arg2 - 1).get("id")
						    + "");
				    startActivity(intent);
				    break;
				case 1:
				    DialogDemo
					    .builder(
						    context,
						    note_title,
						    "该操作会去掉所有笔记带有的该标签！你确定要继续吗？",
						    new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
								DialogInterface arg0,
								int arg1) {
							    ThreadPool
								    .getInstance()
								    .AddThread(
									    new Runnable() {
										Message msg = Message
											.obtain();

										@Override
										public void run() {
										    // TODO
										    // Auto-generated
										    // method
										    // stub
										    try {
											operationData
												.deleteData(
													"tb_notetags",
													"_id=?",
													new String[] { infoMapArr
														.get(arg2 - 1)
														.get("id")
														+ "" });
											operationData
												.deleteData(
													"tb_note_tags",
													"NoteTagID=?",
													new String[] { infoMapArr
														.get(arg2 - 1)
														.get("id")
														+ "" });
										
											msg.what = Constants.DELETE_DATA_SUCCESS;
										    } catch (Exception e) {
											// TODO:
											// handle
											// exception
											msg.what = Constants.DELETE_DATA_FAIL;
										    }
										    deleteHandler
											    .sendMessage(msg);
										}
									    });
							}
						    });
				    break;
				default:
				    break;
				}
			    }
			});
		return true;
	    }
	});
    }

    @Override
    public void updateLocalData(
	    ArrayList<HashMap<String, Object>> infoMapArr_Original) {
	// TODO Auto-generated method stub
	super.updateLocalData(infoMapArr_Original);
	for (HashMap<String, Object> map : infoMapArr_Original) {
	    HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    String time = map.get("CreateTime").toString();
	    String title = (String) map.get("NoteTagname");
	    String id = map.get("_id") + "";
	    hashMap.put("note_title", title);
	    int count = operationData.viewDatas("tb_note_tags", "NoteTagID=?",
		    new String[] { id }).size();
	    hashMap.put("note_content",
		    TimeUtils.getTimestampString(TimeUtils.StrToDate(time))
			    + "  共有" + count + "篇笔记使用该标签");
	    hashMap.put("note_icon", ImageUtils.zoomDrawable(getResources()
		    .getDrawable(R.drawable.notelist_thumbnail_att), size[0],
		    size[1]));
	    hashMap.put("CreateTime", time);
	    hashMap.put("id", id);
	    infoMapArr.add(hashMap);
	}
	TimeUtils.compareTimeSort(infoMapArr);
	adapter.notifyDataSetChanged();
	textView.setText("全部标签(" + infoMapArr.size() + ")");
    }

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub
	super.onRefresh();
    }

}
