package com.example.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;

public class DialogDemo {
    // ������Ϣ�Ի���,�������Լ�д�ķ��������Σ����ò���������ȥ����
    public static void builder(Context context, String title, String message) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(title);
	builder.setMessage(message);
	builder.setPositiveButton("ȷ��", null);
	builder.show();// ѧϰ����
    }
    /*
     * ���湦��ר��
     */
    public static void builder(final Context context, String title, String message,
	    final Button button) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(title);
	builder.setMessage(message);
	builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		button.performClick();
	    }
	});
	builder.setNegativeButton("ȡ��", null);
	builder.setNeutralButton("������", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		Activity activity = (Activity) context;
		activity.finish();
	    }
	});
	builder.show();// ѧϰ����
    }
    /*
     * ɾ������
     */
    public static void builder(Context context, String title, String message,
	    OnClickListener onClickListener) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(title);
	builder.setMessage(message);
	builder.setPositiveButton("ȷ��", onClickListener);
	builder.setNeutralButton("ȡ��",null);
	builder.show();// ѧϰ����
    }
    
    /*
     * ��ѡ�б���
     */
    public static void builder(Context context, String title, String[] items,
	    OnClickListener onClickListener) {
	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle(title);
	//builder.setMessage(message);
	builder.setItems(items, onClickListener);
	builder.show();// ѧϰ����
    }
   
};

