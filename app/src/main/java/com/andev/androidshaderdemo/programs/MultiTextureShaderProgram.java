package com.andev.androidshaderdemo.programs;


import android.content.Context;

import com.andev.androidshaderdemo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;

public class MultiTextureShaderProgram extends ShaderProgram{
	private final int []uTextureUnitLocation;
	private final int uStrength;


	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	public MultiTextureShaderProgram(Context context) {
		super(context, R.raw.multi_texture_vertex_shader, R.raw.multi_texture_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		uStrength = glGetUniformLocation(program, U_STRENGTH);

		uTextureUnitLocation = new int[6];

		for(int i = 0; i < uTextureUnitLocation.length; i++){
			uTextureUnitLocation[i] = glGetUniformLocation(program, U_TEXTURE_UNIT + i);
		}
	}

	public void setUniforms(int [] textureIDs, float strength) {
		for(int i = 0; i < textureIDs.length; i++){
			glActiveTexture(GL_TEXTURE0  + i);
			glBindTexture(GL_TEXTURE_2D, textureIDs[i]);
			glUniform1i(uTextureUnitLocation[i], i);
		}

		glUniform1f(uStrength, strength);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}
}
