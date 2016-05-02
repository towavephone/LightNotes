package com.example.lightnotes;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.AllListViewAdapter;
import com.example.db_service.OperationData;
import com.example.tools.Constants;
import com.example.tools.ImageUtils;
import com.example.tools.ScreenUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;
import com.example.views.XListView;

public class SearchActivity extends Activity implements
	XListView.IXListViewListener {
    private Context context;
    XListView listView;
    TextView textView;
    SearchView searchView;
    private int size[];
    private ArrayList<HashMap<String, Object>> infoMapArr;
    private AllListViewAdapter adapter;
    String NoteTypeID;
    ArrayList<String> ids;
    private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
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
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_search);
	context = getApplicationContext();
	getActionBar().setDisplayHomeAsUpEnabled(true);
	NoteTypeID = getIntent().getStringExtra("NoteTypeID");
	ids = getIntent().getStringArrayListExtra("ids");
	init();
	addListener();
	loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	switch (item.getItemId()) {
	case android.R.id.home:
	    Intent intent = new Intent();
	    intent.setClass(SearchActivity.this, MainActivity.class);
	    if (NoteTypeID != null) {
		intent.putExtra("currentpager", 1);
	    } else if (ids != null) {
		intent.putExtra("currentpager", 2);
	    } else {
		intent.putExtra("currentpager", 0);
	    }
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
	    startActivity(intent);
	    break;

	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    public void updateLocalData(
	    ArrayList<HashMap<String, Object>> infoMapArr_Original) {
	// TODO Auto-generated method stub
	infoMapArr.clear();
	size = ScreenUtils.getZoomPictureSize();
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
	textView.setText("搜索结果(" + adapter.getCount() + ")");
	adapter.notifyDataSetChanged();
    }

    private void loadData() {
	// TODO Auto-generated method stub
	ThreadPool.getInstance().AddThread(new Runnable() {
	    Message message;
	    OperationData operationData = new OperationData(context);

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
		    ArrayList<HashMap<String, Object>> infoMapArr_Original;
		    if (NoteTypeID != null) {
			infoMapArr_Original = operationData.viewDatas(
				"tb_notes", "NoteTypeID=?",
				new String[] { NoteTypeID });
		    } else if (ids != null) {
			infoMapArr_Original = new ArrayList<HashMap<String, Object>>();
			for (String str : ids) {
			    HashMap<String, Object> hashMap = operationData
				    .viewData("tb_notes", "_id=?",
					    new String[] { str });
			    infoMapArr_Original.add(hashMap);
			}
		    } else {
			infoMapArr_Original = operationData.viewDatas(
				"tb_notes", null, null);
		    }
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

    private void addListener() {
	// TODO Auto-generated method stub
	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		// TODO Auto-generated method stub
		infoMapArr = adapter.getInfoMapArr();
		Intent intent = new Intent(context, ShowNoteActivity.class);
		intent.putExtra("search", "search");
		intent.putExtra("id", infoMapArr.get(arg2 - 1).get("_id") + "");
		if (NoteTypeID != null) {
		    intent.putExtra("NoteTypeID", NoteTypeID);
		}
		if (ids != null) {
		    intent.putExtra("ids", ids);
		}
		startActivityForResult(intent, 1);
	    }
	});
    }

    private void init() {
	// TODO Auto-generated method stub
	textView = (TextView) findViewById(R.id.hint_message);
	listView = (XListView) findViewById(R.id.note_list);
	listView.setPullRefreshEnable(true, true);
	listView.setXListViewListener(this);
	infoMapArr = new ArrayList<HashMap<String, Object>>();
	adapter = new AllListViewAdapter(context, infoMapArr, textView);
	listView.setTextFilterEnabled(true);
	listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.search, menu);
	searchView = (SearchView) menu.findItem(R.id.action_search)
		.getActionView();
	searchView.setNextFocusForwardId(R.id.note_list);// 这句话特别重要，没有这句话第一次点击listview时没效果
	searchView.setQueryHint("输入标题搜索");
	searchView.setOnQueryTextListener(new OnQueryTextListener() {
	    @Override
	    public boolean onQueryTextSubmit(String arg0) {
		if (arg0 != null && arg0.length() > 0) {
		    hideSoftInput();
		}
		return true;
	    }

	    @Override
	    public boolean onQueryTextChange(String arg0) {
		// textView.setText("搜索结果("+infoMapArr.size()+")");
		if (arg0 != null && arg0.length() > 0) {
		    listView.setFilterText(arg0);
		    listView.setPullRefreshEnable(false, false);
		} else {
		    listView.clearTextFilter();
		    listView.setPullRefreshEnable(true, true);
		}
		adapter.getFilter().filter(arg0);
		return true;
	    }
	});
	return true;
    }

    private void hideSoftInput() {
	InputMethodManager inputMethodManager;
	inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

	if (inputMethodManager != null) {
	    View v = this.getCurrentFocus();
	    if (v == null) {
		return;
	    }
	    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
		    InputMethodManager.HIDE_NOT_ALWAYS);
	    searchView.clearFocus();
	}
    }

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if (resultCode != RESULT_OK) {
	    return;
	}
	if (requestCode == 1) {
	    NoteTypeID = data.getStringExtra("NoteTypeID");
	    ids = data.getStringArrayListExtra("ids");
	    //loadData();
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK
		&& event.getAction() == KeyEvent.ACTION_DOWN) {
	    Intent intent = new Intent();
	    intent.setClass(SearchActivity.this, MainActivity.class);
	    if (NoteTypeID != null) {
		intent.putExtra("currentpager", 1);
	    } else if (ids != null) {
		intent.putExtra("currentpager", 2);
	    } else {
		intent.putExtra("currentpager", 0);
	    }
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
	    startActivity(intent);
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }
}
