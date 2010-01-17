#include <jni.h>
#include <stdio.h>
#include "gtk2_interface.h"
#include "gtk_file_chooser_interface.h"
#include "sun_awt_X11_GtkFileDialogPeer.h"

static JavaVM *java_vm;

union env_union {
	void *void_env;
	JNIEnv *jni_env;
};

JNIEnv *env() {
	union env_union tmp;
	(*java_vm)->GetEnv(java_vm, &tmp.void_env, JNI_VERSION_1_2) == JNI_OK;
	return tmp.jni_env;
}

static gboolean filenameFilterCallback(const GtkFileFilterInfo *filter_info,
		gpointer obj) {
	jclass cx = (*env())->GetObjectClass(env(), (jobject) obj);

	jmethodID id = (*env())->GetMethodID(env(), cx, "filenameFilterCallback",
			"(Ljava/lang/String;)Z");

	jstring filename = (*env())->NewStringUTF(env(), filter_info->filename);

	return (*env())->CallBooleanMethod(env(), obj, id, filename);
}

static void handle_response(GtkWidget *dialog, gint responseId, gpointer obj) {
	char *filename = NULL;

	if (responseId == GTK_RESPONSE_ACCEPT) {
		filename = fp_gtk_file_chooser_get_filename(GTK_FILE_CHOOSER(dialog));
	}

	jclass cx = (*env())->GetObjectClass(env(), (jobject) obj);

	jmethodID id = (*env())->GetMethodID(env(), cx, "setFileInternal",
			"(Ljava/lang/String;)V");

	jstring jfilename = (*env())->NewStringUTF(env(), filename);

	(*env())->CallVoidMethod(env(), obj, id, jfilename);
	fp_g_free(filename);

	fp_gtk_widget_hide(dialog);
	fp_gtk_widget_destroy(dialog);
	fp_gtk_main_quit();
}

void init_GtkFileDialogPeer(JNIEnv *env) {
	if (java_vm == NULL) {
		(*env)->GetJavaVM(env, &java_vm);
		gtk2_load();
	}
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    start
 * Signature: (Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/io/FilenameFilter;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_start(JNIEnv *env,
		jobject jpeer, jstring jtitle, jint mode, jstring jdir, jstring jfile,
		jobject jfilter) {

	init_GtkFileDialogPeer(env);

	const char *title = (*env)->GetStringUTFChars(env, jtitle, 0);

	GtkWidget *dialog;
	if (mode == 1) {
		// Save action
		dialog = fp_gtk_file_chooser_dialog_new(title, NULL,
				GTK_FILE_CHOOSER_ACTION_SAVE, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_SAVE, GTK_RESPONSE_ACCEPT, NULL);
	} else {
		// Default action OPEN
		dialog = fp_gtk_file_chooser_dialog_new(title, NULL,
				GTK_FILE_CHOOSER_ACTION_OPEN, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_OPEN, GTK_RESPONSE_ACCEPT, NULL);
	}

	(*env)->ReleaseStringUTFChars(env, jtitle, title);

	// Set the directory
	if (jdir != NULL) {
		const char *dir = (*env)->GetStringUTFChars(env, jdir, 0);
		fp_gtk_file_chooser_set_current_folder(GTK_FILE_CHOOSER(dialog), dir);
		(*env)->ReleaseStringUTFChars(env, jdir, dir);
	}

	// Set the filename
	if (jfile != NULL) {
		const char *filename = (*env)->GetStringUTFChars(env, jfile, 0);
		fp_gtk_file_chooser_set_filename(GTK_FILE_CHOOSER(dialog), filename);
		(*env)->ReleaseStringUTFChars(env, jfile, filename);
	}

	// Set the file filter
	if (jfilter != NULL) {
		GtkFileFilter *filter;
		filter = fp_gtk_file_filter_new();
		fp_gtk_file_filter_add_custom(filter, GTK_FILE_FILTER_FILENAME,
				filenameFilterCallback, jpeer, NULL);
		fp_gtk_file_chooser_set_filter(GTK_FILE_CHOOSER(dialog), filter);
	}

	//Other Properties
	if (fp_gtk_check_version(2, 8, 0) == NULL) {
		fp_gtk_file_chooser_set_do_overwrite_confirmation (GTK_FILE_CHOOSER(dialog), TRUE);
	}

	fp_g_signal_connect(G_OBJECT(dialog), "response", G_CALLBACK(handle_response),
			jpeer);
	fp_gtk_widget_show(dialog);

	fp_gtk_main();
}
