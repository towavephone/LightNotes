package com.example.lightnotes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateTagActivity extends CreateTypeTagActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setText("��ǩ����");
        setTable_name("tb_notetags");
        setKey("NoteTagname");
        setCurrentpager(2);
    }

}
