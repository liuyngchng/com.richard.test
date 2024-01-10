/**
 * gcc -o gtk_test gtk_test.c `pkg-config --cflags --libs gtk+-2.0`
 */
#include <gtk/gtk.h>
int main(int argc, char*argv[]) {
    GtkWidget *window;
    GtkWidget *label;
    gtk_init(&argc,&argv);
    /* create the main, top level, window */
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    /* give it the title */
    gtk_window_set_title(GTK_WINDOW(window),"hello world");
    /* connect the destroy signal of the window to gtk_main_quit
     * when the window is about to be destroyed we get a notification and
     * stop the main GTK+ loop
     */
    g_signal_connect(window,"destroy",G_CALLBACK(gtk_main_quit),NULL);
    /* create the "Hello, World" label */
    label = gtk_label_new("hello world, a new window");
    /* and insert it into the main window */
    gtk_container_add(GTK_CONTAINER(window),label);
    /* make sure that everything, window and label, are visible */
    gtk_widget_show_all(window);
    /* start the main loop, and let it rest until the application is closed */
    gtk_main();
    return 0;
}
