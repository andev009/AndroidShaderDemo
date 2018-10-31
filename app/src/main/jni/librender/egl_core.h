#ifndef ANDROIDSHADERDEMO_EGL_CORE_H
#define ANDROIDSHADERDEMO_EGL_CORE_H

#include "./../libcommon/CommonTools.h"
#include <android/native_window.h>
#include <EGL/egl.h>
#include <EGL/eglext.h>

class EGLCore {
public:
	EGLCore();
    virtual ~EGLCore();

    bool init();

    bool init(EGLContext sharedContext);

  	EGLSurface createWindowSurface(ANativeWindow* _window);

    bool makeCurrent(EGLSurface eglSurface);

    void doneCurrent();

    bool swapBuffers(EGLSurface eglSurface);

    void releaseSurface(EGLSurface eglSurface);
    void release();

    EGLContext getContext();
    EGLDisplay getDisplay();
    EGLConfig getConfig();

private:
	EGLDisplay display;
	EGLConfig config;
	EGLContext context;
};




#endif //ANDROIDSHADERDEMO_EGL_CORE_H
