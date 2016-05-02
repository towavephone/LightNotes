package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.adapter.AllListViewAdapter;
import com.example.db_service.OperationData;
import com.example.lightnotes.R;
import com.example.tools.Constants;
import com.example.tools.ImageUtils;
import com.example.tools.ScreenUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;
import com.example.views.XListView;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends Fragment implements
	XListView.IXListViewListener {
    protected XListView listView;
    protected Context context;
    protected TextView textView;
    protected View view;
    protected int size[];
    // protected ArrayList<HashMap<String, Object>> infoMapArr_Original;
    protected ArrayList<HashMap<String, Object>> infoMapArr;
    protected AllListViewAdapter adapter;
    protected ProgressBar progressBar;
    OperationData operationData;
    protected Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    progressBar.setVisibility(View.GONE);
	    switch (msg.what) {
	    case Constants.LOAD_LOCAL_DATA_SUCCESS:
		updateLocalData((ArrayList<HashMap<String, Object>>) msg.obj);
		break;
	    case Constants.LOAD_LOCAL_DATA_FAIL:
		T.show(context, "我的笔记页面本地数据加载失败", Toast.LENGTH_SHORT);
		break;
	    default:
		break;
	    }
	}

    };

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	view = inflater.inflate(R.layout.all_lists, null);
	context = getActivity();
	operationData = new OperationData(context);
	init();
	return view;
    }

    public void updateLocalData(
	    ArrayList<HashMap<String, Object>> infoMapArr_Original) {
	// TODO Auto-generated method stub
	infoMapArr.clear();
	size = ScreenUtils.getZoomPictureSize();
    };

    public void loadData(final String database_name, final String selection) {
	// TODO Auto-generated method stub
	progressBar.setVisibility(View.VISIBLE);
	ThreadPool.getInstance().AddThread(new Runnable() {
	    Message message;

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		message = Message.obtain();
		try {
		    // ContentValues values = new ContentValues();
		    // values.put("NoteTypename", 1);
		    // values.put("_id", "0");
		    // values.put("CreateTime", "2015年9月9日17:16:06");
		    // values.put("NoteContent", ImageUtils
		    // .drawableToByte(getResources().getDrawable(
		    // R.drawable.icon_settings)));
		    // values.put("CreateTime", "2015年9月7日16:56:00");
		    // operationData.addData("tb_notetypes", values);
		    ArrayList<HashMap<String, Object>> infoMapArr_Original = operationData
			    .viewDatas(database_name, selection, null);
		    message.obj = infoMapArr_Original;
		    message.what = Constants.LOAD_LOCAL_DATA_SUCCESS;
		    handler.sendMessage(message);
		} catch (Exception e) {
		    // TODO: handle exception
		    message.what = Constants.LOAD_LOCAL_DATA_FAIL;
		    handler.sendMessage(message);
		}

	    }
	});

    }

    public void init() {
	// TODO Auto-generated method stub
	textView = (TextView) view.findViewById(R.id.hint_message);
	progressBar = (ProgressBar) view.findViewById(R.id.loading_local_data);
	listView = (XListView) view.findViewById(R.id.note_list);
	listView.setPullRefreshEnable(true, true);
	listView.setXListViewListener(this);
	infoMapArr = new ArrayList<HashMap<String, Object>>();
	adapter = new AllListViewAdapter(context, infoMapArr);
	listView.setAdapter(adapter);
    }
}
