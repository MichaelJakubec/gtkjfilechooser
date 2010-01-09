#include <jni.h>
#include <gtk/gtk.h>
#include "GtkFileDialogPeer.h"

/**
 * The global AWT lock object.
 */
static jobject global_lock;
static JavaVM *java_vm;

union env_union {
	void *void_env;
	JNIEnv *jni_env;
};

JNIEnv *env() {
	union env_union tmp;
	g_assert((*java_vm)->GetEnv(java_vm, &tmp.void_env, JNI_VERSION_1_2)
			== JNI_OK);
	return tmp.jni_env;
}

static void lock() {
	if ((*env())->MonitorEnter(env(), global_lock) != JNI_OK) {
		g_print("failure while entering GTK monitor\n");
	}
}

static void unlock() {
	if ((*env())->MonitorExit(env(), global_lock)) {
		g_print("failure while exiting GTK monitor\n");
	}
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_init
(JNIEnv *env, jclass cls) {
	g_assert((*env)->GetJavaVM(env, &java_vm) == 0);

	/* init threads */
	if (!g_thread_supported()) {
		gdk_threads_set_lock_functions(&lock, &unlock);
		g_thread_init(NULL);
	}
	gdk_threads_init();

	/* init gtk */
	gtk_init(NULL, NULL);
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
		filename = gtk_file_chooser_get_filename(GTK_FILE_CHOOSER(dialog));

		jclass cx = (*env())->GetObjectClass(env(), (jobject) obj);

		jmethodID id = (*env())->GetMethodID(env(), cx,
				"setFile", "(Ljava/lang/String;)V");

		jstring jfilename =
				(*env())->NewStringUTF(env(), filename);

		(*env())->CallVoidMethod(env(), obj, id, jfilename);
		g_free(filename);
	}
	gtk_widget_hide(dialog);
	gtk_main_quit();
}

/*
 * Class:     sun_awt_X11_GtkFileDialogPeer
 * Method:    start
 * Signature: (Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/io/FilenameFilter;)V
 */
JNIEXPORT void JNICALL Java_sun_awt_X11_GtkFileDialogPeer_start(JNIEnv *env,
		jobject jpeer, jstring jtitle, jint mode, jstring jdir, jstring jfile,
		jobject jfilter) {

	global_lock = (*env)->NewGlobalRef(env, jpeer);
	gdk_threads_enter();

	const char *title = (*env)->GetStringUTFChars(env, jtitle, 0);

	GtkWidget *window;
	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);

	GtkWidget *dialog;
	if (mode == 1) {
		dialog = gtk_file_chooser_dialog_new(title, GTK_WINDOW(window),
				GTK_FILE_CHOOSER_ACTION_SAVE, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_SAVE, GTK_RESPONSE_ACCEPT, NULL);
	} else if (mode == 2) {
		dialog = gtk_file_chooser_dialog_new(title, GTK_WINDOW(window),
				GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_OPEN, GTK_RESPONSE_ACCEPT, NULL);
	} else if (mode == 3) {
		dialog = gtk_file_chooser_dialog_new(title, GTK_WINDOW(window),
				GTK_FILE_CHOOSER_ACTION_CREATE_FOLDER, GTK_STOCK_CANCEL,
				GTK_RESPONSE_CANCEL, GTK_STOCK_OPEN, GTK_RESPONSE_ACCEPT, NULL);
	} else {
		//Default action OPEN
		dialog = gtk_file_chooser_dialog_new(title, GTK_WINDOW(window),
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


	//Other Properties
#if GTK_MINOR_VERSION >= 8
	gtk_file_chooser_set_do_overwrite_confirmation (GTK_FILE_CHOOSER(dialog), TRUE);
#endif

	g_signal_connect(G_OBJECT(dialog), "response", G_CALLBACK(handle_response),
			jpeer);
	gtk_widget_show(dialog);

	gtk_main();
	gdk_threads_leave();
}
