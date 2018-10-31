package com.andev.androidshaderdemo.nativedemo;


import android.graphics.Bitmap;
import android.view.Surface;

public class NativeController {
	public native void init();

	public native void setSurface(Surface surface);

	public native void resetSize(int width, int height);

	public native void showBitmap(Bitmap bitmap);

	public native void stop();
}
