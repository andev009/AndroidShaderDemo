#include "preview_controller.h"


#define LOG_TAG "PreviewController"

PicPreviewController::PicPreviewController() :
		previewSurface(0), eglCore(0) {
	LOGI("VideoDutePlayerController instance created");
	renderer = new PicPreviewRender();
	this->screenWidth = 720;
	this->screenHeight = 720;
}

PicPreviewController::~PicPreviewController() {
	LOGI("VideoDutePlayerController instance destroyed");
}

void PicPreviewController::setWindow(ANativeWindow *window) {
	_window = window;

	previewSurface = eglCore->createWindowSurface(_window);
    eglCore->makeCurrent(previewSurface);
    	LOGI("Initializing window Success");
}

void PicPreviewController::resetSize(int width, int height) {
	LOGI("VideoDutePlayerController::resetSize width:%d; height:%d", width, height);
	this->screenWidth = width;
    this->screenHeight = height;
    renderer->resetRenderSize(0, 0, width, height);
}

void PicPreviewController::initialize() {
	eglCore = new EGLCore();
	eglCore->init();
	LOGI("Initializing context Success");
}

void PicPreviewController::loadTexture(byte* pixels, int frameWidth, int frameHeight) {
    picPreviewTexture = new PicPreviewTexture();
	bool createTexFlag = picPreviewTexture->createTexture();
	if(!createTexFlag){
		LOGI("createTexFlag is failed...");
		destroy();
		return;
	}

	picPreviewTexture->updateTexImage(pixels, frameWidth, frameHeight);

    bool isRendererInitialized = renderer->init(screenWidth, screenHeight, picPreviewTexture);
    if (!isRendererInitialized) {
    	LOGI("Renderer failed on initialized...");
    	return;
    }
	LOGI("loadTexture Success");
}

void PicPreviewController::draw() {
    drawFrame();
}

void PicPreviewController::destroy() {
	LOGI("dealloc renderer ...");

	if(eglCore){
		eglCore->releaseSurface(previewSurface);
		eglCore->release();
		eglCore = NULL;
	}
	return;
}

void PicPreviewController::drawFrame() {
	renderer->render();

	if (!eglCore->swapBuffers(previewSurface)) {
		LOGE("eglSwapBuffers() returned error %d", eglGetError());
	}
}







