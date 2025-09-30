#include <gtk/gtk.h>


int main( int   argc,
          char *argv[] )
{
  /* GtkWidget is the storage type for widgets */
  GtkWidget     *window;
  GtkTooltips     *tooltips;
  GtkWidget     *button;

  /* Initialize the toolkit */
  gtk_init (&argc, &argv);

  /* Create a new window */
  window = gtk_window_new (GTK_WINDOW_TOPLEVEL);

  gtk_window_set_title (GTK_WINDOW (window), "Arrow Buttons");

  /* It's a good idea to do this for all windows. */
  g_signal_connect (window, "destroy",
                    G_CALLBACK (gtk_main_quit), NULL);

  /* Sets the border width of the window. */
  gtk_container_set_border_width (GTK_CONTAINER (window), 10);



  tooltips = gtk_tooltips_new ();
  button = gtk_button_new_with_label ("button 1");
  gtk_tooltips_set_tip (tooltips, button, "This is button 1, You can click it", NULL);
  gtk_container_add(GTK_CONTAINER (window), button);

  gtk_widget_show_all(window);

  /* Rest in gtk_main and wait for the fun to begin! */
  gtk_main ();

  return 0;
}
