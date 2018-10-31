#include "./pic_preview_texture.h"

#define LOG_TAG "PicPreviewTexture"

PicPreviewTexture::PicPreviewTexture() {
}

PicPreviewTexture::~PicPreviewTexture() {
}

bool PicPreviewTexture::createTexture() {
	LOGI("enter PicPreviewTexture::createTexture");
	texture = 0;
	int ret = initTexture();
	if (ret < 0) {
		LOGI("init texture failed...");
		this->dealloc();
		return false;
	}
	return true;
}

void PicPreviewTexture::updateTexImage(byte* pixels, int frameWidth, int frameHeight) {
	if (pixels) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		if (checkGlError("glBindTexture")) {
			return;
		}
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, frameWidth, frameHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
}

bool PicPreviewTexture::bindTexture(GLint uniformSampler) {
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, texture);
	if (checkGlError("glBindTexture")) {
		return false;
	}
	glUniform1i(uniformSampler, 0);
	return true;
}

int PicPreviewTexture::initTexture() {
	glGenTextures(1, &texture);
	glBindTexture(GL_TEXTURE_2D, texture);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	return 1;
}

bool PicPreviewTexture::checkGlError(const char* op) {
	GLint error;
	for (error = glGetError(); error; error = glGetError()) {
		LOGI("error::after %s() glError (0x%x)\n", op, error);
		return true;
	}
	return false;
}

void PicPreviewTexture::dealloc() {
	LOGI("enter PicPreviewTexture::dealloc");
	if (texture) {
		glDeleteTextures(1, &texture);
	}
}
