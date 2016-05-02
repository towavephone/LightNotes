package com.example.lightnotes;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db_service.OperationData;
import com.example.tools.Constants;
import com.example.tools.DialogDemo;
import com.example.tools.ImageUtils;
import com.example.tools.L;
import com.example.tools.ScreenUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;

public class ShowNoteActivity extends Activity {
    TextView titleTV, tagTV, contentTV;
    ImageView contentIV;
    HashMap<String, Object> hashMap;
    ProgressBar progressBar;
    Context context;
    Bitmap bitmap;
    int noteClass;
    String id;
    String All;
    String search;
    ArrayList<String> NoteTagname;
    String NoteTypeID;
    ArrayList<String> ids;
    int size_real[] = ScreenUtils.getRealPictureSize();
    private Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    progressBar.setVisibility(View.GONE);
	    switch (msg.what) {
	    case Constants.LOAD_LOCAL_DATA_SUCCESS:
		String time = hashMap.get("CreateTime").toString();
		String title = (String) hashMap.get("NoteTitle");
		if (title.equals("")) {
		    title += time;
		    if (noteClass == 0) {
			title += "文字";
		    }
		    if (noteClass == 1) {
			title += "图片";
		    }
		}
		titleTV.setText(title);
		String tagname = "";
		for (String str : NoteTagname) {
		    tagname += str + " ";
		}
		tagTV.setText(tagname);
		// T.showShort(context, hashMap + " ::::: " + tagname);
		byte[] content = (byte[]) hashMap.get("NoteContent");
		if (noteClass == 0) {
		    contentTV.setText(new String(content));
		}
		if (noteClass == 1) {
		    contentIV.setImageBitmap(bitmap);
		}

		break;
	    case Constants.LOAD_LOCAL_DATA_FAIL:
		T.showShort(context, "加载异常");
		break;
	    case Constants.DELETE_DATA_FAIL:
		T.showShort(context, "删除异常");
		break;
	    case Constants.DELETE_DATA_SUCCESS:
		// T.showShort(context, "加载异常");
		if ((Boolean) msg.obj) {
		    T.showShort(context, "删除成功");
		} else {
		    T.showShort(context, "删除失败");
		}
		Intent intent = new Intent();
		intent.setClass(ShowNoteActivity.this, MainActivity.class);
		intent.putExtra("currentpager", 0);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		startActivity(intent);
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_show_note);
	context = getApplicationContext();
	NoteTypeID = getIntent().getStringExtra("NoteTypeID");
	ids = getIntent().getStringArrayListExtra("ids");
	id = getIntent().getStringExtra("id");
	search = getIntent().getStringExtra("search");
	All = getIntent().getStringExtra("All");
	initViews();
	loadData();
    }

    private void loadData() {
	// TODO Auto-generated method stub
	progressBar.setVisibility(View.VISIBLE);
	ThreadPool.getInstance().AddThread(new Runnable() {
	    Message msg = Message.obtain();
	    OperationData operationData = new OperationData(context);

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		try {
		    hashMap = operationData.viewData("tb_notes", "_id=?",
			    new String[] { id });
		    noteClass = Integer.valueOf(hashMap
			    .get("NoteClassification") + "");
		    if (noteClass == 1) {
			bitmap = ImageUtils.zoomBitmap(
				BitmapFactory.decodeFile(hashMap
					.get("ImagePath") + ""), size_real[0],
				size_real[1]);
		    }
		    ArrayList<HashMap<String, Object>> infoMapArr_parent = operationData
			    .viewDatas("tb_note_tags", "NoteID=?",
				    new String[] { id });
		    ArrayList<String> arrayList = new ArrayList<String>();
		    NoteTagname = new ArrayList<String>();
		    for (HashMap<String, Object> map : infoMapArr_parent) {
			HashMap<String, Object> hashMap = operationData
				.viewData("tb_notetags", "_id=?",
					new String[] { map.get("NoteTagID")
						+ "" });
			NoteTagname.add(hashMap.get("NoteTagname") + "");
		    }
		    // L.e(NoteTagname + "");
		    msg.what = Constants.LOAD_LOCAL_DATA_SUCCESS;
		} catch (Exception e) {
		    // TODO: handle exception
		    msg.what = Constants.LOAD_LOCAL_DATA_FAIL;
		    // L.e(e.toString());
		}
		handler.sendMessage(msg);
	    }
	});
    }

    private void initViews() {
	// TODO Auto-generated method stub
	progressBar = (ProgressBar) findViewById(R.id.progressBar);
	titleTV = (TextView) findViewById(R.id.titleTV);
	tagTV = (TextView) findViewById(R.id.tagTV);
	contentTV = (TextView) findViewById(R.id.contentTV);
	contentIV = (ImageView) findViewById(R.id.contentIV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.show_note, menu);
	return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	if (resultCode != RESULT_OK) {
	    return;
	}
	if (requestCode == 1) {
	    id = data.getStringExtra("id");
	    search = data.getStringExtra("search");
	    All = data.getStringExtra("All");
	    NoteTypeID = data.getStringExtra("NoteTypeID");
	    ids = data.getStringArrayListExtra("ids");
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	// TODO Auto-generated method stub
	Intent intent;
	switch (item.getItemId()) {
	case R.id.action_editnote:
	    intent = new Intent(ShowNoteActivity.this, EditNoteActivity.class);
	    intent.putExtra("id", id);
	    if (search != null) {
		intent.putExtra("search", search);
	    }
	    if (All != null) {
		intent.putExtra("All", All);
	    }
	    if (NoteTypeID != null) {
		intent.putExtra("NoteTypeID", NoteTypeID);
	    }
	    if (ids != null) {
		intent.putExtra("ids", ids);
	    }
	    startActivityForResult(intent, 1);
	    break;
	case R.id.action_clearnote:
	    DialogDemo.builder(ShowNoteActivity.this, "确认删除？",
		    "此操作会删除该笔记以及该笔记所关联的所有标签！你确定要删除吗？",
		    new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    // TODO Auto-generated method stub
			    ThreadPool.getInstance().AddThread(new Runnable() {
				Message msg = Message.obtain();

				@Override
				public void run() {
				    // TODO Auto-generated method stub
				    try {
					OperationData operationData = new OperationData(
						context);
					boolean flag = operationData
						.deleteData("tb_notes",
							"_id=?",
							new String[] { id });
					operationData.deleteData(
						"tb_note_tags", "NoteID=?",
						new String[] { id });
					msg.obj = flag;
					msg.what = Constants.DELETE_DATA_SUCCESS;
				    } catch (Exception e) {
					// TODO: handle exception
					msg.what = Constants.DELETE_DATA_FAIL;
				    }
				    handler.sendMessage(msg);
				}
			    });
			}
		    });

	    break;
	default:
	    break;
	}
	return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK
		&& event.getAction() == KeyEvent.ACTION_DOWN) {
	    Intent intent = new Intent();
	    ;
	    if (search != null) {
		intent.setClass(ShowNoteActivity.this, SearchActivity.class);
		if (NoteTypeID != null) {
		    intent.putExtra("NoteTypeID", NoteTypeID);
		}
		if (ids != null) {
		    intent.putExtra("ids", ids);
		}
		setResult(Activity.RESULT_OK, intent);
		// finish();// 结束之后会将结果传回From
	    }
	    if (All != null) {
		intent.setClass(ShowNoteActivity.this, MainActivity.class);
		intent.putExtra("currentpager", 0);
	    }
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
	    startActivity(intent);
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }
}
