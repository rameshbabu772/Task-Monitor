LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE := taskmon-jni
LOCAL_CFLAGS := -Werror
LOCAL_SRC_FILES := taskmon-jni.c
LOCAL_LDLIBS := -llog 
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../kernel/usr/include

include $(BUILD_SHARED_LIBRARY)

