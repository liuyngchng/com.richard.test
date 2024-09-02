#include <stdlib.h>
#include <gtk/gtk.h>

int main( int argc,
          char *argv[] )
{
    GtkWidget *window;
    GtkWidget *event_box;
    GtkWidget *label;

    gtk_init (&argc, &argv);

    window = gtk_window_new (GTK_WINDOW_TOPLEVEL);

    gtk_window_set_title (GTK_WINDOW (window), "Event Box");

    /**
     * g_signal_connect 第二个参数可以传递的参数  event 清单
     * event
        button_press_event
        button_release_event
        scroll_event
        motion_notify_event
        delete_event
        destroy_event
        expose_event
        key_press_event
        key_release_event
        enter_notify_event
        leave_notify_event
        configure_event
        focus_in_event
        focus_out_event
        map_event
        unmap_event
        property_notify_event
        selection_clear_event
        selection_request_event
        selection_notify_event
        proximity_in_event
        proximity_out_event
        visibility_notify_event
        client_event
        no_expose_event
        window_state_event
     */
    g_signal_connect (window, "destroy",
                  G_CALLBACK (exit), NULL);

    gtk_container_set_border_width (GTK_CONTAINER (window), 10);

    /* Create an EventBox and add it to our toplevel window */

    event_box = gtk_event_box_new ();
    gtk_container_add (GTK_CONTAINER (window), event_box);
    gtk_widget_show (event_box);

    /* Create a long label */

    label = gtk_label_new ("Click here to quit, quit, quit, quit, quit");
    gtk_container_add (GTK_CONTAINER (event_box), label);
    gtk_widget_show (label);

    /* Clip it short. */
    gtk_widget_set_size_request (label, 110, 20);

    /* And bind an action to it */
    gtk_widget_set_events (event_box, GDK_BUTTON_PRESS_MASK);
    g_signal_connect (event_box, "button_press_event",
                  G_CALLBACK (exit), NULL);

    /* Yet one more thing you need an X window for ... */

    gtk_widget_realize (event_box);
    gdk_window_set_cursor (event_box->window, gdk_cursor_new (GDK_HAND1));

    gtk_widget_show (window);

    gtk_main ();

    return 0;
}
