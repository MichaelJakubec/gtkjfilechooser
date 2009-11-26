#include <jni.h>
#include <stdio.h>
#include <gtk/gtk.h>
#include "j_gtk_version.h"
#include "HelloWorld.h"


JNIEXPORT jstring JNICALL Java_eu_kostia_gtkjfilechooser_GtkVersion_check
  (JNIEnv *env, jclass cls, jint required_major, jint required_minor, jint required_micro) {
  	version gtk_check_version(required_major, required_minor, required_micro);
  	return (*env)->NewStringUTF(env, version);
}
