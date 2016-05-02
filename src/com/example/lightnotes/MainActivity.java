package com.example.lightnotes;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.FragementAdapter;
import com.example.db_service.OperationData;
import com.example.fragment.NoteAllLists;
import com.example.fragment.NoteTagLists;
import com.example.fragment.NoteTypeLists;
import com.example.tools.Constants;
import com.example.tools.ScreenUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;

public class MainActivity extends FragmentActivity {

    private int drawables[] = { R.drawable.viewpager_head1,
	    R.drawable.viewpager_head2, R.drawable.viewpager_head3 };
    private int selecteds[] = { R.drawable.selected_head1,
	    R.drawable.selected_head2, R.drawable.selected_head3 };
    private int ids[] = { R.id.textview1, R.id.textview2, R.id.textview3 };
    private int currentpager = 0;
    private long exitTime = 0;
    private ArrayList<Fragment> items;
    private ViewPager viewPager;
    private Context context;
    private TextView[] textView;
    private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case Constants.INIT_DATA_SUCCESS:
		if ((Boolean) msg.obj) {
		    T.showShort(context, "初始化成功");
		} else {
		    T.showShort(context, "初始化失败");
		}
		break;
	    case Constants.INIT_DATA_FAIL:
		T.showShort(context, "初始化异常");
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	context = getApplicationContext();
	ScreenUtils.getDisplay(context);
	initTextView();
	initViewPager();
	initData();
    }

    private void initData() {
	// TODO Auto-generated method stub
	SharedPreferences sharedPreferences = getSharedPreferences(
		"LightNotes", MODE_PRIVATE);
	boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
	if (isFirstIn) {
	    ThreadPool.getInstance().AddThread(new Runnable() {
		Message msg = Message.obtain();

		@Override
		public void run() {
		    // TODO Auto-generated method stub
		    try {
			OperationData operationData = new OperationData(context);
			ContentValues values = new ContentValues();
			values.put("NoteTypename", "默认");
			values.put("CreateTime", TimeUtils.DateToStr(
				new Date(), Constants.DATE_FORMAT));
			boolean flag = operationData.addData("tb_notetypes",
				values);
			msg.obj = flag;
			msg.what = Constants.INIT_DATA_SUCCESS;
		    } catch (Exception e) {
			// TODO: handle exception
			msg.what = Constants.INIT_DATA_FAIL;
		    }
		    handler.sendMessage(msg);

		}
	    });
	    Editor editor = sharedPreferences.edit();
	    editor.putBoolean("isFirstIn", false);
	    editor.commit();
	}
    }

    private void initTextView() {
	textView = new TextView[ids.length];
	for (int i = 0; i < ids.length; i++) {
	    textView[i] = (TextView) findViewById(ids[i]);
	    textView[i].setOnClickListener(new TextViewListener(i));
	}
	setTextViewBg(textView[0]);
    }

    public class TextViewListener implements OnClickListener {
	private int index = 0;

	public TextViewListener(int i) {
	    index = i;
	}

	@Override
	public void onClick(View v) {
	    viewPager.setCurrentItem(index);
	    currentpager = index;
	    setTextViewBg(v);
	}
    }

    private void initViewPager() {
	// TODO Auto-generated method stub
	viewPager = (ViewPager) findViewById(R.id.viewpager);
	items = new ArrayList<Fragment>();
	NoteAllLists noteAllLists = new NoteAllLists();
	NoteTypeLists noteTypeLists = new NoteTypeLists();
	NoteTagLists noteTagLists = new NoteTagLists();
	items.add(noteAllLists);
	items.add(noteTypeLists);
	items.add(noteTagLists);
	viewPager.setAdapter(new FragementAdapter(getSupportFragmentManager(),
		items));
	viewPager.setOffscreenPageLimit(3);
	Intent intent = getIntent();
	if (intent != null)
	    currentpager = intent.getIntExtra("currentpager", 0);
	setTextViewBg(textView[currentpager]);
	viewPager.setCurrentItem(currentpager);
	viewPager.setOnPageChangeListener(new OnPageChangeListener() {

	    @Override
	    public void onPageSelected(int arg0) {
		currentpager = arg0;
		invalidateOptionsMenu();// 更新actionbar上的menu样式
		setTextViewBg(textView[arg0]);
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }
	});
    }

    private void setTextViewBg(View view) {
	// TODO Auto-generated method stub
	for (int i = 0; i < textView.length; i++) {
	    textView[i].setBackgroundResource(drawables[i]);
	    if (view.equals(textView[i]))
		view.setBackgroundResource(selecteds[i]);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main, menu);
	// MenuItem shareItem = menu.findItem(R.id.action_share);
	// ShareActionProvider shareActionProvider = (ShareActionProvider)
	// shareItem
	// .getActionProvider();
	// Intent shareIntent = new Intent(Intent.ACTION_SEND);
	// shareIntent.setType("text/plain"); // 分享的数据类型
	// shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 主题
	// shareIntent.putExtra(Intent.EXTRA_TEXT,
	// "来自LightNote for Android  :我已记录许多重要信息, 详情请点击"); // 内容
	// shareActionProvider.setShareIntent(shareIntent);
	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	menu.clear();
	MenuInflater menuInflater = this.getMenuInflater();
	switch (currentpager) {
	case 0:
	    menuInflater.inflate(R.menu.create_note, menu);
	    MenuItem shareItem = menu.findItem(R.id.action_share);
	    ShareActionProvider shareActionProvider = (ShareActionProvider) shareItem
		    .getActionProvider();
	    Intent shareIntent = new Intent(Intent.ACTION_SEND);
	    shareIntent.setType("text/plain"); // 分享的数据类型
	    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享"); // 主题
	    shareIntent.putExtra(Intent.EXTRA_TEXT,
		    "来自LightNote for Android  :我已记录许多重要信息, 详情请点击"); // 内容
	    shareActionProvider.setShareIntent(shareIntent);
	    break;
	case 1:
	    menuInflater.inflate(R.menu.create_type, menu);
	    break;
	case 2:
	    menuInflater.inflate(R.menu.create_tag, menu);
	    break;
	default:
	    break;
	}
	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	Intent intent = new Intent();
	switch (item.getItemId()) {
	case R.id.action_create_note:
	    intent.setClass(MainActivity.this, CreateNoteActivity.class);
	    startActivity(intent);
	    break;
	case R.id.action_create_type:
	    intent.setClass(MainActivity.this, CreateTypeActivity.class);
	    startActivity(intent);
	    break;
	case R.id.action_create_tag:
	    intent.setClass(MainActivity.this, CreateTagActivity.class);
	    startActivity(intent);
	    break;
	case R.id.action_search:
	    intent.setClass(MainActivity.this, SearchActivity.class);
	    startActivity(intent);
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
    
    
//    /*
//     * (non-Javadoc)必须先调用此方法fragment的onActivityResult才有响应
//     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
//     */
//    @Override
//    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//	// TODO Auto-generated method stub
//	super.onActivityResult(arg0, arg1, arg2);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK
		&& event.getAction() == KeyEvent.ACTION_DOWN) {
	    if ((System.currentTimeMillis() - exitTime) > 2000) {
		Toast.makeText(getApplicationContext(), "再按一次退出程序",
			Toast.LENGTH_SHORT).show();
		exitTime = System.currentTimeMillis();
	    } else {
		finish();
		System.exit(0);
	    }
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }
}
