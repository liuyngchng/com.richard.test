#include <gtk/gtk.h>

/* *
 * Create a new hbox with an image and a label packed into it
 * and return the box.
 * */
static GtkWidget *pic_label_box( gchar     *xpm_filename,
                                 gchar     *label_text )
{
    GtkWidget *box;
    GtkWidget *label;
    GtkWidget *image;

    /* Create box for image and label */
    box = gtk_hbox_new (FALSE, 0);
    gtk_container_set_border_width (GTK_CONTAINER (box), 2);

    /* Now on to the image stuff */
    image = gtk_image_new_from_file (xpm_filename);

    /* Create a label for the button */
    label = gtk_label_new (label_text);

    /* Pack the image and label into the box */
    gtk_box_pack_start (GTK_BOX (box), image, FALSE, FALSE, 3);
    gtk_box_pack_start (GTK_BOX (box), label, FALSE, FALSE, 3);
    gtk_widget_show (image);
    gtk_widget_show (label);

    return box;
}

static GtkWidget *fixed;

void on_button_clicked(GtkButton *button, gpointer user_data) {
    g_print("I am %s, can u see me?\n", (gchar *) user_data);

    gint x = ((GtkWidget *)button)->allocation.x;
    gint y = ((GtkWidget *)button)->allocation.y;
    gtk_fixed_move (GTK_FIXED (fixed), button, x, y+5);

}

int main(int argc, char *argv[]) {
    gtk_init(&argc, &argv);

    GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_default_size(GTK_WINDOW(window), 800, 600);
    gtk_window_set_title(GTK_WINDOW(window),"Tom and Jerry Game");
    fixed = gtk_fixed_new();
    GtkWidget *button1 = gtk_button_new();

    GtkWidget *button2 = gtk_button_new();

    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect(button1, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Tom");
    g_signal_connect(button2, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Jerry");
    GtkWidget *box1 = pic_label_box ("jerry.png", "Jerry");
    gtk_container_add (GTK_CONTAINER (button1), box1);
    GtkWidget *box2 = pic_label_box ("tom.png", "Tom");
        gtk_container_add (GTK_CONTAINER (button2), box2);
    gtk_container_add(GTK_CONTAINER(window), fixed);
    gtk_fixed_put(GTK_FIXED(fixed), button1, 0, 300);
    gtk_fixed_put(GTK_FIXED(fixed), button2, 800, 300);
    // test move
//    gtk_fixed_move (GTK_FIXED (fixed), button1, 10, 20);
    gtk_widget_show_all(window);
    gtk_main();

    return 0;
}
