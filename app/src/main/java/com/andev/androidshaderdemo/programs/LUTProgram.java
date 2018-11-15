package com.andev.androidshaderdemo.programs;

import android.content.Context;

import com.andev.androidshaderdemo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

public class LUTProgram extends ShaderProgram{
	private final int []uTextureUnitLocation;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	public LUTProgram(Context context) {
		super(context, R.raw.multi_texture_vertex_shader, R.raw.lut_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		uTextureUnitLocation = new int[2];

		for(int i = 0; i < uTextureUnitLocation.length; i++){
			uTextureUnitLocation[i] = glGetUniformLocation(program, U_TEXTURE_UNIT + i);
		}
	}

	public void setUniforms(int [] textureIDs) {
		for(int i = 0; i < textureIDs.length; i++){
			glActiveTexture(GL_TEXTURE0  + i);
			glBindTexture(GL_TEXTURE_2D, textureIDs[i]);
			glUniform1i(uTextureUnitLocation[i], i);
		}
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}
}