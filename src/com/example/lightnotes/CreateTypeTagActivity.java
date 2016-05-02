package com.example.lightnotes;

import java.util.Date;
import java.util.HashMap;

import com.example.db_service.OperationData;
import com.example.fragment.BaseFragment;
import com.example.tools.Constants;
import com.example.tools.DialogDemo;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTypeTagActivity extends Activity {

    protected TextView textView;
    protected EditText editText;
    protected Context context;
    protected String table_name;
    protected String key;
    protected int currentpager;
    protected OperationData operationData;
    protected String tagID;
    protected String typeID;
    protected Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case Constants.SAVE_SUCCESS:
		if ((Boolean) msg.obj) {
		    T.show(context, "添加成功", Toast.LENGTH_SHORT);
		} else {
		    T.show(context, "添加失败", Toast.LENGTH_SHORT);
		}
		Intent intent;
		if (getIntent().getStringExtra("id") != null) {
		    intent = new Intent();
		    intent.putExtra("id", getIntent().getStringExtra("id"));
		    setResult(Activity.RESULT_OK, intent);
		    finish();// 结束之后会将结果传回From
		} else if (getIntent().getStringArrayListExtra("ids") != null) {
		    intent = new Intent();
		    intent.putExtra("ids",
			    getIntent().getStringArrayListExtra("ids"));
		    setResult(Activity.RESULT_OK, intent);
		    finish();// 结束之后会将结果传回From
		} else {
		    intent = new Intent(CreateTypeTagActivity.this,
			    MainActivity.class);
		    intent.putExtra("currentpager", currentpager);
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		    startActivity(intent);
		}
		break;
	    case Constants.SAVE_FAIL:
		T.showShort(context, "添加或修改异常");
		break;
	    case Constants.UPDATE_SUCCESS:
		if ((Boolean) msg.obj) {
		    T.show(context, "修改成功", Toast.LENGTH_SHORT);
		} else {
		    T.show(context, "修改失败", Toast.LENGTH_SHORT);
		}
		intent = new Intent(CreateTypeTagActivity.this,
			MainActivity.class);
		intent.putExtra("currentpager", currentpager);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		startActivity(intent);
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_create_type_tag);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	context = getApplicationContext();
	operationData = new OperationData(context);
	tagID = getIntent().getStringExtra("tagID");
	typeID = getIntent().getStringExtra("typeID");
	textView = (TextView) findViewById(R.id.titleTV);
	editText = (EditText) findViewById(R.id.contentET);
	loadData();
    }

    private void loadData() {
	// TODO Auto-generated method stub
	if (typeID != null) {
	    HashMap<String, Object> hashMap = operationData.viewData(
		    "tb_notetypes", "_id=?", new String[] { typeID });
	    editText.setText(hashMap.get("NoteTypename") + "");
	} else if (tagID != null) {
	    HashMap<String, Object> hashMap = operationData.viewData(
		    "tb_notetags", "_id=?", new String[] { tagID });
	    editText.setText(hashMap.get("NoteTagname") + "");
	}
    }

    public void setText(String title) {
	textView.setText(title);
    }

    public void setTable_name(String table_name) {
	this.table_name = table_name;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public void setCurrentpager(int currentpager) {
	this.currentpager = currentpager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.create_type_tag, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
	    finish();
	    break;
	case R.id.action_create_type_tag:
	    final String name = editText.getText().toString();
	    int length = name.length();
	    if (length == 0 || length > 100) {
		DialogDemo.builder(CreateTypeTagActivity.this, "轻笔记",
			"你输入的名称不符合要求。要求长度不超过100");
		// 不能传入context,要传入this
	    } else {
		ThreadPool.getInstance().AddThread(new Runnable() {
		    Message msg = Message.obtain();

		    @Override
		    public void run() {
			// TODO Auto-generated method stub
			try {
			    boolean flag;
			    ContentValues values = new ContentValues();
			    values.put(key, name);
			    values.put("CreateTime", TimeUtils.DateToStr(
				    new Date(), Constants.DATE_FORMAT));
			    if (typeID != null) {
				flag = operationData.updateData("tb_notetypes",
					values, "_id=?", new String[] { typeID });
				msg.what = Constants.UPDATE_SUCCESS;
			    } else if (tagID != null) {
				flag = operationData.updateData("tb_notetags",
					values, "_id=?", new String[] { tagID });
				msg.what = Constants.UPDATE_SUCCESS;
			    } else {
				flag = operationData
					.addData(table_name, values);
				msg.what = Constants.SAVE_SUCCESS;
			    }
			    msg.obj = flag;
			} catch (Exception e) {
			    // TODO: handle exception
			    msg.what = Constants.SAVE_FAIL;
			    // handler.sendMessage(msg);
			}
			handler.sendMessage(msg);
		    }
		});
	    }
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

}
