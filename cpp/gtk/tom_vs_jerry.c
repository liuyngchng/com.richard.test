#include <gtk/gtk.h>

void on_button_clicked(GtkButton *button, gpointer user_data) {
    g_print("I am %s, can u see me?\n", (gchar *) user_data);
}

int main(int argc, char *argv[]) {
    gtk_init(&argc, &argv);

    GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_default_size(GTK_WINDOW(window), 800, 600);
    gtk_window_set_title(GTK_WINDOW(window),"Tom and Jerry");
    GtkWidget *fixed = gtk_fixed_new();
    GtkWidget *button1 = gtk_button_new_with_label("Tom");

    GtkWidget *button2 = gtk_button_new_with_label("Jerry");

    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect(button1, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Tom");
    g_signal_connect(button2, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Jerry");

    gtk_container_add(GTK_CONTAINER(window), fixed);
    gtk_fixed_put(GTK_FIXED(fixed), button1, 0, 300);
    gtk_fixed_put(GTK_FIXED(fixed), button2, 800, 300);

    gtk_widget_show_all(window);
    gtk_main();

    return 0;
}
