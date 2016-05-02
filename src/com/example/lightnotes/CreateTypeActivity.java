package com.example.lightnotes;

import android.os.Bundle;

public class CreateTypeActivity extends CreateTypeTagActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setText("∑÷¿‡√˚≥∆");
        setTable_name("tb_notetypes");
        setKey("NoteTypename");
        setCurrentpager(1);
    }
}
