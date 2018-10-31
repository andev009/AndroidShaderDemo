LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_ARM_MODE := arm

LOCAL_CFLAGS := -DHAVE_CONFIG_H -DFPM_ARM -ffast-math -O3 

LOCAL_SRC_FILES += \
./egl_core.cpp \
./preview_controller.cpp \
./pic_preview_texture.cpp \
./preview_render.cpp \
    
LOCAL_MODULE := librender
include $(BUILD_STATIC_LIBRARY)