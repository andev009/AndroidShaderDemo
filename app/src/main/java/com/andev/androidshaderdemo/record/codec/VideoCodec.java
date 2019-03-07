package com.andev.androidshaderdemo.record.codec;


import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import com.andev.androidshaderdemo.record.render.OffScreenRender;
import com.andev.androidshaderdemo.record.utils.StorageUtil;
import com.andev.androidshaderdemo.record.utils.VideoFrameData;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoCodec {
	private static final String TAG = "VideoCodec";
	public static final String VIDEO_FILE = StorageUtil.getExternalStoragePath() +
			File.separator + "shader" + ".mp4";

	Context context;
	private EGLContext glContext;

	private MediaCodec mediaCodec;
	private Surface inputSurface;

	private VideoConfig videoConfig;

	private HandlerThread handlerThread;
	private Handler recordHandler;

	private OffScreenRender offScreenRender;

	private int track;
	private MediaMuxer mediaMuxer;

	private long startTimeStamp;
	private long mLastTimeStamp;
	private long duration;

	public VideoCodec(Context context, VideoConfig videoConfig){
		this.context = context;
		this.videoConfig = videoConfig;
	}

	public void prepareCodec() throws IOException {
		MediaFormat format = MediaFormat.createVideoFormat("video/avc",
				videoConfig.getVideoWidth(),
				videoConfig.getVideoHeight());
		format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
				MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
		format.setInteger(MediaFormat.KEY_BIT_RATE, videoConfig.getBitRate());
		format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
		format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 0);

		mediaCodec = MediaCodec.createEncoderByType("video/avc");

		mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		inputSurface = mediaCodec.createInputSurface();
		glContext = videoConfig.getGLContext();

		mediaMuxer = new MediaMuxer(VIDEO_FILE,
				MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
	}


	public void start(){
		handlerThread = new HandlerThread("record");
		handlerThread.start();

		recordHandler = new Handler(handlerThread.getLooper());

		recordHandler.post(new Runnable() {
			@Override
			public void run() {
				offScreenRender = new OffScreenRender(glContext, inputSurface);
				mediaCodec.start();
			}
		});
	}

	public void stop(){
		if(recordHandler == null){
			return;
		}
		recordHandler.post(new Runnable() {
			@Override
			public void run() {
				drain(true);
				mediaCodec.stop();
				mediaCodec.release();
				mediaCodec = null;
				mediaMuxer.stop();
				mediaMuxer.release();
				mediaMuxer = null;
//		if (listener != null) {
//			listener.onRecordFinish(new ClipInfo(configuration.getFileName(), duration, getDataType()));
//		}
				offScreenRender.release();
				offScreenRender = null;
				inputSurface = null;
				glContext = null;
				videoConfig = null;
				recordHandler.getLooper().quitSafely();
				recordHandler = null;

				Log.w("VideoCodec", "stop:" + Thread.currentThread().getName());

			}
		});
	}

	public void frameAvailableCallback(final VideoFrameData frameData) {
		if(recordHandler == null){
			return;
		}

		recordHandler.post(new Runnable() {
			@Override
			public void run() {
				if(offScreenRender == null){
					return;
				}

				Log.w("VideoCodec", "frameAvailableCallback:" + Thread.currentThread().getName());


				offScreenRender.draw(frameData.getFilter(), frameData.getMatrix(), frameData.getTextureId()
						,frameData.getTimestamp());
				drain(false);
			}
		});
	}

	private void drain(boolean endOfStream) {
		if (endOfStream) {
			mediaCodec.signalEndOfInputStream();
		}
		ByteBuffer[] encoderOutputBuffers = mediaCodec.getOutputBuffers();
		while (true) {
			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int encoderStatus = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
			if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
				//没有数据
				if (!endOfStream) {
					break;
				}
			} else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
				// not expected for an encoder
				encoderOutputBuffers = mediaCodec.getOutputBuffers();
			} else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
				MediaFormat newFormat = mediaCodec.getOutputFormat();
				track = mediaMuxer.addTrack(newFormat);
				mediaMuxer.start();
			} else if (encoderStatus < 0) {
				Log.e(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
						encoderStatus);
			} else {
				ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
				if (encodedData == null) {
					throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
							" was null");
				}

				if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
					bufferInfo.size = 0;
				}

				if (bufferInfo.size != 0) {
					adaptTimeUs(bufferInfo);
					encodedData.position(bufferInfo.offset);
					encodedData.limit(bufferInfo.offset + bufferInfo.size);
					mediaMuxer.writeSampleData(track, encodedData, bufferInfo);
//					if (progressListener != null) {
//						progressListener.onRecordProgress(duration);
//					}
//					if (duration >= configuration.getMaxDuration()) {
//						stop();
//					}
				}
				mediaCodec.releaseOutputBuffer(encoderStatus, false);

				if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
					break;
				}
			}

		}
	}

	private void adaptTimeUs(MediaCodec.BufferInfo info) {
		info.presentationTimeUs = (long) (info.presentationTimeUs / 1);
		if (startTimeStamp == 0) {
			startTimeStamp = info.presentationTimeUs;
		} else {
			duration = info.presentationTimeUs - startTimeStamp;
		}
//        //偶现时间戳错乱，这里做个保护，假设一秒30帧
//        if (info.presentationTimeUs <= mLastTimeStamp) {
//            info.presentationTimeUs = (long) (mLastTimeStamp + C.SECOND_IN_US / 30 / factor);
//        }
		mLastTimeStamp = info.presentationTimeUs;
	}

}
