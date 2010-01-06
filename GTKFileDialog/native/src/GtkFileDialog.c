#include <gtk/gtk.h>
#include <string.h>

GtkWidget *dialog;
const char* _title;

void set_directory(const char *directory) {
	gtk_file_chooser_set_current_folder(GTK_FILE_CHOOSER(dialog), directory);
}

/**
 * LOAD = 0; SAVE = 1;
 */
void set_mode(int mode) {
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

void init(const char* title) {
	gtk_init(NULL, NULL);
	_title = title;
	set_mode(0);
}

void set_filter(const char* pattern, const char* name) {
	GtkFileFilter *filter = gtk_file_filter_new();
	gtk_file_filter_add_pattern(filter, pattern);
	gtk_file_filter_set_name(filter, name);
	gtk_file_chooser_set_filter(GTK_FILE_CHOOSER(dialog), filter);
}

const char* run() {
	char *filename = NULL;
	if (gtk_dialog_run(GTK_DIALOG(dialog)) == GTK_RESPONSE_ACCEPT) {
		filename = gtk_file_chooser_get_filename(GTK_FILE_CHOOSER(dialog));
	}

	gtk_widget_destroy(dialog);

	return filename;
}

int main(int argc, char *argv[]) {
	init("File Dialog");

	if (argc > 1) {
		if (strcmp("1", argv[1]) == 0) {
			set_mode(1);
		}
	}

	set_filter("*.txt", "Only text files");
	const char *filename = run();
	g_print("Filename: %s\n", filename);

	return 0;
}
