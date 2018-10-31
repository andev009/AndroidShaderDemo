#include "egl_core.h"

#define LOG_TAG "EGLCore"

EGLCore::EGLCore() {
	display = EGL_NO_DISPLAY;
	context = EGL_NO_CONTEXT;
}

EGLCore::~EGLCore() {
}

void EGLCore::release() {
	eglMakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
	LOGI("after eglMakeCurrent...");
	eglDestroyContext(display, context);
	LOGI("after eglDestroyContext...");
	display = EGL_NO_DISPLAY;
	context = EGL_NO_CONTEXT;
}

void EGLCore::releaseSurface(EGLSurface eglSurface) {
	eglDestroySurface(display, eglSurface);
	eglSurface = EGL_NO_SURFACE;
}

EGLContext EGLCore::getContext(){
	LOGI("return EGLCore getContext...");
	return context;
}

EGLDisplay EGLCore::getDisplay(){
	return display;
}

EGLConfig EGLCore::getConfig(){
	return config;
}

EGLSurface EGLCore::createWindowSurface(ANativeWindow* _window) {
	EGLSurface surface = NULL;
	EGLint format;
	if (!eglGetConfigAttrib(display, config, EGL_NATIVE_VISUAL_ID, &format)) {
		LOGE("eglGetConfigAttrib() returned error %d", eglGetError());
		release();
		return surface;
	}
	ANativeWindow_setBuffersGeometry(_window, 0, 0, format);
	if (!(surface = eglCreateWindowSurface(display, config, _window, 0))) {
		LOGE("eglCreateWindowSurface() returned error %d", eglGetError());
	}
	return surface;
}

bool EGLCore::swapBuffers(EGLSurface eglSurface) {
	return eglSwapBuffers(display, eglSurface);
}

bool EGLCore::makeCurrent(EGLSurface eglSurface) {
	return eglMakeCurrent(display, eglSurface, eglSurface, context);
}

void EGLCore::doneCurrent() {
	eglMakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
}

bool EGLCore::init() {
	return this->init(NULL);
}

bool EGLCore::init(EGLContext sharedContext) {
	EGLint numConfigs;
	EGLint width;
	EGLint height;

	const EGLint attribs[] = { EGL_BUFFER_SIZE, 32, EGL_ALPHA_SIZE, 8, EGL_BLUE_SIZE, 8, EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8, EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
	EGL_SURFACE_TYPE, EGL_WINDOW_BIT, EGL_NONE };

	if ((display = eglGetDisplay(EGL_DEFAULT_DISPLAY)) == EGL_NO_DISPLAY) {
		LOGE("eglGetDisplay() returned error %d", eglGetError());
		return false;
	}
	if (!eglInitialize(display, 0, 0)) {
		LOGE("eglInitialize() returned error %d", eglGetError());
		return false;
	}

	if (!eglChooseConfig(display, attribs, &config, 1, &numConfigs)) {
		LOGE("eglChooseConfig() returned error %d", eglGetError());
		release();
		return false;
	}

	EGLint eglContextAttributes[] = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE };
	if (!(context = eglCreateContext(display, config, NULL == sharedContext ? EGL_NO_CONTEXT : sharedContext, eglContextAttributes))) {
		LOGE("eglCreateContext() returned error %d", eglGetError());
		release();
		return false;
	}

	return true;
}


