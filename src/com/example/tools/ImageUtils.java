package com.example.tools;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils {
    // ------------------------将drawable 图像转化成二进制字节----------------
    public static byte[] drawableToByte(Drawable drawable) {

	if (drawable != null) {
	    Bitmap bitmap = Bitmap
		    .createBitmap(
			    drawable.getIntrinsicWidth(),
			    drawable.getIntrinsicHeight(),
			    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				    : Bitmap.Config.RGB_565);
	    Canvas canvas = new Canvas(bitmap);
	    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		    drawable.getIntrinsicHeight());
	    drawable.draw(canvas);
	    int size = bitmap.getWidth() * bitmap.getHeight() * 4;
	    // 创建一个字节数组输出流,流的大小为size
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
	    // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    // 将字节数组输出流转化为字节数组byte[]
	    byte[] imagedata = baos.toByteArray();
	    return imagedata;
	}
	return null;
    }

    public static Drawable byteToDrawable(byte[] img) {
	Bitmap bitmap;
	if (img != null) {

	    bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
	    Drawable drawable = new BitmapDrawable(bitmap);

	    return drawable;
	}
	return null;

    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
	Bitmap bitmap = zoomBitmap(transformBitmap(drawable), w, h);
	return transformDrawable(bitmap);
    }

    public static Drawable transformDrawable(Bitmap bitmap) {
	BitmapDrawable bd = new BitmapDrawable(bitmap);
	return bd;
    }

    public static Bitmap transformBitmap(Drawable drawable) {
	BitmapDrawable bd = (BitmapDrawable) drawable;
	Bitmap bm = bd.getBitmap();
	return bm;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
	int width = bitmap.getWidth();
	int height = bitmap.getHeight();
	Matrix matrix = new Matrix();
	float scaleWidth = ((float) w / width);
	float scaleHeight = ((float) h / height);
	matrix.postScale(scaleWidth, scaleHeight);
	Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
		matrix, true);
	return newbmp;
    }

}
