#include <jni.h>
#include <gtk/gtk.h>
#include "GtkFileDialogPeer.h"


GtkWidget *dialog;
const char* _title;
static JNIEnv *_env;

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    init
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_init
(JNIEnv *env, jobject obj, jstring jtitle)
{
	const char *title = (*env)->GetStringUTFChars(env, jtitle, 0);
	gtk_init(NULL, NULL);
	_title = title;
	_env = env;
}

JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_destroy
(JNIEnv *env, jobject obj) {
	gtk_widget_destroy(dialog);
}
/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setDirectory
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setDirectoryNative (JNIEnv *env, jobject obj, jstring jdir) {
	const char *directory = (*env)->GetStringUTFChars(env, jdir, 0);
	gtk_file_chooser_set_current_folder(GTK_FILE_CHOOSER(dialog), directory);

	(*env)->ReleaseStringUTFChars(env, jdir, directory);
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setFile
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setFileNative
(JNIEnv *env, jobject obj, jstring jfile) {
	const char *filename = (*env)->GetStringUTFChars(env, jfile, 0);
	gtk_file_chooser_set_filename(GTK_FILE_CHOOSER(dialog), filename);

	(*env)->ReleaseStringUTFChars(env, jfile, filename);
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setMode
(JNIEnv *env, jobject obj, jint mode) {
	if (mode == 1) {
		dialog = gtk_file_chooser_dialog_new(_title, NULL,
				GTK_FILE_CHOOSER_ACTION_SAVE, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_SAVE, GTK_RESPONSE_ACCEPT, NULL);
	} else {
		dialog = gtk_file_chooser_dialog_new(_title, NULL,
				GTK_FILE_CHOOSER_ACTION_OPEN, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_OPEN, GTK_RESPONSE_ACCEPT, NULL);
	}
}

const char* run() {
	char *filename = NULL;

	if (gtk_dialog_run(GTK_DIALOG(dialog)) == GTK_RESPONSE_ACCEPT) {
		filename = gtk_file_chooser_get_filename(GTK_FILE_CHOOSER(dialog));
	}

	return filename;
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    run
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_sun_awt_X11_GtkFileDialogPeer_run(JNIEnv *env,
		jobject obj) {
	const char *filename = run();
	return (*env)->NewStringUTF(env, filename);
}

/* This function interfaces with the Java callback method of the same name.
 This function extracts the filename from the GtkFileFilterInfo object,
 and passes it to the Java method.  The Java method will call the filter's
 accept() method and will give back the return value. */
static gboolean filenameFilterCallback(const GtkFileFilterInfo *filter_info,
		gpointer obj) {
	jclass cx;
	jmethodID methodID;
	jstring *filename;

	cx = (*_env)->GetObjectClass(_env, (jobject) obj);

	methodID = (*_env)->GetMethodID(_env, cx, "filenameFilterCallback",
			"(Ljava/lang/String;)Z");

	filename = (*_env)->NewStringUTF(_env, filter_info->filename);

	return (*_env)->CallBooleanMethod(_env, obj, methodID, filename);
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setFilenameFilterNative
 * Signature: (Ljava/io/FilenameFilter;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setFilenameFilterNative
(JNIEnv *env, jobject obj, jobject filefilter) {
	GtkFileFilter *filter;
	filter = gtk_file_filter_new();

	gtk_file_filter_add_custom(filter, GTK_FILE_FILTER_FILENAME,
			filenameFilterCallback, obj, NULL);

	gtk_file_chooser_set_filter(GTK_FILE_CHOOSER(dialog), filter);
}
