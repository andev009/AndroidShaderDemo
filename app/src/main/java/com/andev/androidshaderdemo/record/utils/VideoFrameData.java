package com.andev.androidshaderdemo.record.utils;

import com.andev.androidshaderdemo.filter.CameraInputFilter;

public class VideoFrameData {
	private CameraInputFilter mFilter;
	private float[] mMatrix;
	private long mTimeStamp;
	private int mTextureId;

	public VideoFrameData(CameraInputFilter filter, float[] matrix, long timestamp, int textureId) {
		this.mFilter = filter;
		this.mMatrix = matrix;
		this.mTimeStamp = timestamp;
		this.mTextureId = textureId;
	}

	public CameraInputFilter getFilter() {
		return mFilter;
	}

	public float[] getMatrix() {
		return mMatrix;
	}

	public long getTimestamp() {
		return mTimeStamp;
	}

	public int getTextureId() {
		return mTextureId;
	}
}
