#ifndef ANDROIDSHADERDEMO_PREVIEW_CONTROLLER_H
#define ANDROIDSHADERDEMO_PREVIEW_CONTROLLER_H

#include <android/native_window.h>
#include <EGL/egl.h>
#include <EGL/eglext.h>
#include "egl_core.h"
#include "preview_render.h"
#include "pic_preview_texture.h"

class PicPreviewController {
public:
	PicPreviewController();
	virtual ~PicPreviewController();

	void initialize();
	void setWindow(ANativeWindow* window);
	void resetSize(int width, int height);
	void loadTexture(byte* pixels, int frameWidth, int frameHeight);
	void draw();
private:
	PicPreviewRender* renderer;
	PicPreviewTexture* picPreviewTexture;

	int screenWidth;
	int screenHeight;
	// android window, supported by NDK r5 and newer
	ANativeWindow* _window;

	EGLCore* eglCore;
	EGLSurface previewSurface;

	void drawFrame();
	void destroy();

};


#endif //ANDROIDSHADERDEMO_PREVIEW_CONTROLLER_H
