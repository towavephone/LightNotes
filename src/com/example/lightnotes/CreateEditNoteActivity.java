package com.example.lightnotes;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.db_service.OperationData;
import com.example.tools.Constants;
import com.example.tools.DialogDemo;
import com.example.tools.ImageUtils;
import com.example.tools.ScreenUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;

public class CreateEditNoteActivity extends Activity implements
	OnClickListener, OnMenuItemClickListener {
    protected Button buttons[];
    protected String imagePath;
    protected int btnIds[] = { R.id.transformBtn, R.id.photographBtn,
	    R.id.addpictureBtn, R.id.otherBtn, R.id.saveBtn };
    protected EditText titleET, contentET;
    protected ImageView contentIV;
    protected ScrollView scrollView_text, scrollView_image;
    protected Spinner classificationSp;
    protected Context context;
    protected File phoneFile;
    protected int NoteTypeID = 1;
    protected int selectedId;
    protected ArrayList<String> NoteTagID;
    protected OperationData operationData;
    String All;
    String search;
    String id;
    String NoteTypeID_Edit;
    ArrayList<String> ids;
    protected int size_zoom[] = ScreenUtils.getZoomPictureSize();
    protected int size_real[] = ScreenUtils.getRealPictureSize();
    protected Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    Intent intent;
	    switch (msg.what) {
	    case Constants.SAVE_SUCCESS:
		if ((Boolean) msg.obj) {
		    T.show(context, "��ӱʼǳɹ�", Toast.LENGTH_SHORT);
		} else {
		    T.show(context, "��ӱʼ�ʧ��", Toast.LENGTH_SHORT);
		}
		intent = new Intent();
		intent.setClass(CreateEditNoteActivity.this, MainActivity.class);
		intent.putExtra("currentpager", 0);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ע�Ȿ�е�FLAG����
		startActivity(intent);
		break;
	    case Constants.SAVE_FAIL:
		T.showShort(context, "��ӱʼ��쳣");
		break;
	    case Constants.UPDATE_SUCCESS:
		if ((Boolean) msg.obj) {
		    T.show(context, "�޸ıʼǳɹ�", Toast.LENGTH_SHORT);
		} else {
		    T.show(context, "�޸ıʼ�ʧ��", Toast.LENGTH_SHORT);
		}
		intent = new Intent(CreateEditNoteActivity.this,
			ShowNoteActivity.class);
		if (NoteTypeID_Edit != null) {
		    intent.putExtra("NoteTypeID", NoteTypeID_Edit);
		}
		if (ids != null) {
		    intent.putExtra("ids", ids);
		}
		if (id != null) {
		    intent.putExtra("id", id);
		}
		if (All != null) {
		    intent.putExtra("All", All);
		}
		if (search != null) {
		    intent.putExtra("search", search);
		}
		setResult(Activity.RESULT_OK, intent);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ע�Ȿ�е�FLAG����
		// finish();// ����֮��Ὣ�������From
		startActivity(intent);
		break;
	    case Constants.UPDATE_FAIL:
		T.showShort(context, "�޸ıʼ��쳣");
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_create_note);
	context = getApplicationContext();
	operationData = new OperationData(context);
	ids = getIntent().getStringArrayListExtra("ids");
	id = getIntent().getStringExtra("id");
	search = getIntent().getStringExtra("search");
	All = getIntent().getStringExtra("All");
	NoteTypeID_Edit = getIntent().getStringExtra("NoteTypeID");
	initViews();
	addListener();
    }

    protected void addListener() {
	// TODO Auto-generated method stub
	classificationSp
		.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> arg0, View arg1,
			    int arg2, long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case 0:
			    setViewVisible(scrollView_text, true);
			    setViewVisible(scrollView_image, false);
			    switchButtonVisible(true);

			    break;
			case 1:
			    setViewVisible(scrollView_text, false);
			    setViewVisible(scrollView_image, true);
			    switchButtonVisible(false);
			    break;
			default:
			    break;
			}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		    }
		});
	contentIV.setOnLongClickListener(new OnLongClickListener() {

	    @Override
	    public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		showPopup(titleET, R.menu.image_popmenu);
		return true;
	    }
	});
    }

    /*
     * 0~index��ť�ɼ���index+1~m-1(mΪ�仯��button�ɼ��Ե�����)���ɼ�
     */
    protected void switchButtonVisible(boolean flag) {
	// TODO Auto-generated method stub
	for (int i = 0; i < 3; i++) {
	    if (i == 0) {
		setViewVisible(buttons[i], flag);
	    } else {
		setViewVisible(buttons[i], !flag);
	    }
	}
    }

    protected void setViewVisible(View view, boolean flag) {
	view.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    protected void initViews() {
	// TODO Auto-generated method stub
	buttons = new Button[btnIds.length];
	for (int i = 0; i < btnIds.length; i++) {
	    buttons[i] = (Button) findViewById(btnIds[i]);
	    buttons[i].setOnClickListener(this);
	}
	titleET = (EditText) findViewById(R.id.titleET);
	contentET = (EditText) findViewById(R.id.contentET);
	contentIV = (ImageView) findViewById(R.id.contentIV);
	scrollView_text = (ScrollView) findViewById(R.id.scrollview);
	scrollView_image = (ScrollView) findViewById(R.id.scrollview1);
	classificationSp = (Spinner) findViewById(R.id.classificationSp);
    }

    public void showPopup(View v, int menu) {
	PopupMenu popup = new PopupMenu(this, v);
	popup.inflate(menu);
	popup.setOnMenuItemClickListener(this);
	popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.create_note, menu);
	return true;
    }

    public void operationNote(Message msg) {

    }

    @Override
    public void onClick(View arg0) {
	// TODO Auto-generated method stub
	Intent intent;
	switch (arg0.getId()) {
	case R.id.transformBtn:

	    break;
	case R.id.photographBtn:
	    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    phoneFile = new File(Environment.getExternalStorageDirectory()
		    .getAbsoluteFile()
		    + "/"
		    + TimeUtils.DateToStr(new Date(), Constants.DATE_FORMAT)
		    + ".jpg");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
	    startActivityForResult(intent, 1);
	    break;
	case R.id.addpictureBtn:
	    intent = new Intent(Intent.ACTION_GET_CONTENT);
	    intent.setType("image/*");
	    startActivityForResult(intent, 2);
	    break;
	case R.id.otherBtn:
	    showPopup(buttons[3], R.menu.others_popmenu);
	    break;
	case R.id.saveBtn:
	    selectedId = (int) classificationSp.getSelectedItemId();
	    if (contentIV.getDrawable() == null) {
		selectedId = 0;
	    }
	    ThreadPool.getInstance().AddThread(new Runnable() {
		Message msg = Message.obtain();

		@Override
		public void run() {
		    // TODO Auto-generated method stub
		    operationNote(msg);
		    handler.sendMessage(msg);
		}
	    });

	    break;
	default:
	    break;
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	// result_ok�ж��Ƿ񷵻���Ч����
	if (resultCode != RESULT_OK) {
	    return;
	}
	if (requestCode == 1) {
	    imagePath = phoneFile.getAbsolutePath();
	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
	    contentIV.setImageBitmap(ImageUtils.zoomBitmap(bitmap,
		    size_real[0], size_real[1]));
	}
	if (requestCode == 2) {
	    Uri mImageCaptureUri = data.getData();
	    // ���ص�Uri��Ϊ��ʱ����ôͼƬ��Ϣ���ݶ�����Uri�л�á����Ϊ�գ���ô���Ǿͽ�������ķ�ʽ��ȡ
	    if (data != null) {
		if (mImageCaptureUri != null) {
		    Bitmap image;
		    try {
			// ��������Ǹ���Uri��ȡBitmapͼƬ�ľ�̬����
			image = MediaStore.Images.Media.getBitmap(
				this.getContentResolver(), mImageCaptureUri);
			if (image != null) {
			    contentIV.setImageBitmap(ImageUtils.zoomBitmap(
				    image, size_real[0], size_real[1]));

			}
			// ���￪ʼ�ĵڶ����֣���ȡͼƬ��·����
			String[] proj = { MediaStore.Images.Media.DATA };
			// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
			Cursor cursor = managedQuery(mImageCaptureUri, proj,
				null, null, null);
			// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
			// int column_index =
			// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
			// cursor.moveToFirst();
			// ����������ֵ��ȡͼƬ·��
			// imagePath= cursor.getString(column_index);
			if (cursor != null) {
			    int column_index = cursor
				    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			    }
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    }

	}
	// T.showShort(context, "����·��Ϊ:" + imagePath);
	if (requestCode == 3) {
	    T.showShort(context, "���ķ���ɹ�");
	    String id = data.getStringExtra("id");
	    NoteTypeID = Integer.valueOf(id);
	}
	if (requestCode == 4) {
	    T.showShort(context, "���ı�ǩ�ɹ�");
	    NoteTagID = data.getStringArrayListExtra("ids");
	    // L.e(NoteTagID+"");
	}
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
	Intent intent;
	switch (item.getItemId()) {
	case R.id.action_create_type:
	    intent = new Intent(CreateEditNoteActivity.this,
		    EditTypeActivity.class);
	    intent.putExtra("id", NoteTypeID + "");
	    startActivityForResult(intent, 3);
	    break;
	case R.id.action_create_tag:
	    // T.showShort(context, "1");
	    intent = new Intent(CreateEditNoteActivity.this,
		    EditTagActivity.class);
	    intent.putExtra("ids", NoteTagID);
	    startActivityForResult(intent, 4);
	    break;
	default:
	    break;
	}
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK
		&& event.getAction() == KeyEvent.ACTION_DOWN) {
	    selectedId = (int) classificationSp.getSelectedItemId();
	    if (contentIV.getDrawable() == null) {
		selectedId = 0;
	    }
	    switch (selectedId) {
	    case 0:
		if (!titleET.getText().toString().equals("")
			|| !contentET.getText().toString().equals("")) {
		    DialogDemo.builder(CreateEditNoteActivity.this, "���ͣ�����",
			    "�Ƿ񱣴�Ըñʼǵı༭��", buttons[4]);
		}
		break;
	    case 1:
		if (!titleET.getText().toString().equals("")
			|| contentIV.getDrawable() != null) {
		    DialogDemo.builder(CreateEditNoteActivity.this, "���ͣ�ͼƬ",
			    "�Ƿ񱣴�Ըñʼǵı༭��", buttons[4]);
		}
		break;
	    default:
		break;
	    }
	}
	return super.onKeyDown(keyCode, event);
    }
}
