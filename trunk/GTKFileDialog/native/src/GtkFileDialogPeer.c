#include <jni.h>
#include <gtk/gtk.h>
#include "GtkFileDialogPeer.h"

static JavaVM *java_vm;

union env_union {
	void *void_env;
	JNIEnv *jni_env;
};

JNIEnv *gdk_env() {
	union env_union tmp;
	g_assert((*java_vm)->GetEnv(java_vm, &tmp.void_env, JNI_VERSION_1_2)
			== JNI_OK);
	return tmp.jni_env;
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_init
(JNIEnv *env, jclass cls) {
	g_assert((*env)->GetJavaVM(env, &java_vm) == 0);
	gtk_init(NULL, NULL);
}

static gboolean filenameFilterCallback (const GtkFileFilterInfo *filter_info,
                                        gpointer obj)
{
  jclass cx;
  jmethodID id;
  jstring *filename;
  gboolean accepted;

  cx = (*gdk_env())->GetObjectClass (gdk_env(), (jobject) obj);
  id = (*gdk_env())->GetMethodID (gdk_env(), cx, "filenameFilterCallback",
                                             "(Ljava/lang/String;)Z");

  filename = (*gdk_env())->NewStringUTF(gdk_env(), filter_info->filename);

  accepted = (*gdk_env())->CallBooleanMethod(gdk_env(), obj, id, filename);
  return accepted;
}


/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    start
 * Signature: (Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/io/FilenameFilter;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_sun_awt_X11_GtkFileDialogPeer_start(JNIEnv *env,
		jobject jpeer, jstring jtitle, jint mode, jstring jdir, jstring jfile,
		jobject jfilter) {

	const char *title = (*env)->GetStringUTFChars(env, jtitle, 0);

	GtkWidget *dialog;
	if (mode == 1) {
		dialog = gtk_file_chooser_dialog_new(title, NULL,
				GTK_FILE_CHOOSER_ACTION_SAVE, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_SAVE, GTK_RESPONSE_ACCEPT, NULL);
	} else {
		dialog = gtk_file_chooser_dialog_new(title, NULL,
				GTK_FILE_CHOOSER_ACTION_OPEN, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_OPEN, GTK_RESPONSE_ACCEPT, NULL);
	}

	(*env)->ReleaseStringUTFChars(env, jtitle, title);


	// Set the directory
	if (jdir != NULL) {
		const char *dir = (*env)->GetStringUTFChars(env, jdir, 0);
		gtk_file_chooser_set_current_folder(GTK_FILE_CHOOSER(dialog), dir);
		(*env)->ReleaseStringUTFChars(env, jdir, dir);
	}


	// Set the filename
	if (jfile != NULL) {
		const char *filename = (*env)->GetStringUTFChars(env, jfile, 0);
		gtk_file_chooser_set_filename(GTK_FILE_CHOOSER(dialog), filename);
		(*env)->ReleaseStringUTFChars(env, jfile, filename);
	}


	// Set the file filter
	if (jfilter != NULL) {
		GtkFileFilter *filter;
		filter = gtk_file_filter_new();
		gtk_file_filter_add_custom(filter, GTK_FILE_FILTER_FILENAME,
				filenameFilterCallback, jpeer, NULL);
		gtk_file_chooser_set_filter(GTK_FILE_CHOOSER(dialog), filter);
	}

	char *choosed_file = NULL;

	if (gtk_dialog_run(GTK_DIALOG(dialog)) == GTK_RESPONSE_ACCEPT) {
		choosed_file = gtk_file_chooser_get_filename(GTK_FILE_CHOOSER(dialog));
	}

	gtk_widget_destroy(dialog);

	return (*env)->NewStringUTF(env, choosed_file);
}
