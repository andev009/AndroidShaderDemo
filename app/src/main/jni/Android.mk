LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_CFLAGS += -D__STDC_CONSTANT_MACROS

LOCAL_SRC_FILES = \
./NativeController.cpp

LOCAL_STATIC_LIBRARIES := librender

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_LDLIBS += -lz
LOCAL_LDLIBS += -landroid
LOCAL_LDLIBS += -ljnigraphics
# Link with OpenGL ES
LOCAL_LDLIBS += -lGLESv2
LOCAL_LDLIBS += -lEGL

LOCAL_MODULE := libdrawdemo
include $(BUILD_SHARED_LIBRARY)
include $(call all-makefiles-under,$(LOCAL_PATH))