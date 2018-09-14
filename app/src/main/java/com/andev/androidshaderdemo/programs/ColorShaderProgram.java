package com.andev.androidshaderdemo.programs;


import android.content.Context;

import com.andev.androidshaderdemo.R;

import static android.opengl.GLES20.glGetAttribLocation;

public class ColorShaderProgram extends ShaderProgram{
	private final int aPositionLocation;
	private final int aColorLocation;

	public ColorShaderProgram(Context context){
		super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
	}

	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}

	public int getColorAttributeLocation() {
		return aColorLocation;
	}
}
