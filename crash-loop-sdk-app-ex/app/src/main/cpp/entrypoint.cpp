#include <bugsnag.h>
#include <jni.h>
#include <android/log.h>


extern "C" {

static void __attribute__((used)) somefakefunc(void) {}

bool my_on_error_b(void *event) {
  bugsnag_event_add_metadata_bool(event, "Native Error Flag", "Error", true);
  return true;
}

JNIEXPORT void JNICALL
Java_com_example_bugsnag_android_CrashLoopApplication_performNativeBugsnagSetup(
    JNIEnv *env, jobject instance) {
  bugsnag_add_on_error(&my_on_error_b);
}

}
