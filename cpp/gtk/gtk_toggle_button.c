/**
 * create button with a image and a label
 */
#include <stdlib.h>
#include <gtk/gtk.h>

/* Create a new hbox with an image and a label packed into it
 * and return the box. */

static GtkWidget *xpm_label_box( gchar     *xpm_filename,
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

/* Our usual callback function */
static void toggle_button_callback( GtkWidget *widget,
                      gpointer   data )
{
//    g_print ("Hello again - %s was pressed\n", (char *) data);
     if (gtk_toggle_button_get_active (GTK_TOGGLE_BUTTON (widget)))
    {
         g_print ("%s was pressed, i am active\n", (char *) data);

    } else {

        g_print ("%s was pressed, i am deactive\n", (char *) data);
    }
}

int main( int   argc,
          char *argv[] )
{
    /* GtkWidget is the storage type for widgets */
    GtkWidget *window;
    GtkWidget *button;

    gtk_init (&argc, &argv);

    /* Create a new window */
    window = gtk_window_new (GTK_WINDOW_TOPLEVEL);

    gtk_window_set_title (GTK_WINDOW (window), "Toggle Buttons!");

    /* It's a good idea to do this for all windows. */
    g_signal_connect (window, "destroy",
                  G_CALLBACK (gtk_main_quit), NULL);

    g_signal_connect (window, "delete-event",
               G_CALLBACK (gtk_main_quit), NULL);

    /* Sets the border width of the window. */
    gtk_container_set_border_width (GTK_CONTAINER (window), 10);

    /* Create a new toggle button */
//    button = gtk_toggle_button_new ();
//    button = gtk_toggle_button_new_with_label ("Check it with label");
    button = gtk_toggle_button_new_with_mnemonic ("Check it with mnemonic");

    /* Connect the "clicked" signal of the button to our callback
     *
     * The Button widget has the following signals:
     * pressed - emitted when pointer button is pressed within Button widget
     * released - emitted when pointer button is released within Button widget
     * clicked - emitted when pointer button is pressed and then released within Button widget
     * enter - emitted when pointer enters Button widget
     * leave - emitted when pointer leaves Button widget
     *
     * */
    g_signal_connect (button, "clicked",
              G_CALLBACK (toggle_button_callback), (gpointer) "toggle button");


    gtk_widget_show (button);

    gtk_container_add (GTK_CONTAINER (window), button);

    gtk_widget_show (window);

    /* Rest in gtk_main and wait for the fun to begin! */
    gtk_main ();

    return 0;
}
