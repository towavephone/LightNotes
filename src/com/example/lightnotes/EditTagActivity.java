package com.example.lightnotes;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.adapter.EditListViewAdapter;
import com.example.db_service.OperationData;
import com.example.tools.Constants;
import com.example.tools.T;
import com.example.tools.ThreadPool;

public class EditTagActivity extends Activity {
    EditListViewAdapter adapter;
    ListView listView;
    ArrayList<HashMap<String, Object>> infoMapArr;
    Context context;
    ArrayList<String> realId; // 真正的ID
    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case Constants.LOAD_LOCAL_DATA_FAIL:
		T.showShort(context, "标签加载异常");
		break;
	    case Constants.LOAD_LOCAL_DATA_SUCCESS:
		// T.showShort(context, infoMapArr+"");
		ArrayList<HashMap<String, Object>> arrayMap_tbtype = (ArrayList<HashMap<String, Object>>) msg.obj;
		for (HashMap<String, Object> hashMap : arrayMap_tbtype) {
		    HashMap<String, Object> map = new HashMap<String, Object>();
		    if (realId != null && realId.contains(hashMap.get("_id"))) {
			map.put("is_checked", true);
		    } else {
			map.put("is_checked", false);
		    }
		    map.put("_id", hashMap.get("_id") + "");
		    map.put("name", hashMap.get("NoteTagname").toString());
		    // map.put("CreateTime", hashMap.get("CreateTime") + "");
		    infoMapArr.add(map);
		}
		// TimeUtils.compareTimeSort(infoMapArr);
		adapter.notifyDataSetChanged();
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_type_tag);
	context = getApplicationContext();
	initViews();
	addListener();
	loadData();
    }

    private void loadData() {
	// TODO Auto-generated method stub
	ThreadPool.getInstance().AddThread(new Runnable() {
	    Message msg = Message.obtain();
	    OperationData operationData = new OperationData(context);

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		try {
		    infoMapArr.clear();
		    ArrayList<HashMap<String, Object>> arrayMap_tbtype = operationData
			    .viewDatas("tb_notetags", null, null);
		    // HashMap<String, Object>hashMap= operationData
		    // .viewData("tb_notes", "_id=?", new String[]{});
		    // L.e(tmp+"");
		    msg.obj = arrayMap_tbtype;
		    msg.what = Constants.LOAD_LOCAL_DATA_SUCCESS;
		    handler.sendMessage(msg);
		} catch (Exception e) {
		    // TODO: handle exception
		    msg.what = Constants.LOAD_LOCAL_DATA_FAIL;
		    handler.sendMessage(msg);
		}

	    }
	});
    }

    private void addListener() {
	// TODO Auto-generated method stub
	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		// TODO Auto-generated method stub
		// T.showShort(context, ""+arg2);
		boolean flag = (Boolean) infoMapArr.get(arg2).get("is_checked");
		infoMapArr.get(arg2).put("is_checked", !flag);
		adapter.notifyDataSetChanged();
	    }
	});
    }

    private void initViews() {
	// TODO Auto-generated method stub
	realId = getIntent().getStringArrayListExtra("ids");
	//L.e(realId + "");
	listView = (ListView) findViewById(R.id.listView);
	infoMapArr = new ArrayList<HashMap<String, Object>>();
	adapter = new EditListViewAdapter(context, infoMapArr);
	adapter.setVisibleCheckBox(true);
	// listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.edit_tag, menu);
	return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if (resultCode != RESULT_OK) {
	    return;
	}
	if (requestCode == 1) {
	    realId = data.getStringArrayListExtra("ids");
	    loadData();
	    // adapter.notifyDataSetChanged();
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	Intent intent;
	ArrayList<String> ids;
	switch (item.getItemId()) {
	case R.id.action_create_tag:
	    intent = new Intent(context, CreateTagActivity.class);
	    ids = new ArrayList<String>();
	    for (HashMap<String, Object> map : infoMapArr) {
		if ((Boolean) map.get("is_checked")) {
		    ids.add(map.get("_id") + "");
		}
	    }
	    intent.putExtra("ids", ids);
	    startActivityForResult(intent, 1);
	    break;
	case R.id.action_finish:
	    intent = new Intent();
	    ids = new ArrayList<String>();
	    for (HashMap<String, Object> map : infoMapArr) {
		if ((Boolean) map.get("is_checked")) {
		    ids.add(map.get("_id") + "");
		}
	    }
	    //L.e(ids+"");
	    intent.putExtra("ids", ids);
	    setResult(Activity.RESULT_OK, intent);
	    finish();// 结束之后会将结果传回From
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
}
