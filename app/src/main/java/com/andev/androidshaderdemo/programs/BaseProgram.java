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

public class BaseProgram extends ShaderProgram{
	private final int uTextureUnitLocation;

	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;

	private final int texelWidthOffsetLocation;
	private final int texelHeightOffsetLocation;

	public BaseProgram(Context context){
		super(context, R.raw.simple_texture_vertex_shader, R.raw.simple_texture_fragment_shader);

		uTextureUnitLocation =  glGetUniformLocation(program, U_TEXTURE_UNIT);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		texelWidthOffsetLocation = glGetUniformLocation(program, TexelWidthOffset);
		texelHeightOffsetLocation = glGetUniformLocation(program, TexelHeightOffset);
	}

	public BaseProgram(Context context, int vertexShader, int fragmentShader){
		super(context, vertexShader, fragmentShader);

		uTextureUnitLocation =  glGetUniformLocation(program, U_TEXTURE_UNIT);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

		texelWidthOffsetLocation = glGetUniformLocation(program, TexelWidthOffset);
		texelHeightOffsetLocation = glGetUniformLocation(program, TexelHeightOffset);
	}

	public void setUniforms(int textureId, float widthFactor, float heightFactor) {
		// Set the active texture unit to texture unit 0.
		glActiveTexture(GL_TEXTURE0);

		// Bind the texture to this unit.
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Tell the texture uniform sampler to use this texture in the shader by
		// telling it to read from texture unit 0.
		glUniform1i(uTextureUnitLocation, 0);

		glUniform1f(texelWidthOffsetLocation, widthFactor);
		glUniform1f(texelHeightOffsetLocation, heightFactor);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getTextureCoordinatesAttributeLocation() {
		return aTextureCoordinatesLocation;
	}

}
