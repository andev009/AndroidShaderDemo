/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.andev.androidshaderdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {
	private static final String TAG = "TextureHelper";
	public static final int NO_TEXTURE = -1;
	public static final int NOT_INIT = -1;
	public static final int ON_DRAWN = 1;

	/**
	 * Loads a texture from a resource ID, returning the OpenGL ID for that
	 * texture. Returns 0 if the load failed.
	 *
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static int loadTexture(Context context, int resourceId) {
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not generate a new OpenGL texture object.");
			}
			return 0;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		// Read in the resource
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), resourceId, options);

		if (bitmap == null) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			}

			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		// Bind to the texture in OpenGL
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		// Set filtering: a default must be set, or the texture will be
		// black.
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		// Load the bitmap into the bound texture.
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		// Note: Following code may cause an error to be reported in the
		// ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
		// Failed to generate texture mipmap levels (error=3)
		// No OpenGL error will be encountered (glGetError() will return
		// 0). If this happens, just squash the source image to be
		// square. It will look the same because of texture coordinates,
		// and mipmap generation will work.

		glGenerateMipmap(GL_TEXTURE_2D);

		// Recycle the bitmap, since its data has been loaded into
		// OpenGL.
		bitmap.recycle();

		// Unbind from the texture.
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];
	}

	public static int loadLutTexture(Context context, int resourceId) {
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not generate a new OpenGL texture object.");
			}
			return 0;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		// Read in the resource
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), resourceId, options);

		if (bitmap == null) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			}

			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		// Bind to the texture in OpenGL
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		// Set filtering: a default must be set, or the texture will be
		// black.
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		// Load the bitmap into the bound texture.
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		// Note: Following code may cause an error to be reported in the
		// ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
		// Failed to generate texture mipmap levels (error=3)
		// No OpenGL error will be encountered (glGetError() will return
		// 0). If this happens, just squash the source image to be
		// square. It will look the same because of texture coordinates,
		// and mipmap generation will work.

		glGenerateMipmap(GL_TEXTURE_2D);

		// Recycle the bitmap, since its data has been loaded into
		// OpenGL.
		bitmap.recycle();

		// Unbind from the texture.
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];
	}

	public static int getExternalOESTextureID() {
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		return texture[0];
	}
}
