package com.example.tools;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {
    public static int screenWidth;
    public static int screenHeight;
    public static Display dm;

    @SuppressWarnings("deprecation")
    public static void getDisplay(Context context) {
	// screenHeight = dm.heightPixels;
	// screenWidth = dm.widthPixels;
	WindowManager wm = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
	dm = wm.getDefaultDisplay();
	screenHeight = dm.getHeight();
	screenWidth = dm.getWidth();
    }

    /**
     * ͨ����Ļ��С����ͼƬ�Ĵ�С
     * 
     * @return
     */
    public static int[] getZoomPictureSize() {
	int[] size = new int[2];
	size[0] = screenWidth / 3;
	size[1] = screenHeight / 5;
	return size;
    }
    
    public static int[] getRealPictureSize() {
	int[] size = new int[2];
	size[0] = screenWidth ;
	size[1] = screenHeight;
	return size;
    }
}
