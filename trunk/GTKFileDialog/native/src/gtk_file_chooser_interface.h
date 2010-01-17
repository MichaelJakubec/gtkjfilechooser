#ifndef _GTK_FILE_CHOOSER_INTERFACE_H
#define _GTK_FILE_CHOOSER_INTERFACE_H

#include <stdlib.h>
#include <jni.h>

#define _G_TYPE_CIC(ip, gt, ct)       ((ct*) ip)
#define G_TYPE_CHECK_INSTANCE_CAST(instance, g_type, c_type)    (_G_TYPE_CIC ((instance), (g_type), c_type))
#define GTK_TYPE_FILE_CHOOSER             (fp_gtk_file_chooser_get_type ())
#define GTK_FILE_CHOOSER(obj) (G_TYPE_CHECK_INSTANCE_CAST ((obj), GTK_TYPE_FILE_CHOOSER, GtkFileChooser))
#define fp_g_signal_connect(instance, detailed_signal, c_handler, data) \
    fp_g_signal_connect_data ((instance), (detailed_signal), (c_handler), (data), NULL, (GConnectFlags) 0)
#define	G_CALLBACK(f) ((GCallback) (f))
#define	G_TYPE_FUNDAMENTAL_SHIFT	(2)
#define	G_TYPE_MAKE_FUNDAMENTAL(x)	((GType) ((x) << G_TYPE_FUNDAMENTAL_SHIFT))
#define G_TYPE_OBJECT G_TYPE_MAKE_FUNDAMENTAL (20)
#define G_OBJECT(object) (G_TYPE_CHECK_INSTANCE_CAST ((object), G_TYPE_OBJECT, GObject))
#define GTK_STOCK_CANCEL           "gtk-cancel"
#define GTK_STOCK_SAVE             "gtk-save"
#define GTK_STOCK_OPEN             "gtk-open"

typedef enum {
	GTK_RESPONSE_NONE = -1,
	GTK_RESPONSE_REJECT = -2,
	GTK_RESPONSE_ACCEPT = -3,
	GTK_RESPONSE_DELETE_EVENT = -4,
	GTK_RESPONSE_OK = -5,
	GTK_RESPONSE_CANCEL = -6,
	GTK_RESPONSE_CLOSE = -7,
	GTK_RESPONSE_YES = -8,
	GTK_RESPONSE_NO = -9,
	GTK_RESPONSE_APPLY = -10,
	GTK_RESPONSE_HELP = -11
} GtkResponseType;

typedef struct _GtkWindow GtkWindow;

typedef struct _GtkFileChooser GtkFileChooser;

typedef enum {
	GTK_FILE_CHOOSER_ACTION_OPEN,
	GTK_FILE_CHOOSER_ACTION_SAVE,
	GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER,
	GTK_FILE_CHOOSER_ACTION_CREATE_FOLDER
} GtkFileChooserAction;

typedef struct _GtkFileFilter GtkFileFilter;

typedef enum {
	GTK_FILE_FILTER_FILENAME = 1 << 0,
	GTK_FILE_FILTER_URI = 1 << 1,
	GTK_FILE_FILTER_DISPLAY_NAME = 1 << 2,
	GTK_FILE_FILTER_MIME_TYPE = 1 << 3
} GtkFileFilterFlags;

typedef struct {
	GtkFileFilterFlags contains;

	const gchar *filename;
	const gchar *uri;
	const gchar *display_name;
	const gchar *mime_type;
} GtkFileFilterInfo;

typedef gboolean (*GtkFileFilterFunc)(const GtkFileFilterInfo *filter_info,
		gpointer data);

typedef void (*GDestroyNotify)(gpointer data);

typedef void (*GCallback)(void);

typedef struct _GClosure GClosure;

typedef void (*GClosureNotify)(gpointer data, GClosure *closure);

typedef enum {
	G_CONNECT_AFTER = 1 << 0, G_CONNECT_SWAPPED = 1 << 1
} GConnectFlags;


/**
 * Function Pointers for GtkFileChooser
 */
gchar* (*fp_gtk_file_chooser_get_filename)(GtkFileChooser *chooser);
void (*fp_gtk_widget_hide)(GtkWidget *widget);
void (*fp_gtk_main_quit)(void);
GtkWidget* (*fp_gtk_file_chooser_dialog_new)(const gchar *title,
		GtkWindow *parent, GtkFileChooserAction action,
		const gchar *first_button_text, ...);
gboolean (*fp_gtk_file_chooser_set_current_folder)(GtkFileChooser *chooser,
		const gchar *filename);
gboolean (*fp_gtk_file_chooser_set_filename)(GtkFileChooser *chooser,
		const char *filename);
void (*fp_gtk_file_filter_add_custom)(GtkFileFilter *filter,
		GtkFileFilterFlags needed, GtkFileFilterFunc func, gpointer data,
		GDestroyNotify notify);
void (*fp_gtk_file_chooser_set_filter)(GtkFileChooser *chooser,
		GtkFileFilter *filter);
GType (*fp_gtk_file_chooser_get_type)(void);
GtkFileFilter* (*fp_gtk_file_filter_new)(void);
void (*fp_gtk_file_chooser_set_do_overwrite_confirmation)(
		GtkFileChooser *chooser, gboolean do_overwrite_confirmation);
gulong (*fp_g_signal_connect_data)(gpointer instance,
		const gchar *detailed_signal, GCallback c_handler, gpointer data,
		GClosureNotify destroy_data, GConnectFlags connect_flags);
void (*fp_gtk_widget_show)(GtkWidget *widget);
void (*fp_gtk_main)(void);

#endif /* !_GTK_FILE_CHOOSER_INTERFACE_H */
