#include <jni.h>
#include <gtk/gtk.h>
#include "GtkFileDialogPeer.h"

GtkWidget *dialog;
const char* _title;

struct filterdata {
	JNIEnv *env;
	jobject obj;
};

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
		gpointer data) {

	jclass cls = NULL;
	jmethodID methodID;
	jstring *filename;
	gboolean accepted;

	JNIEnv *env;
	env = ((struct filterdata)data).env;

	jobject obj;
	obj = ((struct filterdata)data).obj;

	cls = (*env)->GetObjectClass(env, obj);
	g_print("111\n");

	methodID = (*env)->GetMethodID(env, cls, "filenameFilterCallback", "(Ljava/lang/String;)Z");
	g_print("115\n");

	filename = (*env)->NewStringUTF(env, filter_info->filename);
	g_print("118\n");

	accepted = (*env)->CallBooleanMethod(env, obj, methodID, filename);
	g_print("121\n");

	return accepted;
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

	//TODO instead of passing the java object (obj), pass a struct with env (JNIEnv) and obj (jobject)

	struct filterdata *data;
	data.env = env;
	data.obj = obj;

	gtk_file_filter_add_custom(filter, GTK_FILE_FILTER_FILENAME,
			filenameFilterCallback, data, NULL);

	gtk_file_chooser_set_filter(GTK_FILE_CHOOSER(dialog), filter);
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    filenameFilterCallbackTest
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_sun_awt_X11_GtkFileDialogPeer_filenameFilterCallbackTest
  (JNIEnv *env, jobject obj, jstring str) {
	jclass cls = NULL;
	jmethodID methodID;
	gboolean accepted;

	cls = (*env)->GetObjectClass(env, (jobject) obj);
	g_print("111\n");

	methodID = (*env)->GetMethodID(env, cls, "filenameFilterCallback", "(Ljava/lang/String;)Z");
	g_print("115\n");


	accepted = (*env)->CallBooleanMethod(env, obj, methodID, str);
	g_print("121\n");

	return accepted;
}
