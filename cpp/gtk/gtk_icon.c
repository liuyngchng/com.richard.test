/*
 * gtk_icon.c
 *
 *  Created on: Oct 23, 2024
 *      Author: rd
 */
#include <gtk/gtk.h>

GdkPixbuf *create_pixbuf (const gchar *filename) {
    GdkPixbuf *pixbuf;
    GError *error = NULL;

    pixbuf = gdk_pixbuf_new_from_file(filename, &error); /* key function */
    if(!pixbuf){
    	g_print("%s\n", error->message);
        g_error_free(error);
    }

    return pixbuf;
}


int main(int argc, char *argv[]) {
    gtk_init(&argc, &argv);
    GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title (GTK_WINDOW (window), "Customized Title");
    gtk_window_set_icon(GTK_WINDOW(window), create_pixbuf("pic1.png"));
    gtk_widget_show_all(GTK_WIDGET(window));

    gtk_main();

    return 0;
}

