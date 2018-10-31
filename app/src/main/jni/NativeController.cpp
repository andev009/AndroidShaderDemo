#include "com_andev_androidshaderdemo_nativedemo_NativeController.h"

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/bitmap.h>
#include "./librender/preview_controller.h"

#define LOG_TAG "NativeController"

static ANativeWindow *window = 0;
static PicPreviewController *controller = 0;

JNIEXPORT void JNICALL Java_com_andev_androidshaderdemo_nativedemo_NativeController_init
  (JNIEnv * env, jobject obj){
  controller = new PicPreviewController();
  controller->initialize();
}


JNIEXPORT void JNICALL Java_com_andev_androidshaderdemo_nativedemo_NativeController_setSurface
  (JNIEnv * env, jobject obj, jobject surface){
   if (surface != 0 && NULL != controller) {
		window = ANativeWindow_fromSurface(env, surface);
		controller->setWindow(window);
	} else if (window != 0) {
		ANativeWindow_release(window);
		window = 0;
	}
}

JNIEXPORT void JNICALL Java_com_andev_androidshaderdemo_nativedemo_NativeController_resetSize
  (JNIEnv * env, jobject obj, jint width, jint height){
   if (NULL != controller) {
		controller->resetSize(width, height);
   }
}


JNIEXPORT void JNICALL Java_com_andev_androidshaderdemo_nativedemo_NativeController_showBitmap
  (JNIEnv * env, jobject obj, jobject bitmap){

  int ret = 0;
  AndroidBitmapInfo bitmapInfo;
  void *pixels = NULL;

  int imgWidth = 0;
  int imgHeight = 0;

  if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        return;
  }

  LOGI("showBitmap width %d, height %d, format %d", bitmapInfo.width, bitmapInfo.height, bitmapInfo.format);

  imgWidth = bitmapInfo.width;
  imgHeight = bitmapInfo.height;

  if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGI("Java_com_example_hellojni_HelloJni_showBitmap invalid rgb format");
        return;
  }

  if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGI("AndroidBitmap_lockPixels() failed ! error=%d", ret);
  }

  controller->loadTexture((byte*)pixels, imgWidth, imgHeight);
  controller->draw();

  AndroidBitmap_unlockPixels(env, bitmap);
}


JNIEXPORT void JNICALL Java_com_andev_androidshaderdemo_nativedemo_NativeController_stop
  (JNIEnv * env, jobject ob){

  }




