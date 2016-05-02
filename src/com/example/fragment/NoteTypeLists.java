package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.adapter.AllListViewAdapter;
import com.example.db_service.OperationData;
import com.example.lightnotes.CreateEditNoteActivity;
import com.example.lightnotes.CreateTypeActivity;
import com.example.lightnotes.MainActivity;
import com.example.lightnotes.R;
import com.example.lightnotes.SearchActivity;
import com.example.tools.Constants;
import com.example.tools.DialogDemo;
import com.example.tools.ImageUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;
import com.example.views.XListView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ProgressBar;

public class NoteTypeLists extends BaseFragment implements
	XListView.IXListViewListener {
    private Handler deleteHandler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case Constants.DELETE_DATA_SUCCESS:
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		intent.putExtra("currentpager", 1);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		startActivity(intent);
		T.showShort(context, "删除成功");
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
	loadData("tb_notetypes", null);
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
		intent.putExtra("NoteTypeID", infoMapArr.get(arg2 - 1)
			.get("id") + "");
		startActivity(intent);
	    }
	});
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
	    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		    final int arg2, long arg3) {
		// TODO Auto-generated method stub
		String[] items = new String[] { "重命名", "删除" };
		if (infoMapArr.get(arg2-1).get("id").toString().equals("1")) {
		    items=new String[]{ "重命名" };
		}
		final String note_title = infoMapArr.get(arg2 - 1).get(
			"note_title")
			+ "";
		DialogDemo.builder(context, "我的分类：" + note_title, items,
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:
				    Intent intent = new Intent(context,
					    CreateTypeActivity.class);
				    intent.putExtra("typeID",
					    infoMapArr.get(arg2 - 1).get("id")
						    + "");
				    startActivity(intent);
				    break;
				case 1:
				    DialogDemo
					    .builder(
						    context,
						    note_title,
						    "该操作会把该分类和该分类下的所有笔记删除！你确定要继续吗？",
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
													"tb_notes",
													"NoteTypeID=?",
													new String[] { infoMapArr
														.get(arg2 - 1)
														.get("id")
														+ "" });
											operationData
												.deleteData(
													"tb_notetypes",
													"_id=?",
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
	    String title = (String) map.get("NoteTypename");
	    String id = map.get("_id") + "";
	    hashMap.put("note_title", title);
	    int count = operationData.viewDatas("tb_notes", "NoteTypeID=?",
		    new String[] { id }).size();
	    hashMap.put("note_content",
		    TimeUtils.getTimestampString(TimeUtils.StrToDate(time))
			    + "  " + count + "篇笔记");
	    hashMap.put("note_icon", ImageUtils.zoomDrawable(getResources()
		    .getDrawable(R.drawable.folderlistitem_cat), size[0],
		    size[1]));
	    hashMap.put("CreateTime", time);
	    hashMap.put("id", id);
	    infoMapArr.add(hashMap);
	}
	TimeUtils.compareTimeSort(infoMapArr);
	adapter.notifyDataSetChanged();
	textView.setText("全部分类(" + infoMapArr.size() + ")");
    }

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub

    }
}
