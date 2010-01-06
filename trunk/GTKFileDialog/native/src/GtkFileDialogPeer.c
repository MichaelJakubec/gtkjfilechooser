#include <jni.h>
#include <gtk/gtk.h>
#include "GtkFileDialogPeer.h"

GtkWidget *dialog;
const char* _title;

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

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setDirectory
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setDirectory (JNIEnv *env, jobject obj, jstring jdir) {
	const char *directory = (*env)->GetStringUTFChars(env, jdir, 0);
	gtk_file_chooser_set_current_folder(GTK_FILE_CHOOSER(dialog), directory);

	(*env)->ReleaseStringUTFChars(env, jdir, directory);
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    setFile
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_setFile
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

	//TODO destroy as separate method
	//gtk_widget_destroy(dialog);

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
