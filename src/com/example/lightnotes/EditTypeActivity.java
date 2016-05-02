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

public class EditTypeActivity extends Activity {
    EditListViewAdapter adapter;
    ListView listView;
    ArrayList<HashMap<String, Object>> infoMapArr;
    Context context;
    int selectId = 0;// 适配器的ID
    String realId; // 真正的ID
    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case Constants.LOAD_LOCAL_DATA_FAIL:
		T.showShort(context, "分类加载异常");
		break;
	    case Constants.LOAD_LOCAL_DATA_SUCCESS:
		// T.showShort(context, infoMapArr+"");
		ArrayList<HashMap<String, Object>> arrayMap_tbtype = (ArrayList<HashMap<String, Object>>) msg.obj;
		for (HashMap<String, Object> hashMap : arrayMap_tbtype) {
		    HashMap<String, Object> map = new HashMap<String, Object>();
		    if (hashMap.get("_id").toString().equals(realId)) {
			map.put("is_selected", true);
			selectId = arrayMap_tbtype.indexOf(hashMap);
		    } else {
			map.put("is_selected", false);

		    }
		    map.put("_id", hashMap.get("_id") + "");
		    map.put("name", hashMap.get("NoteTypename").toString());
		    //map.put("CreateTime", hashMap.get("CreateTime") + "");
		    infoMapArr.add(map);
		}
		//TimeUtils.compareTimeSort(infoMapArr);
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
			    .viewDatas("tb_notetypes", null, null);
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
		if (arg2 != selectId) {
		    infoMapArr.get(selectId).put("is_selected", false);
		    infoMapArr.get(arg2).put("is_selected", true);
		    selectId = arg2;
		    adapter.notifyDataSetChanged();
		}
	    }
	});
    }

    private void initViews() {
	// TODO Auto-generated method stub
	realId = getIntent().getStringExtra("id");
	listView = (ListView) findViewById(R.id.listView);
	infoMapArr = new ArrayList<HashMap<String, Object>>();
	adapter = new EditListViewAdapter(context, infoMapArr);
	adapter.setVisibleRadioButton(true);
	// listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.edit_type, menu);
	return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if (resultCode != RESULT_OK) {
	    return;
	}
	if (requestCode == 1) {
	    realId = data.getStringExtra("id");
	    loadData();
	    // adapter.notifyDataSetChanged();
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	Intent intent;
	switch (item.getItemId()) {
	case R.id.action_create_type:
	    intent = new Intent(context, CreateTypeActivity.class);
	    intent.putExtra("id", infoMapArr.get(selectId).get("_id")
		    .toString());
	    startActivityForResult(intent, 1);
	    break;
	case R.id.action_finish:
	    intent = new Intent();
	    intent.putExtra("id", infoMapArr.get(selectId).get("_id")
		    .toString());
	    setResult(Activity.RESULT_OK, intent);
	    finish();// 结束之后会将结果传回From
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
}
