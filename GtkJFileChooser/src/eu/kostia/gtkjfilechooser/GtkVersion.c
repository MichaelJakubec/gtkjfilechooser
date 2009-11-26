#include <jni.h>
#include <gtk/gtkversion.h>
#include "GtkVersion.h"

JNIEXPORT jboolean JNICALL Java_eu_kostia_gtkjfilechooser_GtkVersion_check0
  (JNIEnv *env, jclass cls, jint required_major, jint required_minor, jint required_micro)
{
    return GTK_CHECK_VERSION(required_major, required_minor, required_micro);
}
