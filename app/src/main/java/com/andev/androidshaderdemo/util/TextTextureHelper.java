package com.andev.androidshaderdemo.util;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.util.Log;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLUtils.texImage2D;

public class TextTextureHelper {
	private static final String TAG = "TextTextureHelper";

	public static Bitmap createBitmap(String text, int width, int height, int textSize){
		Bitmap bitmap;

		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		paint.setColor(Color.argb(255, 255, 255,255));

//		canvas.drawRect(0, 0, width, height, paint);
//		canvas.drawBitmap(bitmap, 0, 0, paint);


		paint.setShadowLayer(1, 0, 1, Color.DKGRAY);
		paint.setTextSize(textSize);
		paint.setTextAlign(Paint.Align.CENTER);

		canvas.drawText(text, width / 2,height/2 , paint);

		return bitmap;
	}

	public static int createTexture(String text, int width, int height, int textSize){
		Bitmap bitmap = createBitmap(text, width, height, textSize);

		if (bitmap == null) {
			Log.w(TAG, "TextTextureHelper createTexture fail.");
		}

		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);

		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
				GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_NEAREST);
		// Load the bitmap into the bound texture.
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();

		// Unbind from the texture.
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];
	}

}
