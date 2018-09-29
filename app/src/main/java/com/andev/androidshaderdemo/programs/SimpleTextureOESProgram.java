package com.andev.androidshaderdemo.programs;


import android.content.Context;
import android.opengl.GLES11Ext;

import com.andev.androidshaderdemo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class SimpleTextureOESProgram extends ShaderProgram {

	private final int uTextureUnitLocation;
	private final int uTextureTransformLocation;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;


	public SimpleTextureOESProgram(Context context) {
		super(context, R.raw.simple_texture_oes_vertex_shader, R.raw.simple_texture_oes_fragment_shader);

		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_OES_UNIT);
		uTextureTransformLocation = glGetUniformLocation(program, U_TEXTURE_TRANSFORM);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
	}

	public void setUniforms(int textureId, float[] textureTransformMatrix) {
		// Set the active texture unit to texture unit 0.
		glActiveTexture(GL_TEXTURE0);

		// Bind the texture to this unit.
		glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);

		// Tell the texture uniform sampler to use this texture in the shader by
		// telling it to read from texture unit 0.
		glUniform1i(uTextureUnitLocation, 0);

		glUniformMatrix4fv(uTextureTransformLocation, 1, false, textureTransformMatrix, 0);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}

}
