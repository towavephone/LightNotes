package com.example.lightnotes;

import java.util.Date;
import java.util.HashMap;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Message;
import com.example.tools.Constants;
import com.example.tools.ImageUtils;
import com.example.tools.TimeUtils;

public class CreateNoteActivity extends CreateEditNoteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
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
	    boolean flag = operationData.addData("tb_notes", values);
	    boolean flag2 = true;
	    if (flag) {
		HashMap<String, Object> hashMap = operationData.viewData(
			"tb_notes", "CreateTime=?", new String[] { time });
		// L.e(NoteTagID+"");
		if (NoteTagID != null) {
		    for (String str : NoteTagID) {
			values = new ContentValues();
			values.put("NoteID", hashMap.get("_id") + "");
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
	    msg.what = Constants.SAVE_SUCCESS;
	    msg.obj = flag && flag2;
	} catch (Exception e) {
	    // TODO: handle exception
	    msg.what = Constants.SAVE_FAIL;
	    // L.e(e.toString());
	}
    }
}
