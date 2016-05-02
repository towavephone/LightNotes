package com.example.lightnotes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.example.tools.Constants;
import com.example.tools.ImageUtils;
import com.example.tools.T;
import com.example.tools.ThreadPool;
import com.example.tools.TimeUtils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class EditNoteActivity extends CreateEditNoteActivity {
    ArrayList<HashMap<String, Object>> infoMapArr;
    HashMap<String, Object> note_item;
    Bitmap bitmap;
    String NoteID;
    private Handler load_handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case Constants.LOAD_LOCAL_DATA_SUCCESS:
		classificationSp.setSelection(Integer.valueOf(note_item
			.get("NoteClassification") + ""));
		NoteTypeID = Integer.valueOf(note_item.get("NoteTypeID") + "");
		titleET.setText(note_item.get("NoteTitle") + "");
		imagePath = note_item.get("ImagePath") + "";
		byte[] content = (byte[]) note_item.get("NoteContent");
		switch (Integer.valueOf(note_item.get("NoteClassification")
			+ "")) {
		case 0:
		    contentET.setText(new String(content));
		    break;
		case 1:
		    contentIV.setImageBitmap(bitmap);
		    break;
		default:
		    break;
		}
		NoteTagID = new ArrayList<String>();
		if (infoMapArr != null) {
		    for (HashMap<String, Object> map : infoMapArr) {
			NoteTagID.add(map.get("NoteTagID") + "");
		    }
		}
		break;
	    case Constants.LOAD_LOCAL_DATA_FAIL:
		T.showShort(context, "º”‘ÿ“Ï≥£");
		break;
	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	loadData();
    }

    private void loadData() {
	// TODO Auto-generated method stub
	NoteID = getIntent().getStringExtra("id");
	ThreadPool.getInstance().AddThread(new Runnable() {
	    Message msg = Message.obtain();
	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		try {
		    note_item = operationData.viewData("tb_notes", "_id=?",
			    new String[] { NoteID });
		    infoMapArr = operationData.viewDatas("tb_note_tags",
			    "NoteID=?", new String[] { NoteID });
		    if (Integer.valueOf(note_item.get("NoteClassification")
			    + "") == 1) {
			bitmap = ImageUtils.zoomBitmap(
				BitmapFactory.decodeFile(note_item
					.get("ImagePath") + ""), size_real[0],
				size_real[1]);
		    }
		    msg.what=Constants.LOAD_LOCAL_DATA_SUCCESS;
		} catch (Exception e) {
		    // TODO: handle exception
		    msg.what=Constants.LOAD_LOCAL_DATA_FAIL;
		}
		load_handler.sendMessage(msg);
	    }
	});

    }

    @Override
    public void operationNote(Message msg) {
	// TODO Auto-generated method stub
	super.operationNote(msg);
	try {
	    String time = TimeUtils
		    .DateToStr(new Date(), Constants.DATE_FORMAT);
	    ContentValues values = new ContentValues();
	    values.put("NoteClassification", selectedId);
	    values.put("NoteTypeID", NoteTypeID + "");
	    values.put("NoteTitle", titleET.getText().toString());
	    values.put("CreateTime", time);
	    values.put("ImagePath", imagePath);
	    if (selectedId == 0) {
		values.put("NoteContent", contentET.getText().toString()
			.getBytes());
	    }
	    if (selectedId == 1) {
		values.put("NoteContent", ImageUtils.drawableToByte(ImageUtils
			.zoomDrawable(contentIV.getDrawable(), size_zoom[0],
				size_zoom[1])));
	    }
	    boolean flag = operationData.updateData("tb_notes", values,
		    "_id=?", new String[] { NoteID });
	    boolean flag2 = true;
	    if (flag) {
		if (infoMapArr != null) {
		    for (HashMap<String, Object> map : infoMapArr) {
			operationData.deleteData("tb_note_tags", "NoteID=?",
				new String[] { NoteID });
		    }
		}
		if (NoteTagID != null) {
		    for (String str : NoteTagID) {
			values = new ContentValues();
			values.put("NoteID", NoteID);
			values.put("NoteTagID", str);
			boolean flag3 = operationData.addData("tb_note_tags",
				values);
			if (!flag3) {
			    flag2 = false;
			}
		    }
		}
		// L.e(operationData.viewDatas("tb_note_tags", null,
		// null)+"");
	    }
	    msg.what = Constants.UPDATE_SUCCESS;
	    msg.obj = flag && flag2;
	} catch (Exception e) {
	    // TODO: handle exception
	    msg.what = Constants.UPDATE_FAIL;
	    // L.e(e.toString());
	}
    }
}
